package design.myconnect4;

/**
 * Connect 4 Board Implementation
 *
 * This class represents the game board for Connect 4, a two-player connection game
 * where players take turns dropping colored discs into a vertical grid. The objective
 * is to be the first to form a horizontal, vertical, or diagonal line of four discs.
 *
 * Board Structure:
 * - Standard Connect 4 board is 6 rows × 7 columns
 * - Gravity-based: pieces fall to the lowest available position in a column
 * - Empty cells represented by space character ' '
 * - Player pieces represented by their color character (e.g., 'R' for red, 'Y' for yellow)
 *
 * Game Rules:
 * - Players alternate turns dropping one disc per turn
 * - Discs fall straight down, occupying the lowest available space within the column
 * - A player wins by connecting 4 of their discs in a row (horizontal, vertical, or diagonal)
 * - Game is a draw if the board fills up with no winner
 *
 * Design Decisions:
 * - 2D character array for simple and efficient storage
 * - Gravity simulation by filling from bottom row upward
 * - Four separate checks for different win conditions (horizontal, vertical, 2 diagonals)
 *
 * Time Complexity:
 * - makeMove(): O(rows) - scans column from bottom to find empty spot
 * - isWinner(): O(rows * cols) - checks all possible 4-in-a-row combinations
 * - display(): O(rows * cols) - prints entire board
 *
 * Space Complexity: O(rows * cols) for the grid
 *
 * Possible Optimizations:
 * - Track last move position to check only relevant win conditions
 * - Use bitboards for faster win checking
 * - Maintain column heights array to avoid scanning for empty spots
 *
 * @author Sandeep Chauhan
 */
public class Board {

    char[][] grid; // 2D grid representing the Connect 4 board

    /**
     * Creates a new Connect 4 board with specified dimensions.
     * Initializes all cells to empty (space character).
     *
     * @param rows Number of rows in the board (typically 6)
     * @param cols Number of columns in the board (typically 7)
     */
    public Board(int rows, int cols) {
        grid = new char[rows][cols];
        // Initialize all cells to empty
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                grid[row][col] = ' ';
            }
        }
    }

    /**
     * Makes a move for the specified player in the given column.
     * The disc falls to the lowest available position (gravity effect).
     *
     * Algorithm:
     * - Start from bottom row and scan upward
     * - Find first empty cell in the column
     * - Place player's disc there
     *
     * Note: This method assumes the column is not full. In a complete implementation,
     * you should validate that the column has space before calling this method.
     *
     * @param col Column number where the disc should be dropped (0-indexed)
     * @param player Player making the move
     */
    public void makeMove(int col, Player player) {
        for (int row = grid.length - 1; row >= 0; row--) {
            if (grid[row][col] == ' ') {
                grid[row][col] = player.color;
                break;
            }
        }
    }

    /**
     * Checks if the specified player has won the game.
     * A player wins by connecting 4 of their discs in a row.
     *
     * Win Conditions Checked:
     * 1. Horizontal: 4 consecutive discs in any row
     * 2. Vertical: 4 consecutive discs in any column
     * 3. Diagonal (bottom-left to top-right): 4 consecutive discs going up-right
     * 4. Diagonal (top-left to bottom-right): 4 consecutive discs going down-right
     *
     * Optimization Note:
     * This checks the entire board. In practice, you could optimize by only checking
     * positions around the last move made.
     *
     * @param player Player to check for winning condition
     * @return true if player has won, false otherwise
     */
    public boolean isWinner(Player player) {
        char color = player.color;

        // Check for 4 across (horizontal)
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length - 3; col++) {
                if (grid[row][col] == color &&
                        grid[row][col + 1] == color &&
                        grid[row][col + 2] == color &&
                        grid[row][col + 3] == color) {
                    return true;
                }
            }
        }

        // Check for 4 up and down (vertical)
        for (int row = 0; row < grid.length - 3; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == color &&
                        grid[row + 1][col] == color &&
                        grid[row + 2][col] == color &&
                        grid[row + 3][col] == color) {
                    return true;
                }
            }
        }

        // Check left diagonal (ascending from bottom-left to top-right)
        for (int row = 0; row < grid.length - 3; row++) {
            for (int col = 3; col < grid[0].length; col++) {
                if (grid[row][col] == color &&
                        grid[row + 1][col - 1] == color &&
                        grid[row + 2][col - 2] == color &&
                        grid[row + 3][col - 3] == color) {
                    return true;
                }
            }
        }

        // Check right diagonal (descending from top-left to bottom-right)
        for (int row = 0; row < grid.length - 3; row++) {
            for (int col = 0; col < grid[0].length - 3; col++) {
                if (grid[row][col] == color &&
                        grid[row + 1][col + 1] == color &&
                        grid[row + 2][col + 2] == color &&
                        grid[row + 3][col + 3] == color) {
                    return true;
                }
            }
        }

        return false; // No winning condition found
    }

    /**
     * Displays the current state of the board in ASCII format.
     *
     * Output format:
     * - Column numbers at top and bottom for easy reference
     * - Horizontal separators between rows
     * - Vertical bars separating cells
     * - Empty cells shown as spaces
     * - Player discs shown as their color character
     */
    public void display() {
        System.out.println(" 0 1 2 3 4 5 6");
        System.out.println("---------------");
        for (int row = 0; row < grid.length; row++) {
            System.out.print("|");
            for (int col = 0; col < grid[0].length; col++) {
                System.out.print(grid[row][col]);
                System.out.print("|");
            }
            System.out.println();
            System.out.println("---------------");
        }
        System.out.println(" 0 1 2 3 4 5 6");
        System.out.println();
    }
}
