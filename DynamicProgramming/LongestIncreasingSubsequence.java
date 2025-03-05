package DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Longest Increasing Subsequence (LIS)
 * 
 * Given an integer array, find the length of the longest strictly increasing subsequence.
 * 
 * Approaches:
 * 1. **Dynamic Programming (O(N²))**:
 *    - Maintain a DP array where `dp[i]` stores the LIS ending at index `i`.
 *    - Iterate over previous elements to find valid increasing sequences.
 *    - Update `dp[i] = max(dp[i], dp[j] + 1)` if `nums[j] < nums[i]`.
 *    - **Time Complexity: O(N²), Space Complexity: O(N).**
 * 
 * 2. **Binary Search + Greedy (O(N log N))**:
 *    - Maintain a list (`sub`) where we store the smallest possible end element for LIS of different lengths.
 *    - If `num > last element of sub`, append it (extend LIS).
 *    - Otherwise, replace the first element in `sub` that is `>= num` (using binary search).
 *    - **Time Complexity: O(N log N), Space Complexity: O(N).**
 */
public class LongestIncreasingSubsequence {
    public static void main(String[] args) {
        int[] arr = {10, 22, 9, 33, 21, 50, 41, 60, 60};

        System.out.println("LIS Length (O(N²) DP): " + findLIS_DP(arr));
        System.out.println("LIS Length (O(N log N) Binary Search): " + findLIS_BinarySearch(arr));
    }

    /**
     * Approach 1: Dynamic Programming (O(N²))
     * Uses a DP array to store the length of LIS ending at each index.
     */
    public static int findLIS_DP(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);  // Initialize LIS length as 1 for all elements
        int maxLength = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                    maxLength = Math.max(maxLength, dp[i]);
                }
            }
        }

        return maxLength;
    }

    /**
     * Approach 2: Binary Search + Greedy (O(N log N))
     * Uses a list (`sub`) to store potential LIS end elements.
     */
    public static int findLIS_BinarySearch(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        List<Integer> sub = new ArrayList<>();
        sub.add(nums[0]); // First element is always included

        for (int num : nums) {
            if (num > sub.get(sub.size() - 1)) {
                // Extend the LIS by adding the new element
                sub.add(num);
            } else {
                // Replace the first element in sub that is >= num (Binary Search)
                int idx = lowerBound(sub, num);
                sub.set(idx, num);
            }
        }

        return sub.size(); // The length of sub is the LIS length
    }

    /**
     * Binary search helper function to find the first index where `num` should be placed.
     */
    private static int lowerBound(List<Integer> sub, int num) {
        int left = 0, right = sub.size() - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sub.get(mid) >= num) right = mid;
            else left = mid + 1;
        }

        return left;
    }
}
