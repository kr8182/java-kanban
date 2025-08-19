import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected int taskId;
    protected TaskType taskType;
    protected String taskName;
    protected String taskDescription;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(int taskId, String taskName, String taskDescription, TaskStatus status) {
        this.taskId = taskId;
        this.taskType = TaskType.TASK;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.startTime = LocalDateTime.now();
    }

    public Task(int taskId, String taskName, String taskDescription) {
        this.taskId = taskId;
        this.taskType = TaskType.TASK;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = TaskStatus.NEW;
        this.startTime = LocalDateTime.now();
    }

    public static boolean isTasksOverlap(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null
                || task1.getDuration() == null || task2.getDuration() == null) {
            return false; // Если нет времени начала или продолжительности - не проверяем
        }

        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = start1.plus(task1.getDuration());
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = start2.plus(task2.getDuration());

        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    // Геттеры и сеттеры
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public TaskType getTaskType() {
        return taskType;
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return taskId +
                "," + taskType +
                "," + taskName +
                "," + taskDescription +
                "," + status +
                "," + duration +
                "," + startTime +
                "," + getEndTime();
    }
}