package arrays.twopointers;

import java.util.*;

/**
 * Problem: Trapping Rain Water (Alternative Implementations)
 *
 * Given bar heights with width 1, compute the total water trapped after rain.
 * This file keeps both the two-pointer and monotonic-stack one-dimensional
 * implementations.
 *
 * Leetcode: https://leetcode.com/problems/trapping-rain-water/ (Hard)
 * Rating:   acceptance 67.8% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Monotonic stack boundaries
 *
 * Example:
 *   Input:  height = [0,1,0,2,1,0,1,3,2,1,2,1]
 *   Output: 6
 *   Why:    each trapped unit is bounded by a taller bar on both sides.
 *
 * Follow-ups:
 *   1. Which approach is easier to extend to per-index water?
 *      The two-pointer method can store each contribution as it processes a side.
 *   2. Which approach exposes basin boundaries?
 *      The stack method naturally identifies left and right boundaries when popping.
 *   3. How does the 2D version change?
 *      Use a min-heap over border cells instead of one-dimensional pointers.
 *
 * Related: Trapping Rain Water II (407), Container With Most Water (11).
 */
public class TrappingRainWaterII {

public static void main(String[] args) {
    TrappingRainWaterII solver = new TrappingRainWaterII();
    int[][] inputs = { {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}, {2, 0, 2} };
    int[] expected = { 6, 2 };

    for (int i = 0; i < inputs.length; i++) {
        int got = solver.trap(inputs[i]);
        System.out.printf("height=%s -> %d  expected=%d%n",
            Arrays.toString(inputs[i]), got, expected[i]);
    }
}
    /**
 * Intuition: water at the lower side can be decided immediately because the
 * opposite side is already at least as tall. Keep the best boundary height
 * seen from each side, add trapped water when the current bar is below that
 * boundary, and move inward.
 *
 * Algorithm:
 *   1. Return 0 when fewer than three bars are present.
 *   2. Start left and right at the ends with leftMax and rightMax at 0.
 *   3. Process the side with the smaller current height.
 *   4. Update that side's max or add trapped water, then move the pointer inward.
 *
 * Time:  O(n) - each bar is processed at most once.
 * Space: O(1) - only pointers, maxima, and the total are stored.
 *
 * @param height elevation map bar heights
 * @return total trapped rain water
 */
    public int trap(int[] height) {
        if (height == null || height.length < 3) {
            return 0;  // Need at least 3 bars to trap water
        }

        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int waterTrapped = 0;

        while (left < right) {
            // Process the side with smaller height (limiting factor)
            if (height[left] < height[right]) {
                // Left side is shorter, process left
                if (height[left] >= leftMax) {
                    leftMax = height[left];  // Update left max
                } else {
                    waterTrapped += leftMax - height[left];  // Trap water
                }
                left++;
            } else {
                // Right side is shorter or equal, process right
                if (height[right] >= rightMax) {
                    rightMax = height[right];  // Update right max
                } else {
                    waterTrapped += rightMax - height[right];  // Trap water
                }
                right--;
            }
        }

        return waterTrapped;
    }

    /**
     * Monotonic stack-based approach
     * Uses monotonic stack to track potential water trapping positions.
     *
     * Algorithm:
     * 1. Maintain stack of indices with decreasing heights
     * 2. When current height > stack top, we found a boundary
     * 3. Pop from stack and calculate water between boundaries
     * 4. Water width = current index - stack top - 1
     * 5. Water height = min of boundaries - popped height
     *
     * Time Complexity: O(N) where N is array length. Each element pushed/popped once.
     *
     * Space Complexity: O(N) for stack in worst case (strictly decreasing heights).
     *
     * @param height array of non-negative integers representing elevation map
     * @return total units of water that can be trapped
     */
    public int trapWithStack(int[] height) {
        if (height == null || height.length < 3) {
            return 0;
        }

        Stack<Integer> stack = new Stack<>();
        int waterTrapped = 0;

        for (int current = 0; current < height.length; current++) {
            // Found a right boundary taller than stack top
            while (!stack.isEmpty() && height[current] > height[stack.peek()]) {
                int topIndex = stack.pop();

                if (stack.isEmpty()) {
                    break;  // No left boundary
                }

                // Calculate water trapped between boundaries
                int leftBoundary = stack.peek();
                int width = current - leftBoundary - 1;
                int boundedHeight = Math.min(height[current], height[leftBoundary]) - height[topIndex];
                waterTrapped += width * boundedHeight;
            }

            stack.push(current);
        }

        return waterTrapped;
    }

    // Helper class to store cell information
    private static class Cell {
        int i, j, height;

        Cell(int i, int j, int height) {
            this.i = i;
            this.j = j;
            this.height = height;
        }
    }
}
