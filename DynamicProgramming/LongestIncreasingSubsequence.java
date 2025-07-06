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
 * Approaches:
 * 1. DP (O(N²)) — Bottom-up LIS using previous comparisons
 * 2. Greedy + Binary Search (O(N log N)) — Optimal with clever sub structure
 *
 * Example:
 * Input: [10, 22, 9, 33, 21, 50, 41, 60] → Output: 5 (LIS: [10, 22, 33, 50, 60])
 *
 * Time Complexity:
 * - O(N²) for basic DP
 * - O(N log N) for Binary Search approach
 */
public class LongestIncreasingSubsequence {
    public static void main(String[] args) {
        int[] arr = {10, 22, 9, 33, 21, 50, 41, 60, 60};

        System.out.println("LIS Length (O(N²) DP): " + findLIS_DP(arr));
        System.out.println("LIS Length (O(N log N) Binary Search): " + findLIS_BinarySearch(arr));
    }

    /**
     * Approach 1: Dynamic Programming
     * For each element at index `i`, look back at all previous `j < i`,
     * and update `dp[i]` if `nums[i] > nums[j]`.
     * This builds up the LIS length at each index.
     *
     *  Time Complexity: O(N²),
     *  Space Complexity: O(N).
     */
    public static int findLIS_DP(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);  // Each element is an LIS of at least length 1
        int maxLength = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    // If nums[i] can extend the LIS ending at nums[j], LIS length at i can be updated
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }

    /**
     * Approach 2: Binary Search + Greedy (O(N log N))
     *  - Maintain a list (`sub`) where we store the smallest possible end element for LIS of different lengths.
     *  - If `num > last element of sub`, append it (extend LIS).
     *  - Otherwise, replace the first element in `sub` that is `>= num` (using binary search).
     *
     *   Time Complexity: O(N log N)
     *   Space Complexity: O(N)
     */
    public static int findLIS_BinarySearch(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        // lisTracker[i] holds the minimum possible last element of an increasing subsequence of length i + 1.
        List<Integer> lisTracker = new ArrayList<>();
        lisTracker.add(nums[0]); // First element is always included

        for (int num : nums) {
            if (num > lisTracker.get(lisTracker.size() - 1)) {
                // Extend the LIS by adding the new element
                lisTracker.add(num);
            } else {
                // Replace the first element in lisTracker that is >= num (Binary Search)
                int idx = lowerBound(lisTracker, num);
                lisTracker.set(idx, num);
            }
        }

        return lisTracker.size(); // The length of lisTracker is the LIS length
    }

    /**
     * Binary search helper function to find the first index where `num` should be placed.
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
