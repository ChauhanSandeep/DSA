package DynamicProgramming.KnapsackUnbounded;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: Recent Calls Counter (Similar to LeetCode 933 - Number of Recent Calls)
 *
 * This class tracks the number of calls made within the last 3000 milliseconds (3 seconds).
 *
 * Design:
 * - Uses a queue (`Queue<Integer> callTimestamps`) to store timestamps of recent calls.
 * - The `ping(int time)` method receives the current timestamp, adds it to the queue,
 *   and removes outdated timestamps (older than `time - 3000`).
 * - Returns the count of valid timestamps within the last 3000 milliseconds.
 *
 * Constraints:
 * - Time is strictly increasing with each `ping` call.
 *
 * Complexity:
 * - Time Complexity: O(N) in the worst case (removing old timestamps), but amortized O(1).
 * - Space Complexity: O(N), where N is the number of calls stored in the queue.
 *
 * LeetCode Link: https://leetcode.com/problems/number-of-recent-calls/
 */
public class CallCounter {

    private final Queue<Integer> callTimestamps;

    /**
     * Constructor to initialize the CallCounter.
     */
    public CallCounter() {
        this.callTimestamps = new LinkedList<>();
    }

    /**
     * Records a new call at the given timestamp and returns the number of calls
     * made within the last 3 seconds (3000 milliseconds).
     *
     * @param time The timestamp of the new call (milliseconds, strictly increasing).
     * @return The number of calls made in the last 3 seconds.
     */
    public int ping(int time) {
        // Add the current timestamp
        callTimestamps.offer(time);

        // Remove timestamps older than 3000 milliseconds
        while (!callTimestamps.isEmpty() && callTimestamps.peek() < time - 3000) {
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