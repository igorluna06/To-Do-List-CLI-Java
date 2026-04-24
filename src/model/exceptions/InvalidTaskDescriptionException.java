package model.exceptions;

public class InvalidTaskDescriptionException extends RuntimeException {
    public InvalidTaskDescriptionException() {
        super("Description cannot be blank");
    }
}
