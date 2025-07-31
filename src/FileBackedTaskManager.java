import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public void save() throws IOException {
        File dir = new File("C:\\documents");
        if (!dir.exists()) {
            dir.mkdirs(); // Создаем директорию, если её нет
        }
        File file = new File(dir, "outTasks.csv");

        // Используем созданный файл с путём
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write(getAllTasks().toString());
        } // Автоматическое закрытие файла
    }

    public static File loadFromFile(File file) {

        return file;

    }
}

