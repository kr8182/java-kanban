package tasks;

public class SubTask extends Task {
    private int epicId;

    public SubTask(int taskId, String taskName, String taskDescription, int epicId) {
        super(taskId, taskName, taskDescription);
        this.taskType = TaskType.SUBTASK;
        this.epicId = epicId;
    }

    public SubTask(int taskId, String taskName, String taskDescription, TaskStatus status, int epicId) {
        super(taskId, taskName, taskDescription);
        this.taskType = TaskType.SUBTASK;
        this.epicId = epicId;
        this.status = status;
    }

    public SubTask(Integer id, String name, TaskStatus status, String details, Integer duration, String startTime, Integer epicID) {
        super(id, name, status, details, duration, startTime);
        this.epicId = epicID;
    }

    public int getEpicId() {

        return epicId;
    }
}