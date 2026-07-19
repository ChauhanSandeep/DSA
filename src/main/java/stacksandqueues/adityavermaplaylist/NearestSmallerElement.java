package stacksandqueues.adityavermaplaylist;

import java.util.*;

/**
 * Problem: Nearest Smaller Element to the Right
 *
 * For every number in an array, return the closest smaller value that appears
 * after it. If no smaller value exists on the right, return -1 for that
 * position.
 *
 * Leetcode: not available (classic stack pattern)
 * Pattern:  Stack | Monotonic increasing stack | Next smaller element
 *
 * Example:
 *   Input:  nums = [4,5,2,10,8]
 *   Output: [2,2,-1,8,-1]
 *   Why:    10 sees 8 immediately to its right, while 2 and 8 have no smaller value after them.
 *
 * Follow-ups:
 *   1. Return nearest smaller indices instead of values?
 *      Store indices on the stack and read nums[stack.peek()] only for comparisons.
 *   2. Find nearest smaller to the left?
 *      Traverse from left to right with the same >= pop condition.
 *   3. Treat equal values as valid answers?
 *      Change the pop condition from >= to > so equal values remain candidates.
 *   4. Use this for histogram widths?
 *      Record boundary indices instead of values and combine with previous smaller indices.
 *
 * Related: Largest Rectangle in Histogram (84), Sum of Subarray Minimums (907).
 */

public class NearestSmallerElement {

        /**
     * Intuition: scanning from right to left makes the stack contain only values
     * to the current element's right. Any value greater than or equal to the
     * current value is useless as a smaller answer, so after popping those, the
     * top is the closest smaller value that survived.
     *
     * Algorithm:
     *   1. Scan nums from right to left.
     *   2. Pop stack values while they are greater than or equal to currentElement.
     *   3. Record -1 if the stack is empty, otherwise record stack.peek().
     *   4. Push currentElement as a candidate for elements to its left.
     *
     * Time:  O(n) - each value is pushed once and popped at most once.
     * Space: O(n) - the result array and stack can each grow to n values.
     *
     * @param nums input array of integers
     * @return nearest smaller value to the right for each position
     */

    public int[] findNearestSmallerElementsToRight(int[] nums) {
        int length = nums.length;
        int[] result = new int[length];
        Stack<Integer> stack = new Stack<>();

        // Traverse from right to left
        for (int index = length - 1; index >= 0; index--) {
            int currentElement = nums[index];

            // Remove elements from stack that are greater than or equal to current element
            while (!stack.isEmpty() && stack.peek() >= currentElement) {
                stack.pop();
            }

            // If stack is empty, no smaller element exists to the right
            result[index] = stack.isEmpty() ? -1 : stack.peek();

            // Push the current element to stack for future elements
            stack.push(currentElement);
        }

        return result;
    }

        public static void main(String[] args) {
        NearestSmallerElement solver = new NearestSmallerElement();
        int[][] inputs = { {4, 5, 2, 10, 8}, {1, 1, 1}, {} };
        int[][] expected = { {2, 2, -1, 8, -1}, {-1, -1, -1}, {} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.findNearestSmallerElementsToRight(inputs[i]);
            System.out.printf("nums=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }
}