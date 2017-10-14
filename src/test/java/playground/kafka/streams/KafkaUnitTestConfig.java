package playground.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.integration.utils.EmbeddedKafkaCluster;
import org.apache.kafka.streams.integration.utils.IntegrationTestUtils;
import org.apache.kafka.test.TestUtils;

import java.util.Properties;

/**
 * A unit test specific configuration that override few methods to help on testing purpose.
 */
public class KafkaUnitTestConfig extends DefaultKafkaConfig {


    public KafkaUnitTestConfig(EmbeddedKafkaCluster embeddedKafkaCluster) throws InterruptedException {

        super(embeddedKafkaCluster.bootstrapServers());
        embeddedKafkaCluster.createTopics(super.paymentScheduleEventTopic(), super.paymentDueEventTopic());

    }

    @Override
    public Properties createStreamProperties(String bootstrapServer) {

        Properties streamsConfiguration = super.createStreamProperties(bootstrapServer);
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        String stateDir = TestUtils.tempDirectory().getPath();
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);
        streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        streamsConfiguration.put(IntegrationTestUtils.INTERNAL_LEAVE_GROUP_ON_CLOSE, true);
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);

        return streamsConfiguration;
    }


}
