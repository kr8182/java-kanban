import exceptions.ManagerSaveException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File dir = new File("C:\\documents");
    File file = new File(dir, "outTasks.csv");

    public static List<Task> loadFromFile(File file) {
        List<Task> tasks = new ArrayList<>();

        if (!file.exists() || file.length() == 0) {
            System.out.println("Файл не существует или пуст. Будет создана новая задача.");
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Пропускаем заголовок
            System.out.println("Файл и директория существуют");
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                // Разбиваем строку с сохранением пустых значений
                String[] parts = line.split(",", -1);
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    TaskType type = TaskType.valueOf(parts[1].trim());
                    String name = parts[2].trim();
                    String description = parts[3].trim();
                    TaskStatus status = TaskStatus.valueOf(parts[4].trim());

                    switch (type) {
                        case TASK -> tasks.add(new Task(id, name, description, status));
                        case EPIC -> tasks.add(new Epic(id, name, description, status));
                        case SUBTASK -> {
                            if (parts.length < 6 || parts[5].isBlank()) {
                                throw new IllegalArgumentException("Отсутствует epicId для подзадачи");
                            }
                            int epicId = Integer.parseInt(parts[5].trim());
                            tasks.add(new SubTask(id, name, description, status, epicId));
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Ошибка в строке: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return tasks;
    }

    public void fromTasksToMemory(List<Task> tasksFromFile) {
        for (Task task : tasksFromFile) {
            if (task.getTaskType() == TaskType.TASK) {
                tasks.put(task.getTaskId(), task);
            } else if (task.getTaskType() == TaskType.EPIC) {
                epics.put(task.getTaskId(), (Epic) task);
            } else {
                subtasks.put(task.getTaskId(), (SubTask) task);
            }
        }
        //Сдвигаем nextId, чтобы при генерации не было дубликтов id
        nextId = tasksFromFile.size() + 1;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void createSubTask(SubTask subtask) {
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

    private void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,description,status,duration,startTime,endTime,epic\n");

            for (Task tas : getPrioritizedTasks()) {
                fileWriter.write(tas.toString() + '\n');
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения", e);
        }
    }
}

