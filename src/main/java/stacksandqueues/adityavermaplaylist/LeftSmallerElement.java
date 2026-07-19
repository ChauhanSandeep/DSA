package stacksandqueues.adityavermaplaylist;

import java.util.*;

/**
 * Problem: Nearest Smaller Element to the Left
 *
 * For every number in an array, return the closest smaller value that appears
 * before it. If no smaller value exists on the left, return -1 for that
 * position.
 *
 * Leetcode: not available (classic stack pattern)
 * Pattern:  Stack | Monotonic increasing stack | Previous smaller element
 *
 * Example:
 *   Input:  nums = [4,5,2,10,8]
 *   Output: [-1,4,-1,2,2]
 *   Why:    10 sees 2 as the nearest smaller value to its left; 4 and 2 have none.
 *
 * Follow-ups:
 *   1. Return indices instead of values?
 *      Store indices on the stack and map empty stack to -1.
 *   2. Find nearest smaller to the right?
 *      Traverse from right to left with the same >= pop condition.
 *   3. Handle duplicates as valid smaller-or-equal answers?
 *      Change the pop condition from >= to > so equal values remain candidates.
 *   4. Support online queries as values stream in?
 *      Keep the same monotonic stack and answer each new value immediately.
 *
 * Related: Next Greater Element I (496), Daily Temperatures (739), Largest Rectangle in Histogram (84).
 */

public class LeftSmallerElement {

        /**
     * Intuition: values that are greater than or equal to the current value can
     * never be the nearest smaller answer for this or any later value, because
     * the current value is closer and no larger. After removing those blocked
     * values, the stack top is exactly the nearest smaller value to the left.
     *
     * Algorithm:
     *   1. Scan nums from left to right.
     *   2. Pop stack values while they are greater than or equal to currentElement.
     *   3. Record -1 if the stack is empty, otherwise record stack.peek().
     *   4. Push currentElement as a candidate for future positions.
     *
     * Time:  O(n) - each value is pushed once and popped at most once.
     * Space: O(n) - the result array and stack can each grow to n values.
     *
     * @param nums input array of integers
     * @return nearest smaller value to the left for each position
     */

    public int[] findNearestSmallerElements(int[] nums) {
        int length = nums.length;
        int[] nearestSmaller = new int[length];
        Stack<Integer> stack = new Stack<>();

        // Process each element in the array
        for (int index = 0; index < length; index++) {
            int currentElement = nums[index];

            // Remove elements from stack that are greater than or equal to current element
            while (!stack.isEmpty() && stack.peek() >= currentElement) {
                stack.pop();
            }

            // If stack is empty, no smaller element exists to the left
            nearestSmaller[index] = stack.isEmpty() ? -1 : stack.peek();

            // Push the current element to stack for future elements
            stack.push(currentElement);
        }

        return nearestSmaller;
    }

        public static void main(String[] args) {
        LeftSmallerElement solver = new LeftSmallerElement();
        int[][] inputs = { {4, 5, 2, 10, 8}, {1, 1, 1}, {} };
        int[][] expected = { {-1, 4, -1, 2, 2}, {-1, -1, -1}, {} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.findNearestSmallerElements(inputs[i]);
            System.out.printf("nums=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }
}