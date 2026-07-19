package design;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Arrays;

/**
 * Problem: Logger Rate Limiter
 *
 * Design a logger that decides whether a message should be printed at a timestamp.
 * The same message can be printed only if at least 10 seconds have passed since its
 * previous printed occurrence.
 *
 * Leetcode: https://leetcode.com/problems/logger-rate-limiter/ (Easy)
 * Rating:   not available (design problem)
 * Pattern:  Design | Hash map | Timestamp cache
 *
 * Example:
 *   Input:  shouldPrintMessage(1,"foo"), shouldPrintMessage(3,"foo"), shouldPrintMessage(11,"foo")
 *   Output: [true, false, true]
 *   Why:    timestamp 3 is too soon after 1, while timestamp 11 is exactly 10 seconds later.
 *
 * Follow-ups:
 *   1. How would you clean old messages to save memory?
 *      Keep a queue of printed events and evict entries older than 10 seconds.
 *   2. How would you support per-message rate limits?
 *      Store each message's configured cooldown alongside its last printed time.
 *   3. How would you make it thread-safe?
 *      Synchronize the check-and-update or use atomic map operations.
 *
 * Related: Design Hit Counter (362), Moving Average from Data Stream (346).
 */
class Logger {

    public static void main(String[] args) {
        Logger logger = new Logger();
        boolean[] got = {
                logger.shouldPrintMessage(1, "foo"),
                logger.shouldPrintMessage(2, "bar"),
                logger.shouldPrintMessage(3, "foo"),
                logger.shouldPrintMessage(8, "bar"),
                logger.shouldPrintMessage(10, "foo"),
                logger.shouldPrintMessage(11, "foo")
        };
        boolean[] expected = {true, true, false, false, false, true};
        System.out.printf("requests=logger-rate-limiter -> %s  expected=%s%n",
                Arrays.toString(got), Arrays.toString(expected));
    }


    Map<String, Integer> map;
    /**
     * Creates an empty limiter with no previously printed messages.
     *
     * Time:  O(1) - initializes the timestamp map.
     * Space: O(1) - no messages are stored yet.
     */
    public Logger() {
        map = new TreeMap<>();
    }
    
    /**
     * Returns whether the message can be printed at the given timestamp.
     *
     * Time:  O(log n) - TreeMap lookup and update for one message key.
     * Space: O(1) - each accepted new message may add one stored timestamp.
     *
     * @param timestamp current timestamp in seconds
     * @param message message text to rate limit
     * @return true when the message should be printed now
     */
    public boolean shouldPrintMessage(int timestamp, String message) {
        if(!map.containsKey(message)) {
            map.put(message, timestamp);
            return true;
        }else{
            int prevTimestamp = map.get(message);
            if(prevTimestamp + 10 > timestamp) {
                return false;
            }else{
                map.put(message, timestamp);
                return true;
            }
        }
        
    }
}