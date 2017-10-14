package playground.kafka.streams.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import playground.kafka.streams.model.PaymentScheduleEvent;

import java.util.Map;


public class PaymentScheduleEventSerializer implements Serializer<PaymentScheduleEvent> {

    private ObjectMapper mapper = new ObjectMapper();

    public void configure(Map<String, ?> map, boolean b) {

    }

    public byte[] serialize(String topic, PaymentScheduleEvent paymentScheduleEvent) {
        try {
            return mapper.writeValueAsBytes(paymentScheduleEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {

    }
}
