package DynamicProgramming;

import java.util.Arrays;

/**
 * LeetCode Problem: Coin Change
 * Link: https://leetcode.com/problems/coin-change/
 *
 * Problem Statement:
 * - Given an array of coin denominations and a target amount,
 *   find the minimum number of coins needed to make up that amount.
 * - If it's not possible to form the target sum, return -1.
 * For example:
 *  Input: coins = [2, 3], target = 7
 *  Output: 3 (2 + 2 + 3)
 *
 *  Explanation:
 *  - You can use two coins of denomination 2 and one coin of denomination 3 to reach the target sum of 7.
 *
 * Approach:
 * - **Dynamic Programming (Bottom-Up)**
 *   - Create a `dp` array where `dp[i]` represents the minimum coins needed to reach amount `i`.
 *   - Initialize `dp[0] = 0` (0 coins needed to reach sum 0).
 *   - Set all other values to `target + 1` (a large value, acting as infinity).
 *   - Iterate over each amount `i`, checking all coin denominations.
 *   - If `i - coin >= 0`, update `dp[i]` with `min(dp[i], dp[i - coin] + 1)`.
 *   - If `dp[target]` remains as `target + 1`, return `-1` (not possible).
 *
 * Time Complexity: **O(target × n)**, where `n` is the number of coins.
 * Space Complexity: **O(target)**, since we use a `dp` array of size `target + 1`.
 */
public class CoinChange {

    public static void main(String[] args) {
        int[] coins = {2, 3};
        int target = 7;
        int result = minCoins(coins, target);
        System.out.printf("Min coins required to reach sum %d is %d%n", target, result);
    }

    /**
     * Finds the minimum number of coins required to reach a target sum.
     *
     * @param coins  Array of available coin denominations
     * @param target Target sum
     * @return Minimum number of coins needed, or -1 if not possible
     */
    public static int minCoins(int[] coins, int target) {
        if (coins == null || coins.length == 0 || target < 0) return -1;

        int[] dp = new int[target + 1];
        Arrays.fill(dp, target + 1); // Initialize to a large value (acting as infinity)
        dp[0] = 0; // 0 coins needed to make sum 0

        // Fill the DP array
        for (int amount = 1; amount <= target; amount++) {
            for (int coin : coins) {
                if (amount >= coin) {
                    dp[amount] = Math.min(dp[amount], dp[amount - coin] + 1);
                }
            }
        }

        // If dp[target] is still a large value, return -1 (impossible case)
        return dp[target] > target ? -1 : dp[target];
    }
}
