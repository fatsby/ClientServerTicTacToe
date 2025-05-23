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

//        System.out.println("Added player to queue"); // for debugging
//        System.out.println(queue);
    }

    private void matchPlayers() {
        if (queue.size() >= 2) {
            ClientHandler player1 = queue.poll();
            ClientHandler player2 = queue.poll();
            GameSession gameSession = new GameSession(player1, player2);
            gameSession.startGame();
            System.out.println("New game started");
        }
    }

    public void removePlayerFromQueue(ClientHandler player) {
        queue.remove(player);
    }

    public boolean hasPlayer(ClientHandler player) {
        return queue.contains(player);
    }

    public Queue<ClientHandler> getQueue() {
        return queue;
    }
}
