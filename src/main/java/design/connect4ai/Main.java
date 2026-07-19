package design.connect4ai;

import java.util.Scanner;

/**
 * Problem: Connect Four AI Demo Driver
 *
 * Provide an entry point for a Connect Four game where a human X player can play
 * against a minimax-controlled O player. The standardized demo below uses a
 * deterministic board sequence instead of interactive input.
 *
 * Pattern:  Design | Game loop | Minimax integration
 *
 * Example:
 *   Input:  X drops four discs in column 1 with O moves in column 2
 *   Output: X wins by column
 *   Why:    four X pieces stack vertically in the same column.
 *
 * Follow-ups:
 *   1. How would you validate user input robustly?
 *      Reject out-of-range and full-column moves before mutating the board.
 *   2. How would you make the AI stronger?
 *      Increase depth and add alpha-beta pruning with a better evaluation function.
 *   3. How would you support replay?
 *      Log each move and rebuild board state from the move list.
 */
public class Main {
    
    static int columnPosition;
    static State theBoard;
    static MinMax computerPlayer;
    ////////////////////
    
    public static void main(String[] args) {
        theBoard = new State();
        int[] columns = {0, 1, 0, 1, 0, 1, 0};
        int[] letters = {State.X, State.O, State.X, State.O, State.X, State.O, State.X};
        for (int i = 0; i < columns.length; i++) {
            theBoard.makeMove(columns[i], letters[i]);
        }

        boolean got = theBoard.checkWinState();
        System.out.printf("moves=vertical-X -> %s  expected=true%n", got);
        System.out.printf("winner -> %d  expected=%d%n", theBoard.winner, State.X);
    }
// end main method
}//end Main class