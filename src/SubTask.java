public class SubTask extends Task {
    private int epicId;

    public SubTask(int taskId, String taskName, String taskDescription, int epicId) {
        super(taskId, taskName, taskDescription);
        this.epicId = epicId;
    }

    public int getEpicId() {

        return epicId;
    }

    //Переопрределяем toString для корректного отображения
    @Override
    public String toString() {
        return "SubTask{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status=" + status +
                ", epicId=" + epicId +
                '}';
    }
}