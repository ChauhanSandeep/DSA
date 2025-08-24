package stacksandqueues.adityavermaplaylist;

import java.util.*;

/**
 * Problem: Maximal Rectangle in a Binary Matrix
 *
 * Given a 2D binary matrix filled with '0's and '1's, find the largest rectangle containing only '1's and
 * return its area.
 *
 * Example:
 * Input:
 * [
 *   ['1','0','1','0','0'],
 *   ['1','0','1','1','1'],
 *   ['1','1','1','1','1'],
 *   ['1','0','0','1','0']
 * ]
 * Output: 6
 *
 * Leetcode: https://leetcode.com/problems/maximal-rectangle/
 */
public class MaxRectangleBinaryMatrix {

    /**
     * Intuition:
     * - Each row of the matrix is treated as a histogram base.
     * - As we go row by row, we build the height of histogram (count of continuous '1's above).
     * - For each row histogram, apply the largestRectangleArea logic (same as histogram single pass).
     *
     * Approach:
     * - Maintain an int[] histogram of size equal to number of columns.
     * - For each row:
     *   - If matrix[i][j] == '1' → height[j] += 1
     *   - Else → height[j] = 0
     *   - Apply histogram logic on this row's heights
     *
     * Time Complexity: O(rows * cols)
     * Space Complexity: O(cols) for histogram and stack
     *
     * @param matrix 2D binary matrix
     * @return Maximum area of rectangle containing only 1's
     */
    public int maximalRectangle(char[][] matrix) {
      if (matrix.length == 0) {
        return 0;
      }

        int maxArea = 0;
        int cols = matrix[0].length;
        int[] heights = new int[cols];

        for (char[] row : matrix) {
            // Build histogram
            for (int j = 0; j < cols; j++) {
                if (row[j] == '1') {
                    heights[j] += 1; // increase height if '1'
                } else {
                    heights[j] = 0;  // reset height if '0'
                }
            }
            // at this point heights[] represents the histogram for this row
            // Calculate max area for this histogram
            maxArea = Math.max(maxArea, largestRectangleArea(heights));
        }

        return maxArea;
    }

    // Single pass largest rectangle in histogram
    private int largestRectangleArea(int[] heights) {
        int length = heights.length;
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;

        for (int i = 0; i <= length; i++) {
            int currentHeight = (i == length) ? 0 : heights[i];

            while (!stack.isEmpty() && currentHeight < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = (stack.isEmpty()) ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }

            stack.push(i);
        }

        return maxArea;
    }

    public static void main(String[] args) {
        MaxRectangleBinaryMatrix solver = new MaxRectangleBinaryMatrix();

        char[][] matrix = {
            {'1','0','1','0','0'},
            {'1','0','1','1','1'},
            {'1','1','1','1','1'},
            {'1','0','0','1','0'}
        };

        int result = solver.maximalRectangle(matrix);
        System.out.println("Maximum rectangle area of 1s: " + result);
    }
}