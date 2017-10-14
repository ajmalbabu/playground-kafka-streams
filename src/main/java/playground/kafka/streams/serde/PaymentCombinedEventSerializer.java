package playground.kafka.streams.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import playground.kafka.streams.model.PaymentCombinedEvent;

import java.util.Map;


public class PaymentCombinedEventSerializer implements Serializer<PaymentCombinedEvent> {

    private ObjectMapper mapper = new ObjectMapper();

    public void configure(Map<String, ?> map, boolean b) {

    }

    public byte[] serialize(String topic, PaymentCombinedEvent paymentCombinedEvent) {
        try {
            return mapper.writeValueAsBytes(paymentCombinedEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {

    }
}
