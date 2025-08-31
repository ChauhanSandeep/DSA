package maths;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: Consecutive Numbers Sum
 *
 * Given an integer n, return the number of ways you can write n as the sum of consecutive positive integers.
 *
 * Example:
 * Input: n = 9
 * Output: 3
 * Explanation: 9 = 9 = 4 + 5 = 2 + 3 + 4
 *
 * LeetCode: https://leetcode.com/problems/consecutive-numbers-sum
 *
 * Follow-up Questions:
 * 1. What if we allow negative integers in the consecutive sequence?
 *    Answer: Problem becomes more complex as we need to handle different cases for negative ranges.
 *
 * 2. How would you modify for non-consecutive arithmetic progressions?
 *    Answer: Generalize to arithmetic sequences with any common difference, not just 1.
 *
 * 3. What if we need to find the actual sequences instead of just counting?
 *    Answer: Modify algorithm to store and return the actual consecutive number sequences.
 *    Related: https://leetcode.com/problems/sum-of-consecutive-numbers/
 *
 * @author Sandeep
 */
public class ConsecutiveNumbersSum {

    /**
     * Brute force approach for small values and verification.
     * Tests all possible starting points and sequence lengths.
     *
     * Time Complexity: O(n^1.5)
     * Space Complexity: O(1)
     */
    public int consecutiveNumbersSumBruteForce(int num) {
        int count = 0;

        // Try all possible starting points
        for (int start = 1; start <= num; start++) {
            int sum = 0;

            // Try consecutive sequences starting from 'start'
            for (int current = start; sum < num; current++) {
                sum += current;

                if (sum == num) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

    /**
     * Counts ways to express n as sum of consecutive positive integers using mathematical approach.
     * # Consecutive Numbers Sum - Concise Solution
     *
     * Observation
     * - For any length k, there's exactly one consecutive sequence that sums to N (if it exists).
     *
     * Equation
     * - k consecutive numbers starting from x:
     *     => x + (x+1) + (x+2) + ... + (x+k-1) = N
     *     => kx + k(k-1)/2 = N
     *     => x = (N - k(k-1)/2) / k
     *
     *
     * Conditions:
     * 1. Loop bound: k * (k-1) / 2 < N (so that numerator > 0)
     * 2. Check validity:
     *    - numerator = N - k(k-1)/2 > 0 (positive starting point)
     *    - numerator % k == 0 (integer starting point)
     * 3. Count: Each valid k contributes 1 to the answer
     *
     * Algorithm:
     * 1. For k consecutive numbers starting from a: n = k*a + k*(k-1)/2
     * 2. Rearranging: numerator = (n - k*(k-1)/2) / k
     * 3. For valid sequence, 'numerator' must be positive integer
     * 4. Iterate through possible values of k and count valid sequences
     * 5. Stop when k*(k-1)/2 >= n (no valid positive 'numerator' possible)
     *
     * Time Complexity: O(√n) where n is the input number
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param num Input positive integer
     * @return Number of ways to write n as sum of consecutive positive integers
     */
    public int consecutiveNumbersSum(int num) {
        if (num <= 0) return 0;

        int count = 0;

        // Try different lengths of consecutive sequences
        for (int k = 1; k * (k - 1) / 2 < num; k++) {
            // For k consecutive numbers: n = k*a + k*(k-1)/2
            // Solving for a: a = (n - k*(k-1)/2) / k

            int numerator = num - (k * (k - 1) / 2);

            // Check if 'a' is a positive integer
            if (numerator > 0 && numerator % k == 0) {
                count++;
            }
        }

        return count;
    }

    /**
     * Returns all actual consecutive sequences that sum to n.
     * Useful for debugging and verification.
     *
     * @param num Input number
     * @return List of consecutive sequences
     */
    public List<List<Integer>> findAllConsecutiveSequences(int num) {
        List<List<Integer>> sequences = new ArrayList<>();

        for (int k = 1; k * (k - 1) / 2 < num; k++) {
            int numerator = num - k * (k - 1) / 2;

            if (numerator > 0 && numerator % k == 0) {
                // As per equation above, x = (N - k(k-1)/2) / k and here x is starting point of sequence
                int start = numerator / k;

                List<Integer> sequence = new ArrayList<>();
                for (int i = 0; i < k; i++) {
                    sequence.add(start + i);
                }
                sequences.add(sequence);
            }
        }

        return sequences;
    }
}