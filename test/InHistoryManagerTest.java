import interfaces.HistoryManager;
import manager.InHistoryManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

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
        assertEquals("[0,EPIC,Эпик 1,Эпик 1_Тест,NEW,null,null,null]", historyManager.getHistory().toString());
    }

    @Test
    void testDeleteFromHistory() {
        List<Task> tasks = new ArrayList<>(5);

        for (int i = 0; i < 3; i++) {
            tasks.add(new Task(i, "tasks.Task " + i, "Description"));
        }

        tasks.forEach(historyManager::addHistory);
        historyManager.remove(1);
        assertEquals(tasks, historyManager.getHistory(), "Задача не удалилась");
    }

    @Test
    void testDeleteFromHistoryButNotFromTasks() {
        List<Task> tasks = new ArrayList<>(5);

        for (int i = 0; i < 2; i++) {
            tasks.add(new Task(i, "tasks.Task " + i, "Description"));
        }

        tasks.forEach(historyManager::addHistory);

        tasks.remove(0);
        assertEquals(historyManager.getHistory().toString(),
                historyManager.getHistory().toString());
    }
}