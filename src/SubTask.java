public class SubTask extends Task {

    private TaskStatus subTaskStatus;
    private int epicId;

    SubTask(int taskId, String taskName, String taskDescription) {
        super(taskId, taskName, taskDescription);

        this.subTaskStatus = TaskStatus.NEW;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public TaskStatus getSubTaskStatus() {
        return subTaskStatus;


    }
}
