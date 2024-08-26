package Exceptions;

public class CurrencyPairExistsException extends Exception {
    public CurrencyPairExistsException(String message) {
        super(message);
    }
}
