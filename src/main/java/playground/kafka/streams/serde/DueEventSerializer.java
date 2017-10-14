package playground.kafka.streams.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import playground.kafka.streams.model.PaymentDueEvent;

import java.util.Map;


public class DueEventSerializer implements Serializer<PaymentDueEvent> {

    private ObjectMapper mapper = new ObjectMapper();

    public void configure(Map<String, ?> map, boolean b) {

    }

    public byte[] serialize(String topic, PaymentDueEvent adClickEvent) {
        try {
            return mapper.writeValueAsBytes(adClickEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {

    }
}
