package linkedlist;

import java.util.*;
import java.util.LinkedList;


/**
 * Problem: Flatten Nested List Iterator
 *
 * Implement an iterator over a nested list where each item is either an integer
 * or another nested list. Repeated next() calls must return all integers in
 * left-to-right flattened order.
 *
 * Leetcode: https://leetcode.com/problems/flatten-nested-list-iterator/ (Medium)
 * Rating:   Not available (not a contest problem)
 * Pattern:  Stack | Recursion | Iterator design
 *
 * Example:
 *   Input:  nestedList = [[1,1],2,[1,1]]
 *   Output: [1,1,2,1,1]
 *   Why:    nested lists expand in place while preserving left-to-right order.
 *
 * Follow-ups:
 *   1. Can hasNext() be lazy?
 *      Keep a stack and expand only the list currently on top.
 *   2. What if nesting is very deep?
 *      Prefer the stack iterator to avoid recursive stack overflow.
 *   3. Can remove() be supported?
 *      Store parent positions so returned elements can be deleted safely.
 *
 * Related: Mini Parser (385), Nested List Weight Sum (339).
 */
public class FlattenNestedListIterator {

    public static void main(String[] args) {
        class SimpleNestedInteger implements NestedInteger {
            private final Integer value;
            private final List<NestedInteger> list;
            SimpleNestedInteger(int value) { this.value = value; this.list = null; }
            SimpleNestedInteger(List<NestedInteger> list) { this.value = null; this.list = list; }
            public boolean isInteger() { return value != null; }
            public Integer getInteger() { return value; }
            public List<NestedInteger> getList() { return list == null ? Collections.emptyList() : list; }
        }
        List<NestedInteger> nestedList = Arrays.asList(
            new SimpleNestedInteger(Arrays.asList(new SimpleNestedInteger(1), new SimpleNestedInteger(1))),
            new SimpleNestedInteger(2),
            new SimpleNestedInteger(Arrays.asList(new SimpleNestedInteger(1), new SimpleNestedInteger(1))));
        NestedIterator iterator = new NestedIterator(nestedList);
        List<Integer> values = new ArrayList<>();
        while (iterator.hasNext()) values.add(iterator.next());
        int[] output = values.stream().mapToInt(Integer::intValue).toArray();
        int[] expected = {1, 1, 2, 1, 1};
        System.out.printf("nestedList=%s -> %s  expected=%s%n", "[[1,1],2,[1,1]]", Arrays.toString(output), Arrays.toString(expected));
        NestedIterator emptyIterator = new NestedIterator(Collections.emptyList());
        int[] emptyOutput = {};
        while (emptyIterator.hasNext()) emptyIterator.next();
        int[] emptyExpected = {};
        System.out.printf("nestedList=%s -> %s  expected=%s%n", "[]", Arrays.toString(emptyOutput), Arrays.toString(emptyExpected));
    }

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
         * Intuition: next() is simplest when all integers are queued up front.
         * The constructor recursively walks the nested structure once and stores
         * integers in the exact order the iterator should return them.
         *
         * Algorithm:
         *   1. Create flattenedList as an empty queue.
         *   2. Recursively scan nestedList from left to right.
         *   3. Offer integers directly and recurse into lists.
         *   4. next() polls the queue, and hasNext() checks whether it is empty.
         *
         * Time:  O(n) - construction visits every nested item once.
         * Space: O(n) - all flattened integers are stored in the queue.
         *
         * @param nestedList nested integer structure to flatten
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

        /** Recursively appends integers from nestedList into flattenedList. */
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