package stacksandqueues;

import java.util.Stack;


/**
 * Leetcode Problem: https://leetcode.com/problems/implement-queue-using-stacks/
 *
 * Problem Statement:
 * Implement a first-in-first-out (FIFO) queue using only two stacks.
 * The implemented queue should support all the standard operations of a queue:
 * `push(x)`, `pop()`, `peek()`, and `empty()`.
 *
 * Example:
 * Input:
 * QueueUsingStacks q = new QueueUsingStacks();
 * q.push(1);
 * q.push(2);
 * q.pop();   // returns 1
 * q.empty(); // returns false
 *
 * Follow-up Questions:
 * 1. Can we optimize to have both push and pop in O(1) worst case?
 *    → No. If you want to keep operations purely stack-based, one of the operations will take O(n) in the worst case.
 * 2. Can we do this using a single stack?
 *    → It's also possible to implement a queue using just **one stack** by leveraging **recursion**
 *    to simulate FIFO behavior.
 *
 *    ➤ Core Idea:
 *    - To perform `pop()` or `peek()`, make recursive call to pop elements until reaching the **bottom-most element**
 *      (which is the front of the queue).
 *    - Return that element and **rebuild the stack** while unwinding the recursion.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class QueueUsingStack {

  public static void main(String[] args) {
    QueueUsingStacks queue = new QueueUsingStacks();
    queue.push(2);
    queue.push(3);
    System.out.println(queue.pop()); // Expected: 2
    queue.push(10);
    System.out.println(queue.pop()); // Expected: 3
    System.out.println(queue.pop()); // Expected: 10
    System.out.println(queue.pop()); // Expected: -1 (Empty queue)
  }
}

/**
 * Implements a queue using two stacks (inputStack and outputStack).
 *
 * Steps:
 * - Use `inputStack` to store incoming elements (enqueue).
 * - Use `outputStack` to serve elements for dequeue.
 * - When popping, if `outputStack` has no elements, transfer all elements from `inputStack` to `outputStack`.
 *
 * Time Complexity:
 * - push(): O(1)
 * - pop(): Amortized O(1), worst-case O(n)
 * - peek(): Amortized O(1)
 * - empty(): O(1)
 *
 * Space Complexity: O(n), where n is the number of elements in the queue.
 */
class QueueUsingStacks {
  private final Stack<Integer> inputStack = new Stack<>(); // This stack is used for enqueue operations.
  private final Stack<Integer> outputStack = new Stack<>(); // This stack is used for dequeue operations.

  /**
   * Inserts an element to the end of the queue.
   *
   * @param value Element to insert.
   */
  public void push(int value) {
    inputStack.push(value);
  }

  /**
   * Removes and returns the element at the front of the queue.
   * Returns -1 if the queue is empty.
   *
   * @return Front element of the queue or -1 if empty.
   */
  public int pop() {
    shiftStacksIfOutputEmpty();
    return outputStack.isEmpty() ? -1 : outputStack.pop();
  }

  /**
   * Returns the front element without removing it.
   * Returns -1 if the queue is empty.
   *
   * @return Front element of the queue or -1 if empty.
   */
  public int peek() {
    shiftStacksIfOutputEmpty();
    return outputStack.isEmpty() ? -1 : outputStack.peek();
  }

  /**
   * Returns true if the queue is empty.
   *
   * @return True if queue is empty, else false.
   */
  public boolean empty() {
    return inputStack.isEmpty() && outputStack.isEmpty();
  }

  /**
   * Transfers elements from inputStack to outputStack if outputStack is empty.
   * Ensures the correct dequeue order (FIFO).
   */
  private void shiftStacksIfOutputEmpty() {
    if (outputStack.isEmpty()) {
      while (!inputStack.isEmpty()) {
        outputStack.push(inputStack.pop());
      }
    }
  }
}