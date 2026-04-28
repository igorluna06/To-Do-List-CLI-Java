package model;
import model.enums.Status;
import model.exceptions.InvalidTaskDescriptionException;
import model.exceptions.InvalidTaskTitleException;
import model.exceptions.InvalidTaskUpdatedDateException;
import java.time.LocalDateTime;


public class Task {

    private int id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime  updatedAt = null;
    private Status status;

    public Task(){}

    public Task(int id,String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    public Task(int id, String title){
        this.id = id;
        this.title = title;
        this.createdAt = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title.isBlank()) {
            throw new InvalidTaskTitleException();
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description.isBlank()) {
            throw new InvalidTaskDescriptionException();
        }
        this.description = description;
    }

    public LocalDateTime  getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime  getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime  updatedAt) {
        if(updatedAt.isBefore(this.updatedAt)){
            throw new InvalidTaskUpdatedDateException();
        }
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    public Status getStatus() {
        return status;
    }

    public void isDone(){
        this.status = Status.DONE;
    }

    public void isPending(){
        this.status = Status.PENDING;
    }
}
