package design.myconnect4;

/**
 * Problem: Connect Four Board
 *
 * Represent a gravity-based Connect Four grid where players drop pieces into
 * columns. The board can place a move, scan for any four-in-a-row win, and display
 * the current grid.
 *
 * Pattern:  Design | 2D grid | Directional win scanning
 *
 * Example:
 *   Input:  four moves by the same player in column 0
 *   Output: isWinner(player) = true
 *   Why:    the pieces occupy four consecutive cells vertically.
 *
 * Follow-ups:
 *   1. How would you reject moves in a full column?
 *      Track column heights and return false when the height reaches the row count.
 *   2. How would you optimize winner detection?
 *      Check only directions through the most recent move.
 *   3. How would you support arbitrary connect-k games?
 *      Parameterize the target run length and board dimensions.
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

    public static void main(String[] args) {
        Board board = new Board(6, 7);
        Player player = new Player("P1", 'B');
        for (int i = 0; i < 4; i++) {
            board.makeMove(0, player);
        }
        System.out.printf("moves=column-0-four-times -> %s  expected=true%n", board.isWinner(player));

        Board emptyBoard = new Board(6, 7);
        System.out.printf("moves=empty-board -> %s  expected=false%n", emptyBoard.isWinner(player));
    }
}
