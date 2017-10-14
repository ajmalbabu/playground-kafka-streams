package playground.kafka.streams.model;


import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static playground.kafka.streams.model.ExternalConfig.MESSAGE_EXPIRE_IN_SECONDS;

/**
 * Event is published to let system know payment due-date for a specific customer account number.
 */
public class PaymentDueEvent {

    private long customerAccountNumber;
    private Date dueDate;
    private Date eventTime; // Can be used if system care about the event-time.
    private double dueAmount;

    public PaymentDueEvent() {
    }

    public long getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(long customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(double dueAmount) {
        this.dueAmount = dueAmount;
    }

    @Override
    public String toString() {
        return "PaymentDueEvent{" +
                "customerAccountNumber=" + customerAccountNumber +
                ", dueDate=" + dueDate +
                ", dueAmount=" + dueAmount +
                ", eventTime=" + eventTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentDueEvent that = (PaymentDueEvent) o;

        if (customerAccountNumber != that.customerAccountNumber) return false;
        if (Double.compare(that.dueAmount, dueAmount) != 0) return false;
        if (dueDate != null ? !dueDate.equals(that.dueDate) : that.dueDate != null) return false;
        return eventTime != null ? eventTime.equals(that.eventTime) : that.eventTime == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (customerAccountNumber ^ (customerAccountNumber >>> 32));
        result = 31 * result + (dueDate != null ? dueDate.hashCode() : 0);
        result = 31 * result + (eventTime != null ? eventTime.hashCode() : 0);
        temp = Double.doubleToLongBits(dueAmount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public boolean expired() {

        return Instant.now()
                .minusMillis(Duration.ofSeconds(MESSAGE_EXPIRE_IN_SECONDS).toMillis())
                .isBefore(Instant.ofEpochMilli(getEventTime().getTime()));


    }

}
