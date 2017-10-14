package playground.kafka.streams;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.streams.StreamsConfig;
import playground.kafka.streams.model.EventTimeExtractor;
import playground.kafka.streams.model.PaymentDueEvent;
import playground.kafka.streams.model.PaymentScheduleEvent;

import java.util.Properties;
import java.util.UUID;

/**
 * Sets up the Kafka configurations (boiler-plate configurations) for payment schedule and payment due-date Kafka topics.
 */
public class DefaultKafkaConfig implements KafkaConfig {

    private final String paymentScheduleEventTopic = "paymentScheduleEventTopic" + UUID.randomUUID().toString();
    private final String paymentDueEventTopic = "paymentDueEventTopic" + UUID.randomUUID().toString();

    protected final Producer<Long, PaymentScheduleEvent> paymentScheduleEventProducer;
    protected final Producer<Long, PaymentDueEvent> paymentDueEventProducer;
    protected Properties streamConfigProperties;
    protected StreamsConfig streamsConfig;

    public DefaultKafkaConfig(String bootstrapServer) {

        paymentScheduleEventProducer = createPaymentScheduleEventProducer(bootstrapServer);
        paymentDueEventProducer = createPaymentDueEventProducer(bootstrapServer);
        streamConfigProperties = createStreamProperties(bootstrapServer);
        streamsConfig = new StreamsConfig(streamConfigProperties);

    }

    protected Producer<Long, PaymentScheduleEvent> createPaymentScheduleEventProducer(String bootstrapServer) {
        Properties paymentEventProps = new Properties();
        paymentEventProps.put("bootstrap.servers", bootstrapServer);
        paymentEventProps.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        paymentEventProps.put("value.serializer", "playground.kafka.streams.serde.PaymentScheduleEventSerializer");
        paymentEventProps.put("linger.ms", 0);
        return new KafkaProducer<>(paymentEventProps);
    }

    protected Producer<Long, PaymentDueEvent> createPaymentDueEventProducer(String bootstrapServer) {
        Properties dueDateEventProps = new Properties();
        dueDateEventProps.put("bootstrap.servers", bootstrapServer);
        dueDateEventProps.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        dueDateEventProps.put("value.serializer", "playground.kafka.streams.serde.DueEventSerializer");
        dueDateEventProps.put("linger.ms", 10000);
        return new KafkaProducer<>(dueDateEventProps);
    }


    protected Properties createStreamProperties(String bootstrapServer) {
        Properties streamProps = new Properties();
        String appId = "PaymentAnomalyApp" + UUID.randomUUID();
        streamProps.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
        streamProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        streamProps.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, EventTimeExtractor.class);
        return streamProps;
    }

    @Override
    public String paymentScheduleEventTopic() {
        return paymentScheduleEventTopic;
    }

    @Override
    public String paymentDueEventTopic() {
        return paymentDueEventTopic;
    }

    @Override
    public StreamsConfig streamsConfig() {
        return streamsConfig;
    }

    @Override
    public Producer<Long, PaymentScheduleEvent> paymentScheduleEventProducer() {
        return paymentScheduleEventProducer;
    }

    @Override
    public Producer<Long, PaymentDueEvent> paymentDueEventProducer() {
        return paymentDueEventProducer;
    }

    @Override
    public Properties streamsConfigProperties() {
        return streamConfigProperties;
    }
}
