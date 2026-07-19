package stacksandqueues.adityavermaplaylist;

import java.util.*;

/**
 * Problem: Maximal Rectangle in a Binary Matrix
 *
 * Given a matrix of '0' and '1' characters, return the area of the largest
 * rectangle containing only '1' cells. Each row is treated as the bottom of a
 * histogram built from consecutive ones above it.
 *
 * Leetcode: https://leetcode.com/problems/maximal-rectangle/ (Hard)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Stack | Histogram transformation | Monotonic increasing stack
 *
 * Example:
 *   Input:  matrix = [[1,0,1,0,0],[1,0,1,1,1],[1,1,1,1,1],[1,0,0,1,0]]
 *   Output: 6
 *   Why:    rows 1-2 and columns 2-4 form a 2 by 3 block of ones.
 *
 * Follow-ups:
 *   1. Return rectangle coordinates instead of area?
 *      Track the row, width, and popped histogram index when maxArea changes.
 *   2. Count all-ones rectangles, not just the largest?
 *      Use row-wise heights with a monotonic stack that accumulates counts.
 *   3. What if the matrix is sparse?
 *      Store one positions per row and update compressed heights only for active columns.
 *   4. Can this be streamed row by row?
 *      Yes, keep only the current heights array and best area so far.
 *
 * Related: Largest Rectangle in Histogram (84), Count Submatrices With All Ones (1504).
 */

public class MaxRectangleBinaryMatrix {

        /**
     * Intuition: a rectangle of ones ending at a row is the same as a rectangle
     * in a histogram whose heights are consecutive ones in each column. The code
     * updates that histogram for every row and reuses the largest rectangle in
     * histogram helper to keep the best area.
     *
     * Algorithm:
     *   1. Return 0 for an empty matrix.
     *   2. Maintain heights[j] as consecutive ones ending at the current row.
     *   3. For each row, increment heights on '1' and reset them on '0'.
     *   4. Run largestRectangleArea on heights and update maxArea.
     *
     * Time:  O(rows * cols) - each row update and histogram pass is linear in columns.
     * Space: O(cols) - heights and the histogram stack store one entry per column.
     *
     * @param matrix binary character matrix
     * @return maximum rectangle area containing only ones
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

    /** Computes the largest rectangle area for one histogram row. */
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
        char[][][] inputs = {
            {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}
            },
            {},
            { {'0'} }
        };
        int[] expected = { 6, 0, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.maximalRectangle(inputs[i]);
            System.out.printf("matrix=%s -> %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }
}