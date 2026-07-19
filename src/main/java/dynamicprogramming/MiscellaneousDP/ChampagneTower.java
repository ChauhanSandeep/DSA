package dynamicprogramming.MiscellaneousDP;

/**
 * Problem: Champagne Tower
 *
 * Champagne is poured into the top glass of a triangular tower. Every glass can
 * hold one cup; any extra splits evenly into the two glasses directly below it.
 * Return how full one requested glass is after the pour settles.
 *
 * Leetcode: https://leetcode.com/problems/champagne-tower/
 * Rating:   1856 (zerotrac Elo)
 * Pattern:  Dynamic programming | Simulation DP | Flow propagation
 *
 * Example:
 *   Input:  poured = 2, query_row = 1, query_glass = 1
 *   Output: 0.5
 *   Why:    the top glass keeps one cup and the one extra cup splits equally into
 *           the two glasses below, so the right glass in row 1 has half a cup.
 *
 * Follow-ups:
 *   1. Can space be reduced to one row?
 *      Yes, update a 1-D row array from right to left so overflow is not reused early.
 *   2. What if glasses have different capacities?
 *      Replace the fixed 1.0 cap with capacity[row][col] in the overflow transition.
 *   3. What if many queries are asked after the same pour?
 *      Precompute the tower up to the largest requested row and answer each query in O(1).
 *
 * Related: Pascal's Triangle (118), Pour Water (755).
 */
public class ChampagneTower {

    public static void main(String[] args) {
        ChampagneTower solver = new ChampagneTower();
        int[][] inputs = {{1, 1, 1}, {2, 1, 1}, {100000009, 33, 17}};
        double[] expected = {0.0, 0.5, 1.0};

        for (int i = 0; i < inputs.length; i++) {
            double got = solver.champagneTower(inputs[i][0], inputs[i][1], inputs[i][2]);
            System.out.printf("poured=%d row=%d col=%d -> %.1f  expected=%.1f%n",
                inputs[i][0], inputs[i][1], inputs[i][2], got, expected[i]);
        }
    }


    /**
     * Intuition: each dp[row][col] cell stores how much champagne ever reaches that
     * glass, not just how much it keeps. If a glass receives more than one cup, the
     * extra amount is the only part that can affect lower rows, and it splits evenly
     * to the two children. Because every glass only sends liquid downward, processing
     * rows from top to bottom guarantees all smaller subproblems are settled before
     * they feed the next row.
     *
     * Algorithm:
     *   1. Allocate dp through glassRow and put all poured champagne in dp[0][0].
     *   2. For each row above the query row, split only the excess above one cup to the two children.
     *   3. Return the queried cell capped at one full glass.
     *
     * Time:  O(r^2) - we fill the triangular table through the requested row.
     * Space: O(r^2) - the DP table stores all glasses up to the requested row.
     *
     * @param poured amount poured into the top glass
     * @param glassRow requested row index
     * @param glassCol requested column index within the row
     * @return amount in the requested glass, capped at 1.0
     */
    public double champagneTower(int poured, int glassRow, int glassCol) {
        // DP table to track champagne flow
        double[][] dp = new double[glassRow + 1][glassRow + 1];
        dp[0][0] = poured; // Pour into the topmost glass

        // Iterate through each row and distribute excess champagne
        for (int row = 0; row < glassRow; row++) {
            for (int col = 0; col <= row; col++) {
                double excess = Math.max(0, dp[row][col] - 1.0); // Overflow only if >1.0
                if (excess > 0) {
                    dp[row + 1][col] += excess / 2.0; // Left child
                    dp[row + 1][col + 1] += excess / 2.0; // Right child
                }
            }
        }

        // Return champagne level in the requested glass (max 1.0)
        return Math.min(1.0, dp[glassRow][glassCol]);
    }
}
