package interfaces;

import exceptions.InvalidTaskTime;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    //Метод получения списка всх объектов в трекере
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubtasks();

    //Метод удаления всех задач.
    void deleteAllTasks() throws IOException;

    void deleteAllEpics() throws IOException;

    void deleteAllSubTasks();

    // Дополнительный метод: получение подзадач эпика
    List<SubTask> getSubtasksByEpicId(int epicId);

    //Метод создания таски
    void createTask(Task task) throws IOException;

    //Метод создания Эпика
    void createEpic(Epic epic) throws IOException;

    //Метод создания сабтаски
    void createSubTask(SubTask subTask) throws ManagerSaveException, IOException;

    void createSubTask(SubTask subTask, int taskId);

    //Метод удаления таски
    void deleteTask(int taskId) throws IOException;

    //Метод удаления Эпика
    void deleteEpic(int id) throws IOException;

    //Метод удаления сабтаски
    void deleteSubTask(int id) throws IOException;

    //Метод просмотра таски
    Task getTask(int id);

    //Метод просмотра сабтаски
    SubTask getSubtask(int id);

    //Метод просмотра эпика
    Epic getEpic(int id);

    //Метод вывода приоритезированных задач
    TreeSet<Task> getPrioritizedTasks();

    List<Task> getAll();

    List<Task> getHistory();

    ArrayList<Integer> getTasksIdList();

    void validateTask(Task task) throws InvalidTaskTime, InvalidTaskTime;

    void updateTask(Task task, Integer id) throws IOException;

    void updateSubtask(SubTask subtask, Integer id) throws IOException;

    void updateEpic(Epic epic, Integer id) throws IOException;

    List<Integer> getEpicsIdList();

    void deleteEverything() throws IOException;

    void deleteAllSubtasks() throws IOException;

    void deleteEpicSubtasks(Integer id) throws IOException;

    void clearHistory() throws IOException;

}
