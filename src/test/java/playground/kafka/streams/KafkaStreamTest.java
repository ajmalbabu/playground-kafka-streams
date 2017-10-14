package playground.kafka.streams;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.integration.utils.EmbeddedKafkaCluster;
import org.apache.kafka.streams.integration.utils.IntegrationTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import playground.kafka.streams.model.Anomaly;
import playground.kafka.streams.model.AnomalyPublisher;
import playground.kafka.streams.model.AnomalyPublisher.QueryableAnomalyPublisher;
import playground.kafka.streams.model.ExternalConfig;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaStreamTest {

    private static final Logger log = LoggerFactory.getLogger(KafkaStreamTest.class);

    @ClassRule
    public static final EmbeddedKafkaCluster embeddedKafkaCluster = new EmbeddedKafkaCluster(1);
    private KafkaStreams kafkaStreams;
    private KafkaUnitTestConfig kafkaUnitTestConfig;
    private QueryableAnomalyPublisher queryableAnomalyPublisher = new QueryableAnomalyPublisher();


    @Before
    public void before() throws InterruptedException {

        kafkaUnitTestConfig = new KafkaUnitTestConfig(embeddedKafkaCluster);
        queryableAnomalyPublisher.clear();
        AnomalyPublisher.registerAnomalyPublisher(queryableAnomalyPublisher.id(), queryableAnomalyPublisher);
        kafkaStreams = new PaymentAnomalyApp().createStreams(kafkaUnitTestConfig, queryableAnomalyPublisher.id());
        kafkaStreams.start();
    }

    @After
    public void whenShuttingDown() throws IOException {
        kafkaStreams.close();
        IntegrationTestUtils.purgeLocalStreamsState(kafkaUnitTestConfig.streamsConfigProperties());

    }

    @Test
    public void testTwoAnomalyGenerationWhoseAmountDueIsMoreThan$150() throws InterruptedException {

        // Given, When
        EventPublisher eventPublisher = new EventPublisher();
        eventPublisher.sendEvents(kafkaUnitTestConfig);

        // Then,  expect two anomalies
        List<Anomaly> anomalies = queryableAnomalyPublisher.blockForAnomalies(2, 50, 12000);

        assertThat(anomalies.size()).isEqualTo(2);
        assertThat(anomalies.get(0).toString()).isEqualTo("CustomerDueAnomaly{customerAccountNumber=1, totalPayment=201.0, totalAmountDue=411.0}");
        assertThat(anomalies.get(1).toString()).isEqualTo("CustomerDueAnomaly{customerAccountNumber=1, totalPayment=201.0, totalAmountDue=623.0}");


    }

    /**
     * There are no anomalies generated because the message expiry is set to 1 second. Hence no anomaly is generated.
     */
    @Test
    public void testAllMessagesAreExpiredAndNoAnomalyIsGenerated() throws InterruptedException {

        // Given, When
        ExternalConfig.MESSAGE_EXPIRE_IN_SECONDS = 1;
        EventPublisher eventPublisher = new EventPublisher();
        eventPublisher.sendEvents(kafkaUnitTestConfig);

        // Then,  expect no anomalies, eventhough we ask for 2 none will come back as all messages would be expired.
        List<Anomaly> anomalies = queryableAnomalyPublisher.blockForAnomalies(1, 50, 12000);

        assertThat(anomalies.size()).isEqualTo(0);

    }
}
