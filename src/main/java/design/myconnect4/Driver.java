package design.myconnect4;

import java.util.Scanner;

/**
 * Connect 4 Game Driver
 *
 * This is the main class that orchestrates a Connect 4 game between two players.
 * It handles the game loop, player turns, move input, and win/draw detection.
 *
 * Game Flow:
 * 1. Initialize a 6×7 board (standard Connect 4 dimensions)
 * 2. Create two players with different colors
 * 3. Alternate turns between players
 * 4. Accept column input from current player
 * 5. Make move and check for winner
 * 6. Continue until someone wins or board is full (42 moves = 6*7)
 * 7. Display final board and announce result
 *
 * Game Rules:
 * - Two players alternate turns
 * - Each turn, player chooses a column (0-6)
 * - Disc falls to lowest available position in that column
 * - First player to connect 4 discs in a row (any direction) wins
 * - If all 42 positions fill with no winner, game is a tie
 *
 * Design Improvements Possible:
 * - Validate column input (must be 0-6 and column must not be full)
 * - Add option to restart game
 * - Implement AI opponent
 * - Add undo/redo functionality
 * - Track game statistics
 *
 * @author Sandeep Chauhan
 */
public class Driver {

    /**
     * Main method that runs the Connect 4 game.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Initialize game board (6 rows × 7 columns)
        Board board = new Board(6, 7);

        int movesCount = 1;  // Track number of moves (max 42 for 6×7 board)

        // Create two players with different colors
        Player player1 = new Player("P1", 'B');  // Blue player
        Player player2 = new Player("P2", 'R');  // Red player

        Player currPlayer = player2;  // Will alternate to player1 on first iteration
        boolean foundWinner = false;

        // Game loop: continue until winner found or board is full
        while(!foundWinner && movesCount <= 42) {
            // Alternate between players
            currPlayer = currPlayer == player1 ? player2 : player1;

            // Display current board state
            board.display();

            // Get move from current player
            System.out.print("Player " + currPlayer.color + ", choose a column: ");
            int move = in.nextInt();

            // Make the move
            board.makeMove(move, currPlayer);

            // Check if current player won
            foundWinner = board.isWinner(currPlayer);

            movesCount++;
        }

        // Display final board state
        board.display();

        // Announce game result
        if (foundWinner) {
            System.out.println(currPlayer.name + " " + currPlayer.color + " won");
        } else {
            System.out.println("Tie game");
        }

        in.close();
    }
}
