package playground.kafka.streams;

import org.apache.kafka.streams.KafkaStreams;
import playground.kafka.streams.model.AnomalyPublisher;

import static playground.kafka.streams.model.AnomalyPublisher.ConsoleAnomalyPublisher;

/**
 * A startup class. Make sure Kafka is running at 9092 port before running this class.
 * If the port needs to be changed, change it in below DefaultKafkaConfig argument.
 * <p>
 * This example shows how to work with Kafka streams and also how to write unit test
 * cases for the streaming code.
 */
public class Startup {

    public static void main(String[] args) {

        KafkaConfig kafkaConfig = new DefaultKafkaConfig("localhost:9092");
        new EventPublisher().sendEvents(kafkaConfig);
        ConsoleAnomalyPublisher consoleAnomalyPublisher = new ConsoleAnomalyPublisher();
        AnomalyPublisher.registerAnomalyPublisher(consoleAnomalyPublisher.id(), consoleAnomalyPublisher);

        KafkaStreams kafkaStreams = new PaymentAnomalyApp().createStreams(kafkaConfig, consoleAnomalyPublisher.id());
        kafkaStreams.start();


    }
}
