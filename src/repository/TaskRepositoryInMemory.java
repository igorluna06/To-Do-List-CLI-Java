package repository;
import model.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskRepositoryInMemory implements TaskRepository {

    private final ArrayList<Task> tasks;

    public TaskRepositoryInMemory(){
        this.tasks = new ArrayList<>();
    }

    public void save(Task task){
        this.tasks.add(task);
    }

    @Override
    public List<Task> findAll() {
        return List.copyOf(this.tasks);
    }

    @Override
    public Task findById(int id) {
        return this.tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Task task) {
        final Task updatedTask = this.tasks.stream()
                .filter(t -> t.getId() == task.getId())
                .findFirst()
                .orElse(null);

        this.tasks.set(this.tasks.indexOf(updatedTask), task);
    }

    @Override
    public void delete(int id) {
        this.tasks.removeIf(task -> task.getId() == id);
    }

}
