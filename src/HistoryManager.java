import java.util.List;

public interface HistoryManager {
    //Метод получения истории
    List<Task> getHistory();

    //Метод добавления нового элемента в историю
    List<Task> addHistory(Task task);
}
