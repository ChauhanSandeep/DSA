package arrays;

/**
 * LeetCode Problem: Trapping Rain Water
 * https://leetcode.com/problems/trapping-rain-water/
 *
 * Problem Statement:
 * Given n non-negative integers representing an elevation map where the width of each bar is 1,
 * compute how much water it is able to trap after raining.
 *
 * Intuition:
 * Water can be trapped at index `i` if there is a taller bar on both the left and the right.
 * The amount of water trapped is determined by the shorter of the two bars minus the current height.
 *
 * 🧪 Example:
 * Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * Output: 6
 *
 * Explanation:
 * The bars trap 1 unit of water at index 2, 1 at 4, 2 at 5, 1 at 6, and 1 at 10 — total = 6 units.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/trapping-rain-water/
 */
public class TrappingRainWater {
  public static void main(String[] args) {
    int[] heights = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
    System.out.println("Max water that can be trapped is " + trap(heights));
  }

  /**
   * Calculates the total water that can be trapped between bars of varying heights.
   *
   * 💡 Approach:
   * - Use two pointers (`left` and `right`) to scan from both ends.
   * - Maintain the `leftMax` and `rightMax` heights seen so far.
   * - At each step, calculate how much water can be trapped at the current bar
   *   based on the min of `leftMax` and `rightMax`.
   * - Move the pointer pointing to the shorter bar inward.
   *
   * Time Complexity: O(n) – Single pass through the array
   * Space Complexity: O(1) – Constant extra space
   *
   * @param height Array representing elevation map
   * @return Total amount of trapped water
   */
  public static int trap(int[] height) {
    // Edge case: no bars or only one bar means no water can be trapped
      if (height == null || height.length == 0) {
          return 0;
      }

    int left = 0;                    // Pointer starting from left
    int right = height.length - 1;  // Pointer starting from right
    int leftMax = 0;                // Max height to the left of current index
    int rightMax = 0;               // Max height to the right of current index
    int result = 0;                 // Accumulator for total trapped water

    // Continue until the two pointers meet
    while (left < right) {
      // Always process the smaller side because it determines the water level
      if (height[left] < height[right]) {
        if (height[left] >= leftMax) {
          // current left height is greater than or equal to leftMax, so update leftMax
          // No water can be trapped at this index
          leftMax = height[left];
        } else {
          // Water can be trapped: leftMax - current height
          result += leftMax - height[left];
        }
        left++; // Move left pointer inward
      } else {
        // Process the right side in symmetric way
        if (height[right] >= rightMax) {
          rightMax = height[right];
        } else {
          result += rightMax - height[right];
        }
        right--; // Move right pointer inward
      }
    }

    return result;
  }
}