package playground.kafka.streams.model;


import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static playground.kafka.streams.model.ExternalConfig.MESSAGE_EXPIRE_IN_SECONDS;

/**
 * Event is published when a payment is scheduled to be processed for a process date for a specific customer account number.
 */
public class PaymentScheduleEvent {

    private long customerAccountNumber;
    private Date paymentProcessDate;
    private Date eventTime; // Can be used if system care about the event-time.
    private double paymentAmount;

    public PaymentScheduleEvent() {
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public long getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(long customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    public Date getPaymentProcessDate() {
        return paymentProcessDate;
    }

    public void setPaymentProcessDate(Date paymentProcessDate) {
        this.paymentProcessDate = paymentProcessDate;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }


    @Override
    public String toString() {
        return "PaymentScheduleEvent{" +
                "customerAccountNumber=" + customerAccountNumber +
                ", paymentProcessDate=" + paymentProcessDate +
                ", eventTime=" + eventTime +
                ", paymentAmount=" + paymentAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentScheduleEvent that = (PaymentScheduleEvent) o;

        if (customerAccountNumber != that.customerAccountNumber) return false;
        if (Double.compare(that.paymentAmount, paymentAmount) != 0) return false;
        if (paymentProcessDate != null ? !paymentProcessDate.equals(that.paymentProcessDate) : that.paymentProcessDate != null)
            return false;
        return eventTime != null ? eventTime.equals(that.eventTime) : that.eventTime == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (customerAccountNumber ^ (customerAccountNumber >>> 32));
        result = 31 * result + (paymentProcessDate != null ? paymentProcessDate.hashCode() : 0);
        result = 31 * result + (eventTime != null ? eventTime.hashCode() : 0);
        temp = Double.doubleToLongBits(paymentAmount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public boolean expired() {

        return Instant.now()
                .minusMillis(Duration.ofSeconds(MESSAGE_EXPIRE_IN_SECONDS).toMillis())
                .isBefore(Instant.ofEpochMilli(getEventTime().getTime()));


    }
}
