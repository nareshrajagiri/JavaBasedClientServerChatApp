import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Constructor to initialize server
    public Server() {
        try {
            server = new ServerSocket(7777); // Bind to port 7777
            System.out.println("Server is ready to accept connections...");
            System.out.println("Waiting...");
            socket = server.accept(); // Accept client connection

            // Set up input stream to receive messages from client
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Set up output stream to send messages to client
            out = new PrintWriter(socket.getOutputStream(), true); // true for auto-flush

            // Start reading and writing in separate threads
            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to continuously read messages from client
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started...");

            try {
                while (!socket.isClosed()) {
                    String msg = br.readLine();
                    if (msg == null || msg.equalsIgnoreCase("EXIT")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection closed!");
            }
        };

        new Thread(r1).start();
    }

    // Method to continuously send messages to client
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
        System.out.println("This is the server, starting now...");
        new Server();
    }
}