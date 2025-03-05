package DynamicProgramming;

/**
 * Problem: Delete and Earn
 *
 * Given an integer array nums, maximize the number of points you can earn by applying the following operation:
 * - Pick any `nums[i]`, earn `nums[i]` points, and remove all occurrences of `nums[i] - 1` and `nums[i] + 1`.
 * - Continue this operation to maximize points.
 *
 * Intuition:
 * - The problem is similar to the "House Robber" problem.
 * - Instead of houses, we have numbers.
 * - We cannot take adjacent numbers (`nums[i] - 1` or `nums[i] + 1`).
 * - We convert the problem into a "take or skip" dynamic programming approach.
 *
 * Approach:
 * 1. **Frequency Array:** Count occurrences of each number (since order doesn’t matter).
 * 2. **Transform into House Robber:** Convert `nums` into `points[]` where `points[i] = i * count[i]`.
 * 3. **DP Solution:** Use a bottom-up approach similar to house robbery.
 *
 * Time Complexity: **O(N)** (Iterate over `nums` and then process `dp[]` once)
 * Space Complexity: **O(N)** (Used `count[]` and `dp[]` arrays)
 *
 * LeetCode Problem Link:
 * https://leetcode.com/problems/delete-and-earn/
 */
public class DeleteAndEarn {
    
    public static void main(String[] args) {
        int[] arr = {3, 4, 2};
        System.out.println("Max Points: " + deleteAndEarn(arr)); // Output: 6

        int[] arr2 = {2, 2, 3, 3, 3, 4};
        System.out.println("Max Points: " + deleteAndEarn(arr2)); // Output: 9
    }

    /**
     * Dynamic Programming approach (Optimized House Robber problem)
     * @param nums Input array
     * @return Maximum points that can be earned
     */
    public static int deleteAndEarn(int[] nums) {
        if (nums == null || nums.length == 0) return 0; // Edge case: empty input

        // Step 1: Count occurrences of each number
        int[] points = new int[10001];
        for (int num : nums) {
            points[num] += num; // Convert to earnings
        }

        // Step 2: Apply House Robber DP approach
        int prevTwo = 0; // dp[i-2]
        int prevOne = points[1]; // dp[i-1]

        for (int i = 2; i <= 10000; i++) {
            int current = Math.max(points[i] + prevTwo, prevOne); // Either take or skip
            prevTwo = prevOne; // Move pointers forward
            prevOne = current;
        }

        return prevOne; // Maximum earnings
    }
}
