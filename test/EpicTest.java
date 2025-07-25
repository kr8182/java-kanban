import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/*Тесты для эпиков. Работоспособность сабтасок проверяется здесь же, поэтому делать отдельный
класс SubTaskTest было бы излишнее, так как для их проверки пришлось бы все равно деллать эпик.
*/

class EpicTest {

    private TaskManager manager;
    private int epicId;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();

        Epic epic = new Epic(0, "Эпик 1", "Эпик 1_Тест");

        manager.createEpic(epic);
        epicId = epic.getTaskId();  // Сохраняем ID созданного эпика
    }

    @Test
    void testGetSubtaskIds() {
        Epic epicFromManager = manager.getEpic(epicId);
        SubTask subtask1 = new SubTask(1, "СабТаска1", "СабТаска1_Тест", epicId);
        manager.createSubTask(subtask1);
        assertEquals("[2]", epicFromManager.getSubtaskIds().toString());
    }

    @Test
    void testAddSubtaskId() {
        Epic epicFromManager = manager.getEpic(epicId);
        SubTask subtask1 = new SubTask(1, "СабТаска1", "СабТаска1_Тест", epicId);
        assertEquals("[]", epicFromManager.getSubtaskIds().toString());
        manager.createSubTask(subtask1);
        assertEquals("[2]", epicFromManager.getSubtaskIds().toString());
    }

    @Test
    void testRemoveSubtaskId() {
        Epic epicFromManager = manager.getEpic(epicId);
        SubTask subtask1 = new SubTask(1, "СабТаска1", "СабТаска1_Тест", epicId);
        assertEquals("[]", epicFromManager.getSubtaskIds().toString());
        manager.createSubTask(subtask1);
        assertEquals("[2]", epicFromManager.getSubtaskIds().toString());
        manager.deleteSubTask(subtask1.getTaskId());
        assertEquals("[]", epicFromManager.getSubtaskIds().toString());
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
}