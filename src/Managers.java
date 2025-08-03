public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFileManager() {
        return new FileBackedTaskManager();
    }
}