package playground.kafka.streams.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import playground.kafka.streams.model.PaymentDueEvent;

import java.util.Map;


public class DueEventDeserializer implements Deserializer<PaymentDueEvent> {

    private ObjectMapper mapper = new ObjectMapper();

    public void configure(Map<String, ?> map, boolean b) {

    }

    public PaymentDueEvent deserialize(String topic, byte[] bytes) {

        try {

            return mapper.readValue(bytes, PaymentDueEvent.class);
        } catch (Exception e) {

            return null;
        }
    }

    public void close() {

    }
}
