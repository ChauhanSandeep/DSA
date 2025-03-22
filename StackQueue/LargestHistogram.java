package StackQueue;

import java.util.Stack;

/**
 * Problem: Find the largest rectangle area in a histogram.
 * 
 * <p>LeetCode Problem Link:
 * <a href="https://leetcode.com/problems/largest-rectangle-in-histogram/">Largest Rectangle in Histogram</a>
 * </p>
 *
 * <p><b>Approach:</b></p>
 * - Use a **monotonic increasing stack** to efficiently compute the largest rectangle.
 * - For each bar, find the **previous smaller element** (left bound) and the **next smaller element** (right bound).
 * - Maintain a stack of indices to track bars in increasing order.
 * - When encountering a smaller bar, calculate area and update `maxArea`.
 * 
 * <p><b>Time Complexity:</b> O(N) - Each element is pushed and popped once.</p>
 * <p><b>Space Complexity:</b> O(N) - Stack usage for indices.</p>
 */
public class LargestHistogram {

    public static void main(String[] args) {
        int[] heights = {2, 1, 5, 6, 2, 3};
        System.out.println("Max rectangle area: " + largestRectangleArea(heights)); // Expected Output: 10
    }

    /**
     * Computes the largest rectangle area in a histogram.
     *
     * @param heights Array representing the heights of histogram bars.
     * @return Maximum rectangular area.
     */
    public static int largestRectangleArea(int[] heights) {
        Stack<Integer> indexStack = new Stack<>();
        int maxArea = 0;

        // Iterate through histogram bars
        for (int i = 0; i < heights.length; i++) {
            // Maintain a monotonic increasing stack
            while (!indexStack.isEmpty() && heights[indexStack.peek()] > heights[i]) {
                maxArea = calculateArea(heights, indexStack, i, maxArea);
            }
            indexStack.push(i);
        }

        // Process remaining stack elements
        while (!indexStack.isEmpty()) {
            maxArea = calculateArea(heights, indexStack, heights.length, maxArea);
        }

        return maxArea;
    }

    /**
     * Helper function to calculate the maximum rectangle area when popping from the stack.
     *
     * @param heights     The histogram heights array.
     * @param indexStack  Stack storing indices of histogram bars.
     * @param rightIndex  The right boundary index for width calculation.
     * @param maxArea     Current maximum area.
     * @return Updated maximum area.
     */
    private static int calculateArea(int[] heights, Stack<Integer> indexStack, int rightIndex, int maxArea) {
        int height = heights[indexStack.pop()];
        int leftIndex = indexStack.isEmpty() ? -1 : indexStack.peek();
        int width = rightIndex - leftIndex - 1;
        return Math.max(maxArea, height * width);
    }
}
