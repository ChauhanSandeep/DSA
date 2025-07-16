package StackQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Given a binary matrix, find the maximum area of a rectangle formed by 1s.
 * This is a variation of the "Largest Histogram Area" problem.
 *
 * Problem Link: https://www.youtube.com/watch?v=St0Jf_VmG_g&list=PL_z_8CaSLPWdeOezg68SKkeLN4-T_jNHd&index=8
 *

 */
public class MaxBinaryRectangle {

    public static void main(String[] args) {
        int[][] matrix = {
                {0, 1, 1, 0},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 0, 0}
        };

        MaxBinaryRectangle solver = new MaxBinaryRectangle();
        int maxArea = solver.findMaxRectangleArea(matrix);
        System.out.println("Maximum Rectangle Area: " + maxArea); // output: 8
    }

    /**
     * Approach:
     * - Treat each row as the base of a histogram and compute the largest rectangle using a stack.
     * - Maintain an array of column heights, updating it as we traverse each row.
     * - Compute the largest rectangle in the histogram for each row.
     *
     * Steps:
     * 1. Initialize a list to store the heights of each column based on the current row.
     * 2. For each row, update the heights:
     *    - If the cell is 1, increment the height from the previous row.
     *    - If it is 0, reset the height to 0.
     * 3. Use a stack to compute the largest rectangle area for the current histogram.
     *
     * Time Complexity: O(rows * cols), since each row computes a histogram in O(cols).
     * Space Complexity: O(cols) for storing histogram heights.
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

    /**
     * Updates the histogram heights based on the current row.
     */
    private void updateHeights(List<Integer> heights, int[] row) {
        for (int col = 0; col < row.length; col++) {
            if (row[col] == 0) {
                heights.set(col, 0);
            } else {
                heights.set(col, heights.get(col) + 1);
            }
        }
    }

    /**
     * Computes the largest rectangle area in a histogram using monotonically increasing stack.
     * For each bar, we find the shortest bar to the left and right, and calculate the area using the height of the popped bar.
     */
    private int computeLargestHistogramArea(List<Integer> heights) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1); // Sentinel value to avoid empty stack issues

        int maxArea = 0;

        for (int i = 0; i < heights.size(); i++) {
            while (stack.peek() != -1 && heights.get(stack.peek()) >= heights.get(i)) {
                maxArea = Math.max(maxArea, computeArea(heights, stack, i));
            }
            stack.push(i);
        }

        // Process remaining elements in stack
        while (stack.peek() != -1) {
            maxArea = Math.max(maxArea, computeArea(heights, stack, heights.size()));
        }

        return maxArea;
    }

    /**
     * Computes the area of a rectangle given a popped index from the stack.
     */
    private int computeArea(List<Integer> heights, Stack<Integer> stack, int rightBoundary) {
        int heightIndex = stack.pop();
        int height = heights.get(heightIndex);
        int width = rightBoundary - stack.peek() - 1; // Compute width based on next left boundary
        return width * height;
    }
}
