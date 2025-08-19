import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/*Тесты для эпиков. Работоспособность сабтасок проверяется здесь же, поэтому делать отдельный
класс SubTaskTest было бы излишнее, так как для их проверки пришлось бы все равно деллать эпик.
*/

class EpicTest {

    private static FileBackedTaskManager manager;
    private static int epicId;

    @BeforeAll
    public static void beforeAll() {
        manager = Managers.getDefaultFileManager();
        Epic epic = new Epic(0, "Эпик 1", "Эпик 1_Тест");
        manager.createEpic(epic);
        epicId = epic.getTaskId();
    }

    @Test
    void testGetSubtaskIds() {
        Epic epicFromManager = manager.getEpic(epicId);
        SubTask subtask1 = new SubTask(1, "СабТаска1", "СабТаска1_Тест", epicId);
        try {
            manager.createSubTask(subtask1);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        assertEquals("[3, 4, 5, 6]", epicFromManager.getSubtaskIds().toString());
    }

    @Test
    void testAddSubtaskId() {
        Epic epicFromManager = manager.getEpic(epicId);
        SubTask subtask2 = new SubTask(2, "СабТаска1", "СабТаска1_Тест", epicId);
        assertEquals("[3, 4, 5, 6]", epicFromManager.getSubtaskIds().toString());
        try {
            manager.createSubTask(subtask2);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        assertEquals("[3, 4, 5, 6, 7]", epicFromManager.getSubtaskIds().toString());
    }

    @Test
    void testRemoveSubtaskId() {
        Epic epicFromManager = manager.getEpic(epicId);
        SubTask subtask3 = new SubTask(3, "СабТаска1", "СабТаска1_Тест", epicId);
        assertEquals("[]", epicFromManager.getSubtaskIds().toString());
        try {
            manager.createSubTask(subtask3);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        assertEquals("[2]", epicFromManager.getSubtaskIds().toString());
        manager.deleteSubTask(subtask3.getTaskId());
        assertEquals("[]", epicFromManager.getSubtaskIds().toString());
    }

    @Test
    void testEpicStatus() {
        Epic epicFromManager = manager.getEpic(epicId);

        SubTask subtask4 = new SubTask(4, "СабТаска1", "СабТаска1_Тест", epicId);
        SubTask subtask5 = new SubTask(5, "СабТаска2", "СабТаска2_Тест", epicId);
        SubTask subtask6 = new SubTask(6, "СабТаска3", "СабТаска3_Тест", epicId);

        manager.createSubTask(subtask4);
        manager.createSubTask(subtask5);

        subtask4.setDuration(Duration.ofHours(25));
        subtask5.setDuration(Duration.ofHours(49));
        subtask6.setStatus(TaskStatus.IN_PROGRESS);

        manager.createSubTask(subtask6);

        assertEquals("IN_PROGRESS", epicFromManager.getStatus().toString());
    }

    @Test
    void testEpicStatusDone() {
        Epic epicFromManager = manager.getEpic(epicId);

        SubTask subtask7 = new SubTask(7, "СабТаска1", "СабТаска1_Тест", epicId);
        SubTask subtask8 = new SubTask(8, "СабТаска2", "СабТаска2_Тест", epicId);
        SubTask subtask9 = new SubTask(9, "СабТаска3", "СабТаска3_Тест", epicId);

        manager.createSubTask(subtask7);
        manager.createSubTask(subtask8);

        subtask7.setDuration(Duration.ofHours(25));
        subtask8.setDuration(Duration.ofHours(49));
        subtask9.setStatus(TaskStatus.DONE);

        manager.createSubTask(subtask9);

        assertEquals("IN_PROGRESS", epicFromManager.getStatus().toString());
    }

    @Test
    void testEpicStatusDoneAll() {
        Epic epicFromManager = manager.getEpic(epicId);

        manager.getAll().stream().forEach(task -> task.setStatus(TaskStatus.DONE));

        SubTask subtask10 = new SubTask(10, "СабТаска1", "СабТаска1_Тест", TaskStatus.DONE, epicId);
        SubTask subtask11 = new SubTask(11, "СабТаска2", "СабТаска2_Тест", TaskStatus.DONE, epicId);
        SubTask subtask12 = new SubTask(12, "СабТаска3", "СабТаска3_Тест", TaskStatus.DONE, epicId);

        manager.createSubTask(subtask10);
        manager.createSubTask(subtask11);
        manager.createSubTask(subtask12);

        assertEquals("DONE", epicFromManager.getStatus().toString());
    }
}

    /* Абсолютно не вижу смысл в подобном тесте, так как:

    1) Условие выполняется на уровне компиляторя. Так как сигнатура метода SubTask и Эпик туда ну никак не пролезет
    2) Он не работает.

    @Test
    void testEpicNotAddingAsEpicInSubtasks() {
        Epic epicFromManager = manager.getEpic(epicId);
        Epic epicToSubtasks = manager.getEpic(epicId2);
        manager.createSubTask(epicFromManager);
    }

     */