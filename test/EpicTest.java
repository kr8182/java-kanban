import interfaces.TaskManager;
import manager.HttpTaskServer;
import manager.KVServer;
import manager.Managers;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskStatus;

import java.io.IOException;

class EpicTest {

    TaskManager taskManager;
    HttpTaskServer server;
    Epic epic;

    static KVServer kvServer;

    @BeforeAll
    static void beforeAll() throws IOException {
        kvServer= new KVServer();
        kvServer.start();
    }

    @AfterAll
    static void afterAll() {
        kvServer.stop();
    }

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        taskManager = Managers.getDefault();
        server = new HttpTaskServer();
        server.start();
        epic = new Epic(1, "testEpic", TaskStatus.NEW, "test details", 33, "22_02_2022|22:23");
        taskManager.createEpic(epic);
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }

    @Test
    void shouldReturnNewStatusForEmptyEpic() {
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void shouldReturnNewStatusForEpicWithNewSubtasks() {
        SubTask subtask1 = new SubTask(2, "testSubtask", TaskStatus.NEW, "test details", 33, "23_02_2022|22:23", 1);
        SubTask subtask2 = new SubTask(3, "testSubtask", TaskStatus.NEW, "test details", 33, "24_02_2022|22:23", 1);
        taskManager.createSubTask(subtask1, 1);
        taskManager.createSubTask(subtask2, 2);
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void shouldReturnDoneStatusForEpicWithDoneSubtasks() {
        SubTask subtask1 = new SubTask(2, "testSubtask", TaskStatus.DONE, "test details", 33, "23_02_2022|22:23", 1);
        SubTask subtask2 = new SubTask(3, "testSubtask", TaskStatus.DONE, "test details", 33, "24_02_2022|22:23", 1);
        taskManager.createSubTask(subtask1, 3);
        taskManager.createSubTask(subtask2, 4);
        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus());
    }
}