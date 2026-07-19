package stacksandqueues.monotonicstack;

import java.util.*;

/**
 * Problem: Largest Rectangle in Histogram
 *
 * Given histogram bar heights with width 1, return the largest rectangle area
 * formed by adjacent bars. The height of any chosen rectangle is limited by the
 * shortest bar inside its width.
 *
 * Leetcode: https://leetcode.com/problems/largest-rectangle-in-histogram/ (Hard)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Stack | Monotonic increasing stack | Sentinel flush
 *
 * Example:
 *   Input:  heights = [2,1,5,6,2,3]
 *   Output: 10
 *   Why:    bars 5 and 6 form width 2 with limiting height 5, so area 10.
 *
 * Follow-ups:
 *   1. Precompute left and right boundaries instead?
 *      Use two monotonic stack passes to find previous and next smaller indices.
 *   2. How do equal heights affect width?
 *      The primary method keeps equal heights until a strictly smaller bar flushes them.
 *   3. Extend to maximal rectangle in a matrix?
 *      Build column heights row by row and run this method for each row.
 *   4. Support range queries for many histograms?
 *      Use divide and conquer with a segment tree for range minimum indices.
 *
 * Related: Maximal Rectangle (85), Trapping Rain Water (42), Sum of Subarray Minimums (907).
 */

public class LargestRectangleInHistogram {

        /**
     * Intuition: an increasing stack delays area calculation while bars can
     * still extend to the right. When a shorter currentHeight appears, every
     * taller popped bar has just found its right boundary; the new stack top is
     * its left boundary.
     *
     * Algorithm:
     *   1. Scan every bar plus one sentinel height 0 at the end.
     *   2. While currentHeight is smaller than the height at the stack top, pop a barIndex.
     *   3. Use the new stack top as previousSmallerIndex, or -1 if empty.
     *   4. Compute barHeight * (currentIndex - previousSmallerIndex - 1) and update maxArea.
     *   5. Push currentIndex and continue.
     *
     * Time:  O(n) - each index is pushed once and popped at most once.
     * Space: O(n) - the stack can hold all indices in increasing-height order.
     *
     * @param heights histogram bar heights
     * @return largest rectangle area in the histogram
     */

    public int largestRectangleArea(int[] heights) {
        Stack<Integer> monotonicStack = new Stack<>(); // Monotonic increasing stack
        int maxArea = 0;
        // Process all bars plus one sentinel (height 0) to flush remaining stack
        for (int currentIndex = 0; currentIndex <= heights.length; currentIndex++) {
            // Use 0 as sentinel height after last bar to trigger all remaining calculations
            int currentHeight = (currentIndex == heights.length) ? 0 : heights[currentIndex];

            // Pop bars taller than current - they can't extend further right
            while (!monotonicStack.isEmpty() && currentHeight < heights[monotonicStack.peek()]) {
                int barIndex = monotonicStack.pop();
                int barHeight = heights[barIndex];

                // Width: from bar after previous stack top to current position (exclusive)
                // If stack empty, bar extends from beginning (index 0)
                int previousSmallerIndex = monotonicStack.isEmpty() ? -1 : monotonicStack.peek();
                int width = currentIndex - previousSmallerIndex - 1;

                maxArea = Math.max(maxArea, barHeight * width);
            }

            monotonicStack.push(currentIndex);
        }

        return maxArea;
    }

    /**
     * Two-pass approach calculating left and right boundaries.
     * Precomputes for each bar how far it can extend left and right.
     */
    public int largestRectangleAreaTwoPass(int[] heights) {
        int length = heights.length;
        if (length == 0) return 0;

        int[] leftBound = new int[length];  // Left boundary for each bar
        int[] rightBound = new int[length]; // Right boundary for each bar

        // Calculate left boundaries
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < length; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            leftBound[i] = stack.isEmpty() ? 0 : stack.peek() + 1;
            stack.push(i);
        }

        // Calculate right boundaries
        stack.clear();
        for (int i = length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            rightBound[i] = stack.isEmpty() ? length - 1 : stack.peek() - 1;
            stack.push(i);
        }

        // Calculate maximum area
        int maxArea = 0;
        for (int i = 0; i < length; i++) {
            int area = heights[i] * (rightBound[i] - leftBound[i] + 1);
            maxArea = Math.max(maxArea, area);
        }

        return maxArea;
    }

    public static void main(String[] args) {
        LargestRectangleInHistogram solver = new LargestRectangleInHistogram();
        int[][] inputs = { {2, 1, 5, 6, 2, 3}, {2, 4}, {} };
        int[] expected = { 10, 4, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.largestRectangleArea(inputs[i]);
            System.out.printf("heights=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }
}