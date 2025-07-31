public class SubTask extends Task {
    private int epicId;

    public SubTask(int taskId, String taskName, String taskDescription, int epicId) {
        super(taskId, taskName, taskDescription);
        this.taskType = TaskType.SUBTASK;
        this.epicId = epicId;
    }

    public int getEpicId() {

        return epicId;
    }

    //Переопределяем toString для корректного отображения
    @Override
    public String toString() {
            return taskId +
                    "," + taskType +
                    "," + taskName +
                    "," + taskDescription +
                    "," + status +
                    "," + epicId;
    }
}