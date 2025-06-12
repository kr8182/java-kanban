import java.util.*;

public class TaskManager {

    private int nextId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subtasks = new HashMap<>();

    private int generateID() {
        return nextId++;
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        ;
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    // Дополнительный метод: получение подзадач эпика
    public Task getSubtasksByEpicId(int epicId) {
        if (tasks.containsKey(epicId)) return tasks.get(epicId);
        if (epics.containsKey(epicId)) return epics.get(epicId);
        return subtasks.get(epicId);
    }

    public void createTask(Task task) {
        task.setTaskId(generateID());
        tasks.put(task.getTaskId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setTaskId(generateID());
        epics.put(epic.getTaskId(), epic);
    }

    public void createSubTask(SubTask subtask) {
        int epicId = subtask.getEpicId();
        if (!epics.containsKey(epicId)) {
            return;
        }
        subtask.setTaskId(generateID());
        subtasks.put(subtask.getTaskId(), subtask);
        epics.get(epicId).addSubTask(subtask);
        updateEpicStatus(epicId);
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null || epic.getSubtasks().isEmpty()) {
            epic.setEpicStatus(TaskStatus.NEW);
            return;
        }
        boolean allDone = true;
        boolean allNew = true;

        for (SubTask subtask : epic.getSubtasks()) {
            if (subtask.getSubTaskStatus() != TaskStatus.DONE) {allDone = false;}
            if (subtask.getSubTaskStatus() != TaskStatus.NEW) {allNew = false;}
        }

        if (allNew) {
            epic.setEpicStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setEpicStatus(TaskStatus.DONE);
        } else {
            epic.setEpicStatus(TaskStatus.IN_PROGRESS);
            }
        }
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpic(int epicId) {
        Epic epic = epics.remove(epicId);
        if (epic == null) return;

        for (SubTask subtask : epic.getSubtasks()) {
            subtasks.remove(subtask.getTaskId());
        }
    }

    public void deleteSubtask(int subtaskId) {
        SubTask subtask = subtasks.remove(subtaskId);
        if (subtask == null) return;

        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubTask(subtask);
        updateEpicStatus(epic.getTaskId());
        }
}

