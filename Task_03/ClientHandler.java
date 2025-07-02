import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String role;
    private String topic;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void run() {
        try {
            // First two messages from client: role and topic
            this.role = in.readLine(); // PUBLISHER or SUBSCRIBER
            this.topic = in.readLine(); // e.g., SPORTS or NEWS

            System.out.println("New client [" + role + "] on topic [" + topic + "]");

            if (role.equalsIgnoreCase("SUBSCRIBER")) {
                // Add this subscriber to topic map
                ServerApp.topicSubscribers.putIfAbsent(topic, new ArrayList<>());
                ServerApp.topicSubscribers.get(topic).add(this);
            }

            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equalsIgnoreCase("terminate")) break;

                if (role.equalsIgnoreCase("PUBLISHER")) {
                    List<ClientHandler> subscribers = ServerApp.topicSubscribers.get(topic);
                    if (subscribers != null) {
                        synchronized (subscribers) {
                            for (ClientHandler subscriber : subscribers) {
                                subscriber.sendMessage("[" + topic + "] " + msg);
                            }
                        }
                    }
                }
            }

            socket.close();

            // Remove subscriber from list if disconnected
            if (role.equalsIgnoreCase("SUBSCRIBER")) {
                List<ClientHandler> subscribers = ServerApp.topicSubscribers.get(topic);
                if (subscribers != null) {
                    subscribers.remove(this);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
