import manager.FileBackedTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import java.io.File;

public class FileBackendTaskManagerTest {

    private static FileBackedTaskManager fileManager;

    @BeforeAll
    public static void beforeAll() {
        fileManager = Managers.getDefaultFileManager();
    }

    @Test
    void fileIsNotNull() {
        File dir = new File("C:\\documents");

        if (!dir.exists()) {
            dir.mkdirs(); // Создаем директорию, если её нет
        }

        File file = new File(dir, "outTasks.csv");

        Epic epic = new Epic(0, "Эпик 1", "Эпик 1_Тест");
        Epic epic2 = new Epic(1, "Эпик 2", "Эпик 2_Тест");

        fileManager.createEpic(epic);
        fileManager.createEpic(epic2);

        Assertions.assertEquals(true, file.exists());
    }
}
