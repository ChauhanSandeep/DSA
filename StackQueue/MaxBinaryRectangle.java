package StackQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Given a binary matrix, find the maximum area of rectangle that can be created from this matrix
 * Modification of LargestHistogram problem
 * https://www.youtube.com/watch?v=St0Jf_VmG_g&list=PL_z_8CaSLPWdeOezg68SKkeLN4-T_jNHd&index=8
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
        int area = new MaxBinaryRectangle().maximalRectangle(matrix);
        System.out.println("max area is " + area);

    }


    public int maximalRectangle(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;
        int rows = matrix.length;
        int cols = matrix[0].length;

        int result = 0;
        List<Integer> heights = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            if (row == 0) {
                for (int j = 0; j < cols; j++) {
                    heights.add(matrix[row][j]);
                }
            } else {
                for (int j = 0; j < cols; j++) {
                    if (matrix[row][j] == 0) {
                        heights.set(j, 0);
                    } else {
                        heights.set(j, heights.get(j) + matrix[row][j]);
                    }
                }
            }
            int tempResult = largestHistogram(heights);
//            System.out.println(tempResult);
            result = Math.max(result, tempResult);
        }
        return result;

    }

    public int largestHistogram(List<Integer> heights) {
//        System.out.println(heights);
        int maxArea = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);

        for (int i = 0; i < heights.size(); i++) {
            int rightIndex = i;

            while (stack.peek() != -1 && heights.get(stack.peek()) >= heights.get(rightIndex)) {
                int currIndex = stack.pop();
                int currHeight = heights.get(currIndex);

                int leftIndex = stack.peek();
                int width = rightIndex - leftIndex - 1;
                maxArea = Math.max(maxArea, width * currHeight);
            }
            stack.push(rightIndex);
        }

        while (stack.peek() != -1) {
            int currIndex = stack.pop();
            int currHeight = heights.get(currIndex);

            int leftIndex = stack.peek();
            int width = heights.size() - leftIndex - 1;
            maxArea = Math.max(maxArea, width * currHeight);
        }
        return maxArea;
    }
}
