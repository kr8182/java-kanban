import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    private static TaskManager manager; //тест пройден

    private static HistoryManager historyManager; //тест пройден

    private static FileBackedTaskManager fileManager;

    public static void main(String[] args) throws IOException {
        /*Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
            Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
            Измените статусы созданных объектов, распечатайте их.
            Проверьте, что статус задачи и подзадачи сохранился,
            а статус эпика рассчитался по статусам подзадач.
            И, наконец, попробуйте удалить одну из задач и один из эпиков.
            Воспользуйтесь дебаггером среды разработки, чтобы понять логику работы программы и отладить её.*/
        System.out.println("Итерация задачника!");

        manager = Managers.getDefault();

        historyManager = Managers.getDefaultHistory();

        fileManager = Managers.getDefaultFileManager();

        File file = fileManager.createDirectoryAndFileWhileBooting();

        List<Task> tasks = fileManager.loadFromFile(file);

        fileManager.fromTasksToMemory(tasks);

        Task task1 = new Task(0, "Таска1", "Таска 1_Тест");
        fileManager.createTask(task1);

        Epic epic1 = new Epic(1, "Эпик 1", "Эпик 1_Тест");
        fileManager.createEpic(epic1);

        int epicId = epic1.getTaskId(); // Получаем реальный ID эпика

        // Создаем подзадачи для эпика
        SubTask subtask1 = new SubTask(1, "СабТаска1", "СабТаска1_Тест", epicId);
        fileManager.createSubTask(subtask1);

        SubTask subtask2 = new SubTask(2, "СабТаска2", "СабТаска2_Тест", epicId);
        fileManager.createSubTask(subtask2);

        SubTask subtask3 = new SubTask(3, "СабТаска3", "СабТаска3_Тест", epicId);
        fileManager.createSubTask(subtask3);

        System.out.println("Давай посмотрим, что с тасками: " + fileManager.getAllTasks().toString());


    }
}
