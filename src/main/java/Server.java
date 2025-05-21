import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private QueueManager playerQueue;
    private ExecutorService threadPool;
    private boolean running;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.playerQueue = QueueManager.getInstance();
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        running = true;
        while (running) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected");
            ClientHandler handler = new ClientHandler(clientSocket, playerQueue);
            threadPool.execute(handler);
        }
    }

    //im gonna put this here just in case i need it, probably wont
    public void stop() throws IOException {
        running = false;
        threadPool.shutdown();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(7010);
        server.start();
    }

}
