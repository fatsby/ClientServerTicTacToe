import java.util.HashMap;

public class GameSession implements Runnable{
    private ClientHandler player1;
    private ClientHandler player2;
    private ClientHandler currentTurn;
    private volatile boolean gameOver;
    private Board board;
    private QueueManager playerQueue;
    private StringBuilder sb;
    private boolean abnormalEnd;

    public GameSession(ClientHandler player1, ClientHandler player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Board();
        this.playerQueue = QueueManager.getInstance();
        this.gameOver = false;
        this.abnormalEnd = false;

        player1.setGame(this, 'X');
        player1.sendMessage("You are X!");

        player2.setGame(this, 'O');
        player2.sendMessage("You are O!");

        currentTurn = player1;
    }

    @Override
    public void run() {
        notifyPlayersGameStarted();

        while (!gameOver) {
            try {
                Thread.sleep(100); // apparently this avoid 100% thread cpu usage and better than doign nothing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Game session interrupted: " + e.getMessage());
                break;
            }
        }

        System.out.println("A game just ended"); // for debugging purposes

        if (!abnormalEnd) {
            cleanUpAndRequeue();
        }
        //game ends, return players to queue, close thread
    }

    private void cleanUpAndRequeue() {
        player1.setGame(null, ' ');
        player2.setGame(null, ' ');

        returnPlayersToQueue();
    }

    public void handleMove(ClientHandler requestedPlayer, String input) {
        if (requestedPlayer != currentTurn || gameOver) {
            requestedPlayer.sendMessage("Not your turn or game is over");
            return;
        }
        try{

            int move = Integer.parseInt(input);
            Character playerSymbol = requestedPlayer.getSymbol();

            if (board.isValidMove(move)) {
                board.move(move, playerSymbol);
                broadcastBoard();
                updateGameState();
                switchTurn();
            } else {
                requestedPlayer.sendMessage("Invalid move");
            }

        } catch(NumberFormatException e) {
            requestedPlayer.sendMessage("Move must be a number from 1-9");
        }
    }

    private void notifyPlayersGameStarted() {
        player1.sendMessage("Game started! You are X. Your turn first.");
        player2.sendMessage("Game started! You are O. Opponent goes first.");
        broadcastMessage("Enter a number from 1-9, corresponding to a cell, to make a move.");
        broadcastMessage("Or enter 'exit' to quit playing.");
        broadcastBoard();
    }

    private void returnPlayersToQueue() {
        System.out.println("Queueing players for a new game"); //for debugging

        broadcastMessage("Queueing for a new game");
        playerQueue.addPlayer(player1);
        playerQueue.addPlayer(player2);
    }

    private void broadcastMessage(String message) {
        player1.sendMessage(message);
        player2.sendMessage(message);
    }

    private void broadcastBoard(){
        HashMap<Integer, Character> cells = board.getCells();

        int count = 0;
        sb = new StringBuilder();
        sb.append("\n");
        for (int i = 1; i <= 9; i++){
            if (count == 3){
                sb.append("\n");
                count = 0;
            }
            sb.append(cells.get(i)).append(" ");
            count++;
        }
        broadcastMessage(sb.toString());
    }

    private void updateGameState() {
        if (board.checkWin()) {
            gameOver = true;

            System.out.println(gameOver);
            System.out.println("Game over reached in updateGamestate"); // for debugging

            player1.sendMessage(currentTurn == player1 ? "You won!" : "You lost!");
            player2.sendMessage(currentTurn == player2 ? "You won!" : "You lost!");
        } else if (board.isBoardFull()) {
            gameOver = true;
            System.out.println("Game over reached in updateGamestate"); // for debugging

            broadcastMessage("Game ended in a Draw!");
        }
    }

    private void switchTurn() {
        currentTurn = (currentTurn == player1) ? player2 : player1;
        player1.sendMessage(currentTurn == player1 ? "Your turn" : "Opponent's turn");
        player2.sendMessage(currentTurn == player2 ? "Your turn" : "Opponent's turn");
    }

    public void playerExit(ClientHandler exitedPlayer) {
        if (gameOver) return;

        gameOver = true;
        abnormalEnd = true;

        ClientHandler remainingPlayer = (player1 == exitedPlayer ? player2 : player1);

        if (remainingPlayer != null) {
            remainingPlayer.sendMessage("The other player exited, queueing for new game");
            remainingPlayer.setGame(null, ' ');
            playerQueue.addPlayer(remainingPlayer);
        }

        player1 = null;
        player2 = null; // cleanup
    }

    public boolean isGameOver() {
        return gameOver;
    }

}
