package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;


/**
 * Problem: Burst Balloons
 *
 * Given a row of balloons, bursting one balloon earns the product of its value
 * and the values of its current left and right neighbors. Missing neighbors act
 * like value 1. Choose the burst order that earns the most coins.
 *
 * Leetcode: https://leetcode.com/problems/burst-balloons/
 * Rating:   acceptance 63.9% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Interval DP | Choose the last action
 *
 * Example:
 *   Input:  nums = [3,1,5,8]
 *   Output: 167
 *   Why:    bursting 1, then 5, then 3, then 8 earns 15 + 120 + 24 + 8,
 *           and choosing the last balloon in each interval lets DP find that order.
 *
 * Follow-ups:
 *   1. What if some balloon values are negative?
 *      The same interval DP still tries every last balloon, but ranges may need
 *      careful initialization because zero is no longer a safe lower bound.
 *   2. Can we reconstruct one optimal burst order?
 *      Store the best last-burst index for every interval and recursively print
 *      left interval, right interval, then that index.
 *   3. What if bursting a balloon also depended on its original neighbors?
 *      The interval split would no longer isolate subproblems; the state must
 *      carry whatever original-neighbor information affects future rewards.
 *
 * Related: Matrix Chain Multiplication, Minimum Score Triangulation of Polygon (1039).
 */
public class BurstBalloon {

        /**
     * Intuition: choosing the first balloon is hard because neighbors change, but
     * choosing the last balloon in an interval is stable. If burstIndex is last,
     * its only remaining neighbors are the fixed open-interval boundaries.
     *
     * Algorithm:
     *   1. Add virtual boundary balloons with value 1.
     *   2. Recursively solve each open interval between two surviving boundaries.
     *   3. Try every balloon in the interval as the last one to burst.
     *   4. Memoize the best coins for each left/right boundary pair.
     *
     * Time:  O(n^3) - O(n^2) intervals each try O(n) last balloons.
     * Space: O(n^2) - memo table plus recursion depth.
     *
     * @param nums balloon values
     * @return maximum coins obtainable
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

        /** Returns max coins for bursting all balloons inside the open interval. */
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
     * Intuition: the same last-burst recurrence can be filled bottom-up by interval
     * width. Shorter open intervals are solved before larger intervals that depend
     * on them.
     *
     * Algorithm:
     *   1. Add virtual boundary balloons with value 1.
     *   2. Iterate open-interval gaps from small to large.
     *   3. For each interval, try each burstIndex as the last burst balloon.
     *   4. Store the best value in dp[leftIndex][rightIndex].
     *
     * Time:  O(n^3) - three nested interval and split loops.
     * Space: O(n^2) - interval DP table.
     *
     * @param nums balloon values
     * @return maximum coins obtainable by the iterative recurrence
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

        /** Returns 1 for virtual out-of-range balloons, otherwise nums[index]. */
    private int getValue(int[] nums, int index) {
        if (index < 0 || index >= nums.length) return 1;
        return nums[index];
    }


    public static void main(String[] args) {
        BurstBalloon solver = new BurstBalloon();
        int[][] inputs = { {}, {3, 1, 5, 8}, {1, 5} };
        int[] expected = {0, 167, 10};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.maxCoinsRecursiveApproach(inputs[i]);
            System.out.printf("nums=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

}
