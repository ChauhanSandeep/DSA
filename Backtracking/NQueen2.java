package Backtracking;

/**
 * LeetCode: https://leetcode.com/problems/n-queens-ii/
 *
 * Problem:
 * The N-Queens problem asks to count the number of valid ways to place N queens on an N×N chessboard
 * such that no two queens attack each other.
 *
 * Optimized Approach:
 * - Uses **bitwise operations** instead of HashSet for faster lookup and reduced space complexity.
 * - Instead of three Sets (O(1) lookup but more memory), we use **three integers as bit masks**:
 *   1. `columns`: Tracks occupied columns.
 *   2. `diagonals`: Tracks occupied major diagonals (`row - col`).
 *   3. `antiDiagonals`: Tracks occupied minor diagonals (`row + col`).
 * - Each integer is a **bit mask**, allowing O(1) checking & updating.
 *
 * Time Complexity:  O(N!) → Each queen placement significantly reduces available options.
 * Space Complexity: O(N)  → Using bitwise tracking instead of extra data structures.
 */
public class NQueen2 {

    public static void main(String[] args) {
        int n = 4;
        NQueen2 solver = new NQueen2();
        int solutions = solver.totalNQueens(n);
        System.out.println("Total solutions for " + n + "-Queens: " + solutions);
    }

    /**
     * Counts the number of valid N-Queens solutions using bitwise optimization.
     *
     * @param n The size of the chessboard (n × n).
     * @return The total number of valid placements.
     */
    public int totalNQueens(int n) {
        return backtrack(0, n, 0, 0, 0);
    }

    /**
     * Backtracking function to explore queen placements with bitwise operations.
     *
     * @param row          The current row being processed.
     * @param n            The size of the board.
     * @param columns      Bit mask tracking occupied columns.
     * @param diagonals    Bit mask tracking occupied major diagonals (`row - col`).
     * @param antiDiagonals Bit mask tracking occupied minor diagonals (`row + col`).
     * @return The count of valid N-Queens solutions.
     */
    private int backtrack(int row, int n, int columns, int diagonals, int antiDiagonals) {
        // Base case: All queens are placed
        if (row == n) return 1;

        // Determine available positions (bit mask where 1 means the column is free)
        int availablePositions = ((1 << n) - 1) & ~(columns | diagonals | antiDiagonals);
        int totalSolutions = 0;

        // Try placing a queen at each valid position
        while (availablePositions != 0) {
            // Pick the rightmost available position
            int position = availablePositions & -availablePositions;
            availablePositions ^= position; // Remove this position from available ones

            // Recur to the next row with updated bit masks
            totalSolutions += backtrack(row + 1, n, columns | position, 
                                        (diagonals | position) << 1, 
                                        (antiDiagonals | position) >> 1);
        }

        return totalSolutions;
    }
}
