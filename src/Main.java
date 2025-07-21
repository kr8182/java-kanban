public class Main {

    private static TaskManager manager; //тест пройден

    private static HistoryManager historyManager; //тест пройден

    public static void main(String[] args) {
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

        //subtask1.setStatus(TaskStatus.DONE);//тест пройден

        //subtask2.setStatus(TaskStatus.DONE);//тест пройден

        //manager.deleteSubTask(4);

        //manager.deleteAllTasks();//тест пройден
        //manager.deleteAllEpics();//тест пройден
        //manager.deleteAllSubTasks();//тест пройден

        /*System.out.println("");
        System.out.println("Работа метода getAll*");
        System.out.println(manager.getAllTasks().toString());//тест пройден
        System.out.println(manager.getAllEpics().toString()); //тест пройден
        System.out.println(manager.getAllSubtasks().toString()); //тест пройден

        System.out.println("");
        System.out.println("Работа метода getSubtasksByEpicId");
        System.out.println(manager.getSubtasksByEpicId(epicId).toString());//тест пройден
         */

        SubTask subtask3 = new SubTask(3, "СабТаска3", "СабТаска3_Тест", epicId);
        manager.createSubTask(subtask3);

        SubTask subtask4 = new SubTask(4, "СабТаска4", "СабТаска4_Тест", epicId);
        manager.createSubTask(subtask3);

        manager.getTask(subtask1.getTaskId());
        historyManager.addHistory(subtask1);

        manager.getSubtask(task1.getTaskId());
        historyManager.addHistory(task1);

        manager.getTask(subtask2.getTaskId());
        historyManager.addHistory(subtask2);

        manager.getTask(subtask3.getTaskId());
        historyManager.addHistory(subtask3);

        manager.getTask(subtask4.getTaskId());
        historyManager.addHistory(subtask4);

        manager.getEpic(epic1.getTaskId());
        historyManager.addHistory(epic1);

        historyManager.remove(epicId);

        System.out.println("История: " + historyManager.getHistory().toString());


    }
}
