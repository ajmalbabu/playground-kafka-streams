package playground.kafka.streams;


import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.streams.StreamsConfig;
import playground.kafka.streams.model.PaymentDueEvent;
import playground.kafka.streams.model.PaymentScheduleEvent;

import java.util.Properties;

/**
 * Defines all the Kafka configuration needed for the sample application. This allows to override and change
 * some of the properties that needs to be changed during unit testing.
 */
public interface KafkaConfig {

    String paymentScheduleEventTopic();

    String paymentDueEventTopic();

    Producer<Long, PaymentScheduleEvent> paymentScheduleEventProducer();

    Producer<Long, PaymentDueEvent> paymentDueEventProducer();

    Properties streamsConfigProperties();

    StreamsConfig streamsConfig();


}
