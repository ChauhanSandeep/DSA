package dynamicprogramming.statemachine;

/**
 * Problem: Delete and Earn
 *
 * Given an integer array nums, maximize the number of points you can earn by performing the following operation:
 * - Choose any number nums[i], earn nums[i] points, and delete all occurrences of nums[i] - 1 and nums[i] + 1.
 * - Repeat the process to maximize the total points.
 *
 * Example 1:
 * Input: [3, 4, 2]
 * Output: 6
 * Explanation: Take 4 → earn 4 → delete 3; take 2 → earn 2 → total = 6
 *
 * Example 2:
 * Input: [2, 2, 3, 3, 3, 4]
 * Output: 9
 * Explanation: Take 3 → earn 3*3 = 9 → delete 2 and 4
 *
 * LeetCode Link: https://leetcode.com/problems/delete-and-earn/
 *
 * Follow-up Questions (FAANG-style):
 * 1. Can this be solved using memoization instead of tabulation?
 *    - Yes. Use top-down recursion with memoization over the "points" index.
 * 2. How would the solution change if you could also delete `nums[i] ± 2`?
 *    - You would modify the DP recurrence to skip two indices ahead.
 */
public class DeleteAndEarn {

  public static void main(String[] args) {
    int[] example1 = {3, 4, 2};
    System.out.println("Max Points: " + getMaxPoints(example1)); // Expected: 6

    int[] example2 = {2, 2, 3, 3, 3, 4};
    System.out.println("Max Points: " + getMaxPoints(example2)); // Expected: 9
  }

  /**
   * Solves the Delete and Earn problem using dynamic programming (bottom-up tabulation).
   *
   * Steps:
   * 1. Create a frequency-weighted points array where points[i] = i * count(i).
   * 2. Reduce to the "House Robber" pattern — where adjacent values (i ± 1) cannot be selected together.
   * 3. Mutate the points[] array to store max earnings up to each i.
   *
   * Time Complexity: O(N), where N = max(nums), as we scan through 0 to maxVal (max 10^4).
   * Space Complexity: O(N), due to the points[] array.
   *
   * @param nums Input array of integers
   * @return Maximum points that can be earned
   */
  public static int getMaxPoints(int[] nums) {
    if (nums == null || nums.length == 0) {
      return 0;
    }

    int maxVal = 0;
    int[] points = new int[10001]; // problem constraint: nums[i] <= 10^4

    // Step 1: Preprocess - convert array to frequency-weighted points
    for (int num : nums) {
      points[num] += num;
      maxVal = Math.max(maxVal, num);
    }

    // Step 2: Apply House Robber DP on the points array itself
    points[1] = Math.max(points[0], points[1]);

    for (int i = 2; i <= maxVal; i++) {
      points[i] = Math.max(points[i - 1], points[i - 2] + points[i]);
    }

    return points[maxVal];
  }
}