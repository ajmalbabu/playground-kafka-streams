package playground.kafka.streams.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import playground.kafka.streams.model.PaymentCombinedEvent;

import java.io.IOException;
import java.util.Map;

public class PaymentCombinedEventDeserializer implements Deserializer<PaymentCombinedEvent> {
    private ObjectMapper mapper = new ObjectMapper();

    public void configure(Map<String, ?> map, boolean b) {

    }

    public PaymentCombinedEvent deserialize(String topic, byte[] bytes) {

        try {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            return mapper.readValue(bytes, PaymentCombinedEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {

    }
}
