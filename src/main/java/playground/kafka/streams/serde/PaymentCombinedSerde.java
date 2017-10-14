package playground.kafka.streams.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import playground.kafka.streams.model.PaymentCombinedEvent;

import java.util.Map;


public class PaymentCombinedSerde implements Serde<PaymentCombinedEvent> {

    public void configure(Map<String, ?> map, boolean b) {

    }

    public void close() {

    }

    public Serializer<PaymentCombinedEvent> serializer() {
        return new PaymentCombinedEventSerializer();
    }

    public Deserializer<PaymentCombinedEvent> deserializer() {
        return new PaymentCombinedEventDeserializer();
    }
}
