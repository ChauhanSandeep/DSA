package arrays.hashmap;

import java.util.HashMap;
import java.util.Map;

import java.util.Arrays;
/**
 * Problem: Pairs of Songs With Total Durations Divisible by 60
 *
 * Given song durations in seconds, count pairs whose total duration is divisible
 * by 60. Each song can be paired with later songs only once, and the answer is
 * the number of index pairs, not the number of distinct duration values.
 *
 * Leetcode: https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/
 * Rating:   1377 (zerotrac Elo, Q2, weekly-contest-128)
 * Pattern:  Arrays | Hash map | Remainder complements
 *
 * Example:
 *   Input:  [30,20,150,100,40]
 *   Output: 3
 *   Why:    the valid pairs are (30,150), (20,100), and (20,40), each summing
 *           to a multiple of 60.
 *
 * Follow-ups:
 *   1. What if the divisor is k instead of 60?
 *      Replace the fixed array size with k and use (k - remainder) % k.
 *   2. What if durations arrive as a stream?
 *      Keep the same remainder counts and update the pair total online.
 *   3. What if you need to return the actual pairs?
 *      Store lists of indices per remainder instead of only counts.
 */
public class PairDivisibleBy60 {
    public static void main(String[] args) {
        PairDivisibleBy60 solver = new PairDivisibleBy60();
        int[][] inputs = {{30, 20, 150, 100, 40}, {60, 60, 60}, {10}};
        int[] expected = {3, 3, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.numPairsDivisibleBy60(inputs[i]);
            System.out.printf("time=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition (interview default): the divisor is fixed at 60, so a tiny array is
     * simpler and faster than a hash map. The array index is the remainder, and the
     * value is how many earlier songs had that remainder. For a current remainder r,
     * the only remainders that can complete the pair are 0 when r is 0, or 60 - r
     * otherwise. Counting before incrementing prevents pairing a song with itself.
     *
     * Time:  O(n) - each duration is processed once with constant-time array access.
     * Space: O(1) - the count array always has exactly 60 slots.
     *
     * @param time song durations in seconds
     * @return number of pairs whose total duration is divisible by 60
     */
    public int numPairsDivisibleBy60(int[] time) {
        Map<Integer, Integer> remainderCount = new HashMap<>();
        int pairs = 0;

        for (int t : time) {
            int mod60Time = t % 60;
            int complement = (mod60Time == 0) ? 0 : 60 - mod60Time;

            // Count pairs formed with existing complements
            pairs += remainderCount.getOrDefault(complement, 0);

            // Update remainder frequency count
            remainderCount.put(mod60Time, remainderCount.getOrDefault(mod60Time, 0) + 1);
        }

        return pairs;
    }

    /** Backward-compatible name for the array-count implementation. */
    public int numPairsDivisibleBy60UsingArray(int[] time) {
        int[] freq = new int[60];
        int pairs = 0;

        for (int t : time) {
            int mod60Time = t % 60;
            pairs += (mod60Time == 0) ? freq[0] : freq[60 - mod60Time]; // Add count of complement
            freq[mod60Time]++; // Update frequency array
        }

        return pairs;
    }
}
