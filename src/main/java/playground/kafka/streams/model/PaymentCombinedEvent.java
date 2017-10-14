package playground.kafka.streams.model;

/**
 * A class that can represent either or both of schedule event or due event from the stream.
 */
public class PaymentCombinedEvent {

    private PaymentScheduleEvent paymentScheduleEvent;
    private PaymentDueEvent paymentDueEvent;


    public PaymentCombinedEvent() {
    }


    public PaymentCombinedEvent(PaymentScheduleEvent paymentScheduleEvent, PaymentDueEvent paymentDueEvent) {
        this.paymentScheduleEvent = paymentScheduleEvent;
        this.paymentDueEvent = paymentDueEvent;
    }

    public PaymentScheduleEvent getPaymentScheduleEvent() {
        return paymentScheduleEvent;
    }

    public void setPaymentScheduleEvent(PaymentScheduleEvent paymentScheduleEvent) {
        this.paymentScheduleEvent = paymentScheduleEvent;
    }

    public PaymentDueEvent getPaymentDueEvent() {
        return paymentDueEvent;
    }

    public void setPaymentDueEvent(PaymentDueEvent paymentDueEvent) {
        this.paymentDueEvent = paymentDueEvent;
    }


    @Override
    public String toString() {
        return "PaymentCombinedEvent{" +
                "paymentScheduleEvent=" + paymentScheduleEvent +
                ", paymentDueEvent=" + paymentDueEvent +
                '}';
    }


}
