import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/*Тесты для эпиков. Работоспособность сабтасок проверяется здесь же, поэтому делать отдельный
класс SubTaskTest было бы излишнее, так как для их проверки пришлось бы все равно деллать эпик.
*/

class EpicTest {

    private FileBackedTaskManager manager;
    private int epicId;

    @BeforeEach
    public void beforeEach() {
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
        assertEquals("[2]", epicFromManager.getSubtaskIds().toString());
    }

    @Test
    void testAddSubtaskId() {
        Epic epicFromManager = manager.getEpic(epicId);
        SubTask subtask1 = new SubTask(2, "СабТаска1", "СабТаска1_Тест", epicId);
        assertEquals("[]", epicFromManager.getSubtaskIds().toString());
        try {
            manager.createSubTask(subtask1);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        assertEquals("[2]", epicFromManager.getSubtaskIds().toString());
    }

    @Test
    void testRemoveSubtaskId() {
        Epic epicFromManager = manager.getEpic(epicId);
        SubTask subtask1 = new SubTask(3, "СабТаска1", "СабТаска1_Тест", epicId);
        assertEquals("[]", epicFromManager.getSubtaskIds().toString());
        try {
            manager.createSubTask(subtask1);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        assertEquals("[2]", epicFromManager.getSubtaskIds().toString());
        manager.deleteSubTask(subtask1.getTaskId());
        assertEquals("[]", epicFromManager.getSubtaskIds().toString());
    }

    @Test
    void testEpicStatus() {
        Epic epicFromManager = manager.getEpic(epicId);

        SubTask subtask1 = new SubTask(4, "СабТаска1", "СабТаска1_Тест", epicId);
        SubTask subtask2 = new SubTask(5, "СабТаска2", "СабТаска2_Тест", epicId);
        SubTask subtask3 = new SubTask(6, "СабТаска3", "СабТаска3_Тест", epicId);

        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);

        subtask1.setDuration(Duration.ofHours(25));
        subtask2.setDuration(Duration.ofHours(49));
        subtask1.setStatus(TaskStatus.IN_PROGRESS);

        manager.createSubTask(subtask3);

        assertEquals("IN_PROGRESS", epicFromManager.getStatus().toString());
    }

    @Test
    void testEpicStatusDone() {
        Epic epicFromManager = manager.getEpic(epicId);

        SubTask subtask1 = new SubTask(7, "СабТаска1", "СабТаска1_Тест", epicId);
        SubTask subtask2 = new SubTask(8, "СабТаска2", "СабТаска2_Тест", epicId);
        SubTask subtask3 = new SubTask(9, "СабТаска3", "СабТаска3_Тест", epicId);

        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);

        subtask1.setDuration(Duration.ofHours(25));
        subtask2.setDuration(Duration.ofHours(49));
        subtask1.setStatus(TaskStatus.DONE);

        manager.createSubTask(subtask3);

        assertEquals("IN_PROGRESS", epicFromManager.getStatus().toString());
    }

    @Test
    void testEpicStatusDoneAll() {
        Epic epicFromManager = manager.getEpic(epicId);

        SubTask subtask1 = new SubTask(10, "СабТаска1", "СабТаска1_Тест", TaskStatus.DONE, epicId);
        SubTask subtask2 = new SubTask(11, "СабТаска2", "СабТаска2_Тест", TaskStatus.DONE, epicId);
        SubTask subtask3 = new SubTask(12, "СабТаска3", "СабТаска3_Тест", TaskStatus.DONE, epicId);

        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);
        manager.createSubTask(subtask3);

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