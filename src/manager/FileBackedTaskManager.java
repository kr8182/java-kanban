package manager;

import exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path path;
    File dir = new File("C:\\documents");
    File file = new File(dir, "outTasks.csv");
    private InHistoryManager InHistoryManager;

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(path);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine();

            while (reader.ready()) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    for (Integer i : historyFromString(reader.readLine())) {
                        if (fileBackedTaskManager.tasks.containsKey(i)) {
                            fileBackedTaskManager.getTask(i);
                        }
                        if (fileBackedTaskManager.epics.containsKey(i)) {
                            fileBackedTaskManager.getEpic(i);
                        }
                        if (fileBackedTaskManager.subtasks.containsKey(i)) {
                            fileBackedTaskManager.getSubtask(i);
                        }
                    }
                    break;
                }
                Task task = fromString(line);
                switch (TaskType.valueOf(line.split(",")[1])) {
                    case TASK:
                        fileBackedTaskManager.tasks.put(task.getTaskId(), task);
                        break;
                    case EPIC:
                        fileBackedTaskManager.epics.put(task.getTaskId(), (Epic) task);
                        break;
                    case SUBTASK:
                        fileBackedTaskManager.subtasks.put(task.getTaskId(), (SubTask) task);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTaskManager;
    }

    public static Task fromString(String value) {
        String[] string = value.split(",");
        Task task = null;
        switch (TaskType.valueOf(string[1])) {
            case TASK:
                task = new Task(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4], Integer.parseInt(string[5]), string[6]);
                break;
            case EPIC:
                task = new Epic(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4], Integer.parseInt(string[5]), string[6]);
                break;
            case SUBTASK:
                task = new SubTask(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4], Integer.parseInt(string[5]), string[6], Integer.parseInt(string[7]));
                break;
        }
        return task;
    }

    static String historyToString(InHistoryManager manager) {
        String[] array = new String[manager.getTasks().size()];
        int i = 0;
        for (Task task : manager.getTasks()) {
            array[i++] = String.valueOf(task.getTaskId());
        }
        return String.join(",", array.toString());
    }

    static List<Integer> historyFromString(String value) {

        String[] string = value.split(",");
        List<Integer> list = new ArrayList<>();

        for (String st : string) {

            if (!st.isEmpty()) {
                list.add(Integer.parseInt(st));
            }
        }
        return list;
    }

    @Override
    public void createEpic(Epic epic) throws IOException {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createTask(Task task) throws IOException {
        super.createTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) throws IOException {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) throws IOException {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) throws IOException {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void createSubTask(SubTask subtask) throws IOException {
        super.createSubTask(subtask);
        save();
    }

    public File createDirectoryAndFileWhileBooting() throws IOException {
        System.out.println("Инициализация приложения и проверка на наличие файла и директории");

        if (!dir.exists()) {
            System.out.println("Директория создается, так как не была найдена...");
            dir.mkdirs();
            System.out.println("Директория создана: " + dir.getAbsolutePath());
        }

        if (!file.exists()) {
            System.out.println("Файл не найден, создаем новый файл...");
            file.createNewFile();
            System.out.println("Файл создан: " + file.getAbsolutePath());

            // Инициализируем файл заголовком
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("id,type,name,description,status,epic\n");
            }
        }
        return file;
    }

    void save() throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(String.valueOf(path)))) {
            out.write("id,type,name,status,description,duration,startTime,epic");
            out.newLine();
            for (Task task : tasks.values()) {
                out.write(task.toString());
                out.newLine();
            }
            for (Task epic : epics.values()) {
                out.write(epic.toString());
                out.newLine();
            }
            for (Task subtask : subtasks.values()) {
                out.write(subtask.toString());
                out.newLine();
            }
            out.newLine();
            out.write(historyToString(InHistoryManager));

        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }
}

