package dynamicprogramming.knapsackunbounded;

import java.util.Arrays;


/**
 * Problem: Coin Change II
 *
 * Count combinations that sum to a target amount when every denomination is available unlimited times. Coin order does not matter.
 *
 * Leetcode: https://leetcode.com/problems/coin-change-ii/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | Unbounded knapsack | Combination counting
 *
 * Example:
 *   Input:  amount = 5, coins = [1, 2, 5]
 *   Output: 4
 *   Why:    the combinations are [5], [2,2,1], [2,1,1,1], and five 1s.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Coin Change (322), Combination Sum IV (377).
 */
public class CoinChangeII {
        public static void main(String[] args) {
        CoinChangeII solver = new CoinChangeII();
        int[][] coinCases = { {1, 2, 5}, {2}, {10} };
        int[] amounts = {5, 3, 0};
        int[] expected = {4, 0, 1};
        for (int i = 0; i < coinCases.length; i++) {
            int got = solver.changeRecursiveMemo(amounts[i], coinCases[i]);
            System.out.printf("coins=%s amount=%d -> %d  expected=%d%n", Arrays.toString(coinCases[i]), amounts[i], got, expected[i]);
        }
    }

        /**
     * Intuition: dp[i][j] counts combinations for amount j using the first i coins. Excluding coin i copies dp[i - 1][j]; including it adds dp[i][j - coins[i - 1]] because the same coin row remains available.
     *
     * Algorithm:
     *   1. Create dp[length + 1][amount + 1].
     *   2. Set dp[i][0] = 1 for all i.
     *   3. Iterate i from 1..length and j from 1..amount.
     *   4. Start from the exclude-current value.
     *   5. Add the include-current value when the coin fits.
     *
     * Time:  O(length * amount) - every table cell is filled once.
     * Space: O(length * amount) - stores the full table.
     *
     * @param amount target amount
     * @param coins coin denominations
     * @return number of combinations
     */
public int change(int amount, int[] coins) {
        int length = coins.length;
        
        // dp[i][j] = number of ways to make amount j using first i coins
        int[][] dp = new int[length + 1][amount + 1];
        
        // Base case: one way to make amount 0 (use no coins)
        for (int i = 0; i <= length; i++) {
            dp[i][0] = 1;
        }
        
        // Fill the DP table
        for (int i = 1; i <= length; i++) {
            for (int j = 1; j <= amount; j++) {
                // Option 1: Don't use the current coin (coins[i-1])
                dp[i][j] = dp[i - 1][j];
                
                // Option 2: Use the current coin (if amount allows)
                if (j >= coins[i - 1]) {
                    // Add ways to make (j - coins[i-1]) using same set of coins
                    // We use dp[i][...] not dp[i-1][...] because coin can be reused (unbounded)
                    dp[i][j] += dp[i][j - coins[i - 1]];
                }
            }
        }
        // print dp table for debugging
        for (int i = 0; i <= length; i++) {
            for (int j = 0; j <= amount; j++) {
                System.out.print(dp[i][j] + "\t");
            }
            System.out.println();
        }
        
        return dp[length][amount];
    }

    /**
     * Approach 3: Top-Down Dynamic Programming (Recursion with Memoization)
     * 
     * Algorithm:
     * 1. Use recursion to explore two choices for each coin: include it or exclude it
     * 2. Use a 2D memoization table to cache results of subproblems
     * 3. State: (coinIndex, remainingAmount) -> number of ways
     * 4. Base cases:
     *    - If remainingAmount == 0: return 1 (found valid combination)
     *    - If coinIndex >= coins.length: return 0 (no more coins to try)
     *    - If remainingAmount < 0: return 0 (invalid combination)
     * 5. Recursive relation:
     *    - Exclude current coin: recurse with (coinIndex + 1, remainingAmount)
     *    - Include current coin: recurse with (coinIndex, remainingAmount - coins[coinIndex])
     * 
     * Key Insights:
     * - We maintain coin index to ensure we don't count duplicate combinations
     * - By not incrementing coin index when including a coin, we allow unlimited reuse (unbounded)
     * - Memoization prevents recalculating same (coinIndex, amount) pairs
     * 
     * Time Complexity: O(n * amount) where n is number of coins
     * - Each unique (coinIndex, amount) pair is computed once and cached
     * - There are n * amount possible states
     * 
     * Space Complexity: O(n * amount) for memoization table + O(amount) for recursion stack
     * - Worst case recursion depth is amount (when using coin value 1)
     */
    public int changeRecursiveMemo(int amount, int[] coins) {
        Integer[][] memo = new Integer[coins.length][amount + 1];
        return coinChangeHelper(coins, 0, amount, memo);
    }

        /** Counts combinations from one coin index and remaining amount. */
private int coinChangeHelper(int[] coins, int coinIndex, int remainingAmount, Integer[][] memo) {
        // Base case 1: Successfully made the amount
        if (remainingAmount == 0) {
            return 1;
        }
        
        // Base case 2: Amount went negative (invalid path)
        if (remainingAmount < 0) {
            return 0;
        }
        
        // Base case 3: No more coins to use
        if (coinIndex >= coins.length) {
            return 0;
        }
        
        // Check if already computed
        if (memo[coinIndex][remainingAmount] != null) {
            return memo[coinIndex][remainingAmount];
        }
        
        // Decision 1: Exclude current coin, move to next coin
        int excludeCoin = coinChangeHelper(coins, coinIndex + 1, remainingAmount, memo);
        
        // Decision 2: Include current coin, stay at same coin index (unbounded - can reuse)
        int includeCoin = coinChangeHelper(coins, coinIndex, remainingAmount - coins[coinIndex], memo);
        
        // Total ways = ways without this coin + ways with this coin
        memo[coinIndex][remainingAmount] = excludeCoin + includeCoin;
        
        return memo[coinIndex][remainingAmount];
    }
    
}
