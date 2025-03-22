package StackQueue;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Find the largest rectangle area in a histogram.
 * 
 * <p>LeetCode Problem Link:
 * <a href="https://leetcode.com/problems/largest-rectangle-in-histogram/">Largest Rectangle in Histogram</a>
 * </p>
 *
 * <p><b>Approach:</b></p>
 * - Use a **monotonic increasing stack** to efficiently determine the largest rectangle.
 * - For each bar:
 *   - Find the **previous smaller element** (left boundary).
 *   - Find the **next smaller element** (right boundary).
 * - Maintain a stack of indices, ensuring it remains in increasing order.
 * - When encountering a smaller bar, calculate the area for previous bars.
 *
 * <p><b>Time Complexity:</b> O(N) (Each element is pushed and popped once).</p>
 * <p><b>Space Complexity:</b> O(N) (Stack storage for indices).</p>
 */
public class LargestRectangleHistogram {

    public static void main(String[] args) {
        int[] heights = {2, 1, 5, 6, 2, 3};
        int maxRectangle = largestRectangleArea(heights);
        System.out.println("Max rectangle area: " + maxRectangle); // Expected Output: 10
    }

    /**
     * Computes the largest rectangle area in a histogram.
     *
     * @param heights Array representing the heights of histogram bars.
     * @return Maximum rectangular area.
     */
    public static int largestRectangleArea(int[] heights) {
        Deque<Integer> indexStack = new ArrayDeque<>();
        indexStack.push(-1); // Sentinel value for ease of calculation
        int maxArea = 0;
        
        // Iterate through each histogram bar
        for (int i = 0; i < heights.length; i++) {
            // Maintain a monotonic increasing stack
            while (indexStack.peek() != -1 && heights[indexStack.peek()] >= heights[i]) {
                maxArea = calculateArea(heights, indexStack, i, maxArea);
            }
            indexStack.push(i);
        }

        // Process remaining bars in the stack
        while (indexStack.peek() != -1) {
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
    private static int calculateArea(int[] heights, Deque<Integer> indexStack, int rightIndex, int maxArea) {
        int height = heights[indexStack.pop()];
        int leftIndex = indexStack.peek();
        int width = rightIndex - leftIndex - 1;
        return Math.max(maxArea, height * width);
    }
}
