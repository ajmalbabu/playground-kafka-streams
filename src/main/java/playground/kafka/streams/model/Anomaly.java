package playground.kafka.streams.model;

public interface Anomaly {

    class CustomerDueAnomaly implements Anomaly {
        private long customerAccountNumber;
        private double totalPayment;
        private double totalAmountDue;

        public CustomerDueAnomaly(long customerAccountNumber, double totalPayment, double totalAmountDue) {
            this.customerAccountNumber = customerAccountNumber;
            this.totalPayment = totalPayment;

            this.totalAmountDue = totalAmountDue;
        }

        public long getCustomerAccountNumber() {
            return customerAccountNumber;
        }


        @Override
        public String toString() {
            return "CustomerDueAnomaly{" +
                    "customerAccountNumber=" + customerAccountNumber +
                    ", totalPayment=" + totalPayment +
                    ", totalAmountDue=" + totalAmountDue +
                    '}';
        }
    }
}
