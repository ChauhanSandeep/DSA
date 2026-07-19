package stacksandqueues.monotonicstack;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Problem: Maximum Binary Rectangle
 *
 * Given a binary matrix, find the maximum area of a rectangle made entirely of
 * 1s. Each row becomes the base of a histogram whose heights count consecutive
 * 1s ending at that row.
 *
 * Leetcode: https://leetcode.com/problems/maximal-rectangle/ (Hard)
 * Pattern:  Stack | Histogram transformation | Monotonic increasing stack
 *
 * Example:
 *   Input:  matrix = [[0,1,1,0],[1,1,1,1],[1,1,1,1],[1,1,0,0]]
 *   Output: 8
 *   Why:    the middle two rows contain four consecutive 1s, forming a 2 by 4 rectangle.
 *
 * Follow-ups:
 *   1. Return coordinates of the rectangle?
 *      Track row, middleIndex, leftIndex, and right boundary when maxArea changes.
 *   2. Count every all-ones subrectangle?
 *      Use histogram heights with stack-based contribution counting per row.
 *   3. Handle a streaming matrix?
 *      Keep only current heights and best area as rows arrive.
 *   4. What if values are booleans or chars?
 *      Only the height update condition changes; the histogram logic is identical.
 *
 * Related: Largest Rectangle in Histogram (84), Maximal Rectangle (85).
 */

public class MaxBinaryRectangle {

        public static void main(String[] args) {
        MaxBinaryRectangle solver = new MaxBinaryRectangle();
        int[][][] inputs = {
            {
                {0, 1, 1, 0},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 0, 0}
            },
            { {0} }
        };
        int[] expected = { 8, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.findMaxRectangleArea(inputs[i]);
            System.out.printf("matrix=%s -> %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: once a row is fixed as the bottom edge, every column has a
     * height equal to how many 1s continue upward. The best all-ones rectangle
     * ending at that row is therefore just the largest rectangle in that
     * histogram.
     *
     * Algorithm:
     *   1. Return 0 for a null or empty matrix.
     *   2. Initialize heights from the first row and compute its histogram area.
     *   3. For each later row, updateHeights by extending 1s or resetting 0s.
     *   4. Compute the histogram area for updated heights and keep maxArea.
     *
     * Time:  O(rows * cols) - each row update and histogram computation is linear in columns.
     * Space: O(cols) - heights and the monotonic stack store column data.
     *
     * @param matrix binary matrix of 0s and 1s
     * @return maximum all-ones rectangle area
     */

    public int findMaxRectangleArea(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;

        int rows = matrix.length;
        int cols = matrix[0].length;
        List<Integer> heights = new ArrayList<>();
        int maxArea = 0;

        // Step1: Initialize the histogram heights with the first row
        for (int col = 0; col < cols; col++) {
            heights.add(matrix[0][col]);
        }

        // Compute the maximum rectangle for the first row
        maxArea = computeLargestHistogramArea(heights);

        for (int row = 1; row < rows; row++) {
            // Step2: Update heights based on the current row
            updateHeights(heights, matrix[row]);
            // Step3: Compute the largest rectangle area for the updated histogram
            maxArea = Math.max(maxArea, computeLargestHistogramArea(heights));
        }

        return maxArea;
    }

        /** Updates histogram heights for one matrix row. */

    private void updateHeights(List<Integer> heights, int[] row) {
        for (int col = 0; col < row.length; col++) {
            if (row[col] == 0) {
                heights.set(col, 0);
            } else {
                heights.set(col, heights.get(col) + 1);
            }
        }
    }

        /** Computes the largest rectangle area in one histogram. */

    private int computeLargestHistogramArea(List<Integer> heights) {
        Stack<Integer> monotonicStack = new Stack<>();
        monotonicStack.push(-1); // Sentinel value to avoid empty stack issues

        int maxArea = 0;

        for (int i = 0; i < heights.size(); i++) {
            while (monotonicStack.peek() != -1 && heights.get(monotonicStack.peek()) >= heights.get(i)) {
                int middleIndex = monotonicStack.pop();
                int leftIndex = monotonicStack.peek();
                maxArea = Math.max(maxArea, computeArea(heights, leftIndex, middleIndex, i));
            }
            monotonicStack.push(i);
        }

        // Process remaining elements in stack
        while (monotonicStack.peek() != -1) {
            int middleIndex = monotonicStack.pop();
            int leftIndex = monotonicStack.peek();
            maxArea = Math.max(maxArea, computeArea(heights, leftIndex, middleIndex, heights.size()));
        }

        return maxArea;
    }

    /** Computes area using middleIndex as the limiting bar. */
    private int computeArea(List<Integer> heights, int leftIndex, int middleIndex, int rightIndex) {
        int height = heights.get(middleIndex);
        int width = rightIndex - leftIndex - 1; // Compute width based on next left boundary
        return width * height;
    }
}
