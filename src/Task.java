public class Task {
   protected String taskName;
   protected String taskDescription;
   protected int taskId;

   protected TaskStatus taskStatus;

   Task(int taskId, String taskName, String taskDescription){
      this.taskId = taskId;
      this.taskName = taskName;
      this.taskDescription = taskDescription;
      this.taskStatus = TaskStatus.NEW;
   }

   public int getTaskId() {
      return taskId;
   }

   public String getTaskName() {
      return taskName;
   }

   public String getTaskDescription() {
      return taskDescription;
   }

   public TaskStatus getTaskStatus() {
      return taskStatus;
   }

   public void setTaskStatus(TaskStatus taskStatus) {
      this.taskStatus = taskStatus;
   }

   @Override
   public String toString(){
      return "Task{" +
              "taskId= " + taskId +
              ", taskName= " + taskName +
              ", taskDescription= " +
              ", status= " + taskStatus +
              "}";
   }
}
