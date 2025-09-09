package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();
    public HashMap<Integer, SubTask> subtasksList = new HashMap<>();
    private LocalDateTime endTime;

    public Epic(int taskId, String taskName, String taskDescription) {
        super(taskId, taskName, taskDescription);
        this.taskType = TaskType.EPIC;
        this.startTime = null;
        this.duration = null;
        this.endTime = null;
    }

    public Epic(int taskId, String taskName, String taskDescription, TaskStatus status) {
        super(taskId, taskName, taskDescription);
        this.taskType = TaskType.EPIC;
        this.startTime = null;
        this.duration = null;
        this.endTime = null;
    }

    public Epic(Integer id, String name, TaskStatus status, String details, Integer duration, String startTime) {
        super(id, name, status, details, duration, startTime);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        if (!subtaskIds.contains(subtaskId)) {
            subtaskIds.add(subtaskId);
        }
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId);
    }

    public void updateTime(List<SubTask> subTasks) {
        if (subTasks == null || subTasks.isEmpty()) {
            this.startTime = null;
            this.duration = null;
            this.endTime = null;
            return;
        }

        LocalDateTime newStartTime = subTasks.stream()
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);

        LocalDateTime newEndTime = subTasks.stream()
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);

        Duration newDuration = subTasks.stream()
                .map(SubTask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        this.startTime = newStartTime;
        this.duration = newDuration;
        this.endTime = newEndTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

}