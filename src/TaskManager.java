import java.util.*;

public class TaskManager {

    private int nextId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subtasks = new HashMap<>();

    private int generateID() {
        return nextId++;
    }

    //Метод получения списка всх объектов в трекере
    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        ;
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    //Метод удаления всех задач.
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

    //Метод создания таски
    public void createTask(Task task) {
        task.setTaskId(generateID());
        tasks.put(task.getTaskId(), task);
    }

    //Метод создания Эпика
    public void createEpic(Epic epic) {
        epic.setTaskId(generateID());
        epics.put(epic.getTaskId(), epic);
    }

    //Метод создания сабтаски
    public void createSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        if (epics.containsKey(epicId)) {
            subTask.setTaskId(generateID());
            subtasks.put(subTask.getTaskId(), subTask);

            Epic epic = epics.get(epicId);
            epic.addSubTask(subTask);
            updateEpicStatus(epicId);
        }
    }

    //Метод обновления статуса эпика
    protected void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<SubTask> epicSubTasks = epic.getSubTasks();
        if (epicSubTasks.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for (SubTask subTask : epicSubTasks) {
            if (subTask.getStatus() != TaskStatus.DONE) {
                allDone = false;
            }
            if (subTask.getStatus() != TaskStatus.NEW) {
                allNew = false;
            }
        }

        if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    //Метод удаления таски
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    //Метод удаления Эпика
    public void deleteEpic(int epicId) {
        Epic epic = epics.remove(epicId);
        if (epic == null) return;

        for (SubTask subtask : epic.getSubTasks()) {
            subtasks.remove(subtask.getTaskId());
        }
    }

    //Метод удаления сабтаски
    public void deleteSubtask(int subtaskId) {
        SubTask subtask = subtasks.remove(subtaskId);
        if (subtask == null) return;

        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubTask(subtask);
        updateEpicStatus(epic.getTaskId());
    }
}