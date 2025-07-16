package se.magus.microservices.core.chat.exception;

public class ChannelDoesNotExist extends Exception {
    public ChannelDoesNotExist(String message) {
        super(message);
    }
}
