package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;


public class HttpTaskServer {

    public static final int PORT = 8080;
    HttpServer server;
    Gson gson;

    TaskManager taskManager;

    public HttpTaskServer() throws IOException, InterruptedException {
        this.taskManager = Managers.getDefault();

        gson = new GsonBuilder().registerTypeAdapter(Task.class, new TaskAdapter()).create();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
    }

    private void handleTasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            String[] uriSplit = httpExchange.getRequestURI().toString().split("/");
            System.out.println(httpExchange.getRequestURI());

            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/+$", path)) {
                        System.out.println("заюзали get tasks");
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(httpExchange, response);
                        break;

                    } else switch (uriSplit[2]) {
                        case "task": {
                            if ((uriSplit.length > 3) && Pattern.matches("\\?id=\\d+$", uriSplit[3])) {
                                String response = gson.toJson(taskManager.getTask(getID(uriSplit[3])));
                                sendText(httpExchange, response);
                                System.out.println("заюзали get tasks/task/?id");
                                break;
                            } else if (Pattern.matches("^/tasks/task/+$", path)) {
                                String response = gson.toJson(taskManager.getAllTasks());
                                sendText(httpExchange, response);
                                System.out.println("заюзали get tasks/task/");
                                break;
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                        }
                        case "epic": {
                            if ((uriSplit.length > 3) && Pattern.matches("\\?id=\\d+$", uriSplit[3])) {
                                String response = gson.toJson(taskManager.getEpic(getID(uriSplit[3])));
                                sendText(httpExchange, response);
                                System.out.println("заюзали get tasks/epic/?id");
                                break;
                            } else if (Pattern.matches("^/tasks/epic/+$", path)) {
                                String response = gson.toJson(taskManager.getAllEpics());
                                sendText(httpExchange, response);
                                System.out.println("заюзали get tasks/epic/");
                                break;
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                        }
                        case "subtask": {
                            if ((uriSplit.length > 5) && Pattern.matches("\\?id=\\d+$", uriSplit[5])) {
                                String response = gson.toJson(taskManager.getEpic(getID(uriSplit[5])).getSubtaskIds());
                                sendText(httpExchange, response);
                                System.out.println("заюзали get tasks/subtask/epic/?id");
                                break;
                            } else if (Pattern.matches("^/tasks/subtasks/+$", path)) {
                                String response = gson.toJson(taskManager.getAllSubtasks());
                                sendText(httpExchange, response);
                                System.out.println("заюзали get tasks/subtasks/");
                                break;
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                        }
                        case "history": {
                            if (Pattern.matches("^/tasks/history/+$", path)) {
                                String response = gson.toJson(taskManager.getHistory());
                                sendText(httpExchange, response);
                                System.out.println("заюзали get tasks/history/");
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                            break;
                        }
                        default: {
                            System.out.println("неверный path");
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    }
                    break;
                }

                case "POST":
                    if (Pattern.matches("^/tasks/+$", path)) {
                        System.out.println("неверный path");
                        httpExchange.sendResponseHeaders(404, 0);
                        break;

                    }

                    switch (uriSplit[2]) {
                        case "task": {
                            if (Pattern.matches("^/tasks/task/+$", path)) {
                                String reqBody = readText(httpExchange);
                                reqBody = reqBody.substring(1, (reqBody.length() - 1));
                                Task task = FileBackedTaskManager.fromString(reqBody);
                                System.out.println("заюзали post tasks/task/");
                                if (taskManager.getTasksIdList().contains(task.getTaskId())) {
                                    taskManager.updateTask(task, task.getTaskId());
                                } else {
                                    taskManager.createTask(task);
                                    break;
                                }
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                            break;

                        }
                        case "epic": {
                            if (Pattern.matches("^/tasks/epic/+$", path)) {
                                String reqBody = readText(httpExchange);
                                reqBody = reqBody.substring(1, (reqBody.length() - 1));
                                Epic task = (Epic) FileBackedTaskManager.fromString(reqBody);
                                System.out.println("заюзали post tasks/epic/");
                                if (taskManager.getEpicsIdList().contains(task.getTaskId())) {
                                    taskManager.updateEpic(task, task.getTaskId());
                                } else {
                                    taskManager.createEpic(task);
                                    break;
                                }
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                            break;
                        }
                        case "subtask": {
                            if (Pattern.matches("^/tasks/subtask/+$", path)) {
                                String reqBody = readText(httpExchange);
                                reqBody = reqBody.substring(1, (reqBody.length() - 1));
                                SubTask task = (SubTask) FileBackedTaskManager.fromString(reqBody);
                                System.out.println("заюзали post tasks/subtask/");
                                if (!taskManager.getAllSubtasks().contains(task.getTaskId())) {
                                    taskManager.createSubTask(task, task.getEpicId());
                                    break;
                                }
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                            break;
                        }
                    }
                    break;
                case "DELETE": {
                    if (Pattern.matches("^/tasks/+$", path)) {
                        taskManager.deleteEverything();
                        System.out.println("заюзали delete tasks");
                        break;
                    } else switch (uriSplit[2]) {
                        case "task": {
                            if ((uriSplit.length > 3) && Pattern.matches("\\?id=\\d+$", uriSplit[3])) {
                                taskManager.deleteTask(getID(uriSplit[3]));
                                System.out.println("заюзали delete tasks/task/?id");
                                break;
                            } else if (Pattern.matches("^/tasks/task/+$", path)) {
                                taskManager.deleteAllTasks();
                                System.out.println("заюзали delete tasks/task/");
                                break;
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                        }
                        case "epic": {
                            if ((uriSplit.length > 3) && Pattern.matches("\\?id=\\d+$", uriSplit[3])) {
                                taskManager.deleteEpic(getID(uriSplit[3]));
                                System.out.println("заюзали delete tasks/epic/?id");
                                break;
                            } else if (Pattern.matches("^/tasks/epic/+$", path)) {
                                taskManager.deleteAllEpics();
                                System.out.println("заюзали delete tasks/epic/");
                                break;
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                        }
                        case "subtask": {
                            if ((uriSplit.length > 4) && Pattern.matches("\\?id=\\d+$", uriSplit[4])) {
                                taskManager.deleteEpicSubtasks(getID(uriSplit[4]));
                                System.out.println("заюзали delete tasks/subtask/epic/?id");
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                            break;
                        }
                        case "history": {
                            if (Pattern.matches("^/tasks/history/+$", path)) {
                                taskManager.clearHistory();
                                System.out.println("заюзали get tasks/history/");
                            } else {
                                System.out.println("неверный path");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                            break;
                        }
                        default: {
                            System.out.println("неверный path");
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    }
                    break;
                }
                default: {
                    System.out.println("Только методы GET, POST, DELETE. Получили - " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }

            }


        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }


    }

    public void start() {
        System.out.println("Started TaskServer " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Stopped TaskServer " + PORT);
    }

    public Integer getID(String string) {
        return Integer.parseInt(string.replaceAll("[^0-9]", ""));
    }

    public String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes());
    }

    public void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    static class TaskAdapter extends TypeAdapter<Task> {

        @Override
        public void write(JsonWriter jsonWriter, Task task) throws IOException {
            jsonWriter.value(task.toString());
        }

        @Override
        public Task read(JsonReader jsonReader) throws IOException {
            return FileBackedTaskManager.fromString(jsonReader.nextString());
        }

    }
}