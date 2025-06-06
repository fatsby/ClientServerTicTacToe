import java.util.HashMap;
import java.util.Random;

public class Board {
    private HashMap<Integer, Character> cells;
    private final int[][] winPatterns;
    private Random rand = new Random();

    public Board() {
        this.cells = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            cells.put(i, '_');
        }

        this.winPatterns = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {1, 4, 7},
                {2, 5, 8},
                {3, 6, 9},
                {1, 5, 9},
                {3, 5, 7}
        };
    }

    public int computerInput() {
        int randomIndex = rand.nextInt(9) + 1;

        // Search from random index to the left
        for (int i = randomIndex; i >= 1; i--) {
            if (cells.get(i) == '_') {
                cells.replace(i, 'O');
                System.out.println("Computer chose: " + i);
                return i;
            }
        }

        // If not found, search to the right
        for (int i = randomIndex + 1; i <= 9; i++) {
            if (cells.get(i) == '_') {
                cells.replace(i, 'O');
                System.out.println("Computer chose: " + i);
                return i;
            }
        }

        return -1; // No valid moves
    }

    public void move(int input, char playerSymbol){
        cells.replace(input, playerSymbol);
    }

    public boolean isValidMove(int input){
        if (input > 9 || input < 1){
           return false;
        }

        return cells.get(input).equals('_');
    }

    public boolean checkWin() {
        for (int[] pattern : winPatterns) {
            char first = cells.get(pattern[0]);
            if (first == '_') continue;

            char second = cells.get(pattern[1]);
            char third = cells.get(pattern[2]);

            if (first == second && second == third) {
                return true;
            }
        }
        return false;
    }

    public boolean isBoardFull() {
        for (int i = 1; i <= 9; i++) {
            if (cells.get(i) == '_') {
                return false;
            }
        }
        return true;
    }

    public HashMap<Integer, Character> getCells(){
        return cells;
    }
}
