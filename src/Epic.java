import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private TaskStatus epicStatus;
    private List<SubTask> subtasksId = new ArrayList<>();


    Epic(int taskId, String taskName, String taskDescription) {
        super(taskId, taskName, taskDescription);
        this.epicStatus = TaskStatus.NEW;
    }

    public List<SubTask> getSubtasksId() {
        return subtasksId;
    }

    public void addSubTask(SubTask subtask) {
        subtasksId.add(subtask);
    }

    public void removeSubTask(SubTask subtask) {
        subtasksId.remove(subtask);
    }
}
