package StackQueue;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Problem: Find the Next Greater Element (NGE) for each element in an array.
 * 
 * Given an array, find the next greater element for each element. The next 
 * greater element for an element x is the first greater element on the right.
 * If no greater element exists, return -1 for that element.
 * 
 * Example:
 * Input: [11, 3, 21, 30]
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
 * 
 * LeetCode Link: https://leetcode.com/problems/next-greater-element-ii/
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
     * Finds the next greater element for each number in the list.
     *
     * @param numbers List of integers.
     * @return List of next greater elements.
     */
    public static List<Integer> findNextGreaterElements(List<Integer> numbers) {
        int n = numbers.size();
        Stack<Integer> stack = new Stack<>();
        List<Integer> result = Arrays.asList(new Integer[n]); // Initialize result list

        // Initialize all elements with -1 (default value)
        Arrays.fill(result.toArray(), -1);

        // Traverse from right to left
        for (int i = n - 1; i >= 0; i--) {
            int currentElement = numbers.get(i);

            // Remove elements from stack that are <= current element
            while (!stack.isEmpty() && stack.peek() <= currentElement) {
                stack.pop();
            }

            // If stack is not empty, top of stack is next greater element
            if (!stack.isEmpty()) {
                result.set(i, stack.peek());
            }

            // Push current element onto stack
            stack.push(currentElement);
        }

        return result;
    }
}
