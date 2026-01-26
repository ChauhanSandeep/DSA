package arrays.twopointers;

import java.util.*;

/**
 * Problem: Trapping Rain Water
 *
 * Given n non-negative integers representing an elevation map where the width of
 * each bar is 1, compute how much water it can trap after raining.
 *
 * Example:
 * Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * Output: 6
 * Explanation: The elevation map (represented by array height) traps 6 units of rain water.
 *
 * Visual:
 *        █
 *    █   ██ █
 *  █ ██ ███████
 *  Water trapped: 6 units (shown as spaces between bars)
 *
 * Constraints:
 * - n == height.length
 * - 1 <= n <= 2 * 10^4
 * - 0 <= height[i] <= 10^5
 *
 * LeetCode Problem: https://leetcode.com/problems/trapping-rain-water
 *
 * Follow-up Questions:
 *
 * 1. What if you need to find which positions trap water, not just total volume?
 *    Answer: Store boolean array or list of positions during calculation. Mark index
 *    as trapping water whenever we add to trapped water count. Return positions array.
 *
 * 2. How would you handle 2D elevation map (trapping water on a surface)?
 *    Answer: Use priority queue starting from borders, process cells inward like
 *    Dijkstra's algorithm. Water level at each cell determined by minimum border height.
 *    Related problem: https://leetcode.com/problems/trapping-rain-water-ii/
 *
 * 3. What if bars have different widths instead of uniform width 1?
 *    Answer: Modify water calculation to multiply trapped height by bar width. Track
 *    widths in separate array. Formula becomes: water += width[i] * (minHeight - height[i]).
 *
 * 4. Can you modify to find maximum water that can be trapped by removing k bars?
 *    Answer: Dynamic programming with state (position, bars_removed, water_trapped).
 *    Try removing or keeping each bar, tracking optimal removal strategy.
 *
 * 5. How would you handle continuous rainfall that fills tank over time?
 *    Answer: Add time dimension. Calculate fill rate based on rainfall intensity.
 *    Simulate water level rising, accounting for overflow and drainage dynamics.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class TrappingRainWaterII {
    /**
     * Calculates trapped rain water using optimal two-pointer approach.
     *
     * Algorithm:
     * 1. Initialize two pointers at both ends of array
     * 2. Track maximum heights seen from left and right
     * 3. Move pointer with smaller height inward:
     *    - If current height < max height, water can be trapped
     *    - Water trapped = max height - current height
     *    - Update max height if current is taller
     * 4. Continue until pointers meet
     *
     * Key insight: Water level at any position is determined by minimum of
     * maximum heights on both sides. By moving from the smaller side, we ensure
     * that side's max is the limiting factor for water at current position.
     *
     * Time Complexity: O(N) where N is array length. Single pass with two pointers.
     * Space Complexity: O(1) using only constant extra variables.
     *
     * @param height array of non-negative integers representing elevation map
     * @return total units of water that can be trapped
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
