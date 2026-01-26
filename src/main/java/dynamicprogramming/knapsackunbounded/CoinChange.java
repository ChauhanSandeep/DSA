package dynamicprogramming.knapsackunbounded;

import java.util.Arrays;

/**
 * Coin Change
 *
 * Problem:
 * You are given an integer array coins representing coins of different denominations and an integer
 * amount representing a total amount of money. Return the fewest number of coins needed to make up
 * that amount. If that amount cannot be made up by any combination of the coins, return -1.
 * You may assume that you have an infinite number of each kind of coin.
 *
 * Example:
 * Input: coins = [1,2,5], amount = 11
 * Output: 3
 * Explanation: 11 = 5 + 5 + 1 (3 coins)
 *
 * Example:
 * Input: coins = [2], amount = 3
 * Output: -1
 * Explanation: Cannot make 3 with only coins of denomination 2
 *
 * Constraints:
 * - 1 <= coins.length <= 12
 * - 1 <= coins[i] <= 2^31 - 1
 * - 0 <= amount <= 10^4
 *
 * LeetCode: https://leetcode.com/problems/coin-change/
 *
 * Follow-up Questions:
 * Q1: What if we need to return the actual combination of coins, not just the count?
 * A1: Store parent pointers in DP array to backtrack and reconstruct the solution path.
 *
 * Q2: How would you optimize if the amount is very large but coins are limited?
 * A2: Use BFS approach with queue, exploring amounts level by level until target is reached.
 *
 * Q3: What if each coin can be used only a limited number of times?
 * A3: Add a third dimension to DP tracking remaining coins for each denomination (bounded knapsack).
 *
 * Q4: Can you solve this with recursion + memoization (top-down DP)?
 * A4: Yes - recursively compute min coins for (amount - coin) and memoize results in a map.
 *
 * Q5: What if we want to find the maximum number of coins instead of minimum?
 * A5: Change the logic to maximize instead of minimize, useful for maximizing change given.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class CoinChange {

    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;
        int result = minCoins(coins, amount);
        System.out.println("Minimum coins needed for amount " + amount + ": " + result);
        
        int[] coins2 = {2};
        int amount2 = 3;
        int result2 = minCoins(coins2, amount2);
        System.out.println("Minimum coins needed for amount " + amount2 + ": " + result2);
    }

    /**
     * Finds the minimum number of coins needed to make up the target amount.
     *
     * Algorithm (Bottom-Up Dynamic Programming):
     * 1. Create DP array where dp[i] = minimum coins needed for amount i
     * 2. Initialize dp[0] = 0 (base case: 0 coins for amount 0)
     * 3. Initialize all other values to infinity (amount + 1 as sentinel)
     * 4. For each amount from 1 to target:
     *    - Try each coin denomination
     *    - If coin <= amount, update: dp[amount] = min(dp[amount], dp[amount - coin] + 1)
     * 5. Return dp[target] if reachable, else -1
     *
     * Key Insight: For each amount, we build on optimal solutions for smaller amounts.
     * If we can make amount-coin with k coins, we can make amount with k+1 coins.
     *
     * Time Complexity: O(amount * n) where n is number of coin denominations
     * Space Complexity: O(amount) for the DP array
     *
     * @param coins Array of available coin denominations (infinite supply of each)
     * @param amount Target amount to make
     * @return Minimum number of coins needed, or -1 if impossible
     */
    public static int minCoins(int[] coins, int amount) {
        if (coins == null || coins.length == 0 || amount < 0) return -1;
        if (amount == 0) return 0;

        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // Use amount + 1 as infinity
        dp[0] = 0;

        // Build up solutions for all amounts from 1 to target
        for (int currentAmount = 1; currentAmount <= amount; currentAmount++) {
            for (int coin : coins) {
                if (currentAmount >= coin) {
                    dp[currentAmount] = Math.min(dp[currentAmount], dp[currentAmount - coin] + 1);
                }
            }
        }

        return dp[amount] > amount ? -1 : dp[amount];
    }
}
