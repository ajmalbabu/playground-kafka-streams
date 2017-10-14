package playground.kafka.streams.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import playground.kafka.streams.PaymentDueAnomalyDetector;

import java.util.Map;


public class PaymentDueAnomalyDetectorSerializer implements Serializer<PaymentDueAnomalyDetector> {

    private ObjectMapper mapper = new ObjectMapper();

    public void configure(Map<String, ?> map, boolean b) {

    }

    public byte[] serialize(String topic, PaymentDueAnomalyDetector paymentDueAnomalyDetector) {
        try {
            return mapper.writeValueAsBytes(paymentDueAnomalyDetector);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {

    }
}
