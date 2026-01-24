package dynamicprogramming.knapsackunbounded;

import java.util.LinkedList;
import java.util.Queue;


/**
 * Design a class that counts the number of recent calls within the last 3000 milliseconds.
 * Each call to `ping(int t)` records a timestamp and returns the number of calls that have
 * happened in the time window [t - 3000, t].
 *
 * Constraint : Each `ping` is called with strictly increasing timestamp values.
 * Example:
 * Input:
 *    ping(1)     -> returns 1
 *    ping(300)   -> returns 2
 *    ping(3000)  -> returns 3
 *    ping(3002)  -> returns 3
 *    ping(7000)  -> returns 1
 *
 *
 * LeetCode Link:
 * https://leetcode.com/problems/number-of-recent-calls/
 *
 * Follow-up Questions (FAANG-relevant):
 * 1. What if the time window changes frequently?
 *    → Allow dynamic window length as a constructor param or in ping().
 *
 * 2. Can you optimize for high-frequency calls (10^6 per second)?
 *    → Use a ring buffer or sliding window with fixed-size arrays instead of a queue.
 *
 * 3. Can this be extended for multiple clients/IDs?
 *    → Use a `Map<ClientId, Queue<Integer>>` to track timestamps per client.
 * LeetCode Contest Rating: 1338
 **/
public class CallCounter {

  private final Queue<Integer> callTimestamps;

  public CallCounter() {
    this.callTimestamps = new LinkedList<>();
  }

  /**
   * Design:
   * - Uses a queue (`Queue<Integer> callTimestamps`) to store timestamps of recent calls.
   * - The `ping(int time)` method receives the current timestamp, adds it to the queue,
   *   and removes outdated timestamps (older than `time - 3000`).
   * - Returns the count of valid timestamps within the last 3000 milliseconds.
   *
   * - Time Complexity: O(N) in the worst case (removing old timestamps), but amortized O(1).
   * - Space Complexity: O(N), where N is the number of calls stored in the queue.
   *
   * @param time The timestamp of the new call (milliseconds, strictly increasing).
   * @return The number of calls made in the last 3 seconds.
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

  /**
   * Main method to demonstrate functionality.
   */
  public static void main(String[] args) {
    CallCounter callCounter = new CallCounter();
    System.out.println(callCounter.ping(1));    // Output: 1
    System.out.println(callCounter.ping(300));  // Output: 2
    System.out.println(callCounter.ping(3000)); // Output: 3
    System.out.println(callCounter.ping(3002)); // Output: 3
    System.out.println(callCounter.ping(7000)); // Output: 1
  }
}