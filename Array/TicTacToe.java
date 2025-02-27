package Array;

/**
 * Create a grid of size n. Player 1 and 2 make move. Return the winner after each move.
 * Return 0 if no player wins.
 */
public class TicTacToe {

    public static void main(String[] args) {
        TicTacToe ticTacToe = new TicTacToe(2);
        System.out.println(ticTacToe.move(0, 1, 1));
        System.out.println(ticTacToe.move(1, 1, 2));
        System.out.println(ticTacToe.move(1, 0, 1));
    }

    int size;
    int[] grid;

    /** Initialize your data structure here. */
    public TicTacToe(int n) {
        this.size = n;
        grid = new int[2*n + 2];
    }

    /** Player {player} makes a move at ({row}, {col}).
     @param row The row of the board.
     @param col The column of the board.
     @param player The player, can be either 1 or 2.
     @return The current winning condition, can be either:
     0: No one wins.
     1: Player 1 wins.
     2: Player 2 wins. */
    public int move(int row, int col, int player) {
        int marker = player == 1 ? 1 : -1;
        grid[row] = grid[row] + marker;
        grid[size + col] =  grid[size + col] + marker;
        if(row == col) {
            grid[2*size] = grid[2*size] + marker;
        }
        if(size - 1 - row == col) {
            grid[2*size + 1] = grid[2*size + 1] + marker;
        }
        int target = marker * size;
        if(grid[row] == target || grid[size + col] == target || grid[2*size] == target || grid[2*size+1] == target) {
            return player;
        }
        return 0;
    }
}
