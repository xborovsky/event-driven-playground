package cz.marek_b.edp.retry_topics_dlq.ex;

public class InvalidAddressException extends Exception {
    public InvalidAddressException(String message) {
        super(message);
    }
}
