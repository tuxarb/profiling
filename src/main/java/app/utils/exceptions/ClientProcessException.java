package app.utils.exceptions;


public class ClientProcessException extends RuntimeException{
    public ClientProcessException(String message) {
        super(message);
    }

    public ClientProcessException() {
    }
}
