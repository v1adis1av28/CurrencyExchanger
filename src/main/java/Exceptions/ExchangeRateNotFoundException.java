package Exceptions;

public class ExchangeRateNotFoundException extends Exception {
    public ExchangeRateNotFoundException(String message) {
        super(message);
    }
}