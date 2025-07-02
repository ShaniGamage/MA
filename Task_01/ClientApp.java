package Task_01;
import java.io.*;
import java.net.*;

public class ClientApp {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java MyClientApp <server_ip> <port>");
            return;
        }

        String serverIP = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(serverIP, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server at " + serverIP + ":" + port);
            String input;
            while (true) {
                System.out.print("Enter message: ");
                input = userInput.readLine();
                out.println(input);
                if (input.equalsIgnoreCase("terminate")) {
                    System.out.println("Terminating connection.");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
