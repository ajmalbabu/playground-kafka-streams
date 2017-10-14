package playground.kafka.streams;


import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import playground.kafka.streams.model.PaymentDueEvent;
import playground.kafka.streams.model.PaymentScheduleEvent;

import java.util.Calendar;
import java.util.Date;

/**
 * Helps to send payment schedule and payment due-date events to respective Kafka topics. Refer README.md sliding window explanation section to
 * understand the consequence of sending the events at various intervals.
 */
public class EventPublisher {

    public void sendEvents(KafkaConfig kafkaConfig) {

        Calendar eventTime = Calendar.getInstance();

        // All below events have the same customer id = 1, send a payment event for $100.
        sendPaymentScheduleEvent(kafkaConfig.paymentScheduleEventProducer(), kafkaConfig.paymentScheduleEventTopic(),
                1, new Date(), eventTime.getTime(), 100.00);

        // Send a payment event for $101 after a 1 second delay.
        eventTime.add(Calendar.SECOND, 1);
        sendPaymentScheduleEvent(kafkaConfig.paymentScheduleEventProducer(), kafkaConfig.paymentScheduleEventTopic(),
                1, new Date(), eventTime.getTime(), 101.00);

        // Send due event for $200 for this customer is fired after 1 second.
        eventTime.add(Calendar.SECOND, 1);
        sendPaymentDueEvent(kafkaConfig.paymentDueEventProducer(), kafkaConfig.paymentDueEventTopic(),
                1, new Date(), eventTime.getTime(), 200.00);

        // Make a 4 minute delay and send another due event for $211.
        eventTime.add(Calendar.SECOND, 4);
        sendPaymentDueEvent(kafkaConfig.paymentDueEventProducer(), kafkaConfig.paymentDueEventTopic(),
                1, new Date(), eventTime.getTime(), 211.00);

        // Send last event for due event for $212.
        eventTime.add(Calendar.SECOND, 1);
        sendPaymentDueEvent(kafkaConfig.paymentDueEventProducer(), kafkaConfig.paymentDueEventTopic(),
                1, new Date(), eventTime.getTime(), 212.00);

    }

    public void sendPaymentScheduleEvent(Producer<Long, PaymentScheduleEvent> paymentScheduleEventProducer, String paymentScheduleEventTopic,
                                         long customerAccountNumber, Date paymentProcessDate, Date eventTime, double paymentAmount) {

        PaymentScheduleEvent paymentScheduleEvent = new PaymentScheduleEvent();
        paymentScheduleEvent.setCustomerAccountNumber(customerAccountNumber);
        paymentScheduleEvent.setPaymentProcessDate(paymentProcessDate);
        paymentScheduleEvent.setEventTime(eventTime);
        paymentScheduleEvent.setPaymentAmount(paymentAmount);
        paymentScheduleEventProducer.send(new ProducerRecord<>(paymentScheduleEventTopic, 0, customerAccountNumber, paymentScheduleEvent));
    }

    public void sendPaymentDueEvent(Producer<Long, PaymentDueEvent> paymentDueEventProducer, String paymentDueEventTopic,
                                    long customerAccountNumber, Date dueDate, Date eventTime, double dueAmount) {

        PaymentDueEvent paymentDueEvent = new PaymentDueEvent();
        paymentDueEvent.setCustomerAccountNumber(customerAccountNumber);
        paymentDueEvent.setDueDate(dueDate);
        paymentDueEvent.setEventTime(eventTime);
        paymentDueEvent.setDueAmount(dueAmount);
        paymentDueEventProducer.send(new ProducerRecord<>(paymentDueEventTopic, 0, customerAccountNumber, paymentDueEvent));
    }
}
