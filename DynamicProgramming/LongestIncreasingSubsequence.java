package DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode #300: Longest Increasing Subsequence
 * https://leetcode.com/problems/longest-increasing-subsequence/
 *
 * Problem:
 * Given an integer array `nums`, return the length of the longest strictly increasing subsequence.
 *
 * Example:
 * Input: nums = [10, 9, 2, 5, 3, 7, 101, 18]
 * Output: 4
 * Explanation: The longest increasing subsequence is [2, 3, 7, 101], so the length is 4.
 *
 * Approaches:
 * 1. DP (O(N²)) — Bottom-up LIS using previous comparisons
 * 2. Greedy + Binary Search (O(N log N)) — Optimal with clever sub structure
 *
 * Time Complexity:
 * - O(N²) for basic DP
 * - O(N log N) for Binary Search approach
 */
public class LongestIncreasingSubsequence {
    public static void main(String[] args) {
        int[] arr = {10, 22, 9, 33, 21, 50, 41, 60, 60};

        System.out.println("LIS Length (O(N²) DP): " + findLisUsingDp(arr));
        System.out.println("LIS Length (O(N log N) Binary Search): " + findLisUsingBinarySearch(arr));
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
     * Approach 2: Binary Search + Greedy (O(N log N))
     * - We track the smallest possible tail for all increasing subsequences of different lengths.
     * - This is done via a greedy approach using a list `lisTracker`.
     *   → `lisTracker[i]` will represent the **smallest possible last element** of an LIS of length `i + 1`.
     *
     * Algorithm:
     * 1. Initialize an empty list `lisTracker`.
     * 2. For each number `num` in the input:
     *    - If `num` > last element in `lisTracker`, we can extend the existing LIS. So, add `num`.
     *    - Else, use binary search to find the **first element ≥ num** in `lisTracker` and replace it.
     *      → This ensures `lisTracker` maintains the smallest possible values, increasing chances of future extensions.
     * 3. The size of `lisTracker` at the end is the length of the LIS.
     *
     * Why this works:
     * - By greedily keeping the smallest tails, we leave room to build longer sequences.
     * - The list does **not** represent an actual LIS, but its **length is guaranteed** to be correct.
     * - Binary search gives us `O(log N)` efficiency per update, leading to `O(N log N)` overall.
     *
     * Time Complexity: O(N log N)
     * - For each of the N elements, binary search takes log N time
     *
     * Space Complexity: O(N)
     * - In the worst case, `lisTracker` stores all elements
     */
    public static int findLisUsingBinarySearch(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        /**
         * lisTracker represents list of list in a single dimension.
         * Each element in lisTracker represents the last element of a list of increasing subsequence of length i + 1.
         * For example, if lisTracker = [2, 3, 7], it means:
         * - There is an increasing subsequence of length 1 ending with 2
         * * - There is an increasing subsequence of length 2 ending with 3
         * * - There is an increasing subsequence of length 3 ending with 7
         * But it does not mean that the actual subsequences is [2, 3, 7].
         */
        List<Integer> lisTracker = new ArrayList<>();
        lisTracker.add(nums[0]); // First element is always included

        for (int num : nums) {
            if (num > lisTracker.get(lisTracker.size() - 1)) {
                // Case 1: num is greater than last element —> extend LIS
                lisTracker.add(num);
            } else {
                // Case 2: num could replace an element to keep lisTracker optimal
                int idx = lowerBound(lisTracker, num);
                lisTracker.set(idx, num);
            }
        }

        return lisTracker.size(); // The length of lisTracker is the LIS length
    }

    /**
     * find the first index where value is greater than `num`.
     */
    private static int lowerBound(List<Integer> lisTracker, int num) {
        int left = 0, right = lisTracker.size() - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (lisTracker.get(mid) >= num) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }
}
