package cz.marek_b.edp.retry_topics_dlq.ex;

public class CarrierTimeoutException extends Exception {
    public CarrierTimeoutException(String message) {
        super(message);
    }
}
