package manager;

import exceptions.InvalidTaskTime;
import exceptions.ManagerValidationException;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected int nextId = 1;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, SubTask> subtasks = new HashMap<>();
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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

    //Метод получения списка всх объектов в трекере
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        epics.values().stream()
                .mapToInt(Task::getTaskId)
                .forEach(this::updateEpicStatus);
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
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
    public void createTask(Task task) throws IOException {
        if (isTaskOverlappingAny(task)) {
            throw new ManagerValidationException("Задача пересекается по времени с существующей");
        }
        task.setTaskId(generateId());
        tasks.put(task.getTaskId(), task);
    }

    //Метод создания Эпика
    @Override
    public void createEpic(Epic epic) throws IOException {
        epic.setTaskId(generateId());
        epics.put(epic.getTaskId(), epic);
        updateEpicStatus(epic.getTaskId());
    }

    //Метод создания сабтаски
    @Override
    public void createSubTask(SubTask subTask) throws IOException {
        int epicId = subTask.getEpicId();
        if (!epics.containsKey(epicId)) {
            throw new IllegalArgumentException("tasks.Epic not found: " + epicId);
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
    public void createSubTask(SubTask subTask, int taskId) {
        taskId = generateId();
        int epicId = subTask.getEpicId();
        if (!epics.containsKey(epicId)) {
            throw new IllegalArgumentException("tasks.Epic not found: " + epicId);
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
    public void deleteTask(int taskId) throws IOException {
        tasks.remove(taskId);
    }

    //Метод удаления Эпика
    @Override
    public void deleteEpic(int id) throws IOException {
        Epic epic = epics.remove(id);
        if (epic != null) {
            // Удаляем все подзадачи этого эпика
            epic.getSubtaskIds().forEach(subtaskId -> subtasks.remove(subtaskId));
        }
    }

    //Метод удаления сабтаски
    @Override
    public void deleteSubTask(int id) throws IOException {
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
                .anyMatch(existingTask -> isTasksOverlap(taskToCheck, existingTask));
    }

    private void updateEpicTime(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<SubTask> epicSubTasks = getSubtasksByEpicId(epicId);
        epic.updateTime(epicSubTasks);
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public ArrayList<Integer> getTasksIdList() {
        return new ArrayList<>(tasks.keySet());
    }

    @Override
    public void validateTask(Task task) throws InvalidTaskTime {
        for (Task task1 : prioritizedTasks) {
            if ((task.getStartTime().isBefore(task1.getStartTime()) && (task.getEndTime().isAfter(task1.getStartTime()))) ||
                    (task.getStartTime().isBefore(task1.getEndTime()) && (task.getEndTime().isAfter(task1.getEndTime())))) {
                throw new InvalidTaskTime("Invalid task time");

            }
        }

    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void updateTask(Task task, Integer id) throws IOException {
        try {
            validateTask(task);
            task.setTaskId(id);
            prioritizedTasks.remove(tasks.get(id));
            tasks.put(id, task);
            prioritizedTasks.add(task);
        } catch (InvalidTaskTime invalidTaskTime) {
            System.out.println(invalidTaskTime.getMessage());
        }
    }

    @Override
    public List<Integer> getEpicsIdList() {
        return new ArrayList<>(tasks.keySet());
    }

    @Override
    public void updateSubtask(SubTask subtask, Integer id) throws IOException {
        try {
            validateTask(subtask);
            subtask.setTaskId(id);
            prioritizedTasks.remove(subtasks.get(id));
            subtasks.put(id, subtask);
            epics.get(subtask.getEpicId());
            validateTask(epics.get(subtask.getEpicId()));
            prioritizedTasks.add(subtask);
        } catch (InvalidTaskTime invalidTaskTime) {
            System.out.println(invalidTaskTime.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic epic, Integer id) throws IOException {
        try {
            validateTask(epic);
            epic.setTaskId(id);
            prioritizedTasks.remove(epics.get(id));
            epics.put(id, epic);
            prioritizedTasks.add(epic);
        } catch (InvalidTaskTime invalidTaskTime) {
            System.out.println(invalidTaskTime.getMessage());
        }

    }

    @Override
    public void deleteEverything() throws IOException {
        tasks.clear();
        subtasks.clear();
        epics.clear();
        inMemoryHistoryManager = Managers.getDefaultHistory();
    }

    @Override
    public void deleteAllTasks() throws IOException {
        for (Integer taskID : tasks.keySet()) {
            prioritizedTasks.remove(tasks.get(taskID));
            inMemoryHistoryManager.remove(taskID);
        }

        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() throws IOException {
        for (SubTask subtask : subtasks.values()) {
            if ((epics.get(subtask.getEpicId()) != null)) {
                epics.get(subtask.getEpicId()).subtasksList.remove(subtask.getTaskId());
            }
        }
        for (Integer subtaskID : subtasks.keySet()) {
            prioritizedTasks.remove(subtasks.get(subtaskID));
            inMemoryHistoryManager.remove(subtaskID);
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() throws IOException {
        for (Epic epic : epics.values()) {
            for (Integer id : epic.getSubtaskIds()) {
                prioritizedTasks.remove(subtasks.get(id));
                subtasks.remove(id);
            }
        }
        for (Integer epicID : epics.keySet()) {
            prioritizedTasks.remove(epics.get(epicID));
            inMemoryHistoryManager.remove(epicID);
        }
        epics.clear();
    }

    @Override
    public void clearHistory() {
        this.inMemoryHistoryManager.deleteHistory();
    }

    @Override
    public void deleteEpicSubtasks(Integer id) throws IOException {
        for (Integer subtaskID : (epics.get(id)).subtasksList.keySet()) {
            prioritizedTasks.remove(subtasks.get(subtaskID));
            subtasks.remove(subtaskID);
            inMemoryHistoryManager.remove(subtaskID);
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
