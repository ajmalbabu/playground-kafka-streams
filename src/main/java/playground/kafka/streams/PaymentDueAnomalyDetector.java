package playground.kafka.streams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import playground.kafka.streams.model.Anomaly.CustomerDueAnomaly;
import playground.kafka.streams.model.AnomalyPublisher;
import playground.kafka.streams.model.PaymentCombinedEvent;
import playground.kafka.streams.model.PaymentDueEvent;
import playground.kafka.streams.model.PaymentScheduleEvent;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * An anomaly detector that looks to see if the payment due is more than $150 at any time it generates an anomaly.
 */
public class PaymentDueAnomalyDetector {

    private static final Logger log = LoggerFactory.getLogger(PaymentDueAnomalyDetector.class);

    /**
     * The hash-code of the message is used as the Map's key.
     */
    private Map<Integer, PaymentScheduleEvent> paymentScheduleEvents = new HashMap<>();

    /**
     * The hash-code of the message is used as the Map's key.
     */
    private Map<Integer, PaymentDueEvent> paymentDueEvents = new HashMap<>();
    private String anomalyPublisherId;

    public PaymentDueAnomalyDetector() {
    }

    public PaymentDueAnomalyDetector(String anomalyPublisherId) {
        this.anomalyPublisherId = anomalyPublisherId;
    }

    public void detectPaymentAnomaly(PaymentCombinedEvent paymentCombinedEvent) {

        if (paymentCombinedEvent.getPaymentScheduleEvent() != null) {
            paymentScheduleEvents.put(paymentCombinedEvent.getPaymentScheduleEvent().hashCode(), paymentCombinedEvent.getPaymentScheduleEvent());
        }

        if (paymentCombinedEvent.getPaymentDueEvent() != null) {
            paymentDueEvents.put(paymentCombinedEvent.getPaymentDueEvent().hashCode(), paymentCombinedEvent.getPaymentDueEvent());
        }
        expireOldEvents();

        log.info("Anomaly called with [schedule event size/due event size]: [{}/{}] event: {}",
                paymentScheduleEvents.size(), paymentDueEvents.size(), paymentCombinedEvent);

        checkForAnomaly();

    }

    /**
     * So that the state store is cleaned-up by removing old events.
     */
    private void expireOldEvents() {

        paymentScheduleEvents = paymentScheduleEvents
                .entrySet().stream().filter(e -> e.getValue().expired())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        paymentDueEvents = paymentDueEvents
                .entrySet().stream().filter(e -> e.getValue().expired())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private void checkForAnomaly() {

        log.debug("totalDue(): {}, totalPayment(): {}", totalDue(), totalPayment());

        // If payment due is more than $150 at any time it generates an anomaly. Calculation is done by adding all schedule payment
        // against due amount and see if due crosses $150 limit. For simplicity the dates (such as schedule payment date) are ignored

        if (totalDue() - totalPayment() > 150) {
            AnomalyPublisher.publishTo(anomalyPublisherId, new CustomerDueAnomaly(findAnyCustomerAccountNumber(), totalPayment(), totalDue()));
        }

    }

    private long findAnyCustomerAccountNumber() {
        return paymentDueEvents.values().stream().findAny().get().getCustomerAccountNumber();
    }

    private double totalPayment() {
        return paymentScheduleEvents.values().stream().reduce(0D,
                (U, paymentScheduleEvent) -> U + paymentScheduleEvent.getPaymentAmount(),
                (U1, U2) -> U1 + U2);
    }

    private double totalDue() {
        return paymentDueEvents.values().stream().reduce(0D,
                (U, paymentDueEvent) -> U + paymentDueEvent.getDueAmount(),
                (U1, U2) -> U1 + U2);
    }


    public Map<Integer, PaymentScheduleEvent> getPaymentScheduleEvents() {
        return paymentScheduleEvents;
    }

    public void setPaymentScheduleEvents(Map<Integer, PaymentScheduleEvent> paymentScheduleEvents) {
        this.paymentScheduleEvents = paymentScheduleEvents;
    }

    public Map<Integer, PaymentDueEvent> getPaymentDueEvents() {
        return paymentDueEvents;
    }

    public void setPaymentDueEvents(Map<Integer, PaymentDueEvent> paymentDueEvents) {
        this.paymentDueEvents = paymentDueEvents;
    }

    public String getAnomalyPublisherId() {
        return anomalyPublisherId;
    }

    public void setAnomalyPublisherId(String anomalyPublisherId) {
        this.anomalyPublisherId = anomalyPublisherId;
    }
}
