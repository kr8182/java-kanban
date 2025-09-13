package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;

import java.io.IOException;
import java.nio.file.Path;

public class Managers {


    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078/");
    }

    public static TaskManager getFileBackedTaskManager(Path path) {
        return new FileBackedTaskManager(path);
    }

    public static HistoryManager getDefaultHistory() {
        return new InHistoryManager();
    }

}