package stacksandqueues.adityavermaplaylist;

import java.util.Arrays;
import java.util.Stack;

/**
 * Problem: Largest Rectangle in Histogram
 *
 * Given bar heights of a histogram where every bar has width 1, return the
 * largest rectangle area that can be formed by choosing one or more adjacent
 * bars and using the shortest chosen bar as the rectangle height.
 *
 * Leetcode: https://leetcode.com/problems/largest-rectangle-in-histogram/ (Hard)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Stack | Monotonic increasing stack | Nearest smaller boundaries
 *
 * Example:
 *   Input:  heights = [2,1,5,6,2,3]
 *   Output: 10
 *   Why:    bars 5 and 6 form width 2 with limiting height 5, so area 5 * 2 = 10.
 *
 * Follow-ups:
 *   1. Can you solve it in one pass without explicit left and right arrays?
 *      Keep increasing indices on a stack and compute area when a shorter bar appears.
 *   2. How do duplicate heights affect the stack condition?
 *      Use the original >= pop rule for boundary arrays so equal bars collapse consistently.
 *   3. How would you extend this to a binary matrix?
 *      Treat each row as the base of a histogram and run this algorithm per row.
 *   4. Can divide and conquer be competitive?
 *      Yes with a segment tree for range minimum queries, giving O(n log n).
 *
 * Related: Maximal Rectangle (85), Trapping Rain Water (42), Daily Temperatures (739).
 */

public class LargestHistogram {

        public static void main(String[] args) {
        int[][] inputs = { {2, 1, 5, 6, 2, 3}, {2, 4}, {} };
        int[] expected = { 10, 4, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = largestRectangleArea(inputs[i]);
            System.out.printf("heights=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: a bar's best rectangle is limited by the first smaller bar on
     * its left and the first smaller bar on its right. The code precomputes
     * those two boundaries with monotonic stacks, then tries each bar as the
     * limiting height of its widest possible rectangle.
     *
     * Algorithm:
     *   1. Initialize previousSmaller to -1 and nextSmaller to length.
     *   2. Scan right to left, popping heights >= current, to fill nextSmaller.
     *   3. Scan left to right, popping heights >= current, to fill previousSmaller.
     *   4. For each bar, compute height * (nextSmaller - previousSmaller - 1).
     *
     * Time:  O(n) - each index is pushed and popped at most once per stack pass.
     * Space: O(n) - boundary arrays and the stack store up to n indices.
     *
     * @param heights histogram bar heights
     * @return largest rectangle area in the histogram
     */

    public static int largestRectangleArea(int[] heights) {
        int length = heights.length;

        int[] previousSmaller = new int[length];  // PSE (Previous Smaller Element) indices
        int[] nextSmaller = new int[length]; // NSE (Next Smaller Element) indices

        // Initialize with boundary values
        for (int i = 0; i < length; i++) {
            previousSmaller[i] = -1;      // Default: no smaller element to the left
            nextSmaller[i] = length;  // Default: no smaller element to the right
        }

        Stack<Integer> stack = new Stack<>();

        System.out.println("Performing Next Smaller Element (NSE) computation...");
        // Find Next Smaller Element (NSE) using monotonic increasing stack
        for (int i = length - 1; i >= 0; i--) {
            // Pop elements >= current (maintain increasing stack)
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            
            // Top of stack is the next smaller element
            if (!stack.isEmpty()) {
                nextSmaller[i] = stack.peek();
            }
            
            stack.push(i);
        }
        
        // Clear stack to reuse
        stack.clear();

        System.out.println("Performing Previous Smaller Element (PSE) computation...");
        // Find Previous Smaller Element (PSE) using monotonic increasing stack
        for (int i = 0; i < length; i++) {
            // Pop elements >= current (maintain increasing stack)
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            
            // Top of stack is the previous smaller element
            if (!stack.isEmpty()) {
                previousSmaller[i] = stack.peek();
            }
            
            stack.push(i);
        }

        // Calculate max area
        int maxArea = 0;
        for (int i = 0; i < length; i++) {
            int height = heights[i];
            int width = nextSmaller[i] - previousSmaller[i] - 1;
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

            // While current height is less than stack top -> time to pop and compute area
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
