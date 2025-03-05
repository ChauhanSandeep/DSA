package DynamicProgramming;

/**
 * Problem: Given an integer `n`, find the number of ways to completely fill a `2 x n` board using `2 x 1` dominoes.
 *
 * **Approach: Dynamic Programming (Fibonacci Variant)**
 * - A `2 x n` board can be tiled in two ways:
 *   1. Placing a vertical domino on the last column → Recurrence relation: `dp[n] = dp[n - 1]`
 *   2. Placing two horizontal dominoes on the last two columns → Recurrence relation: `dp[n] = dp[n - 2]`
 * - This results in the formula:  
 *   **dp[n] = dp[n - 1] + dp[n - 2]** (which resembles Fibonacci numbers)
 *
 * **Time Complexity:** O(N) → Iterates once through `n`.  
 * **Space Complexity:** O(1) → Uses only two variables instead of an array.  
 *
 * **LeetCode Problem (Similar Concept):** https://leetcode.com/problems/domino-and-tromino-tiling/
 */
public class TilingDominoes {
    public static void main(String[] args) {
        int n = 5;
        System.out.println("Number of ways to tile a 2 x " + n + " board: " + countWaysToTile(n));
    }

    public static int countWaysToTile(int n) {
        // Base cases
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;

        // Space-optimized DP approach (instead of storing entire DP array)
        int prev2 = 1; // dp[1]
        int prev1 = 2; // dp[2]
        int currentWays = 0;

        for (int i = 3; i <= n; i++) {
            currentWays = prev1 + prev2; // dp[i] = dp[i-1] + dp[i-2]
            prev2 = prev1;
            prev1 = currentWays;
        }

        return currentWays;
    }
}
