import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<SubTask> subtasksId = new ArrayList<>();


    Epic(int taskId, String taskName, String taskDescription) {
        super(taskId, taskName, taskDescription);
    }

    public List<SubTask> getSubTasks() {
        return subtasksId;
    }

    public void addSubTask(SubTask subtask) {
        subtasksId.add(subtask);
    }

    public void removeSubTask(SubTask subtask) {
        subtasksId.remove(subtask);
    }

    //Переопрределяем toString для корректного отображения
    @Override
    public String toString() {
        return "Epic{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status=" + status +
                ", subTasks=" + subtasksId +
                '}';
    }
}

