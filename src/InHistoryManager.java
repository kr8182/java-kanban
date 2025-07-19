import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void addHistory(Task task) {
        Node node = new Node(task, null, null);
        if (task != null) {
            // Убираем из истории
            remove(task.getTaskId());
            // Добавляем в конец
            linkLast(node);
            // Добавляем в map
            nodeMap.put(task.getTaskId(), node);
        }
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> historyTasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            historyTasks.add(node.task);
            node = node.next;
        }
        return historyTasks;
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.get(id);
        if (node != null) {
            removeNode(node);
        }
    }

    void removeNode(Node node) {
        nodeMap.remove(node.task.getTaskId());
    }

    private void linkLast(Node node) {
        if (tail == null) {
            head = node;
        } else {
            node.prev = tail;
            tail.next = node;
        }
        tail = node;
    }

    // Объявляем класс нода
    private static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }
}