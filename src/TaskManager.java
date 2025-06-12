import java.util.*;

public class TaskManager {

    private int nextId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subtasks = new HashMap<>();

    private int generateID() {
        return nextId++;
    }

    public List<Task> getAllTasks(){
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    public void deleteAllTasks(){
        epics.clear();
        tasks.clear();
    }

    public Task getSubtasksByEpicId(int epicId){
        if(epics.containsKey(epicId)) return epics.get(epicId);
        return subtasks.get(epicId);
    }

    public void createEpic(Epic epic){
        epic.setTaskId(generateID());
        epics.put(epic.getTaskId(), epic);
    }

    public void createSubTask(SubTask subtask){
        int epicId = subtask.getEpicId();
        if(!epics.containsKey(epicId)){
            return;
        }
        subtask.setTaskId(generateID());
        subtasks.put(subtask.getTaskId(), subtask);
        epics.get(epicId).addSubTask(subtask);
        updateEpicStatus(epicId);
    }

    private void updateEpicStatus(Epic epic) {
        if (!epics.containsKey(epic.getTaskId())) return;
        Epic savedEpic = epics.get(epic.getTaskId());
        savedEpic.setTaskName(epic.getTaskName());
        savedEpic.setTaskDescription(epic.getTaskDescription());
    }


}
