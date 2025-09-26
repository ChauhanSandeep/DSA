package stacksandqueues.adityavermaplaylist;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;


/**
 * Problem: Find the Next Greater Element (NGE) for each element in an array.
 *
 * Given an array, find the next greater element for each element. The next
 * greater element for an element x is the first greater element on the right.
 * If no greater element exists, return -1 for that element.
 *
 * LeetCode Link: https://leetcode.com/problems/next-greater-element-i/
 *
 * Example:
 * Input:   [11, 3, 21, 30]
 * Output: [21, 21, 30, -1]
 *
 * Approach:
 * - Iterate from right to left using a **monotonic decreasing stack**.
 * - For each element, pop from the stack until we find a greater element.
 * - If the stack is empty, there is no greater element → store -1.
 * - Push the current element to stack for future comparisons.
 *
 * Time Complexity: **O(N)**
 * Space Complexity: **O(N)** (for stack & result storage)
 */
public class NextGreaterElement {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(11, 3, 21, 30);
        List<Integer> nextGreaterElements = findNextGreaterElements(numbers);

        // Printing results
        for (int i = 0; i < numbers.size(); i++) {
            System.out.println(numbers.get(i) + " --> " + nextGreaterElements.get(i));
        }
    }

    /**
     * Finds the next greater element for each number using left-to-right traversal.
     *
     * @param numbers List of integers.
     * @return List of next greater elements.
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
