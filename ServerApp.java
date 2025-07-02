import java.io.*;
import java.net.*;

public class ServerApp {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java MyServerApp <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected from " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("terminate")) {
                    System.out.println("Client requested termination.");
                    break;
                }
                System.out.println("Client: " + message);
            }

            clientSocket.close();
            System.out.println("Server stopped.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
