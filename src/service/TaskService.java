package service;
import model.Task;
import model.exceptions.InvalidTaskDescriptionException;
import model.exceptions.InvalidTaskIdException;
import model.exceptions.InvalidTaskTitleException;
import model.exceptions.TaskNotFoundException;
import repository.TaskRepository;
import utils.IdGenerator;

import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;
    private final IdGenerator idGenerator;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.idGenerator = new IdGenerator();
    }

    public void createTask(String title, String description) {
        if(title.isBlank()) {
            throw new InvalidTaskTitleException();
        }
        if(description.isBlank()) {
            throw new InvalidTaskDescriptionException();
        }
        Task task = new Task(idGenerator.generateId(), title, description);
        taskRepository.save(task);
    }

    public void createTask(String title){
        if(title.isBlank()) {
            throw new InvalidTaskTitleException();
        }
        Task task = new Task(idGenerator.generateId(),title);
        taskRepository.save(task);
    }

    public Task getTaskById(int id) {
        if(id < 1 || id > idGenerator.getCurrentId()) {
            throw new InvalidTaskIdException(id);
        }
        final Task taskFound = taskRepository.findById(id);
        if(taskFound == null){
            throw new TaskNotFoundException(id);
        }
        return taskFound;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    private void updateTask(Task task) {
        if (task.getTitle().isBlank()) {
            throw new InvalidTaskTitleException();
        }
        if (task.getDescription() != null && task.getDescription().isBlank()) {
            throw new InvalidTaskDescriptionException();
        }
        task.setUpdatedAt();
        taskRepository.update(task);
    }

    public void changeTitle(int id, String newTitle) {
        if(id < 1 || id > idGenerator.getCurrentId()) {
            throw new InvalidTaskIdException(id);
        }
        final Task task = this.getTaskById(id);
        task.setTitle(newTitle);
        this.updateTask(task);
    }

    public void changeDescription(int id, String newDescription) {
        if(id < 1 || id > idGenerator.getCurrentId()) {
            throw new InvalidTaskIdException(id);
        }
        final Task task = this.getTaskById(id);
        task.setDescription(newDescription);
        this.updateTask(task);
    }

    public void taskIsDone(int id)
    {
        if(id < 1 || id > idGenerator.getCurrentId()) {
            throw new InvalidTaskIdException(id);
        }
        final Task task = this.getTaskById(id);
        task.isDone();
        this.updateTask(task);
    }

    public void taskIsPending(int id)
    {
        if(id < 1 || id > idGenerator.getCurrentId()) {
            throw new InvalidTaskIdException(id);
        }
        final Task task = this.getTaskById(id);
        task.isPending();
        this.updateTask(task);
    }

    public void deleteTask(int id) {
        if(id < 1 || id > idGenerator.getCurrentId()) {
            throw new InvalidTaskIdException(id);
        }
        taskRepository.delete(id);
    }

}
