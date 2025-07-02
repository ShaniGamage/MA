
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerApp {
    // Map of topic -> list of subscribers
    static Map<String, List<ClientHandler>> topicSubscribers = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(clientSocket);
            handler.start(); // Start new thread for each client
        }
    }
}
