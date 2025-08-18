import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class Main {

    private static TaskManager manager; //тест пройден

    private static HistoryManager historyManager; //тест пройден

    private static FileBackedTaskManager fileManager;

    public static void main(String[] args) {
        /*Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
            Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
            Измените статусы созданных объектов, распечатайте их.
            Проверьте, что статус задачи и подзадачи сохранился,
            а статус эпика рассчитался по статусам подзадач.
            И, наконец, попробуйте удалить одну из задач и один из эпиков.
            Воспользуйтесь дебаггером среды разработки, чтобы понять логику работы программы и отладить её.*/
        try {
            System.out.println("Итерация задачника!");

            manager = Managers.getDefault();

            historyManager = Managers.getDefaultHistory();

            fileManager = Managers.getDefaultFileManager();

            File file = fileManager.createDirectoryAndFileWhileBooting();

            List<Task> tasks = fileManager.loadFromFile(file);

            fileManager.fromTasksToMemory(tasks);

        } catch (IOException e) {
            System.err.println("Ошибка сохранения данных: " + e.getMessage());
            e.printStackTrace();
        }

        Task task1 = new Task(0, "Таска1", "Таска 1_Тест");
        fileManager.createTask(task1);
        task1.setDuration(Duration.ofHours(36));

        Epic epic1 = new Epic(1, "Эпик 1", "Эпик 1_Тест");
        fileManager.createEpic(epic1);

        int epicId = epic1.getTaskId(); // Получаем реальный ID эпика

        Task task2 = new Task(2, "Таска2", "Таска 2_Тест");
        fileManager.createTask(task1);
        task2.setDuration(Duration.ofHours(36));

        System.out.println("Давай посмотрим, что с тасками: " + fileManager.getAllTasks().toString());

        // Создаем подзадачи для эпика
        SubTask subtask1 = new SubTask(3, "СабТаска1", "СабТаска1_Тест", epicId);
        fileManager.createSubTask(subtask1);
        subtask1.setDuration(Duration.ofHours(48));
        subtask1.setStatus(TaskStatus.IN_PROGRESS);

        System.out.println("Давай посмотрим, что с тасками: " + fileManager.getAllTasks().toString());
        SubTask subtask2 = new SubTask(4, "СабТаска2", "СабТаска2_Тест", epicId);
        fileManager.createSubTask(subtask2);
        subtask2.setDuration(Duration.ofHours(30));

        SubTask subtask3 = new SubTask(5, "СабТаска3", "СабТаска3_Тест", epicId);
        fileManager.createSubTask(subtask3);
        subtask3.setDuration(Duration.ofHours(47));

        SubTask subtask4 = new SubTask(6, "СабТаска4", "СабТаска4_Тест", epicId);
        fileManager.createSubTask(subtask4);
        subtask4.setDuration(Duration.ofHours(40));

        System.out.println("Давай посмотрим, что с тасками: " + fileManager.getAllTasks().toString());

    }
}
