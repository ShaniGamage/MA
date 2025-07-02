package Task_02;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerApp {

    // Shared list of all subscriber client handlers
    private static final List<ClientHandler> subscribers = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Server <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start(); // Run in a separate thread
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Inner class for handling each client
    static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String role; // PUBLISHER or SUBSCRIBER

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Read client role
                role = in.readLine();
                System.out.println("Client role: " + role);

                // Add to subscriber list if applicable
                if ("SUBSCRIBER".equalsIgnoreCase(role)) {
                    subscribers.add(this);
                    System.out.println("Subscriber added. Total subscribers: " + subscribers.size());
                }

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equalsIgnoreCase("terminate")) {
                        break;
                    }

                    if ("PUBLISHER".equalsIgnoreCase(role)) {
                        System.out.println("Received from publisher: " + msg);

                        // Send to all subscribers
                        synchronized (subscribers) {
                            for (ClientHandler subscriber : subscribers) {
                                subscriber.out.println("Message from Publisher: " + msg);
                            }
                        }
                    }
                }

            } catch (IOException e) {
                System.err.println("Connection error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if ("SUBSCRIBER".equalsIgnoreCase(role)) {
                    subscribers.remove(this);
                    System.out.println("Subscriber removed. Remaining: " + subscribers.size());
                }

                System.out.println("Client disconnected: " + socket.getInetAddress().getHostAddress());
            }
        }
    }
}