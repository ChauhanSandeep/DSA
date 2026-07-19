package maths;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: Consecutive Numbers Sum
 *
 * Given a positive integer n, count how many ways it can be written as a sum
 * of one or more consecutive positive integers. Each representation is defined
 * by its length and starting value.
 *
 * Leetcode: https://leetcode.com/problems/consecutive-numbers-sum/ (Hard)
 * Rating:   1694 (zerotrac Elo)
 * Pattern:  Math | Arithmetic series | Divisibility by sequence length
 *
 * Example:
 *   Input:  n = 9
 *   Output: 3
 *   Why:    9 can be written as 9, 4 + 5, and 2 + 3 + 4.
 *
 * Follow-ups:
 *   1. What if negative integers are allowed in the sequence?
 *      The count can become unbounded unless a minimum start or length cap is imposed.
 *   2. What if the common difference can be any positive d?
 *      Replace k * (k - 1) / 2 with d * k * (k - 1) / 2 and test divisibility.
 *   3. How would you return the actual sequences?
 *      Use the same valid k test, then materialize start = numerator / k through start + k - 1.
 *
 * Related: Arithmetic Slices (413), Sum of Square Numbers (633).
 */

public class ConsecutiveNumbersSum {

    public static void main(String[] args) {
        ConsecutiveNumbersSum solver = new ConsecutiveNumbersSum();
        int[] inputs = { 1, 9, 15, 0 };
        int[] expected = { 1, 3, 4, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.consecutiveNumbersSum(inputs[i]);
            System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


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
    * Intuition: a run of k consecutive numbers is determined by its first
    * value. If that first value is x, then the sum is k * x plus the fixed
    * staircase 0 + 1 + ... + (k - 1). For each possible k, the original code
    * checks whether the remaining numerator divides evenly by k, which means x
    * is a positive integer.
    *
    * Algorithm:
    *   1. Return 0 for non-positive num.
    *   2. Try every length k while k * (k - 1) / 2 is still below num.
    *   3. Compute numerator = num - k * (k - 1) / 2.
    *   4. Count the length when numerator is positive and divisible by k.
    *
    * Time:  O(sqrt(n)) - the triangular prefix grows quadratically in k.
    * Space: O(1) - only counters and the current numerator are stored.
    *
    * @param num input integer to represent as consecutive positive integers
    * @return number of valid consecutive-sum representations
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