package StackQueue;

import java.util.Stack;

/**
 * Given an array of integers heights representing the histogram's
 * bar height where the width of each bar is 1, return the area
 * of the largest rectangle in the histogram.
 */
public class LargestHistogram {
    public static void main(String[] args) {
        int[] heights = {2,1,5,6,2,3};
        System.out.println("max rectangle area is " +
                new LargestHistogram().largestRectangleArea(heights));
    }

    public int largestRectangleArea(int[] heights) {

        int maxArea = 0;
        Stack<Integer> stack = new Stack<>();

        stack.push(-1);
        for(int i= 0; i< heights.length; i++) {

            while(stack.peek() != -1 && heights[stack.peek()] >= heights[i]) {
                int currentHeight = heights[stack.pop()];
                int prevSmaller = stack.peek();
                int currentWidth = i - prevSmaller - 1;
                maxArea = Math.max(maxArea, currentHeight * currentWidth);
            }
            stack.push(i);
        }

        while(stack.peek() != -1) {
            int currentHeight = heights[stack.pop()];
            int prevSmaller = stack.peek();
            int currentWidth = heights.length - prevSmaller - 1;
            maxArea = Math.max(maxArea, currentHeight*currentWidth);
        }
        return maxArea;
    }

}
