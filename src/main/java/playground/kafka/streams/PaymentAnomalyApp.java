package playground.kafka.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import playground.kafka.streams.model.PaymentCombinedEvent;
import playground.kafka.streams.model.PaymentDueEvent;
import playground.kafka.streams.model.PaymentScheduleEvent;
import playground.kafka.streams.serde.PaymentCombinedSerde;

import static playground.kafka.streams.model.ExternalConfig.OUTER_JOIN_SLIDING_WIDOW_DURATION_MILLIS;
import static playground.kafka.streams.serde.PaymentEventSerdes.*;


/**
 * This is the main coordinator class that does below.
 * <p>
 * 1) It uses the properties needed to connect to Kafka server by utilizing the KafkaConfig class.
 * 2) Then create KStreams to read from two topics: (a) payment schedule topic and (b) payment due-date topic.
 * 3) Joins the stream using outer join based on sliding window of 5 seconds
 * 4) Create a KTable from the outer join stream.
 * 5) Within the KTable PaymentDueAnomalyDetector is plugged in which detectPaymentAnomaly the payment events within a specified time-window
 * for a specific customer. This detectPaymentAnomaly also expire older events from KTable which are older than a specified time.
 * The PaymentDueAnomalyDetector can include any sort of logic such as summary of the data within all the events, or
 * determining an anomaly condition in those events within that time-frame. Refer to that class for details of anomaly it is looking for.
 * 6) The KTable is resilient by a transparent compacted Kafka topic and would get recreated in case of instance failure
 * 7) The KTable is scalable as well as only partitioned set of records flow into an aggregation instance. Scale out
 * is possible by topic partitioning and adding more aggregator instances.
 */
public class PaymentAnomalyApp {


    public KafkaStreams createStreams(KafkaConfig kafkaConfig, String anomalyPublisherId) {

        KStreamBuilder kStreamBuilder = new KStreamBuilder();

        // Read from two topics
        KStream<Long, PaymentScheduleEvent> paymentStream = kStreamBuilder.stream(Serdes.Long(), PAYMENT_SCHEDULE_EVENT_SERDE, kafkaConfig.paymentScheduleEventTopic());
        KStream<Long, PaymentDueEvent> dueStream = kStreamBuilder.stream(Serdes.Long(), PAYMENT_DUE_EVENT_SERDE, kafkaConfig.paymentDueEventTopic());

        // Outer join of the topics for 5 seconds window duration. This joining duration does not matter for this use-case as events are
        // stored in a local store and are expired and controlled by the application specific aggregator (PaymentDueAnomalyDetectorCreator).
        // But, an explanation of implication of this duration setting in terms of how events would be grouped inside the sliding window
        // is explained in README.md in the Additional Notes section.
        KStream<Long, PaymentCombinedEvent> paymentEventsOuterJoinStream = paymentStream.outerJoin(dueStream,
                PaymentCombinedEvent::new, JoinWindows.of(OUTER_JOIN_SLIDING_WIDOW_DURATION_MILLIS),
                Serdes.Long(), PAYMENT_SCHEDULE_EVENT_SERDE, PAYMENT_DUE_EVENT_SERDE);

        paymentAnomalyDetector(paymentEventsOuterJoinStream, anomalyPublisherId);

        return new KafkaStreams(kStreamBuilder, kafkaConfig.streamsConfig());

    }

    private void paymentAnomalyDetector(KStream<Long, PaymentCombinedEvent> paymentEventsOuterJoinStream, String anomalyPublisherId) {

        // The events are aggregated, expired as needed and stored in state store.
        paymentEventsOuterJoinStream
                .groupByKey(Serdes.Long(), new PaymentCombinedSerde())
                .aggregate(new PaymentDueAnomalyDetectorCreator(anomalyPublisherId),
                        (aggKey, paymentCombinedEvent, paymentDueAnomalyDetector) -> {
                            paymentDueAnomalyDetector.detectPaymentAnomaly(paymentCombinedEvent);
                            return paymentDueAnomalyDetector;
                        },
                        PAYMENT_DUE_ANOMALY_SERDE,
                        "payment-anomaly-store");


    }


}
