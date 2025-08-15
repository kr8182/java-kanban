import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected int nextId = 1;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, SubTask> subtasks = new HashMap<>();


    //Метод получения списка всх объектов в трекере
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        epics.values().stream().mapToInt(Task::getTaskId).forEach(this::updateEpicStatus);
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
        return Optional.ofNullable(epics.get(epicId))
                .map(Epic::getSubtaskIds)
                .map(ids -> ids.stream()
                        .map(subtasks::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
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
            epic.getSubtaskIds().forEach(subtaskId -> subtasks.remove(subtaskId));
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

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        allTasks.stream()
                .sorted(Comparator.comparing(Task::getStartTime));
        return allTasks;
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
