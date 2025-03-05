package DynamicProgramming;

import java.util.Arrays;
import java.util.Stack;

/**
 * Problem: Count Submatrices with All Ones
 * 
 * Given a binary matrix, count the number of submatrices where all elements are 1.
 * 
 * Approach:
 * - Convert the binary matrix into a **height matrix**, where each cell stores the number of 
 *   consecutive 1s in the column up to that row.
 * - For each row in this height matrix, count the number of rectangular submatrices using 
 *   the **Largest Rectangle in Histogram** approach.
 * - Use a **monotonic increasing stack** to efficiently compute the number of valid submatrices.
 * 
 * Time Complexity:
 * - Constructing height matrix: **O(R * C)**
 * - Computing submatrices per row using stack: **O(R * C)**
 * - Overall Complexity: **O(R * C)**
 * 
 * Space Complexity:
 * - **O(R * C)** (for height matrix)
 * - **O(C)** (for stack and sum array)
 * 
 * LeetCode Problem Link:
 * https://leetcode.com/problems/count-submatrices-with-all-ones/
 */
public class CountSubmatrices {

    public static void main(String[] args) {
        int[][] matrix = {
                {0, 1, 1, 0},
                {0, 1, 1, 1},
                {1, 1, 1, 0}
        };
        CountSubmatrices solver = new CountSubmatrices();
        System.out.println("Total Submatrices with All Ones: " + solver.countAllOneSubmatrices(matrix));
    }

    /**
     * Counts the number of submatrices that contain only ones.
     *
     * @param matrix Binary matrix (0s and 1s)
     * @return Total count of all valid submatrices
     */
    public int countAllOneSubmatrices(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;
        
        int rows = matrix.length, cols = matrix[0].length;
        int[][] heightMatrix = computeHeightMatrix(matrix, rows, cols);
        
        int totalSubmatrices = 0;
        for (int[] heights : heightMatrix) {
            totalSubmatrices += countSubmatricesInHistogram(heights);
        }
        return totalSubmatrices;
    }

    /**
     * Computes the height matrix where each cell represents the number of consecutive ones above it.
     *
     * @param matrix Input binary matrix
     * @param rows Number of rows
     * @param cols Number of columns
     * @return Height matrix
     */
    private int[][] computeHeightMatrix(int[][] matrix, int rows, int cols) {
        int[][] heightMatrix = new int[rows][cols];

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                if (matrix[row][col] == 0) {
                    heightMatrix[row][col] = 0;
                } else {
                    heightMatrix[row][col] = (row == 0) ? 1 : heightMatrix[row - 1][col] + 1;
                }
            }
        }
        return heightMatrix;
    }

    /**
     * Uses a monotonic stack to count submatrices in a given histogram row.
     *
     * @param heights Array representing heights of 1s in a row
     * @return Count of valid submatrices
     */
    private int countSubmatricesInHistogram(int[] heights) {
        int[] submatricesCount = new int[heights.length];
        Stack<Integer> stack = new Stack<>();
        stack.push(-1); // Sentinel value to avoid empty stack issues

        for (int i = 0; i < heights.length; i++) {
            while (stack.peek() != -1 && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }

            int prevIndex = stack.peek(); // Previous smaller element index
            submatricesCount[i] = heights[i] * (i - prevIndex);

            if (prevIndex != -1) {
                submatricesCount[i] += submatricesCount[prevIndex];
            }

            stack.push(i);
        }
        
        return Arrays.stream(submatricesCount).sum();
    }
}
