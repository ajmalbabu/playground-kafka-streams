package playground.kafka.streams.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import playground.kafka.streams.model.PaymentScheduleEvent;

import java.util.Map;


public class PaymentScheduleEventSerde implements Serde<PaymentScheduleEvent> {

    public void configure(Map<String, ?> map, boolean b) {

    }

    public void close() {

    }

    public Serializer<PaymentScheduleEvent> serializer() {
        return new PaymentScheduleEventSerializer();
    }

    public Deserializer<PaymentScheduleEvent> deserializer() {
        return new PaymentScheduleEventDeserializer();
    }
}
