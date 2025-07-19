import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
    }

    //Тест проверяет, что экземпляры и наследники класса Task равны друг другу, если равен их id.;
    @Test
    void testGetTaskId() {
        Task a = new Task(0, "Таска1", "Таска 1_Тест");
        Task b = new Task(1, "Таска2", "Таска 2_Тест");
        manager.createTask(a);
        manager.createTask(b);
        assertNotEquals(a.toString(), b.toString());
    }
}