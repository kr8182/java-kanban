import java.util.List;

public interface HistoryManager {
    //Метод получения истории
    List<Task> getHistory();

    //Метод добавления нового элемента в историю
    void addHistory(Task task);

    //Метод удаления сущности Task по id
    void remove(int id);
}
