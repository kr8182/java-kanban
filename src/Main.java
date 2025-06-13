import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        /*Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
            Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
            Измените статусы созданных объектов, распечатайте их.
            Проверьте, что статус задачи и подзадачи сохранился,
            а статус эпика рассчитался по статусам подзадач.
            И, наконец, попробуйте удалить одну из задач и один из эпиков.
            Воспользуйтесь дебаггером среды разработки, чтобы понять логику работы программы и отладить её.*/
        System.out.println("Итерация задачника!");


        TaskManager manager = new TaskManager();


        Task task1 = new Task(0, "Таска1", "Таска 1_Тест");
        manager.createTask(task1);

        Epic epic1 = new Epic(1, "Эпик 1", "Эпик 1_Тест");
        manager.createEpic(epic1);

        int epicId = epic1.getTaskId(); // Получаем реальный ID эпика

        // Создаем подзадачи для эпика
        SubTask subtask1 = new SubTask(1, "СабТаска1", "СабТаска1_Тест", epicId);
        manager.createSubTask(subtask1);

        SubTask subtask2 = new SubTask(2, "СабТаска2", "СабТаска2_Тест", epicId);
        manager.createSubTask(subtask2);

        subtask2.setStatus(TaskStatus.DONE);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateEpicStatus(epicId);//тест пройден

        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.deleteSubtask(4);

        manager.updateEpicStatus(epicId); //тест пройден, статус Эпика поменялся.



        System.out.println(manager.getAllTasks().toString());//тест пройден

        System.out.println(manager.getSubtasksByEpicId(epicId));//тест пройден




    }
}
