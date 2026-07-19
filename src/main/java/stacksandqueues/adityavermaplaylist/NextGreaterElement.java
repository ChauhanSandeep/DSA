package stacksandqueues.adityavermaplaylist;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;


/**
 * Problem: Next Greater Element
 *
 * For every value in a list, return the first greater value that appears to its
 * right. If no greater value exists, return -1 for that position.
 *
 * Leetcode: https://leetcode.com/problems/next-greater-element-i/ (Easy)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Stack | Monotonic decreasing stack | Resolve pending indices
 *
 * Example:
 *   Input:  numbers = [11,3,21,30]
 *   Output: [21,21,30,-1]
 *   Why:    21 is the first greater value after 11 and 3; 30 has no greater value after it.
 *
 * Follow-ups:
 *   1. How do you answer nums1 queries against nums2?
 *      Precompute next greater values for nums2, then look up nums1 values in a map.
 *   2. What changes for a circular array?
 *      Iterate twice and use modulo indices while only recording first-pass answers.
 *   3. Return distances instead of values?
 *      Keep indices on the stack and store currentIndex - prevIndex when resolved.
 *   4. Find next greater frequency instead of value?
 *      Compare element frequencies while preserving the same stack mechanics.
 *
 * Related: Next Greater Element II (503), Daily Temperatures (739), Online Stock Span (901).
 */

public class NextGreaterElement {

        public static void main(String[] args) {
        List<List<Integer>> inputs = Arrays.asList(
            Arrays.asList(11, 3, 21, 30),
            Arrays.asList(5, 4, 3)
        );
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(21, 21, 30, -1),
            Arrays.asList(-1, -1, -1)
        );

        for (int i = 0; i < inputs.size(); i++) {
            List<Integer> got = findNextGreaterElements(inputs.get(i));
            System.out.printf("numbers=%s -> %s  expected=%s%n",
                inputs.get(i), got, expected.get(i));
        }
    }

        /**
     * Intuition: each index waits on the stack until a future value is big enough
     * to answer it. When currentElement is greater than the value at the stack
     * top, it is the first greater value for that waiting index because all
     * earlier scanned values failed to resolve it.
     *
     * Algorithm:
     *   1. Fill result with -1 and keep unresolved indices in indexStack.
     *   2. Scan numbers from left to right.
     *   3. While currentElement is greater than the value at indexStack.peek(), pop and record it.
     *   4. Push currentIndex for a future greater value to resolve.
     *
     * Time:  O(n) - each index is pushed once and popped at most once.
     * Space: O(n) - result and indexStack store up to n entries.
     *
     * @param numbers list of integers
     * @return list of next greater values, or -1 where none exists
     */

    public static List<Integer> findNextGreaterElements(List<Integer> numbers) {
        int n = numbers.size();
        Integer[] result = new Integer[n];
        Arrays.fill(result, -1); // Default to -1

        Stack<Integer> indexStack = new Stack<>(); // Stack to store indices

        for (int currentIndex = 0; currentIndex < n; currentIndex++) {
            int currentElement = numbers.get(currentIndex);

            // Resolve NGE for all elements in the stack smaller than currentElement
            while (!indexStack.isEmpty() && numbers.get(indexStack.peek()) < currentElement) {
                int prevIndex = indexStack.pop();
                result[prevIndex] = currentElement;
            }

            // Push currentElement index to stack to resolve in future
            indexStack.push(currentIndex);
        }
        // the NGE for the remaining elements in the stack is -1

        return Arrays.asList(result);
    }
}
