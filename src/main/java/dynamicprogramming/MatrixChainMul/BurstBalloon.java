package dynamicprogramming.MatrixChainMul;

import java.util.Arrays;


/**
 * LeetCode Problem: Burst Balloons
 * Link: https://leetcode.com/problems/burst-balloons/
 *
 * Problem Statement:
 * - You are given `n` balloons, indexed from `0` to `n-1`, each having a value in an array `nums`.
 * - When you burst the `i-th` balloon, you gain `nums[i-1] * nums[i] * nums[i+1]` coins.
 * - If `i-1` or `i+1` is out of bounds, assume a balloon with value `1` exists there.
 * - Find the maximum coins that can be collected by bursting all balloons optimally.
 *
 * Example:
 * Input: nums = [3, 1, 5, 8]
 * Output: 167
 * Explanation:
 * - Burst balloons in the order: 1, 2, 0, 3
 * - nums = [3,1,5,8] --> [3,5,8] --> [3,8] --> [8] --> []
 * - coins =  3*1*5    +   3*5*8   +  1*3*8  + 1*8*1 = 167
 *
 * Follow-up Questions:
 * 1. How would the solution change if we wanted to minimize the coins instead?
 *      - We could modify the DP to take min instead of max, but the problem is about max;
 *      minimization might not make sense in this context as coins are positive.
 * 2. What if balloons could be burst in groups?
 *      - That would require a different DP state, perhaps segmenting into groups, but here it's individual bursts.
 * 3. How to handle if some balloons give negative coins?
 *      - DP would still work, but we'd need to consider if skipping is allowed; here all must be burst.
 * Relevant follow-up problem: https://leetcode.com/problems/matrix-chain-multiplication/ (similar interval DP optimization).
 */
public class BurstBalloon {

    public static void main(String[] args) {
        int[] nums = {3, 1, 5, 8};
        BurstBalloon solver = new BurstBalloon();
        System.out.println("Maximum coins: " + solver.maxCoinsIterative(nums));
    }

    /**
     * Solves the Burst Balloons problem using recursion with memoization.
     *
     * Intuition:
     * - Think of the last balloon to burst between a given range (left, right).
     * - Bursting balloon `i` at last will maximize coins for subproblem (left, right).
     * - So, try every balloon as the last one to burst in the current range.
     * - Use memoization to store results of (left, right) to avoid recomputation.
     *
     * Steps:
     * 1. Add virtual balloons with value 1 at the start and end of nums.
     * 2. Recursively calculate the maximum coins for each subarray.
     * 3. Memoize results to optimize overlapping subproblems.
     *
     * Time Complexity: O(n^3)
     * - For each (left, right) pair (~n^2 pairs), we try all balloons in between (~n choices).
     *
     * Space Complexity: O(n^2)
     * - For memoization table + O(n) stack space due to recursion depth.
     *
     * @param nums the array of balloon values
     * @return maximum coins collected
     */
    public int maxCoinsRecursiveApproach(int[] nums) {
        int length = nums.length;
        
        // Create arr with virtual balloons: [1, nums[0], nums[1], ..., nums[n-1], 1]
        int[] arr = new int[length + 2];
        arr[0] = 1;                    // Virtual left balloon
        arr[length + 1] = 1;           // Virtual right balloon
        for (int i = 0; i < length; i++) {
            arr[i + 1] = nums[i];      // Original balloons shifted right by 1
        }
        
        // Memo for intervals in extended array (1 to length)
        int[][] memo = new int[length + 2][length + 2];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        
        // Solve for interval (0, length+1) - burst all original balloons
        return burstRecHelper(arr, 0, length + 1, memo);
    }

    /**
     * Recursive helper: max coins bursting balloons in OPEN interval (left, right)
     * arr[left] and arr[right] are NOT burst - they become neighbors!
     */
    private int burstRecHelper(int[] arr, int left, int right, int[][] memo) {
        // Base case: no balloons between left and right
        if (right - left <= 1) {
            return 0;
        }
        
        // Return memoized result
        if (memo[left][right] != -1) {
            return memo[left][right];
        }
        
        int maxCoins = 0;
        
        // Try each balloon burstIndex in (left, right) as LAST to burst
        for (int burstIndex = left + 1; burstIndex < right; burstIndex++) {

            // + coins from left subproblem (left, burstIndex)
            // + coins from right subproblem (burstIndex, right)
            int leftCoins = burstRecHelper(arr, left, burstIndex, memo);
            int rightCoins = burstRecHelper(arr, burstIndex, right, memo);

            // When burstIndex is burst LAST:
            // - All other balloons from left+1 to right-1 are GONE
            // - Neighbors are arr[left] and arr[right] (virtual balloons stay!)
            int coins = arr[left] * arr[burstIndex] * arr[right];
            
            maxCoins = Math.max(maxCoins, coins + leftCoins + rightCoins);
        }
        
        memo[left][right] = maxCoins;
        return maxCoins;
    }

    /**
     * Iterative Solution
     * 
     * Algorithm Breakdown:
     * 1. Create arr[] with virtual balloons: [1, nums[0], nums[1], ..., nums[n-1], 1]
     * 2. dp[left][right] = max coins from bursting balloons in open interval (left, right)
     * 3. Iterate by increasing interval length (len = 2 to n+1)
     * 4. For each interval [left, right], try bursting each balloon burstIndex LAST
     * 5. When burstIndex is burst last, its neighbors are arr[left] and arr[right]
     * 6. Coins = arr[left] * arr[burstIndex] * arr[right] + dp[left][burstIndex] + dp[burstIndex][right]
     * 
     * Time Complexity: O(n^3) because of three nested loops
     * Space Complexity: O(n^2) for the DP table
     */
    public int maxCoinsIterative(int[] nums) {
        int length = nums.length;

        // Create new array with virtual balloons at both ends
        // Why? Eliminates boundary checks: arr[-1]=1 and arr[n]=1
        int[] arr = new int[length + 2];
        arr[0] = 1;
        arr[length + 1] = 1;
        for (int i = 0; i < length; i++) {
            arr[i + 1] = nums[i];
        }

        // dp[i][j] = max coins from bursting balloons from (i+1) to (j-1)
        int[][] dp = new int[length + 2][length + 2];
        
        // Build DP table by increasing interval length
        // gap represents the distance between left and right boundaries
        for (int gap = 2; gap < length + 2; gap++) {
            // Try all possible intervals of this length
            for (int leftIndex = 0; leftIndex + gap < length + 2; leftIndex++) {
                int rightIndex = leftIndex + gap;
                
                // Try bursting each balloon in (left, right) as the LAST one
                for (int burstIndex = leftIndex + 1; burstIndex < rightIndex; burstIndex++) {
                    int leftCoins = dp[leftIndex][burstIndex]; // burst balloons in [left+1 to burstIndex-1]
                    int rightCoins = dp[burstIndex][rightIndex]; // burst balloons in [burstIndex+1 to right-1]
                    int coins = leftCoins + rightCoins;

                    // When burstIndex is burst LAST in interval (left, right):
                    // - Its neighbors remaining are arr[left] and arr[right]
                    // - Subproblems [left+1 to burstIndex-1] and [burstIndex+1 to right-1] are already solved
                    coins = arr[leftIndex] * arr[burstIndex] * arr[rightIndex];
                    
                    dp[leftIndex][rightIndex] = Math.max(dp[leftIndex][rightIndex], coins);
                }
            }
        }

        // Answer: max coins from bursting all balloons in interval (0, length+1)
        return dp[0][length + 1];
    }

    /**
     * Helper function to handle out-of-bounds cases (treats missing elements as `1`).
     */
    private int getValue(int[] nums, int index) {
        if (index < 0 || index >= nums.length) return 1;
        return nums[index];
    }
}
