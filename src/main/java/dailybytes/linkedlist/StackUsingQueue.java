package dailybytes.linkedlist;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class implements a stack using a single queue.
 *
 * Algorithm:
 * - Use a single queue to simulate the stack operations.
 * - Time Complexity: O(n) for pop and O(1) for push and peek operations.
 * - Space Complexity: O(n)
 *
 * LeetCode Problem Link:
 * https://leetcode.com/problems/implement-stack-using-queues/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class StackUsingQueue {
    private Queue<Integer> queue;

    public StackUsingQueue() {
        this.queue = new LinkedList<>();
    }

    public static void main(String[] args) {
        StackUsingQueue myStack = new StackUsingQueue();
        myStack.push(1);
        myStack.push(2);
        myStack.push(3);
        myStack.push(4);
        myStack.push(5);
        System.out.println("Popped: " + myStack.pop());
        System.out.println("Popped: " + myStack.pop());
        System.out.println("Popped: " + myStack.pop());
        System.out.println("Popped: " + myStack.pop());
        System.out.println("Popped: " + myStack.pop());
    }

    /**
     * Pushes an element onto the stack.
     * 
     * @param element The element to be pushed.
     */
    public void push(int element) {
        queue.add(element);
    }

    /**
     * Pops an element from the stack.
     * 
     * @return The popped element.
     * @throws RuntimeException if the stack is empty.
     */
    public int pop() {
        if (queue.isEmpty()) {
            throw new RuntimeException("The stack is empty");
        }

        int size = queue.size();
        while (size > 1) {
            queue.add(queue.poll());
            size--;
        }
        return queue.poll();
    }

    /**
     * Peeks the top element of the stack.
     * 
     * @return The top element of the stack.
     */
    public int peek() {
        if (queue.isEmpty()) {
            throw new RuntimeException("The stack is empty");
        }

        int size = queue.size();
        int topElement = 0;
        while (size > 0) {
            topElement = queue.poll();
            queue.add(topElement);
            size--;
        }
        return topElement;
    }
}
