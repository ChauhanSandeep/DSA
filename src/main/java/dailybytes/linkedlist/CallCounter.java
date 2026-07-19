package dailybytes.linkedlist;
import java.util.*;

/**
 * Problem: Number of Recent Calls
 *
 * Track how many ping calls occurred in the last 3000 milliseconds, including
 * the current call. Timestamps are strictly increasing, so every new time can be
 * inserted and the recent suffix can be counted.
 *
 * Leetcode: https://leetcode.com/problems/number-of-recent-calls/ (Easy)
 * Rating:   1338 (zerotrac Elo)
 * Pattern:  Ordered set | Sliding time window | Recent suffix query
 *
 * Example:
 *   Input:  times = [1, 300, 3000, 3002]
 *   Output: [1, 2, 3, 3]
 *   Why:    at time 3002, only calls with time at least 2 are within the last
 *           3000 milliseconds, so 300, 3000, and 3002 are counted.
 *
 * Follow-ups:
 *   1. Support duplicate timestamps?
 *      Use a queue or multiset instead of TreeSet because TreeSet keeps unique values.
 *   2. Change the window size per query?
 *      Use an ordered structure that can count values in [time - window, time].
 *   3. Bound memory for an endless stream?
 *      Remove timestamps older than the largest possible window as time advances.
 *   4. Make ping thread-safe?
 *      Synchronize access to the shared timestamp structure or use a concurrent design.
 *
 * Related: Moving Average from Data Stream (346), Hit Counter (362).
 */
public class CallCounter {
    public static void main(String[] args) {
        int[] inputs = { 1, 300, 3000, 3002, 7000 };
        int[] expected = { 1, 2, 3, 3, 1 };

        set.clear();
        for (int i = 0; i < inputs.length; i++) {
            int output = ping(inputs[i]);
            System.out.printf("time=%d -> %d  expected=%d%n", inputs[i], output, expected[i]);
        }
    }
    static TreeSet<Integer> set = new TreeSet<>();

    /**
     * Intuition: a recent call is any timestamp in the suffix starting at
     * time - 3000. Since set is ordered, tailSet can expose exactly that suffix
     * after the current time is inserted.
     *
     * Algorithm:
     *   1. Add the current time to set.
     *   2. Ask set for all timestamps greater than or equal to time - 3000.
     *   3. Return the size of that recent suffix.
     *
     * Time:  O(log n + r) - insertion is logarithmic and sizing the suffix depends on the view.
     * Space: O(n) - all ping timestamps remain stored in set.
     *
     * @param time current call timestamp in milliseconds
     * @return number of calls within the inclusive last-3000-millisecond window
     */
    public static int ping(int time) {
        set.add(time);
        return set.tailSet(time - 3000).size();
    }
}
