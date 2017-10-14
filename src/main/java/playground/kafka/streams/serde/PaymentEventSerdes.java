package playground.kafka.streams.serde;


public class PaymentEventSerdes {

    public static PaymentScheduleEventSerde PAYMENT_SCHEDULE_EVENT_SERDE = new PaymentScheduleEventSerde();
    public static PaymentDueEventSerde PAYMENT_DUE_EVENT_SERDE = new PaymentDueEventSerde();
    public static PaymentDueAnomalyDetectorSerde PAYMENT_DUE_ANOMALY_SERDE = new PaymentDueAnomalyDetectorSerde();
}
