package DynamicProgramming;

/**
 * Problem: Stone Game
 * 
 * Given an array `piles` where `piles[i]` represents the number of stones in each pile,
 * two players (Alex & Lee) take turns picking stones from either end. The goal is to determine
 * whether Alex can win given both play optimally.
 *
 * Solution Approaches:
 * 1. **Mathematical Proof**: The problem guarantees an Alex win for even-length piles.
 * 2. **Dynamic Programming (Memoized Recursion)**: Uses a `dp` table to store previously computed results.
 *
 * Time Complexity:
 * - **Mathematical Proof:** O(1)
 * - **Recursive DP (Optimal Substructure):** O(N²)
 *
 * LeetCode Problem: https://leetcode.com/problems/stone-game/
 */
public class StoneGame {

    public static void main(String[] args) {
        int[] piles = {5, 3, 4, 5};
        System.out.println("Alex wins? " + stoneGameMathProof());  // Always true
        System.out.println("Alex wins? " + stoneGameDP(piles));   // Using DP
    }

    /**
     * **Mathematical Proof Approach**
     * - Alex **always** wins when `piles.length` is even.
     * - The game guarantees a winning strategy for the first player.
     * - Proof: The sum of stones in odd and even indexed piles can always be manipulated for a win.
     *
     * Time Complexity: O(1)
     */
    public static boolean stoneGameMathProof() {
        return true;  // Always true for an even-length piles array
    }

    /**
     * **Dynamic Programming (Top-Down Memoization)**
     * - Uses a `dp` table where `dp[i][j]` stores the maximum stones Alex can collect
     *   from `piles[i]` to `piles[j]`, assuming both play optimally.
     *
     * Time Complexity: O(N²), Space Complexity: O(N²)
     */
    public static boolean stoneGameDP(int[] piles) {
        int n = piles.length;
        int[][] dp = new int[n][n];

        // Base case: When i == j, the only choice is taking the single pile
        for (int i = 0; i < n; i++) {
            dp[i][i] = piles[i];
        }

        // Fill DP table for subarrays of increasing length
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                // Alex picks either left (i) or right (j)
                dp[i][j] = Math.max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }

        // Alex wins if his total stones are greater than half of the total
        return dp[0][n - 1] > 0;
    }
}
