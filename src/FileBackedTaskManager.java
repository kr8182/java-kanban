import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public static File loadFromFile(File file) {

        return file;

    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save() throws IOException {
        File dir = new File("C:\\documents");
        if (!dir.exists()) {
            dir.mkdirs(); // Создаем директорию, если её нет
        }
        File file = new File(dir, "outTasks.csv");

        // Используем созданный файл с путём
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic" + "\n");
            fileWriter.write(getAllEpics().toString() + '\n');
            fileWriter.write(getAllTasks().toString() + '\n');
            fileWriter.write(getAllSubtasks().toString() + '\n');
        } // Автоматическое закрытие файла
    }
}

