package dynamicprogramming.knapsackunbounded;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;


/**
 * Problem: Number of Recent Calls
 *
 * Design a counter that records ping timestamps and returns how many pings happened in the inclusive window [time - 3000, time]. Calls are strictly increasing, so expired calls leave from the front.
 *
 * Leetcode: https://leetcode.com/problems/number-of-recent-calls/ (Easy)
 * Rating:   contest Elo 1338
 * Pattern:  Queue | Sliding window | Online stream counting
 *
 * Example:
 *   Input:  ping times = [1, 300, 3000, 3002]
 *   Output: [1, 2, 3, 3]
 *   Why:    at time 3002, only 300, 3000, and 3002 remain in the last 3000 ms.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Moving Average from Data Stream (346), Logger Rate Limiter (359).
 */
public class CallCounter {

  private final Queue<Integer> callTimestamps;

  public CallCounter() {
    this.callTimestamps = new LinkedList<>();
  }

    /**
   * Intuition: callTimestamps stores exactly the pings that may still be inside the latest 3000 ms window. Since times increase, expired values are always at the front; after adding time and polling old fronts, the queue size is the answer.
   *
   * Algorithm:
   *   1. Append time to callTimestamps.
   *   2. Compute maxAllowedTimestamp = time - 3000.
   *   3. Poll front timestamps while they are older than maxAllowedTimestamp.
   *   4. Return the remaining queue size.
   *
   * Time:  O(1) amortized - each timestamp is inserted and removed once.
   * Space: O(w) - stores calls in the active window.
   *
   * @param time timestamp of the new ping
   * @return number of pings in [time - 3000, time]
   */
public int ping(int time) {
    // Add the current timestamp
    callTimestamps.offer(time);

    int maxAllowedTimestamp = time - 3000;

    // Remove timestamps older than 3000 milliseconds
    while (!callTimestamps.isEmpty() && callTimestamps.peek() < maxAllowedTimestamp) {
      callTimestamps.poll();
    }

    // Return the count of valid timestamps
    return callTimestamps.size();
  }

    public static void main(String[] args) {
    int[][] inputs = { {1, 300, 3000, 3002}, {7000} };
    int[][] expected = { {1, 2, 3, 3}, {1} };
    for (int i = 0; i < inputs.length; i++) {
      CallCounter callCounter = new CallCounter();
      int[] got = new int[inputs[i].length];
      for (int j = 0; j < inputs[i].length; j++) got[j] = callCounter.ping(inputs[i][j]);
      System.out.printf("times=%s -> %s  expected=%s%n", Arrays.toString(inputs[i]), Arrays.toString(got), Arrays.toString(expected[i]));
    }
  }
}