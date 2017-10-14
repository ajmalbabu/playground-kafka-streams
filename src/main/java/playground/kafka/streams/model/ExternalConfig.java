package playground.kafka.streams.model;


/**
 * Externalized configs, the configs that can be controlled during program/unit test execution.
 */
public class ExternalConfig {

    /**
     * The messages beyond this time in seconds will be expired in the Kafka state store.
     */
    public static int MESSAGE_EXPIRE_IN_SECONDS = 20;


    public static int OUTER_JOIN_SLIDING_WIDOW_DURATION_MILLIS = 5000;

}
