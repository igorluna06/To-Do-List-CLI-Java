package model.exceptions;

public class InvalidTaskIdException extends RuntimeException {
    public InvalidTaskIdException(int id) {
        super("Invalid task id: " + id);
    }
}
