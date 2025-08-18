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
        Set<Integer> affectedEpics = new HashSet<>();
        for (SubTask subTask : subtasks.values()) {
            affectedEpics.add(subTask.getEpicId());
        }

        subtasks.clear();

        for (int epicId : affectedEpics) {
            Epic epic = epics.get(epicId);
            if (epic != null) {
                epic.getSubtaskIds().clear();
                updateEpicStatus(epicId);
                updateEpicTime(epicId); // Добавлен вызов
            }
        }
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
        if (isTaskOverlappingAny(task)) {
            throw new ManagerValidationException("Задача пересекается по времени с существующей");
        }
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
        if (isTaskOverlappingAny(subTask)) {
            throw new ManagerValidationException("Подзадача пересекается по времени с существующей");
        }
        subTask.setTaskId(generateId());
        subtasks.put(subTask.getTaskId(), subTask);

        Epic epic = epics.get(epicId);
        epic.addSubtaskId(subTask.getTaskId());
        updateEpicTime(epicId);
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
                updateEpicTime(epic.getTaskId()); // Добавлен вызов
            }
        }

    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        Comparator<Task> comparator = Comparator.comparing(
                Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())
        ).thenComparing(Task::getTaskId);

        TreeSet<Task> sortedSet = new TreeSet<>(comparator);
        sortedSet.addAll(tasks.values());
        sortedSet.addAll(epics.values());
        sortedSet.addAll(subtasks.values());

        return sortedSet;
    }

    @Override
    public List<Task> getAll() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    private boolean isTaskOverlappingAny(Task taskToCheck) {
        if (taskToCheck.getStartTime() == null) {
            return false; // Задачи без времени не проверяем
        }

        return getAllTasks().stream()
                .filter(task -> task.getTaskId() != taskToCheck.getTaskId()) // Исключаем текущую задачу
                .filter(task -> task.getStartTime() != null) // Фильтруем задачи с временем
                .anyMatch(existingTask -> Task.isTasksOverlap(taskToCheck, existingTask));
    }

    private void updateEpicTime(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<SubTask> epicSubTasks = getSubtasksByEpicId(epicId);
        epic.updateTime(epicSubTasks);
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
