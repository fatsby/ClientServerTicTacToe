import java.util.*;

public class SinglePlayerGameSession extends GameSession {
    private final ClientHandler player;

    public SinglePlayerGameSession(ClientHandler player) {
        super(player, null);
        this.player = player;

        player.setGame(this, 'X');
        player.sendMessage("You are X! Playing against AI.");

        setCurrentTurn(player); // player starts first
    }

    @Override
    public void startGame() {
        player.sendMessage("Game started! You are X. Your turn first.");
        player.sendMessage("Enter a number from 1-9 to make a move.");
        broadcastBoard();
//        setTimer(this, 10);
    }

    @Override
    public void handleMove(ClientHandler requestedPlayer, String input) {
        if (requestedPlayer != getCurrentTurn() || isGameOver()) {
            requestedPlayer.sendMessage("Not your turn or game is over");
            return;
        }

        try {
            int move = Integer.parseInt(input);
            Character symbol = requestedPlayer.getSymbol();

            if (getBoard().isValidMove(move)) {
                getBoard().move(move, symbol);
                broadcastBoard();
                updateGameState();
                if (!isGameOver()) {
                    setCurrentTurn(null);
                    aiMove();
                }
            } else {
                requestedPlayer.sendMessage("Invalid move");
            }
        } catch (NumberFormatException e) {
            requestedPlayer.sendMessage("Move must be a number from 1-9");
        }
    }

    private void aiMove() {
        int move = getBoard().computerInput();
        player.sendMessage("AI moved at position: " + move);
        broadcastBoard();
        updateGameState();
        if (!isGameOver()) {
            setCurrentTurn(player);
            player.sendMessage("Your turn");
//            setTimer(this, 10);
        }
    }

    @Override
    public void playerExit(ClientHandler exitedPlayer) {
        player.sendMessage("You exited the game.");
        setGameOver(true);
    }

    @Override
    protected void cleanUpAndRequeue() {
//        if (getTimer() != null) getTimer().stop();
        player.sendMessage("Exiting Computer Game, type 'pve' or 'pvp' again to choose gamemode!");
        player.setGame(null, ' ');
    }

    @Override
    protected void broadcastMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    protected void broadcastBoard() {
        HashMap<Integer, Character> cells = getBoard().getCells();
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 1; i <= 9; i++) {
            sb.append(cells.get(i)).append(" ");
            if (i % 3 == 0) sb.append("\n");
        }
        player.sendMessage(sb.toString());
    }
}
