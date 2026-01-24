package stacksandqueues.monotonicstack;

import java.util.*;

/**
 * 84. Largest Rectangle in Histogram
 *
 * Problem: Given array of integers heights representing histogram bar heights
 * where width of each bar is 1, return the area of the largest rectangle.
 *
 * Example:
 * Input: heights = [2,1,5,6,2,3]
 * Output: 10
 * Explanation: Rectangle with height 5 and width 2 has area = 10.
 *
 * LeetCode: https://leetcode.com/problems/largest-rectangle-in-histogram
 *
 * Follow-up questions:
 * Q: How to handle very large arrays efficiently?
 * A: Use divide-and-conquer or optimize stack operations with careful indexing.
 *
 * Q: Can we find all rectangles above a certain area threshold?
 * A: Modify stack algorithm to collect all valid rectangles during traversal.
 *
 * Q: How to extend to 2D maximum rectangle problem?
 * A: Use this as subroutine, treating each row as histogram base.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class LargestRectangleInHistogram {

    /**
     * Monotonic stack approach - optimal solution.
     *
     * Algorithm: Stack-based scanning
     * - Use a stack to maintain indices of histogram bars in increasing order of height.
     * - For each bar, pop from stack until current bar is taller than stack top.
     * - For the popped bar, calculate area with it as the smallest bar:
     *   - Width = current index - previous smaller index - 1 (Bars in between are taller or of equal height as the popped bar)
     *   - Area = height * width
     * - Update maxArea if current area is larger.
     *
     * Time Complexity: O(n) - each element pushed/popped once
     * Space Complexity: O(n) for stack storage
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
}