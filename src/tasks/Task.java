package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected Integer taskId;
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

    public Task(Integer taskId, String taskName, TaskStatus status, String details, Integer duration, String startTime) {
        this.taskName = taskName;
        this.taskDescription = details;
        this.taskId = taskId;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("dd_MM_yyyy|HH:mm"));
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
        return String.format("%d,%S,%s,%s,%s,%d,%s", taskId, this.getClass().getSimpleName(), taskName, status, taskDescription, duration.toMinutes(), startTime.format(DateTimeFormatter.ofPattern("dd_MM_yyyy|HH:mm")));
    }
}