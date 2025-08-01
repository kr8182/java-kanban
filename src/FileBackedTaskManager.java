import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public static List<Task> loadFromFile(File file) {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Пропускаем заголовок
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
                        case TASK:
                            tasks.add(new Task(id, name, description, status));
                            break;
                        case EPIC:
                            tasks.add(new Epic(id, name, description, status));
                            break;
                        case SUBTASK:
                            if (parts.length < 6 || parts[5].isBlank()) {
                                throw new IllegalArgumentException("Отсутствует epicId для подзадачи");
                            }
                            int epicId = Integer.parseInt(parts[5].trim());
                            tasks.add(new SubTask(id, name, description, status, epicId));
                            break;
                    }
                } catch (Exception e) {
                    System.err.println("Ошибка в строке: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + e.getMessage(), e);
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
    public void deleteTask(int id) {
        super.deleteTask(id);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
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
            //Создаем структуру файла по колонкам
            fileWriter.write("id,type,name,description,status,epic" + "\n");
            //Начинаем перетирать записи в файле или создавать новые, меняя всю таблицу целиком
            for (Epic epic : getAllEpics()) {
                fileWriter.write(epic.toString() + '\n');
            }

            for (Task task : getAllTasks()) {
                fileWriter.write(task.toString() + '\n');
            }

            for (SubTask subtask : getAllSubtasks()) {
                fileWriter.write(subtask.toString() + '\n');
            }
        } // Автоматическое закрытие файла
    }
}

