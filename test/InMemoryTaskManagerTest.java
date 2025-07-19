import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    static TaskManager manager;

    /*Тестовые сценарии ниже тестрируют, что проверьте, что InMemoryTaskManager действительно
     добавляет задачи разного типа и может найти их по id;

     проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера; -
     Данное условие нивелируется приватным методом generateId() - проверка избыточна
     */

    @BeforeAll
    static void beforeAll() {
        manager = Managers.getDefault();

        Epic epic = new Epic(0, "Эпик 1", "Эпик 1_Тест");
        Epic epic2 = new Epic(1, "Эпик 2", "Эпик 2_Тест");

        Task a = new Task(0, "Таска1", "Таска 1_Тест");
        Task b = new Task(1, "Таска2", "Таска 2_Тест");

        manager.createEpic(epic);
        manager.createEpic(epic2);
        int epicId = epic.getTaskId();

        SubTask subtask1 = new SubTask(1, "СабТаска1", "СабТаска1_Тест", epicId);

        manager.createTask(a);
        manager.createTask(b);

        manager.createSubTask(subtask1);
    }


    @Test
    void getTask() {
        assertEquals("[Task{taskId=3, taskName='Таска1', taskDescription='Таска 1_Тест', " +
                "status=NEW}, Task{taskId=4, taskName='Таска2', " +
                "taskDescription='Таска 2_Тест', status=NEW}]", manager.getAllTasks().toString());
    }

    @Test
    void getSubtask() {
        assertEquals("[SubTask{taskId=5, taskName='СабТаска1', " +
                        "taskDescription='СабТаска1_Тест', status=NEW, epicId=1}]",
                manager.getAllSubtasks().toString());
    }

    @Test
    void getEpic() {
        assertEquals("[Epic{taskId=1, taskName='Эпик 1', taskDescription='Эпик 1_Тест', " +
                        "status=NEW, subtaskIds=[5]}, " +
                        "Epic{taskId=2, taskName='Эпик 2', taskDescription='Эпик 2_Тест', status=NEW, subtaskIds=[]}]",
                manager.getAllEpics().toString());
    }

    @Test
    void getTaskById() {
        assertNotNull(manager.getTask(3));
    }

    @Test
    void getEpicById() {
        assertNotNull(manager.getEpic(1));
    }

    @Test
    void getSubTaskById() {
        assertNotNull(manager.getSubtask(5));
    }

}