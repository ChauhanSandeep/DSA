package arrays.hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Find all pairs of song durations where the sum is divisible by 60.
 */
public class PairDivisibleBy60 {

    public static void main(String[] args) {
        int[] times = {30, 20, 150, 100, 40};
        PairDivisibleBy60 solution = new PairDivisibleBy60();

        System.out.println("Using HashMap: " + solution.numPairsDivisibleBy60(times));
        System.out.println("Using Array: " + solution.numPairsDivisibleBy60UsingArray(times));
    }

    /**
     * Uses a HashMap to count occurrences of time % 60.
     * Time Complexity: O(N), Space Complexity: O(60) ≈ O(1)
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

    /**
     * Uses an array (size 60) instead of HashMap for efficiency.
     * Time Complexity: O(N), Space Complexity: O(60) ≈ O(1)
     */
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
