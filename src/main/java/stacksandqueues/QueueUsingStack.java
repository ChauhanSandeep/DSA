package stacksandqueues;

import java.util.Stack;
import java.util.Arrays;


/**
 * Problem: Implement Queue Using Stacks
 *
 * Implement a FIFO queue using only stack operations. `push` adds to the back,
 * while `pop` and `peek` must return the oldest still-present element. This
 * implementation returns -1 for pop/peek on an empty queue.
 *
 * Leetcode: https://leetcode.com/problems/implement-queue-using-stacks/ (Easy)
 * Rating:   acceptance 70.1% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Stack | Amortized analysis | Two-stack queue
 *
 * Example:
 *   Input:  push(1), push(2), peek(), pop(), empty()
 *   Output: 1, 1, false
 *   Why:    peek and pop both return 1 because moving to the output stack reverses [1,2] into FIFO order.
 *
 * Follow-ups:
 *   1. Can every operation be O(1) worst-case with only two ordinary stacks?
 *      Not with the simple transfer model; use incremental rebuilding for real-time queues.
 *   2. Implement the queue with one stack?
 *      Use recursion to reach the bottom element, then rebuild while unwinding.
 *   3. Make it thread-safe?
 *      Guard both stacks with one lock or use a concurrent queue instead.
 *   4. Support `size()` in O(1)?
 *      Maintain a counter updated on successful push/pop.
 *
 * Related: Implement Stack using Queues (225), Design Circular Queue (622).
 */
public class QueueUsingStack {
  public static void main(String[] args) {
    QueueUsingStacks emptyQueue = new QueueUsingStacks();
    Object[] emptyOutputs = {emptyQueue.pop(), emptyQueue.peek(), emptyQueue.empty()};
    System.out.printf("operations=pop,peek,empty -> %s  expected=[-1, -1, true]%n", Arrays.toString(emptyOutputs));
    QueueUsingStacks queue = new QueueUsingStacks();
    queue.push(2); queue.push(3); int firstPop = queue.pop(); queue.push(10);
    int firstPeek = queue.peek(); int secondPop = queue.pop(); int thirdPop = queue.pop(); int fourthPop = queue.pop();
    int[] outputs = {firstPop, firstPeek, secondPop, thirdPop, fourthPop};
    System.out.printf("operations=push2,push3,pop,push10,peek,pop,pop,pop -> %s  expected=[2, 3, 3, 10, -1]%n", Arrays.toString(outputs));
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
