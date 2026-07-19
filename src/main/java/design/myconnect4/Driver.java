package design.myconnect4;

import java.util.Scanner;

/**
 * Problem: Connect Four Game Driver
 *
 * Coordinate a two-player Connect Four game by creating a board, alternating turns,
 * applying moves, and checking for a winner or draw. The standardized demo below is
 * deterministic so it can run without console input.
 *
 * Pattern:  Design | Game orchestration | Turn loop
 *
 * Example:
 *   Input:  player B drops four discs in column 0
 *   Output: player B wins
 *   Why:    the board detects a vertical run of four B discs.
 *
 * Follow-ups:
 *   1. How would you validate interactive input?
 *      Check numeric range and column capacity before placing a disc.
 *   2. How would you add undo?
 *      Store a stack of moves and clear the last occupied cell in that column.
 *   3. How would you add an AI player?
 *      Replace one input source with minimax or Monte Carlo tree search.
 */
public class Driver {

    /**
     * Main method that runs the Connect 4 game.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Board board = new Board(6, 7);
        Player player1 = new Player("P1", 'B');
        Player player2 = new Player("P2", 'R');
        int[] columns = {0, 1, 0, 1, 0, 1, 0};
        Player[] players = {player1, player2, player1, player2, player1, player2, player1};
        for (int i = 0; i < columns.length; i++) {
            board.makeMove(columns[i], players[i]);
        }

        System.out.printf("moves=vertical-B -> %s  expected=true%n", board.isWinner(player1));
        System.out.printf("moves=vertical-B,check-R -> %s  expected=false%n", board.isWinner(player2));
    }

}
