package dailybytes.linkedlist;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;

/**
 * Problem: Implement Stack Using Queues
 *
 * Implement stack behavior with a single queue. Push enqueues normally, while
 * pop rotates all earlier elements behind the newest element so the last pushed
 * value can be removed first.
 *
 * Leetcode: https://leetcode.com/problems/implement-stack-using-queues/ (Easy)
 * Rating:   acceptance 70.5% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Queue | Stack simulation | Rotation
 *
 * Example:
 *   Input:  push [1,2,3], then pop twice
 *   Output: [3,2]
 *   Why:    the queue is rotated before each pop so the most recently pushed
 *           value reaches the front.
 *
 * Follow-ups:
 *   1. Make pop O(1) instead?
 *      Rotate the queue during push so the newest element is always at the front.
 *   2. Implement the same stack with two queues?
 *      Move all but one element to a helper queue during pop, then swap queues.
 *   3. Add an empty operation matching Leetcode's API?
 *      Return queue.isEmpty().
 *   4. Support generic element types?
 *      Parameterize the class and queue with T instead of Integer.
 *
 * Related: Implement Queue using Stacks (232), Min Stack (155).
 */
public class StackUsingQueue {
    private Queue<Integer> queue;

    public StackUsingQueue() {
        this.queue = new LinkedList<>();
    }

    public static void main(String[] args) {
        int[][] inputs = { {1, 2, 3}, {42} };
        int[][] expected = { {3, 2, 1}, {42} };

        for (int i = 0; i < inputs.length; i++) {
            StackUsingQueue myStack = new StackUsingQueue();
            int[] output = new int[inputs[i].length];
            for (int value : inputs[i]) {
                myStack.push(value);
            }
            for (int j = 0; j < output.length; j++) {
                output[j] = myStack.pop();
            }
            System.out.printf("push=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(output), Arrays.toString(expected[i]));
        }
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
     * Intuition: the newest pushed value is at the back of the queue, but a stack
     * pops from the top. Rotate the older values to the back until only the newest
     * value remains at the front, then poll it.
     *
     * Algorithm:
     *   1. Throw if the queue is empty.
     *   2. Record the current queue size.
     *   3. Move size - 1 front elements to the back.
     *   4. Poll and return the remaining front element.
     *
     * Time:  O(n) - pop may rotate every queued element except the top.
     * Space: O(1) - rotation reuses the existing queue.
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
     * Intuition: the top stack value is the last queue element. Rotate every
     * element once while remembering the last value seen; the queue returns to its
     * original order and topElement holds the stack top.
     *
     * Algorithm:
     *   1. Throw if the queue is empty.
     *   2. Rotate each queued element from front to back exactly once.
     *   3. Keep assigning topElement to the value just polled.
     *   4. Return topElement after the full rotation.
     *
     * Time:  O(n) - every queued element is moved once.
     * Space: O(1) - only topElement and size are stored.
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
