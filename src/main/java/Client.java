import java.util.Scanner;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner userInput;

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userInput = new Scanner(System.in);
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        }
    }

    public void start(){
        //listener thread
        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
            }
        }).start();

        //handles user-input
        while (true) {
            String input = userInput.nextLine();
            out.println(input);
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 7010);
        client.start();
    }
}
