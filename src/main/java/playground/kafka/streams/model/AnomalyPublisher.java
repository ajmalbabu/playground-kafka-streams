package playground.kafka.streams.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Anomaly publisher interface.
 */
public interface AnomalyPublisher {


    String id();

    void publish(Anomaly anomaly);


    /**
     * All available anomaly publishers.
     */
    Map<String, AnomalyPublisher> anomalyPublishers = new HashMap<>();

    /**
     * Register an anomaly publisher with a specific id.
     */
    static void registerAnomalyPublisher(String anomalyPublisherId, AnomalyPublisher anomalyPublisher) {
        anomalyPublishers.put(anomalyPublisherId, anomalyPublisher);
    }

    /**
     * Publish the anomaly to the right target. All such routing decision can be in one place.
     * When using framework such as spring/play, integrate here to look for the right bean from
     * the underlying framework using the registered anomalyPublisherId.
     *
     * @param anomalyPublisherId - Target anomaly publisher where anomaly should be published.
     * @param anomaly            - The anomaly to be published.
     */
    static void publishTo(String anomalyPublisherId, Anomaly anomaly) {

        if (anomalyPublishers.get(anomalyPublisherId) != null) {
            anomalyPublishers.get(anomalyPublisherId).publish(anomaly);
        } else {
            new ConsoleAnomalyPublisher().publish(anomaly);
        }

    }


    /**
     * An anomaly publisher that can publish anomaly to the console.
     */
    class ConsoleAnomalyPublisher implements AnomalyPublisher {

        Logger log = LoggerFactory.getLogger(ConsoleAnomalyPublisher.class);

        @Override
        public String id() {
            return hashCode() + "";
        }

        @Override
        public void publish(Anomaly anomaly) {
            log.error("Publishing anomaly: {}", anomaly);
        }
    }


    /**
     * An anomaly publisher that help with unit testing.
     */
    class QueryableAnomalyPublisher implements AnomalyPublisher {

        private List<Anomaly> anomalies = new ArrayList<>();

        @Override
        public String id() {
            return hashCode() + "";
        }

        @Override
        public void publish(Anomaly anomaly) {
            anomalies.add(anomaly);
        }

        public List<Anomaly> getAnomalies() {
            return anomalies;
        }

        public void clear() {
            anomalies.clear();
        }

        public List<Anomaly> blockForAnomalies(int numberOfAnomalies, long sleepIntervalMillis, long maxSleepMillis) throws InterruptedException {
            Instant startTime = Instant.now();
            while (anomalies.size() != numberOfAnomalies && Instant.now().minusMillis(maxSleepMillis).isBefore(startTime)) {
                Thread.sleep(sleepIntervalMillis);
            }
            return getAnomalies();
        }

    }
}


