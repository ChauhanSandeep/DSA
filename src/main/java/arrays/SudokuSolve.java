package arrays;

/**
 * Sudoku Solver using Backtracking
 *
 * Sudoku is a 9×9 grid-based number puzzle where the objective is to fill the grid
 * so that each row, column, and 3×3 sub-grid contains all digits from 1 to 9.
 *
 * Algorithm: Backtracking
 * This solution uses backtracking to systematically try all possible valid placements
 * until a solution is found or all possibilities are exhausted.
 *
 * Key Optimization: Constraint Tracking
 * Instead of validating constraints on every placement attempt, this implementation
 * maintains three boolean arrays to track which digits are already used in:
 * 1. Each row (rowDigit)
 * 2. Each column (colDigit)
 * 3. Each 3×3 block (blockDigit)
 *
 * This reduces validation time from O(1) checks (instead of O(9) checks per placement).
 *
 * Backtracking Process:
 * 1. Find next empty cell (marked with '.')
 * 2. Try placing digits 1-9 in that cell
 * 3. For each digit, check if it violates constraints
 * 4. If valid, place the digit and recurse to next cell
 * 5. If recursion succeeds, solution found
 * 6. If recursion fails, backtrack (undo placement) and try next digit
 * 7. If all digits fail, return false (no solution from this state)
 *
 * Time Complexity: O(9^(n*n)) in worst case, where n=9 (grid size)
 * - In practice, much faster due to constraint pruning
 * - Most cells have limited valid choices
 *
 * Space Complexity: O(n*n) for constraint tracking arrays + recursion stack
 *
 * LeetCode Problem: https://leetcode.com/problems/sudoku-solver/
 *
 * Related Problems:
 * - N-Queens Problem (similar backtracking pattern)
 * - Valid Sudoku (checking if a board is valid)
 * - Combination Sum (backtracking with constraints)
 *
 * @author Sandeep Chauhan
 */
public class SudokuSolve {
    private static final int SIZE = 9; // Standard Sudoku size (9x9 grid)

    // Constraint tracking arrays for fast validation
    private static boolean[][] rowDigit = new boolean[SIZE][SIZE];    // rowDigit[i][d] = true if digit d is used in row i
    private static boolean[][] colDigit = new boolean[SIZE][SIZE];    // colDigit[j][d] = true if digit d is used in column j
    private static boolean[][] blockDigit = new boolean[SIZE][SIZE];  // blockDigit[b][d] = true if digit d is used in block b

    /**
     * Main method demonstrating Sudoku solver usage.
     * Uses a standard Sudoku puzzle as input.
     */
    public static void main(String[] args) {
        char[][] board =
                {
                        {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                        {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                        {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                        {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                        {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                        {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                        {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                        {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                        {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
                };

        if (solveSudoku(board)) {
            printBoard(board);
        } else {
            System.out.println("No solution exists!");
        }
    }

    /**
     * Solves the given Sudoku board using backtracking.
     *
     * @param board 9x9 character array representing Sudoku board ('.' for empty cells, '1'-'9' for filled cells)
     * @return true if solution found and board is modified with solution, false if no solution exists
     */
    public static boolean solveSudoku(char[][] board) {
        if (board == null || board.length != SIZE || board[0].length != SIZE) {
            return false; // Invalid board dimensions
        }

        initializeConstraints(board);
        return solve(0, board);
    }

    /**
     * Initializes constraint tracking arrays based on pre-filled cells.
     *
     * Processes the initial board state and marks which digits are already used
     * in each row, column, and 3×3 block. This initialization allows O(1) constraint
     * checking during the solving process.
     *
     * Block Indexing:
     * - Blocks are numbered 0-8 from left to right, top to bottom
     * - Block number = (row / 3) * 3 + (col / 3)
     * - Example: cell (4,5) is in block (4/3)*3 + (5/3) = 1*3 + 1 = 4 (center block)
     *
     * @param board The Sudoku board to analyze
     */
    private static void initializeConstraints(char[][] board) {
        rowDigit = new boolean[SIZE][SIZE];
        colDigit = new boolean[SIZE][SIZE];
        blockDigit = new boolean[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != '.') {
                    int num = board[i][j] - '1';
                    int block = (i / 3) * 3 + (j / 3); // (i / 3) * 3 gives the row of the block and (j / 3) gives the column of the block
                    rowDigit[i][num] = colDigit[j][num] = blockDigit[block][num] = true;
                }
            }
        }
    }

    /**
     * Recursive backtracking function to solve Sudoku.
     *
     * This function processes cells in row-major order (left to right, top to bottom).
     * For each empty cell, it tries placing digits 1-9 and recursively attempts to
     * solve the rest of the board.
     *
     * Backtracking Strategy:
     * 1. Base case: If index reaches 81 (9*9), all cells filled successfully
     * 2. Skip cells that are already filled (pre-filled or solved earlier)
     * 3. For empty cells, try each digit 1-9
     * 4. Check if digit violates row, column, or block constraints
     * 5. If valid, place digit and mark it as used in constraint arrays
     * 6. Recursively try to solve remaining cells
     * 7. If recursion succeeds, propagate success upward
     * 8. If recursion fails, backtrack: remove digit and unmark constraints
     * 9. If all digits fail, return false (dead end, need to backtrack further)
     *
     * @param index Current cell position (0-80, calculated as row*9 + col)
     * @param board The Sudoku board being solved
     * @return true if solution found from this state, false otherwise
     */
    private static boolean solve(int index, char[][] board) {
        if (index == SIZE * SIZE) return true; // Base case: All cells filled successfully

        int row = index / SIZE;    // Convert linear index to 2D coordinates
        int col = index % SIZE;
        int block = (row / 3) * 3 + (col / 3); // Calculate which 3×3 block this cell belongs to

        if (board[row][col] != '.') {
            return solve(index + 1, board); // Cell already filled, move to next
        }

        // Try placing each digit 1-9
        for (char c = '1'; c <= '9'; c++) {
            int num = c - '1'; // Convert character to index (0-8)

            // Check if placing this digit violates any constraints
            if (!rowDigit[row][num] && !colDigit[col][num] && !blockDigit[block][num]) {
                // CHOOSE: Place the digit and mark constraints
                board[row][col] = c;
                rowDigit[row][num] = colDigit[col][num] = blockDigit[block][num] = true;

                // EXPLORE: Recursively try to solve rest of the board
                if (solve(index + 1, board)) return true; // Solution found!

                // UNCHOOSE (Backtrack): Remove digit and unmark constraints
                board[row][col] = '.';
                rowDigit[row][num] = colDigit[col][num] = blockDigit[block][num] = false;
            }
        }
        return false; // No valid digit found for this cell, need to backtrack
    }

    /**
     * Prints the Sudoku board in a formatted grid.
     * Includes visual separators for the 3×3 sub-grids.
     *
     * @param board The Sudoku board to print
     */
    private static void printBoard(char[][] board) {
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
