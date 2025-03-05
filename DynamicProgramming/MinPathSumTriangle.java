package DynamicProgramming;

import java.util.List;
import java.util.Arrays;

/**
 * Problem: Minimum Path Sum in a Triangle
 *
 * Given a triangle array, find the minimum path sum from **top to bottom**.
 * Each step, you may move to adjacent numbers in the row below.
 *
 * Example:
 * Input:
 * [
 *      [2],
 *     [3, 4],
 *    [6, 5, 7],
 *   [4, 1, 8, 3]
 * ]
 * Output: 11  (Path: 2 → 3 → 5 → 1)
 *
 * Approach:
 * - **Bottom-Up Dynamic Programming (Optimized Space)**
 * - Instead of using a full `dp[][]`, we use a **single 1D array**.
 * - Process the triangle **bottom-up**, modifying the `dp` array in place.
 *
 * Time Complexity: **O(n²)**  
 * Space Complexity: **O(n) (Optimized from O(n²))**
 */
public class MinPathSumTriangle {
    public static void main(String[] args) {
        List<List<Integer>> triangle = Arrays.asList(
                Arrays.asList(2),
                Arrays.asList(3, 4),
                Arrays.asList(6, 5, 7),
                Arrays.asList(4, 1, 8, 3)
        );
        System.out.println("Minimum Path Sum in Triangle: " + minimumTotalDP(triangle));
    }

    /**
     * Optimized Bottom-Up Dynamic Programming (O(n) Space)
     */
    public static int minimumTotalDP(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[] dp = new int[n];

        // Start with the last row
        for (int i = 0; i < n; i++) {
            dp[i] = triangle.get(n - 1).get(i);
        }

        // Build the minimum path sum from bottom to top
        for (int row = n - 2; row >= 0; row--) {
            for (int col = 0; col <= row; col++) {
                dp[col] = triangle.get(row).get(col) + Math.min(dp[col], dp[col + 1]);
            }
        }

        return dp[0]; // Minimum path sum at the top
    }
}
