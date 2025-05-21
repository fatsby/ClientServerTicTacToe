import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private QueueManager playerQueue;
    private GameSession currentGame;
    private char symbol;
    private boolean exit;

    public ClientHandler(Socket socket, QueueManager queue) throws IOException {
        this.clientSocket = socket;
        this.playerQueue = queue;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.exit = false;
    }

    @Override
    public void run() {
        try {
            sendMessage("Welcome to TicTacToe! Waiting for opponent...");
            playerQueue.addPlayer(this);

            String inputLine;
            while ((inputLine = in.readLine()) != null && !exit) {
                processInput(inputLine);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            cleanup();
        }
    }

    public void setGame(GameSession game, char symbol) {
        this.currentGame = game;
        this.symbol = symbol;
    }

    public void processInput(String input) {
        try {
            if (input.equalsIgnoreCase("exit")) {
                clientSocket.close();
                exit = true;
                return;
            }

            if (currentGame != null) {
                currentGame.handleMove(this, input);
            }
        } catch (IOException e) {
            System.out.println("Error processing input: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void cleanup() {
        try {
            if (currentGame != null) {
                handleExit();
            } else if (playerQueue.hasPlayer(this)) {
                playerQueue.removePlayerFromQueue(this);
            }

            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();

            System.out.println("Client disconnected and resources cleaned up");
        } catch (IOException e) {
            System.out.println("Error during cleanup: " + e.getMessage());
        }
    }


    public Character getSymbol() {
        return symbol;
    }

    private void handleExit(){
        currentGame.playerExit(this);
        if (playerQueue.hasPlayer(this)){
            playerQueue.removePlayerFromQueue(this);
        }
    }

    public void close() throws IOException {
        exit = true;
        clientSocket.close();
    }
}
