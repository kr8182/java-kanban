import java.util.ArrayList;
import java.util.List;

public class InHistoryManager implements HistoryManager {
    protected List<Task> historyTasks = new ArrayList<>();

    @Override
    public List<Task> addHistory(Task task) {
        if (historyTasks.size() >= 9) {
            historyTasks.remove(0);
        }
        historyTasks.add(task);
        return historyTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }

}
