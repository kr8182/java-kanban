package interfaces;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    //Метод получения истории
    List<Task> getHistory();

    //Метод добавления нового элемента в историю
    void addHistory(Task task);

    //Метод удаления сущности tasks.Task по id
    void remove(int id);

    void deleteHistory();

    ArrayList<Task> getTasks();
}
