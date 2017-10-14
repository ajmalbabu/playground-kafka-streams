package playground.kafka.streams.model;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An extractor that show-case how to use event-time semantic.
 */
public class EventTimeExtractor implements TimestampExtractor {

    private static final Logger log = LoggerFactory.getLogger(EventTimeExtractor.class);

    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long previousTimestamp) {
        long timestamp = -1;
        final Object payload = consumerRecord.value();
        if (payload != null && payload instanceof PaymentScheduleEvent) {
            timestamp = ((PaymentScheduleEvent) payload).getEventTime().getTime();
            log.debug("PaymentScheduleEvent event time: {}", timestamp);
        } else if (payload instanceof PaymentDueEvent) {
            timestamp = ((PaymentDueEvent) payload).getEventTime().getTime();
            log.debug("PaymentDueEvent event time: {}", timestamp);
        }
        if (timestamp < 0) {
            return System.currentTimeMillis();
        } else {
            return timestamp;
        }
    }
}
