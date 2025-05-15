package StackQueue;

import java.util.Stack;

public class QueueUsingStack {
    public static void main(String[] args) {
        QueueUsingStacks queue = new QueueUsingStacks();
        queue.push(2);
        queue.push(3);
        System.out.println(queue.pop()); // Expected: 2
        queue.push(10);
        System.out.println(queue.pop()); // Expected: 3
    }
}

/**
 * Implements a Queue using two stacks (FIFO order).
 * 
 * Approach:
 * - Push elements onto `inputStack`.
 * - For `pop()`, transfer elements to `outputStack` only when it's empty, ensuring correct order.
 * 
 * Time Complexity:
 * - Push: O(1) (amortized)
 * - Pop: O(1) (amortized) due to lazy transfer
 * - Worst-case Pop: O(N) when transferring elements
 * 
 * Space Complexity: O(N), where N is the number of elements stored.
 * 
 * LeetCode Problem: https://leetcode.com/problems/implement-queue-using-stacks/
 */
class QueueUsingStacks {
    private Stack<Integer> inputStack = new Stack<>();  // For enqueue operation
    private Stack<Integer> outputStack = new Stack<>(); // For dequeue operation

    /**
     * Pushes an element into the queue.
     */
    public void push(int x) {
        inputStack.push(x);
    }

    /**
     * Removes and returns the front element of the queue.
     * Returns -1 if the queue is empty.
     */
    public int pop() {
        if (outputStack.isEmpty()) {
            if (inputStack.isEmpty()) {
                return -1; // Queue is empty
            }
            // Transfer elements from inputStack to outputStack for correct order
            while (!inputStack.isEmpty()) {
                outputStack.push(inputStack.pop());
            }
        }
        return outputStack.pop();
    }
}
