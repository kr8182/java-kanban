import java.util.List;

public interface TaskManager {
    //Метод получения списка всх объектов в трекере
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubtasks();

    //Метод удаления всех задач.
    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    // Дополнительный метод: получение подзадач эпика
    List<SubTask> getSubtasksByEpicId(int epicId);

    //Метод создания таски
    void createTask(Task task);

    //Метод создания Эпика
    void createEpic(Epic epic);

    //Метод создания сабтаски
    void createSubTask(SubTask subTask);

    //Метод удаления таски
    void deleteTask(int taskId);

    //Метод удаления Эпика
    void deleteEpic(int id);

    //Метод удаления сабтаски
    void deleteSubTask(int id);

    //Метод просмотра таски
    Task getTask(int id);

    //Метод просмотра сабтаски
    SubTask getSubtask(int id);

    //Метод просмотра эпика
    Epic getEpic(int id);
}
