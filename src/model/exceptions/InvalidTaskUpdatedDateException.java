package model.exceptions;

public class InvalidTaskUpdatedDateException extends RuntimeException {
    public InvalidTaskUpdatedDateException() {
        super("Updated date cannot be before the current updated date");
    }
}
