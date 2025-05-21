import java.util.LinkedList;
import java.util.Queue;

public class QueueManager {
    //SINGLETON
    private static QueueManager instance;
    private final Queue<ClientHandler> queue;

    public QueueManager() {
        this.queue = new LinkedList<>();
    }

    public static QueueManager getInstance() {
        if (instance == null) {
            instance = new QueueManager();
        }
        return instance;
    }

    public synchronized void addPlayer(ClientHandler player) {
        queue.add(player);
        matchPlayers();
    }

    private void matchPlayers() {
        if (queue.size() >= 2) {
            ClientHandler player1 = queue.poll();
            ClientHandler player2 = queue.poll();
            GameSession gameSession = new GameSession(player1, player2);
            Thread gameThread = new Thread(gameSession);
            gameThread.start();
            System.out.println("New game started");
        }
    }
}
