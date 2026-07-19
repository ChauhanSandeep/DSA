package maths;

/**
 * Problem: Number of Digit One
 *
 * Given n, count how many times the digit 1 appears in all non-negative
 * integers from 0 through n. The mathematical solution counts contribution by
 * digit position instead of visiting every number.
 *
 * Leetcode: https://leetcode.com/problems/number-of-digit-one/ (Hard)
 * Rating:   acceptance 39.2% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Digit counting | Place-value decomposition
 *
 * Example:
 *   Input:  n = 13
 *   Output: 6
 *   Why:    the digit 1 appears in 1, 10, 11 twice, 12, and 13.
 *
 * Follow-ups:
 *   1. How would you count a digit other than 1?
 *      Adjust the current-digit cases, taking special care when the target digit is 0.
 *   2. How would you count ones in a range [a, b]?
 *      Compute countDigitOne(b) - countDigitOne(a - 1).
 *   3. How would you handle n larger than 32-bit integers?
 *      Promote the input and count to long or use decimal string place-value logic.
 *
 * Related: Factorial Trailing Zeroes (172), Count Primes (204).
 */

public class NumberOfDigitOne {

    public static void main(String[] args) {
        NumberOfDigitOne solver = new NumberOfDigitOne();
        int[] inputs = { 0, 13, 99 };
        int[] expected = { 0, 6, 20 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.countDigitOne(inputs[i]);
            System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: focus on one decimal position at a time. The higher digits tell
     * how many full cycles of 0 through 9 occur at that position, the current
     * digit decides whether there is a partial extra cycle, and the lower digits
     * decide how much of that partial cycle is included.
     *
     * Algorithm:
     *   1. Iterate i over place values 1, 10, 100, and so on.
     *   2. Split n into higher, current, and lower parts around i.
     *   3. Add higher * i when current is 0.
     *   4. Add higher * i + lower + 1 when current is 1.
     *   5. Add (higher + 1) * i when current is greater than 1.
     *
     * Time:  O(log n) - one pass per decimal digit.
     * Space: O(1) - only place-value counters are stored.
     *
     * @param n inclusive upper bound
     * @return total occurrences of digit 1 from 0 through n
     */

    public int countDigitOne(int n) {
        int count = 0;

        // Iterate through each digit position (1s, 10s, 100s, etc.)
        for (long i = 1; i <= n; i *= 10) {
            // Split the number into higher, current, and lower parts
            long higher = n / (i * 10);  // Numbers before current digit
            long current = (n / i) % 10;  // Current digit
            long lower = n % i;           // Numbers after current digit

            if (current == 0) {
                // Case 1: Current digit is 0
                // Count is determined by higher digits * position
                count += higher * i;
            } else if (current == 1) {
                // Case 2: Current digit is 1
                // Count is higher * position + lower + 1
                count += higher * i + lower + 1;
            } else {
                // Case 3: Current digit > 1
                // Count is (higher + 1) * position
                count += (higher + 1) * i;
            }
        }

        return count;
    }

    /**
     * Alternative solution using string manipulation (less efficient but more intuitive).
     * This is provided for understanding but not recommended for large n.
     */
    public int countDigitOneBruteForce(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            count += countOnesInNumber(i);
        }
        return count;
    }

        /** Counts digit 1 occurrences inside one number. */

    private int countOnesInNumber(int num) {
        int count = 0;
        while (num > 0) {
            if (num % 10 == 1) {
                count++;
            }
            num /= 10;
        }
        return count;
    }
}
