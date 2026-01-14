package pl.edu.agh.to.bgg.exception;

public class AccessDeniedRuntimeException extends RuntimeException {

    public AccessDeniedRuntimeException() {
        super("You have no access");
    }

    public AccessDeniedRuntimeException(String message) {
        super(message);
    }
}
