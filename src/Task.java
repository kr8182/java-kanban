public class Task {
    protected int taskId;
    protected TaskType taskType;
    protected String taskName;
    protected String taskDescription;
    protected TaskStatus status;

    public Task(int taskId, String taskName, String taskDescription) {
        this.taskId = taskId;
        this.taskType = TaskType.TASK;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = TaskStatus.NEW;
    }

    // Геттеры и сеттеры
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return taskId +
                "," + taskType +
                "," + taskName +
                "," + taskDescription +
                "," + status;
    }
}