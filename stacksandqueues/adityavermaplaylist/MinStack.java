package stacksandqueues.adityavermaplaylist;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * MinStack Implementation
 * -----------------------
 * This class implements a stack that supports the following operations in **O(1) time**:
 * - push(x): Pushes an element onto the stack.
 * - pop(): Removes the top element from the stack.
 * - getMin(): Retrieves the minimum element in the stack.
 *
 * The core idea is to use an **additional stack (`minStack`)** to keep track of
 * the minimum element at each stage.
 *
 * **Algorithm & Complexity:**
 * - **push(x):** Push to the main stack and update minStack if necessary. **O(1)**
 * - **pop():** Remove from both stacks if the popped element was the min. **O(1)**
 * - **getMin():** Returns the top of `minStack`, which always has the minimum value. **O(1)**
 *
 * **Edge Cases Considered:**
 * - Calling `pop()` or `getMin()` on an empty stack throws `EmptyStackException`.
 * - Handling duplicate minimum values correctly.
 *
 * **LeetCode Link:**
 * https://leetcode.com/problems/min-stack/
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
     * Pushes an element onto the stack.
     * If the new element is <= current minimum, push it onto minStack as well.
     *
     * @param value The element to push.
     */
    public void push(int value) {
        stack.push(value);
        if (minStack.isEmpty() || value <= minStack.peek()) {
            minStack.push(value);
        }
    }

    /**
     * Removes and returns the top element from the stack.
     * Ensures minStack remains synchronized.
     *
     * @return The popped element.
     * @throws EmptyStackException if the stack is empty.
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
     * Retrieves the minimum element in the stack.
     *
     * @return The minimum element.
     * @throws EmptyStackException if the stack is empty.
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
        System.out.println("Min: " + minStack.getMin()); // 3

        minStack.push(2);
        minStack.push(1);
        System.out.println("Min: " + minStack.getMin()); // 1

        minStack.pop();
        System.out.println("Min: " + minStack.getMin()); // 2

        minStack.pop();
        System.out.println("Min: " + minStack.getMin()); // 3
    }
}

