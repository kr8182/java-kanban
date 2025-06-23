import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InHistoryManagerTest extends InHistoryManager {

    private HistoryManager historyManager;

    @BeforeEach
    void beforeAll() {
        historyManager = Managers.getDefaultHistory();

        Epic epic = new Epic(0, "Эпик 1", "Эпик 1_Тест");
        historyManager.addHistory(epic);
    }


    @Test
    void testAddHistory() {
        assertNotNull(historyManager.getHistory());
    }

    @Test
    void testGetHistory() {
        assertNotNull(historyManager.getHistory());
        assertEquals("[Epic{taskId=0, taskName='Эпик 1', taskDescription='Эпик 1_Тест', " +
                "status=NEW, subtaskIds=[]}]", historyManager.getHistory().toString());
    }
}