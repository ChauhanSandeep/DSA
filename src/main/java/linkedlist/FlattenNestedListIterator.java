package linkedlist;

import java.util.*;
import java.util.LinkedList;


/**
 * Problem: Flatten Nested List Iterator
 *
 * You are given a nested list of integers nestedList. Each element is either an integer or a list
 * whose elements may also be integers or other lists. Implement an iterator to flatten it.
 * Implement the NestedIterator class with next() and hasNext() methods.
 *
 * Example:
 * Input: nestedList = [[1,1],2,[1,1]]
 * Output: [1,1,2,1,1]
 * Explanation: By calling next repeatedly until hasNext returns false.
 *
 * LeetCode: https://leetcode.com/problems/flatten-nested-list-iterator
 *
 * Follow-up Questions:
 * 1. What if the nested structure is very deep? Would recursion cause stack overflow?
 *    Answer: Yes, we can use iterative approach with stack to avoid deep recursion.
 *
 * 2. How would you handle infinite nested structures?
 *    Answer: Add cycle detection using visited set or limit recursion depth.
 *
 * 3. Can we implement lazy evaluation to save memory?
 *    Answer: Yes, the stack-based approach only processes elements as needed.
 *    Related: https://leetcode.com/problems/mini-parser/
 *
 * @author Sandeep
 */
public class FlattenNestedListIterator {

    // This is the interface that allows for creating nested lists
    public interface NestedInteger {
        // @return true if this NestedInteger holds a single integer, rather than a nested list
        public boolean isInteger();

        // @return the single integer that this NestedInteger holds, if it holds a single integer
        // Return null if this NestedInteger holds a nested list
        public Integer getInteger();

        // @return the nested list that this NestedInteger holds, if it holds a nested list
        // Return empty list if this NestedInteger holds a single integer
        public List<NestedInteger> getList();
    }

    /**
     * Approach 1: Flatten during construction using recursion.
     * Good enough Naive Approach.
     * Pre-processes all elements and stores them in a queue.
     *
     * Algorithm
     * 1. Initialize a queue to store flattened integers.
     * 2. Iterate through the nested list and flatten it.
     * 3. Add each integer to the queue.
     * 4. Return the queue.
     *
     * Time Complexity: O(n) for constructor, and O(1) for next() and hasNext()
     * Space Complexity: O(n) for storing all flattened integers
     */
    public static class NestedIterator {
        private Queue<Integer> flattenedList;

        /**
         * Initialize iterator with the nested list.
         * Flattens the entire structure during construction.
         *
         * @param nestedList The nested list of integers
         */
        public NestedIterator(List<NestedInteger> nestedList) {
            this.flattenedList = new LinkedList<>();
            flattenList(nestedList);
        }

        /**
         * Returns the next integer in the flattened list.
         *
         * @return Next integer
         */
        public Integer next() {
            return flattenedList.poll();
        }

        /**
         * Checks if there are more integers to iterate.
         *
         * @return true if there are more integers
         */
        public boolean hasNext() {
            return !flattenedList.isEmpty();
        }

        // Recursively flattens the nested list
        private void flattenList(List<NestedInteger> nestedList) {
            for (NestedInteger element : nestedList) {
                if (element.isInteger()) {
                    flattenedList.offer(element.getInteger());
                } else {
                    flattenList(element.getList());
                }
            }
        }
    }

    /**
     * Approach 2: Lazy evaluation using stack (more memory efficient).
     * Only processes elements as needed, avoiding unnecessary memory usage.
     *
     * Algorithm:
     * 1. Use a stack to store elements of the nested list.
     * 2. Add elements in reverse order to maintain correct sequence.
     * 3. In hasNext(), flatten the top of the stack if it's a list.
     * 4. In next(), return the top of the stack.
     *
     * Time Complexity: O(1) amortized for next() and hasNext()
     * Space Complexity: O(d) where d is the maximum depth of nesting
     */
    public static class NestedIteratorLazy {
        private Stack<NestedInteger> stack;

        /**
         * Initialize iterator with lazy evaluation approach.
         *
         * @param nestedList The nested list of integers
         */
        public NestedIteratorLazy(List<NestedInteger> nestedList) {
            this.stack = new Stack<>();

            // Add elements in reverse order to maintain correct sequence
            for (int i = nestedList.size() - 1; i >= 0; i--) {
                stack.push(nestedList.get(i));
            }
        }

        /**
         * Returns the next integer in the nested list.
         * Assumes hasNext() has been called and returned true.
         *
         * @return Next integer
         */
        public Integer next() {
            // hasNext() ensures top element is an integer
            return stack.pop().getInteger();
        }

        /**
         * Checks if there are more integers and flattens as needed.
         *
         * @return true if there are more integers
         */
        public boolean hasNext() {
            while (!stack.isEmpty()) {
                NestedInteger current = stack.peek();
                if (current.isInteger()) {
                    return true;
                }

                // Current element is a list, flatten it
                stack.pop();
                List<NestedInteger> currentList = current.getList();

                // Add elements in reverse order to maintain sequence
                for (int i = currentList.size() - 1; i >= 0; i--) {
                    stack.push(currentList.get(i));
                }
            }

            return false;
        }
    }
}