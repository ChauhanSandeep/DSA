package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Trapping Rain Water
 *
 * Given bar heights with width 1, compute how much water is trapped after rain.
 * Water above an index is limited by the shorter maximum wall seen on its left
 * and right.
 *
 * Leetcode: https://leetcode.com/problems/trapping-rain-water/ (Hard)
 * Rating:   acceptance 67.8% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Running boundary maxima
 *
 * Example:
 *   Input:  height = [0,1,0,2,1,0,1,3,2,1,2,1]
 *   Output: 6
 *   Why:    the bounded valleys across the elevation map hold 6 total units.
 *
 * Follow-ups:
 *   1. Return water trapped at each index?
 *      Store per-index contributions while processing or use prefix/suffix maxima arrays.
 *   2. Handle bars with variable widths?
 *      Multiply each trapped height by that bar's width.
 *   3. Extend to a 2D height map?
 *      Use a min-heap from the border cells, as in Trapping Rain Water II.
 *
 * Related: Container With Most Water (11), Trapping Rain Water II (407).
 */
public class TrappingRainWater {
public static void main(String[] args) {
  int[][] inputs = { {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}, {4, 2, 0, 3, 2, 5} };
  int[] expected = { 6, 9 };

  for (int i = 0; i < inputs.length; i++) {
    int got = trap(inputs[i]);
    System.out.printf("height=%s -> %d  expected=%d%n",
        Arrays.toString(inputs[i]), got, expected[i]);
  }
}

  /**
 * Intuition: the lower side determines the water level because the opposite
 * side already has a boundary at least that high. Process the lower side,
 * update its running max, or add the difference between that max and the
 * current bar.
 *
 * Algorithm:
 *   1. Return 0 for null or empty height.
 *   2. Start left and right at the ends with leftMax and rightMax at 0.
 *   3. Process the side with the smaller current height.
 *   4. Update that side's max or add trapped water, then move that pointer inward.
 *
 * Time:  O(n) - each pointer moves inward across the array once.
 * Space: O(1) - only boundary maxima, pointers, and total water are kept.
 *
 * @param height elevation map bar heights
 * @return total trapped rain water
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