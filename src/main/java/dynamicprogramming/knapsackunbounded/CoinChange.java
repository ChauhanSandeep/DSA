package dynamicprogramming.knapsackunbounded;

import java.util.Arrays;

/**
 * Problem: Coin Change
 *
 * Return the fewest coins needed to make an amount using unlimited copies of each denomination. Return -1 if the amount is unreachable.
 *
 * Leetcode: https://leetcode.com/problems/coin-change/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | Unbounded knapsack | Minimum count
 *
 * Example:
 *   Input:  coins = [1, 2, 5], amount = 11
 *   Output: 3
 *   Why:    11 = 5 + 5 + 1 uses three coins, and no two coins can make 11.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Coin Change II (518), Combination Sum IV (377).
 */
public class CoinChange {

        public static void main(String[] args) {
        int[][] coinCases = { {1, 2, 5}, {2}, {1} };
        int[] amounts = {11, 3, 0};
        int[] expected = {3, -1, 0};
        for (int i = 0; i < coinCases.length; i++) {
            int got = minCoins(coinCases[i], amounts[i]);
            System.out.printf("coins=%s amount=%d -> %d  expected=%d%n", Arrays.toString(coinCases[i]), amounts[i], got, expected[i]);
        }
    }

        /**
     * Intuition: dp[currentAmount] is the fewest coins for exactly currentAmount. If coin is the last coin used, the previous optimal amount is currentAmount - coin, so trying all fitting coins gives the minimum.
     *
     * Algorithm:
     *   1. Reject invalid inputs and handle amount 0.
     *   2. Fill dp with amount + 1 as infinity.
     *   3. Set dp[0] = 0.
     *   4. For each amount, try every coin that fits.
     *   5. Return -1 if amount remains unreachable.
     *
     * Time:  O(amount * coins.length) - each amount tries each coin.
     * Space: O(amount) - one array of amounts.
     *
     * @param coins denominations with unlimited supply
     * @param amount target amount
     * @return minimum coins or -1
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
