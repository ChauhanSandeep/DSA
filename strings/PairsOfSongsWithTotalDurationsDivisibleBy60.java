package strings;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 1010. Pairs of Songs With Total Durations Divisible by 60
 *
 * You are given a list of songs where the ith song has a duration of time[i] seconds.
 * Return the number of pairs of songs for which their total duration in seconds is divisible by 60.
 * Formally, we want the number of indices i, j such that i < j with (time[i] + time[j]) % 60 == 0.
 *
 * Example 1:
 * Input: time = [30,20,150,100,40]
 * Output: 3
 * Explanation: Three pairs have a total duration divisible by 60: (30, 150), (20, 100), (20, 40)
 *
 * Example 2:
 * Input: time = [60,60,60]
 * Output: 3
 * Explanation: All three pairs have a total duration of 120, which is divisible by 60.
 *
 * LeetCode Link: https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/
 *
 * Follow-up Questions:
 * - How would you modify for divisible by any number k? (Replace 60 with k throughout algorithm)
 * - Can you optimize for very large arrays with limited time ranges? (Use array instead of HashMap)
 * - How would you find actual pairs instead of just count? (Store indices along with remainders)
 * - What if we need triplets instead of pairs? (Extend to three nested loops or more complex logic)
 */
public class PairsOfSongsWithTotalDurationsDivisibleBy60 {

    /**
     * Counts pairs of songs with total duration divisible by 60.
     *
     * Algorithm:
     * 1. For each song, calculate remainder when divided by 60
     * 2. For remainder r, we need to find songs with remainder (60-r) or 0 if r=0
     * 3. Use HashMap to count frequency of each remainder
     * 4. For each song, add count of complementary remainders seen so far
     * 5. Update remainder count after processing each song
     *
     * Time Complexity: O(n) where n is number of songs
     * Space Complexity: O(1) - at most 60 different remainders
     *
     * @param time Array of song durations in seconds
     * @return Number of pairs with total duration divisible by 60
     */
    public int numPairsDivisibleBy60(int[] time) {
        if (time == null || time.length < 2) {
            return 0;
        }

        Map<Integer, Integer> remainderCount = new HashMap<>();
        int pairCount = 0;

        for (int duration : time) {
            int remainder = duration % 60;
            int complement = (60 - remainder) % 60;

            // Add count of songs that can pair with current song
            pairCount += remainderCount.getOrDefault(complement, 0);

            // Update count of current remainder
            remainderCount.put(remainder, remainderCount.getOrDefault(remainder, 0) + 1);
        }

        return pairCount;
    }

    /**
     * Optimized approach using array instead of HashMap for better performance.
     */
    public int numPairsDivisibleBy60Optimized(int[] time) {
        if (time == null || time.length < 2) {
            return 0;
        }

        int[] remainderCount = new int[60]; // Only 60 possible remainders
        int pairCount = 0;

        for (int duration : time) {
            int remainder = duration % 60;
            int complement = (60 - remainder) % 60;

            // Add count of songs that can pair with current song
            pairCount += remainderCount[complement];

            // Update count of current remainder
            remainderCount[remainder]++;
        }

        return pairCount;
    }

    /**
     * Alternative brute force approach for verification (less efficient).
     */
    public int numPairsDivisibleBy60BruteForce(int[] time) {
        int pairCount = 0;

        for (int i = 0; i < time.length; i++) {
            for (int j = i + 1; j < time.length; j++) {
                if ((time[i] + time[j]) % 60 == 0) {
                    pairCount++;
                }
            }
        }

        return pairCount;
    }
}
