import java.io.*;
import java.net.*;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        String serverIP = args[0];
        int port = Integer.parseInt(args[1]);
        String role = args[2];   // PUBLISHER or SUBSCRIBER
        String topic = args[3];  // SPORTS, NEWS, etc.

        Socket socket = new Socket(serverIP, port);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Send role and topic to server
        out.println(role);
        out.println(topic);

        if (role.equalsIgnoreCase("PUBLISHER")) {
            String input;
            while ((input = userInput.readLine()) != null) {
                out.println(input);
                if (input.equalsIgnoreCase("terminate")) break;
            }
        } else if (role.equalsIgnoreCase("SUBSCRIBER")) {
            String msg;
            while ((msg = serverInput.readLine()) != null) {
                System.out.println("Received: " + msg);
            }
        }

        socket.close();
    }
}
