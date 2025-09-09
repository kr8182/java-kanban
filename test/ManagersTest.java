import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.InHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/* Тест проверяет, что утилитарный класс всегда возвращает
проинициализированные и готовые к работе экземпляры менеджеров;
*/

class ManagersTest extends Managers {


    @Test
    void testGetDefault() throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();

        // Проверяем, что экземпляр не null
        assertNotNull(taskManager, "interfaces.TaskManager не должен быть null");

        // Проверяем, что возвращается именно manager.InMemoryTaskManager
        assertTrue(taskManager instanceof InMemoryTaskManager,
                "Должен возвращаться экземпляр manager.InMemoryTaskManager");
    }

    @Test
    void testGetDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        // Проверяем, что экземпляр не null
        assertNotNull(historyManager, "interfaces.HistoryManager не должен быть null");

        // Проверяем, что возвращается именно manager.InHistoryManager
        assertTrue(historyManager instanceof InHistoryManager,
                "Должен возвращаться экземпляр manager.InHistoryManager");
    }
}