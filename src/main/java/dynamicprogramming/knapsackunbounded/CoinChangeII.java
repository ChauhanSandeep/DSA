package dynamicprogramming.knapsackunbounded;


/**
 * Problem: Coin Change II
 * 
 * You are given an integer array coins representing coins of different denominations 
 * and an integer amount representing a total amount of money.
 * 
 * Return the number of combinations that make up that amount. If that amount of money 
 * cannot be made up by any combination of the coins, return 0.
 * 
 * You may assume that you have an infinite number of each kind of coin.
 * The answer is guaranteed to fit into a signed 32-bit integer.
 * 
 * Example 1:
 * Input: amount = 5, coins = [1,2,5]
 * Output: 4
 * Explanation: There are four ways to make up the amount:
 * 5 = 5
 * 5 = 2 + 2 + 1
 * 5 = 2 + 1 + 1 + 1
 * 5 = 1 + 1 + 1 + 1 + 1
 * 
 * LeetCode: https://leetcode.com/problems/coin-change-ii/description/
 * 
 * Follow-up Questions:
 * 
 * 1. Q: What is the difference between Coin Change I and Coin Change II?
 *    A: Coin Change I asks for the minimum number of coins to make the amount (optimization problem), 
 *    while Coin Change II asks for the number of combinations to make the amount (counting problem). 
 *    Coin Change I uses min() operation, while Coin Change II uses sum() operation in DP transitions.
 *    Related: LeetCode 322 - Coin Change (https://leetcode.com/problems/coin-change/)
 * 
 * 2. Q: Why does iterating coins in outer loop and amount in inner loop avoid counting duplicates?
 *    A: By fixing the coin order in the outer loop, we ensure combinations are built in a consistent 
 *    sequence. For example, [1,2] and [2,1] are treated as the same combination because we always 
 *    consider coin 1 before coin 2. If we iterate amount in outer loop, we would count permutations 
 *    instead of combinations.
 * 
 * 3. Q: How would you modify this to count permutations instead of combinations?
 *    A: Swap the loop order - iterate amount in outer loop and coins in inner loop. This allows 
 *    different orderings of the same coins to be counted separately (e.g., [1,2] and [2,1] as different).
 *    Related: LeetCode 377 - Combination Sum IV (https://leetcode.com/problems/combination-sum-iv/)
 * 
 * 4. Q: Can you solve this with space complexity O(amount) instead of O(n * amount)?
 *    A: Yes, see Approach 2 below. Since we only need the previous row to compute the current row, 
 *    we can use a 1D array and update it in-place. This is possible because we iterate coins in 
 *    outer loop and amounts in inner loop.
 * 
 * 5. Q: What if you need to return the actual combinations instead of just the count?
 *    A: Modify the DP to store lists of combinations instead of counts. At each step, for each way 
 *    to make (amount - coin), append the current coin to create new combinations. This significantly 
 *    increases space complexity to O(number_of_combinations * average_combination_length).
 */
public class CoinChangeII {
    public static void main(String[] args) {
        CoinChangeII solver = new CoinChangeII();
        int[] coins = {1, 2, 5};
        int amount = 5;

        System.out.println("Number of combinations (2D DP): " + solver.change(amount, coins));
        System.out.println("Number of combinations (Recursive with Memoization): " + solver.changeRecursiveMemo(amount, coins));
    }

    /**
     * 2D Dynamic Programming (Bottom-Up)
     * 
     * Algorithm:
     * 1. Create a 2D DP table where dp[i][j] represents the number of ways to make 
     *    amount j using the first i coins
     * 2. Base case: dp[i][0] = 1 (one way to make 0: use no coins)
     * 3. For each coin i and amount j:
     *    - Option 1: Don't use coin i, inherit from dp[i-1][j]
     *    - Option 2: Use coin i (if possible), add dp[i][j-coins[i-1]]
     * 4. Return dp[n][amount]
     * 
     * Key Insights:
     * - This is an unbounded knapsack problem (unlimited use of each item)
     * - dp[i][j] = dp[i-1][j] + dp[i][j-coins[i-1]]
     *   The first term: don't use current coin
     *   The second term: use current coin (stay at same coin index since unbounded)
     * - We iterate coins in outer loop to avoid counting duplicate combinations
     * 
     * Why This Avoids Duplicates:
     * By processing coins in a fixed order, we ensure each combination is counted once.
     * For example, with coins [1,2] and amount 3:
     * - When processing coin 1: we get combinations starting with 1: [1,1,1]
     * - When processing coin 2: we get combinations with 2 (including earlier 1s): [1,2], [2,1] becomes just [1,2]
     * 
     * Time Complexity: O(n * amount) where n is number of coins
     * Space Complexity: O(n * amount) for the DP table
     * 
     * @param amount target amount to make change for
     * @param coins array of coin denominations
     * @return number of combinations that sum to amount
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

    /**
     * Helper method for recursive solution with memoization.
     * 
     * @param coins array of coin denominations
     * @param coinIndex current coin index being considered
     * @param remainingAmount amount left to make
     * @param memo memoization table to cache computed results
     * @return number of ways to make remainingAmount using coins from coinIndex onwards
     */
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
