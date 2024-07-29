package hu.nero.exception;

public class LineNotEmptyException extends RuntimeException {
    public LineNotEmptyException(String message) {
        super(message);
    }
}
