package playground.kafka.streams.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import playground.kafka.streams.PaymentDueAnomalyDetector;

import java.util.Map;


public class PaymentDueAnomalyDetectorSerde implements Serde<PaymentDueAnomalyDetector> {

    public void configure(Map<String, ?> map, boolean b) {

    }

    public void close() {

    }

    public Serializer<PaymentDueAnomalyDetector> serializer() {
        return new PaymentDueAnomalyDetectorSerializer();
    }

    public Deserializer<PaymentDueAnomalyDetector> deserializer() {
        return new PaymentDueAnomalyDetectorDeserializer();
    }
}
