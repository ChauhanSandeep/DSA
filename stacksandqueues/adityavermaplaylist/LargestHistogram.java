package stacksandqueues.adityavermaplaylist;

import java.util.Stack;

/**
 * Problem: Find the largest rectangle area in a histogram.
 * Given an array of integers heights representing the histogram's bar height where the width of each bar is 1,
 * return the area of the largest rectangle in the histogram.
 *
 * LeetCode Problem Link: https://leetcode.com/problems/largest-rectangle-in-histogram/
 *
 * Intuition:
 * - For each bar, the largest rectangle it can form is bounded by the nearest smaller bars on the left and right.
 * - The width of the rectangle is determined by the distance between these bounds.
 * - A monotonic increasing stack helps maintain indices of histogram bars in increasing order of height.
 * This allows efficient determination of the previous smaller element (left bound) and the
 * next smaller element (right bound) for each bar.
 */
public class LargestHistogram {

    public static void main(String[] args) {
        int[] heights = {2, 1, 5, 6, 2, 3};
        System.out.println("Max rectangle area: " + largestRectangleArea(heights)); // Expected Output: 10
    }

    /**
     * Computes the largest rectangular area in a histogram using the concepts of
     * Nearest Smaller to Left (NSL) and Nearest Smaller to Right (NSR).
     *
     * Intuition:
     * - For every bar, we want to know how far we can extend to the left and right without hitting a smaller bar.
     * - NSL tells us the previous bar that is smaller → left boundary
     * - NSR tells us the next bar that is smaller → right boundary
     * - Width = (NSR - NSL - 1), Area = height * width
     *
     * Approach:
     * - Compute NSL and NSR indices for each bar.
     * - For each bar, calculate its maximum area using:
     *   area = height * (rightSmallerIndex - leftSmallerIndex - 1)
     * - Track the maximum of all such areas.
     *
     * Time Complexity: O(n)
     * - Each element is pushed/popped at most once in each stack.
     *
     * Space Complexity: O(n)
     * - Arrays for NSL, NSR, and stack
     *
     * @param heights An array representing heights of histogram bars
     * @return The area of the largest rectangle that can be formed
     */
    public static int largestRectangleArea(int[] heights) {
        int length = heights.length;

        int[] leftSmaller = new int[length];  // NSL indices
        int[] rightSmaller = new int[length]; // NSR indices

        Stack<Integer> stack = new Stack<>();

        // Find Nearest Smaller to Right (NSR)
        for (int i = 0; i < length; i++) {
            // If current element is smaller than stack top, it's NSR for those
            while (!stack.isEmpty() && heights[i] < heights[stack.peek()]) {
                int idx = stack.pop();
                rightSmaller[idx] = i;
            }

            stack.push(i);
        }
        // Clear stack to reuse
        stack.clear();

        // Find Nearest Smaller to Left (NSL)
        for (int i = length - 1; i >= 0; i--) {
            // While stack has elements that are >= current → they're not useful
            while (!stack.isEmpty() && heights[i] < heights[stack.peek()]) {
                int idx = stack.pop();
                leftSmaller[idx] = i; // Current index is nearest smaller to left for popped index
            }

            stack.push(i);
        }

        // Calculate max area
        int maxArea = 0;
        for (int i = 0; i < length; i++) {
            int height = heights[i];
            int width = rightSmaller[i] - leftSmaller[i] - 1;
            int area = height * width;
            maxArea = Math.max(maxArea, area);
        }

        return maxArea;
    }

    /**
     * Intuition:
     * - We iterate through bars and maintain a stack of increasing bar indices.
     * - When we find a bar shorter than the one on top of the stack, we pop and calculate the area.
     * - We add a sentinel '0' at the end to flush out remaining bars from the stack.
     *
     * Approach:
     * - Append 0 to the end of histogram so all bars get processed.
     * - For each bar, while current height < stack top, pop and calculate area:
     *     height = heights[poppedIndex]
     *     width = (stack is empty ? i : i - stack.peek() - 1)
     *     area = height * width
     * - Track max area
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n) for stack
     */
    public int largestRectangleAreaSinglePass(int[] heights) {
        int length = heights.length;
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;

        // Append sentinel 0 to flush the stack at the end
        for (int currentIndex = 0; currentIndex <= length; currentIndex++) {
            int currentHeight = (currentIndex == length) ? 0 : heights[currentIndex];

            // While current height is less than stack top → time to pop and compute area
            while (!stack.isEmpty() && currentHeight < heights[stack.peek()]) {
                int maxIndex = stack.pop(); // index of the bar with max height in this range
                int maxHeight = heights[maxIndex];

                // Width is either full from beginning or between current and stack top
                Integer leftSmallerNumberIndex = stack.peek(); // next smaller element on left
                int width = stack.isEmpty() ? currentIndex : currentIndex - leftSmallerNumberIndex - 1;

                int area = maxHeight * width;
                maxArea = Math.max(maxArea, area);
            }

            stack.push(currentIndex); // Push current index
        }

        return maxArea;
    }
}
