package dynamicprogramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Given an integer array nums, return the length of the longest strictly increasing subsequence.
 * A subsequence is a sequence that can be derived from an array by deleting some or no elements 
 * without changing the order of the remaining elements.
 *
 * Example 1:
 * Input: nums = [10,9,2,5,3,7,101,18]
 * Output: 4
 * Explanation:
 * The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
 * Other valid subsequences: [2,5,7,101], [2,3,7,18], etc.
 *
 *
 * LeetCode link: https://leetcode.com/problems/longest-increasing-subsequence/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - Can you return the actual longest increasing subsequence, not just its length?
 *    → Yes, modify DP to store parent pointers and backtrack from the maximum position.
 *  - How would you handle the longest non-decreasing subsequence (allowing equals)?
 *    → Change comparison from nums[j] < nums[i] to nums[j] <= nums[i].
 *  - What if you need to find all possible LIS of maximum length?
 *    → Use backtracking with DP to enumerate all paths leading to maximum length.
 *  - Can you solve this for 2D LIS (longest increasing path in a matrix)?
 *    → Use DFS with memoization from each cell, checking all four directions.
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 354 (Russian Doll Envelopes): https://leetcode.com/problems/russian-doll-envelopes/
 *  - LeetCode 673 (Number of Longest Increasing Subsequence): https://leetcode.com/problems/number-of-longest-increasing-subsequence/
 *  - LeetCode 1048 (Longest String Chain): https://leetcode.com/problems/longest-string-chain/
 *  - LeetCode 646 (Maximum Length of Pair Chain): https://leetcode.com/problems/maximum-length-of-pair-chain/
 */
public class LongestIncreasingSubsequence {
    public static void main(String[] args) {
        int[] arr = {10, 22, 9, 33, 21, 50, 41, 60, 60};

        System.out.println("LIS Length (O(N²) DP): " + findLisUsingDp(arr));
        System.out.println("LIS Length (O(N log N) Binary Search): " + new LongestIncreasingSubsequence().lengthOfLIS(arr));
    }

    /**
     * Approach 1: Dynamic Programming
     * - For each index `i`, we find the length of the LIS that ends at `i`.
     * - To do this, we look back at all previous indices `j < i` and:
     *   → If `nums[j] < nums[i]`, then `nums[i]` can be appended to the LIS ending at `j`.
     *   → We take the **maximum LIS length among all such `j`** and add 1.
     *
     * Why it works:
     * - We break down the problem into optimal subproblems: LIS ending at each index.
     * - Each subproblem depends only on prior computed results (typical DP structure).
     * - We ensure we explore all increasing subsequences that could end at position `i`.
     *
     * Time Complexity: O(N²)
     * Space Complexity: O(N)
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
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            // Place num at the found position
            tails[left] = num;
            
            // If num extends the longest subsequence, increment size
            if (left == size) {
                size++;
            }
        }
        
        return size;
    }
}
