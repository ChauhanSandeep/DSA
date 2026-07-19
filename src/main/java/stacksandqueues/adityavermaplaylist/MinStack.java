package stacksandqueues.adityavermaplaylist;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Problem: Min Stack
 *
 * Design a stack that supports push, pop, and retrieving the current minimum in
 * constant time. Duplicate minimum values must be handled so popping one copy
 * does not lose another copy that is still in the stack.
 *
 * Leetcode: https://leetcode.com/problems/min-stack/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Stack | Auxiliary minimum stack | Design
 *
 * Example:
 *   Input:  push 3, push 5, push 2, push 1, getMin, pop, getMin
 *   Output: [1,2]
 *   Why:    1 is the minimum before it is popped; then 2 is the smallest remaining value.
 *
 * Follow-ups:
 *   1. Can this be done with one stack?
 *      Store encoded differences from the current minimum or store value-min pairs.
 *   2. How would you support getMax too?
 *      Maintain a second auxiliary stack for maximum values.
 *   3. How do duplicate minimums stay correct?
 *      Push values <= current minimum to minStack so each duplicate has its own marker.
 *   4. What changes for a thread-safe stack?
 *      Synchronize all operations or guard both stacks with the same lock.
 *
 * Related: Max Stack (716), Design Browser History (1472).
 */

class MinStack {
    private Stack<Integer> stack;
    private Stack<Integer> minStack;

    // Constructor
    public MinStack() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }

        /**
     * Intuition: minStack mirrors only the moments when the minimum changes, with
     * duplicates included. If value is no larger than the current minimum, it
     * becomes the minimum marker for this stack depth.
     *
     * Algorithm:
     *   1. Push value onto the main stack.
     *   2. If minStack is empty or value <= minStack.peek(), push value to minStack.
     *   3. Leave minStack unchanged when value is larger than the current minimum.
     *
     * Time:  O(1) - each operation touches only stack tops.
     * Space: O(1) - one push adds at most one extra minimum marker.
     *
     * @param value element to push
     */

    public void push(int value) {
        stack.push(value);
        if (minStack.isEmpty() || value <= minStack.peek()) {
            minStack.push(value);
        }
    }

        /**
     * Intuition: when the popped value equals the current minimum marker, that
     * marker belongs to this stack level and must be removed too. Otherwise the
     * previous minimum is still valid for the remaining stack.
     *
     * Algorithm:
     *   1. Throw EmptyStackException if the main stack is empty.
     *   2. Pop the main stack.
     *   3. If the popped value equals minStack.peek(), pop minStack as well.
     *
     * Time:  O(1) - only the top of each stack is inspected or removed.
     * Space: O(1) - no extra data structure is allocated.
     *
     * @return popped top value
     */

    public int pop() {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        int poppedValue = stack.pop();
        if (poppedValue == minStack.peek()) {
            minStack.pop();
        }
        return poppedValue;
    }

        /**
     * Intuition: minStack's top is always the smallest value among all elements
     * still present, because every new minimum was pushed and every removed
     * minimum is popped in sync with the main stack.
     *
     * Algorithm:
     *   1. Throw EmptyStackException if minStack is empty.
     *   2. Return minStack.peek().
     *   3. Do not mutate either stack.
     *
     * Time:  O(1) - reads one stack top.
     * Space: O(1) - no extra storage is used.
     *
     * @return current minimum element
     */

    public int getMin() {
        if (minStack.isEmpty()) {
            throw new EmptyStackException();
        }
        return minStack.peek();
    }


    // Driver method to test the MinStack class
        public static void main(String[] args) {
        MinStack minStack = new MinStack();
        minStack.push(3);
        minStack.push(5);
        minStack.push(2);
        minStack.push(1);
        int firstMin = minStack.getMin();
        minStack.pop();
        int secondMin = minStack.getMin();
        minStack.pop();
        int thirdMin = minStack.getMin();

        int[] got = { firstMin, secondMin, thirdMin };
        int[] expected = { 1, 2, 3 };
        System.out.printf("ops=%s -> %s  expected=%s%n",
            "push(3),push(5),push(2),push(1),getMin,pop,getMin,pop,getMin",
            Arrays.toString(got), Arrays.toString(expected));
    }
}

