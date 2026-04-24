package model.exceptions;

public class InvalidTaskTitleException extends RuntimeException {
    public InvalidTaskTitleException() {
        super("Title cannot be blank");
    }
}
