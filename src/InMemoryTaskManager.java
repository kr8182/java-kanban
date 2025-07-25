import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int nextId = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subtasks = new HashMap<>();


    //Метод получения списка всх объектов в трекере
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        for (Epic epic : epics.values()) {
            updateEpicStatus(epic.getTaskId());
        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //Метод удаления всех задач.
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subtasks.clear();
    }


    // Дополнительный метод: получение подзадач эпика
    @Override
    public List<SubTask> getSubtasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return Collections.emptyList();
        }

        List<SubTask> result = new ArrayList<>();
        for (int subtaskId : epic.getSubtaskIds()) {
            SubTask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                result.add(subtask);
            }
        }
        return result;
    }

    //Метод создания таски
    @Override
    public void createTask(Task task) {
        task.setTaskId(generateId());
        tasks.put(task.getTaskId(), task);
    }

    //Метод создания Эпика
    @Override
    public void createEpic(Epic epic) {
        epic.setTaskId(generateId());
        epics.put(epic.getTaskId(), epic);
    }

    //Метод создания сабтаски
    @Override
    public void createSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        if (!epics.containsKey(epicId)) {
            throw new IllegalArgumentException("Epic not found: " + epicId);
        }

        subTask.setTaskId(generateId());
        subtasks.put(subTask.getTaskId(), subTask);

        Epic epic = epics.get(epicId);
        epic.addSubtaskId(subTask.getTaskId());
        updateEpicStatus(epicId);
    }

    @Override
    public Task getTask(int id) {
        return tasks.get(id);
    }

    @Override
    public SubTask getSubtask(int id) {

        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            updateEpicStatus(epic.getTaskId()); // Обновляем статус перед возвратом
        }
        return epic;
    }

    //Метод удаления таски
    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    //Метод удаления Эпика
    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            // Удаляем все подзадачи этого эпика
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    //Метод удаления сабтаски
    @Override
    public void deleteSubTask(int id) {
        SubTask subTask = subtasks.remove(id);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic.getTaskId());
            }
        }
    }


    //Приватный метод перемещен ниже публичных
    private int generateId() {
        return nextId++;
    }

    //Метод обновления статуса эпика
    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<SubTask> epicSubTasks = getSubtasksByEpicId(epicId);
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
}
