import interfaces.HistoryManager;
import manager.InHistoryManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* Тест проверяет, что утилитарный класс всегда возвращает
проинициализированные и готовые к работе экземпляры менеджеров;
*/

class ManagersTest extends Managers {

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