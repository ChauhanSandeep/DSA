package backtrack;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem: N-Queens II
 *
 * Count how many distinct ways n queens can be placed on an n x n board so no
 * two queens share a row, column, or diagonal. Unlike N-Queens I, only the count
 * is returned.
 *
 * Leetcode: https://leetcode.com/problems/n-queens-ii/
 * Rating:   acceptance 79.0% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Bitmask pruning | Count only
 *
 * Example:
 *   Input:  n = 4
 *   Output: 2
 *   Why:    n = 4 has exactly the two valid boards from N-Queens I, and this
 *           version returns only their count.
 *
 * Follow-ups:
 *   1. Return boards instead of a count?
 *      Keep column placements and render them at the base case, as in N-Queens I.
 *   2. Reduce symmetric work?
 *      Place the first queen only in half the columns and mirror counts, handling the center separately.
 *   3. Support n beyond integer bit width?
 *      Use long up to 63 columns, or BigInteger/BitSet for larger boards.
 *   4. Parallelize the search?
 *      Split by first-row column choices; each subtree is independent.
 *
 * Related: N-Queens (51).
 *
 *   Approach             Method                 Time   Space (extra)
 *   -------------------  ---------------------  -----  -------------
 *   HashSet attacks      totalNQueensUsingSets  O(n!)  O(n)
 *   Bitmask attacks      totalNQueens           O(n!)  O(n)
 */
public class NQueen2 {

    /**
     * Intuition: this is N-Queens without needing to store the actual boards. We
     * still place one queen per row and use sets to remember which columns and
     * diagonals are attacked. Every time we successfully place queens through the
     * last row, that path represents exactly one valid board, so the base case
     * returns 1 instead of copying a board.
     *
     * Algorithm:
     *   1. Return 0 for non-positive board sizes.
     *   2. DFS row by row with sets for occupied columns, main diagonals, and anti-diagonals.
     *   3. For the current row, try each column that is not present in any attack set.
     *   4. Mark the queen's attacks, add the number of solutions from the next row,
     *      then remove those marks before trying the next column.
     *   5. Return 1 when all rows have been filled, because one complete arrangement was found.
     *
     * Time:  O(n!) - each row chooses from the remaining safe columns, so choices shrink quickly.
     * Space: O(n) recursion depth and attack sets.
     *
     * @param boardSize board size n
     * @return number of valid queen placements
     */
    public int totalNQueensUsingSets(int boardSize) {
        if (boardSize <= 0) return 0;

        return countWithSets(0, boardSize, new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    /** Counts valid queen placements using sets for occupied columns and diagonals. */
    private int countWithSets(int row, int boardSize,
                              Set<Integer> occupiedColumns,
                              Set<Integer> occupiedMainDiagonals,
                              Set<Integer> occupiedAntiDiagonals) {
        if (row == boardSize) return 1;

        int totalSolutions = 0;
        for (int col = 0; col < boardSize; col++) {
            int mainDiagonal = row - col;
            int antiDiagonal = row + col;
            if (occupiedColumns.contains(col)
                || occupiedMainDiagonals.contains(mainDiagonal)
                || occupiedAntiDiagonals.contains(antiDiagonal)) {
                continue;
            }

            occupiedColumns.add(col);
            occupiedMainDiagonals.add(mainDiagonal);
            occupiedAntiDiagonals.add(antiDiagonal);
            totalSolutions += countWithSets(row + 1, boardSize,
                occupiedColumns, occupiedMainDiagonals, occupiedAntiDiagonals);
            occupiedColumns.remove(col);
            occupiedMainDiagonals.remove(mainDiagonal);
            occupiedAntiDiagonals.remove(antiDiagonal);
        }
        return totalSolutions;
    }

    /**
     * Intuition (interview default): the three attack sets can be compressed into
     * bitmasks. A 1 bit means that column position is blocked for the current row.
     * OR'ing the column and diagonal masks gives all unsafe positions, and the
     * remaining 1 bits are exactly the legal places for the next queen. Picking
     * the lowest set bit explores one legal column, then shifting diagonal masks
     * prepares the attacks for the next row.
     *
     * Algorithm:
     *   1. Return 0 for non-positive board sizes and build a mask with the lowest n bits set.
     *   2. At each row, compute available positions by removing occupied columns
     *      and diagonals from the full board mask.
     *   3. While legal positions remain, take one set bit as the queen's column and remove it from available.
     *   4. Recurse with that column marked occupied and both diagonal masks shifted
     *      to represent their attacks on the next row.
     *   5. Return 1 when the occupied-column mask equals the full board mask, meaning n queens were placed.
     *
     * Time:  O(n!) - each row tries only currently available bit positions.
     * Space: O(n) recursion depth.
     *
     * @param boardSize board size n
     * @return number of valid queen placements
     */
    public int totalNQueens(int boardSize) {
        if (boardSize <= 0) return 0;

        int allColumnsMask = (1 << boardSize) - 1;
        return countWithBits(allColumnsMask, 0, 0, 0);
    }

    /** Counts valid queen placements using bitmasks for columns and diagonals. */
    private int countWithBits(int allColumnsMask, int occupiedColumns,
                              int occupiedMainDiagonals, int occupiedAntiDiagonals) {
        if (occupiedColumns == allColumnsMask) return 1;

        int availablePositions = allColumnsMask
            & ~(occupiedColumns | occupiedMainDiagonals | occupiedAntiDiagonals);
        int totalSolutions = 0;

        while (availablePositions != 0) {
            int position = availablePositions & -availablePositions;
            availablePositions ^= position;
            totalSolutions += countWithBits(
                allColumnsMask,
                occupiedColumns | position,
                (occupiedMainDiagonals | position) << 1,
                (occupiedAntiDiagonals | position) >> 1
            );
        }
        return totalSolutions;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        NQueen2 solver = new NQueen2();

        int[] inputs = {1, 4, 5};
        int[] expected = {1, 2, 10};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.totalNQueens(inputs[i]);
            System.out.printf("n=%d  ->  %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }
}
