import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Тест проверяет, что утилитарный класс всегда возвращает
проинициализированные и готовые к работе экземпляры менеджеров;
*/

class ManagersTest extends Managers {


    @Test
    void testGetDefault() {
        TaskManager taskManager = Managers.getDefault();

        // Проверяем, что экземпляр не null
        assertNotNull(taskManager, "TaskManager не должен быть null");

        // Проверяем, что возвращается именно InMemoryTaskManager
        assertTrue(taskManager instanceof InMemoryTaskManager,
                "Должен возвращаться экземпляр InMemoryTaskManager");
    }

    @Test
    void testGetDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        // Проверяем, что экземпляр не null
        assertNotNull(historyManager, "HistoryManager не должен быть null");

        // Проверяем, что возвращается именно InHistoryManager
        assertTrue(historyManager instanceof InHistoryManager,
                "Должен возвращаться экземпляр InHistoryManager");
    }
}