package strings.hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Pairs of Songs With Total Durations Divisible by 60
 *
 * Given song durations, count pairs of indices i < j whose total duration is a
 * multiple of 60. Only each duration's remainder modulo 60 matters.
 *
 * Leetcode: https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/ (Medium)
 * Rating:   acceptance 53.5% (Medium), contest rating 1377
 * Pattern:  Hash map | Remainder complements | One-pass counting
 *
 * Example:
 *   Input:  time = [30,20,150,100,40]
 *   Output: 3
 *   Why:    pairs (30,150), (20,100), and (20,40) have sums divisible by 60.
 *
 * Follow-ups:
 *   1. Make the divisor configurable?
 *      Replace 60 with k and store k remainder buckets.
 *   2. Return the actual index pairs?
 *      Store lists of previous indices for each remainder instead of just counts.
 *   3. Count triplets divisible by 60?
 *      Track pair-remainder counts before adding each new duration.
 *
 * Related: Subarray Sums Divisible by K (974), Two Sum (1).
 */
public class PairsOfSongsWithTotalDurationsDivisibleBy60 {

    public static void main(String[] args) {
        PairsOfSongsWithTotalDurationsDivisibleBy60 solver = new PairsOfSongsWithTotalDurationsDivisibleBy60();
        int[][] inputs = { {30, 20, 150, 100, 40}, {60, 60, 60}, {10} };
        int[] expected = {3, 3, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.numPairsDivisibleBy60(inputs[i]);
            System.out.printf("time=%s -> %d  expected=%d%n",
                java.util.Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: a duration with remainder r pairs only with earlier durations whose
     * remainder is (60 - r) mod 60. Count complements seen so far before recording
     * the current song, which naturally enforces i < j.
     *
     * Algorithm:
     *   1. Return 0 when fewer than two durations are available.
     *   2. For each duration, compute its remainder modulo 60 and complement remainder.
     *   3. Add the number of previously seen complement remainders to pairCount.
     *   4. Record the current remainder for future songs.
     *
     * Time:  O(n) - one pass through the durations.
     * Space: O(1) - at most 60 remainder counts are stored.
     *
     * @param time song durations in seconds
     * @return number of pairs whose total duration is divisible by 60
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
