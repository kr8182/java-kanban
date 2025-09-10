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

    public void updateEpic() {
        int newTasks = 0;
        int doneTasks = 0;
        for (SubTask subtask : subtasksList.values()) {
            if (subtask.getStatus() == TaskStatus.NEW) {
                newTasks++;
            } else if (subtask.getStatus() == TaskStatus.DONE) doneTasks++;
        }
        if (subtasksList.size() == newTasks) {
            setStatus(TaskStatus.NEW);
        } else if (subtasksList.size() == doneTasks) {
            setStatus(TaskStatus.DONE);
        } else setStatus(TaskStatus.IN_PROGRESS);

        LocalDateTime startTime = this.startTime;
        LocalDateTime endTime = this.endTime;
        Duration duration = Duration.ofSeconds(0);

        for (SubTask subtask : subtasksList.values()) {
            if (startTime.isAfter(subtask.startTime)) {
                startTime = subtask.startTime;
            }
            if (getEndTime().isBefore(subtask.getEndTime())) {
                endTime = subtask.getEndTime();
            }
            duration = duration.plus(subtask.duration);
        }
        if (duration.isZero()) {
            duration = Duration.between(startTime, endTime);
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

}