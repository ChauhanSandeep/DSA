package dynamicprogramming.sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Longest Increasing Subsequence
 *
 * Return the length of the longest strictly increasing subsequence. A subsequence preserves order but may delete elements.
 *
 * Leetcode: https://leetcode.com/problems/longest-increasing-subsequence/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | Patience sorting | Binary search
 *
 * Example:
 *   Input:  nums = [10, 9, 2, 5, 3, 7, 101, 18]
 *   Output: 4
 *   Why:    [2, 3, 7, 101] is one increasing subsequence of length 4.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Number of LIS (673), Russian Doll Envelopes (354).
 */
public class LongestIncreasingSubsequence {
        public static void main(String[] args) {
        LongestIncreasingSubsequence solver = new LongestIncreasingSubsequence();
        int[][] inputs = { {10, 9, 2, 5, 3, 7, 101, 18}, {}, {7, 7, 7} };
        int[] expected = {4, 0, 1};
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.lengthOfLIS(inputs[i]);
            System.out.printf("nums=%s -> %d  expected=%d%n", Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: dp[current] is the LIS length that must end at current. Any previous smaller value can be immediately before current, so dp[previous] + 1 is a candidate and the best candidate becomes dp[current].
     *
     * Algorithm:
     *   1. Return 0 for null or empty input.
     *   2. Fill dp with 1.
     *   3. For each current index, scan all previous indices.
     *   4. Extend from previous when nums[previous] < nums[current].
     *   5. Track the maximum dp value.
     *
     * Time:  O(n^2) - each pair is checked once.
     * Space: O(n) - one DP value per index.
     *
     * @param nums input array
     * @return LIS length
     */
public static int findLisUsingDp(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int length = nums.length;
        int[] dp = new int[length]; // dp[i] will hold the length of LIS ending at index i
        Arrays.fill(dp, 1);  // Each element is an LIS of at least length 1
        int maxLength = 1;

        for (int current = 1; current < length; current++) {
            for (int previous = 0; previous < current; previous++) {
                if (nums[previous] < nums[current]) {
                    // If nums[current] can extend the LIS ending at nums[previous], LIS length at current can be updated
                    dp[current] = Math.max(dp[current], dp[previous] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[current]);
        }
        return maxLength;
    }

    /**
     * Optimized method: Finds LIS length using Binary Search with Patience Sorting (Optimal).
     * Step-by-step:
     *  1. Maintain an array 'tails' where tails[i] = smallest tail element of all 
     *     increasing subsequences of length i+1.
     *  2. For each element in nums:
     *     a. If element is larger than all tails: append to tails (extend longest subsequence)
     *     b. Otherwise: find the smallest tail >= element using binary search and replace it
     *  3. The length of tails array at the end is the LIS length.
     *
     * Key Insight:
     * By keeping the smallest possible tail for each length, we maximize chances of 
     * extending subsequences. Binary search finds the correct position to maintain this property.
     * This is related to Patience Sorting algorithm where we minimize number of piles.
     *
     * Algorithm: Binary Search with Greedy Approach (Patience Sorting variant).
     * Time Complexity: O(n log n), where n is array length. Binary search O(log n) for each element.
     * Space Complexity: O(n) for tails array in worst case (all elements increasing).
     */
    public int lengthOfLIS(int[] nums) {
        int length = nums.length;
        if (length == 0) return 0;
        
        // tails[i] = smallest tail of all increasing subsequences of length i+1
        int[] tails = new int[length];
        int size = 0; // Current length of tails array
        
        for (int num : nums) {
            // Find the first index where tails[mid] >= num
            int left = 0;
            int right = size;
            
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < num) {
                    // Move right if current tail is less than num
                    left = mid + 1;
                } else {
                    // Move left if current tail is greater than or equal to num
                    right = mid;
                }
            }
            
            // Place num at the found position
            tails[left] = num; // tails[left] is the smallest tail for subsequence of length left+1
            
            // If num extends the longest subsequence, increment size
            if (left == size) {
                size++;
            }
        }
        
        return size;
    }
}
