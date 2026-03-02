package cz.marek_b.edp.retry_topics_dlq.ex;

public class CarrierRateLimitException extends Exception {

    public CarrierRateLimitException(String message) {
        super(message);
    }

}
