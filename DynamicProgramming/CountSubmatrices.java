package DynamicProgramming;

import java.util.Arrays;
import java.util.Stack;

/**
 * https://leetcode.com/problems/count-submatrices-with-all-ones/
 */
public class CountSubmatrices {

    public static void main(String[] args) {
        int[][] mat = {
                {0, 1, 1, 0},
                {0, 1, 1, 1},
                {1, 1, 1, 0}
        };
        System.out.println(new CountSubmatrices().numSubmat(mat));
    }

    int rows;
    int cols;

    public int numSubmat(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) return 0;
        this.rows = mat.length;
        this.cols = mat[0].length;

        int[][] heightMat = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0) {
                    heightMat[i][j] = mat[i][j];
                } else if (mat[i][j] == 0) {
                    heightMat[i][j] = 0;
                } else {
                    heightMat[i][j] = mat[i][j] + heightMat[i - 1][j];
                }
            }
        }

        int sum = 0;
        for (int[] heights : heightMat) {
            sum += findSum(heights);
        }
        return sum;
    }

    public int findSum(int[] heights) {
        System.out.println(Arrays.toString(heights));

        int[] sumArr = new int[heights.length];
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);

        for (int i = 0; i < heights.length; ++i) {
            while (stack.peek() != -1 && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }

            int currIndex = i;
            int currHeight = heights[currIndex];
            int prevIndex = stack.peek(); // previous Index with smaller height than current
            sumArr[currIndex] = currHeight * (currIndex - prevIndex);
            if(prevIndex != -1) {
                // add from previous submatrix with smaller height
                sumArr[i] += sumArr[prevIndex];
            }
            stack.push(i);
        }
        System.out.println(Arrays.toString(sumArr));

        return Arrays.stream(sumArr).sum();
    }
}
