package Task_02;


import java.io.*;
import java.net.*;

public class ClientApp {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Client <server_ip> <port> <PUBLISHER/SUBSCRIBER>");
            return;
        }

        String serverIp = args[0];
        int port = Integer.parseInt(args[1]);
        String role = args[2].toUpperCase();

        try (
            Socket socket = new Socket(serverIp, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server as " + role);

            // Send role to server
            out.println(role);

            if ("PUBLISHER".equals(role)) {
                String msg;
                System.out.println("Type messages to send. Type 'terminate' to disconnect.");
                while ((msg = userInput.readLine()) != null) {
                    out.println(msg);
                    if ("terminate".equalsIgnoreCase(msg)) break;
                }

            } else if ("SUBSCRIBER".equals(role)) {
                String msg;
                System.out.println("Waiting for messages from publishers...");
                while ((msg = serverIn.readLine()) != null) {
                    System.out.println(msg);
                }
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("Disconnected.");
    }
    
    
}
