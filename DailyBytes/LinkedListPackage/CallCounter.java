package DailyBytes.LinkedListPackage;

import java.util.TreeSet;

/**
 * This class tracks the number of calls a client has made within the last 3 seconds.
 * 
 * Algorithm:
 * - Use a TreeSet to store the timestamps of calls.
 * - Use the tailSet method to retrieve and count the number of calls made within the last 3 seconds.
 * - Time Complexity: O(log n) for insertion and O(log n) for the tailSet operation.
 * - Space Complexity: O(n)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/number-of-recent-calls/
 */
public class CallCounter {

    private static TreeSet<Integer> timestamps = new TreeSet<>();

    public static void main(String[] args) {
        CallCounter counter = new CallCounter();
        System.out.println("Number of calls within last 3 seconds: " + counter.ping(1));
        System.out.println("Number of calls within last 3 seconds: " + counter.ping(300));
        System.out.println("Number of calls within last 3 seconds: " + counter.pping(3000));
        System.out.println("Number of calls within last 3 seconds: " + counter.ping(3002));
        System.out.println("Number of calls within last 3 seconds: " + counter.ping(7000));
    }

    /**
     * Tracks the number of calls made within the last 3 seconds.
     * @param time The current timestamp (in milliseconds) of a new call being made.
     * @return The number of calls made within the last 3 seconds.
     */
    public int ping(int time) {
        timestamps.add(time);
        return timestamps.tailSet(time - 3000).size();
    }
}
