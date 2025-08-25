package dynamicprogramming;

/**
 * LeetCode Problem: Champagne Tower
 * Link: https://leetcode.com/problems/champagne-tower/
 *
 * Problem Statement:
 * - A stack of glasses is arranged in a pyramid shape.
 * - If a glass overflows, the excess champagne splits equally into the two glasses below it.
 * - Given the amount of poured champagne, determine how much is in a specific glass.
 *
 * Approach:
 * - **Dynamic Programming (DP) Table**:
 *   - Use a 2D DP array where `dp[row][col]` stores the amount of champagne in a given glass.
 *   - If a glass contains more than 1 unit, the excess overflows evenly into the two glasses below.
 *   - We only need to process glasses up to `glassRow` since overflow doesn’t affect higher levels.
 *
 * Time Complexity: **O(glassRow²)** (Only need to fill up to `glassRow`)
 * Space Complexity: **O(glassRow²)** (DP table of size `glassRow × glassRow`)
 */
public class ChampagneTower {

    public static void main(String[] args) {
        ChampagneTower solver = new ChampagneTower();
        double result = solver.champagneTower(3, 3, 1);
        System.out.println("Champagne in glass (3,1): " + result);
    }

    /**
     * Computes the amount of champagne in a specific glass after pouring a given amount.
     *
     * @param poured    Amount of champagne poured
     * @param glassRow  Target row of the glass
     * @param glassCol  Target column in the row
     * @return Amount of champagne in the given glass (max 1.0)
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
