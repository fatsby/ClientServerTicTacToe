import java.util.Scanner;
import java.io.*;
import java.net.Socket;

public class InterfaceClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private volatile boolean running = true;
    private SwingInterface swingInterface;
    private ClientProtocolManager clientProtocolManager;

    public InterfaceClient(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //opens the interface
            swingInterface = new SwingInterface(this);
            clientProtocolManager = new ClientProtocolManager(swingInterface, this);
            swingInterface.setVisible(true);

        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
            running = false;
        }
    }

    public void start(){
        // listener thread
        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                    if (response.startsWith("PROTOCOL")){
                        clientProtocolManager.process(response);
                    } else{
                        swingInterface.updateLabelMessage(response);
                    }
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
            } finally {
                running = false;
                closeResources();
            }
        }).start();

        // handles user input
    }

    //extra safety barrier, probably useless
    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }

    public void write(String message){
        out.println(message);
    }

    public void close(){
        System.exit(0);
    }

    public static void main(String[] args) {
        InterfaceClient client = new InterfaceClient("localhost", 7010);
        client.start();
    }
}
