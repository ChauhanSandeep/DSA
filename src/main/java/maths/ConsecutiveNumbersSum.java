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
    * Counts ways to represent n as sum of consecutive integers using mathematical approach.
    * Step-by-step:
    *  1. Mathematical derivation:
    *     - Let the consecutive integers be: x, x+1, x+2, ..., x+(k-1)
    *     - Sum formula: k*x + k*(k-1)/2 = n
    *     - Rearranging: k*x = n - k*(k-1)/2
    *     - For valid representation: x must be a positive integer
    *  2. For each possible length k (starting from 1):
    *     a. Calculate: numerator = n - k*(k-1)/2
    *     b. Check if numerator > 0 and divisible by k
    *     c. If yes, we found a valid way to represent n
    *  3. Loop continues while k*(k-1)/2 < n (ensures numerator stays positive)
    *  4. Count all valid representations.
    *
    * Key Insight:
    * For k consecutive integers starting at x:
    * Sum = k*x + (0 + 1 + 2 + ... + (k-1)) = k*x + k*(k-1)/2 = n
    * So: x = (n - k*(k-1)/2) / k
    * For x to be a positive integer: n - k*(k-1)/2 must be divisible by k and > 0.
    *
    * Algorithm: Mathematical iteration.
    * Time Complexity: O(sqrt(n)), loop runs until k*(k+1)/2 > n, which gives k ≈ sqrt(2n).
    * Space Complexity: O(1), only constant extra space.
    *
    * @param n Input positive integer
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
                // The sequence starts from (numerator/k)
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