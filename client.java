import java.net.Socket;
import java.io.*;

public class Client {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Constructor to initialize client
    public Client() {
        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7777); // Connect to server
            System.out.println("Connection established!");

            // Set up input stream to receive messages from server
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Set up output stream to send messages to server
            out = new PrintWriter(socket.getOutputStream(), true); // true for auto-flush

            // Start reading and writing in separate threads
            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to continuously read messages from server
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started...");

            try {
                while (!socket.isClosed()) {
                    String msg = br.readLine();
                    if (msg == null || msg.equalsIgnoreCase("EXIT")) {
                        System.out.println("Server terminated the chat!");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection closed!");
            }
        };

        new Thread(r1).start();
    }

    // Method to continuously send messages to server
    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writer started...");

            try {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                while (!socket.isClosed()) {
                    String content = br1.readLine();
                    out.println(content);

                    if (content.equalsIgnoreCase("EXIT")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection closed!");
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is the client...");
        new Client();
    }
}