package playground.kafka.streams;

import org.apache.kafka.streams.kstream.Initializer;


public class PaymentDueAnomalyDetectorCreator implements Initializer<PaymentDueAnomalyDetector> {


    private String anomalyPublisherId;

    public PaymentDueAnomalyDetectorCreator(String anomalyPublisherId) {
        this.anomalyPublisherId = anomalyPublisherId;
    }

    @Override
    public PaymentDueAnomalyDetector apply() {
        return new PaymentDueAnomalyDetector(anomalyPublisherId);
    }
}
