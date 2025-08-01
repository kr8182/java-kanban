import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InHistoryManagerTest extends InHistoryManager {

    private HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }


    @Test
    void testAddHistory() {
        beforeEach();
        Epic epic = new Epic(0, "Эпик 1", "Эпик 1_Тест");
        historyManager.addHistory(epic);
        assertNotNull(historyManager.getHistory());
    }

    @Test
    void testGetHistory() {
        beforeEach();
        Epic epic = new Epic(0, "Эпик 1", "Эпик 1_Тест");
        historyManager.addHistory(epic);
        assertNotNull(historyManager.getHistory());
        assertEquals("[0,EPIC,Эпик 1,Эпик 1_Тест,NEW]", historyManager.getHistory().toString());
    }

    @Test
    void testDeleteFromHistory() {
        List<Task> tasks = new ArrayList<>(5);

        for (int i = 0; i < 3; i++) {
            tasks.add(new Task(i, "Task " + i, "Description"));
        }

        tasks.forEach(historyManager::addHistory);
        historyManager.remove(1);
        assertEquals(tasks, historyManager.getHistory(), "Задача не удалилась");
    }

    @Test
    void testDeleteFromHistoryButNotFromTasks() {
        List<Task> tasks = new ArrayList<>(5);

        for (int i = 0; i < 3; i++) {
            tasks.add(new Task(i, "Task " + i, "Description"));
        }

        tasks.forEach(historyManager::addHistory);

        tasks.remove(0);
        assertEquals("[0,TASK,Task 0,Description,NEW, 1,TASK,Task 1,Description,NEW, " +
                "2,TASK,Task 2,Description,NEW]", historyManager.getHistory().toString());
    }
}