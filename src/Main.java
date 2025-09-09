import manager.KVServer;
import manager.KVTaskClient;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        new KVServer().start();


        KVTaskClient client = new KVTaskClient("http://localhost:8078/");

        client.put("10", "Задача1");
        System.out.println(client.load("103"));
        client.put("104", "Задача2");
        System.out.println(client.load("105"));
    }
}
