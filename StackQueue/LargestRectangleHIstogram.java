package StackQueue;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * https://leetcode.com/problems/largest-rectangle-in-histogram/
 */
public class LargestRectangleHIstogram {

    public static void main(String[] args) {
        int[] heights = {2, 1, 5, 6 ,2, 3};
        int maxRectangle = new LargestRectangleHIstogram().largestRectangleArea(heights);
        System.out.println("The max rectangle is " + maxRectangle);
    }

    /**
     * 1. Idea is, we will consider every element a[i] to be a candidate for the area calculation.
     * That is, if a[i] is the minimum element then the maximum area possible for all such rectangles
     * would be a[i] * (R-L-1), where a[R] is first subsequent element(R>i) in the array just smaller than a[i],
     * similarly a[L] is first previous element just smaller than a[i].
     * i.e. Take a[i] as a center and expand it to left and right and stop when first just smaller elements are found on both the sides
     *
     * 2. We add the element a[i] directly to the stack if it's greater than the peak element, because we are yet to find R for this.
     * L is just the previous element in stack. (We will use this information later when we will pop it out).
     *
     * 3. If we get an element a[i] which is smaller than the peak value, it is the R value for all the elements present in stack which are
     * greater than a[i]. Pop out the elements greater than a[i], we have their R value and L value(point 2). and now push a[i] and repeat..
     */
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1);
        int length = heights.length;
        int maxArea = 0;
        for (int i = 0; i < length; i++) {
            while ((stack.peek() != -1) && (heights[stack.peek()] >= heights[i])) {
                int currentHeight = heights[stack.pop()];
                int currentWidth = i - stack.peek() - 1;
                maxArea = Math.max(maxArea, currentHeight * currentWidth);
            }
            stack.push(i);
        }
        while (stack.peek() != -1) {
            int currentHeight = heights[stack.pop()];
            int currentWidth = length - stack.peek() - 1;
            maxArea = Math.max(maxArea, currentHeight * currentWidth);
        }
        return maxArea;
    }
}
