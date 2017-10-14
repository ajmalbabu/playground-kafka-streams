package playground.kafka.streams.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import playground.kafka.streams.model.PaymentDueEvent;

import java.util.Map;

public class PaymentDueEventSerde implements Serde<PaymentDueEvent> {

    public void configure(Map<String, ?> map, boolean b) {

    }

    public void close() {

    }

    public Serializer<PaymentDueEvent> serializer() {
        return new DueEventSerializer();
    }

    public Deserializer<PaymentDueEvent> deserializer() {
        return new DueEventDeserializer();
    }
}
