package Backtracking;

import java.util.HashSet;
import java.util.Set;

/**
 * Count the number of pattern for N queen problem in board of size nXn
 */
public class NQueen2 {

    public static void main(String[] args) {
        int solutions = new NQueen2().totalNQueens(4);
        System.out.println("Total solutions are " + solutions);
    }

    private int size;

    public int totalNQueens(int n) {
        size = n;
        return backtrack(0, new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    private int backtrack(int row, Set<Integer> diagonals, Set<Integer> antiDiagonals, Set<Integer> cols) {
        // Base case - N queens have been placed
        if (row == size) {
            return 1;
        }

        int result = 0;
        for (int col = 0; col < size; col++) {
            int currDiagonal = row - col;
            int currAntiDiagonal = row + col;
            // If the queen is not placeable
            if (cols.contains(col) || diagonals.contains(currDiagonal) || antiDiagonals.contains(currAntiDiagonal)) {
                continue;
            }

            // Add queen
            cols.add(col);
            diagonals.add(currDiagonal);
            antiDiagonals.add(currAntiDiagonal);

            // Next row
            result += backtrack(row + 1, diagonals, antiDiagonals, cols);

            // Remove queen
            cols.remove(col);
            diagonals.remove(currDiagonal);
            antiDiagonals.remove(currAntiDiagonal);
        }
        return result;
    }
}
