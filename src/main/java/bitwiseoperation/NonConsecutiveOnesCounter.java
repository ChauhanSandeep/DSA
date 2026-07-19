package bitwiseoperation;

/**
 * Problem: Non-negative Integers Without Consecutive Ones
 *
 * Given a positive integer n, count the integers in the range [0, n] whose
 * binary representations do not contain the substring "11". The dynamic program
 * counts valid binary strings by ending bit and then subtracts valid strings that
 * would exceed n.
 *
 * Leetcode: https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/ (Hard)
 * Rating:   acceptance 42.9% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Bit manipulation | Digit DP | Fibonacci bit counts
 *
 * Example:
 *   Input:  n = 5
 *   Output: 5
 *   Why:    0, 1, 2, 4, and 5 have no consecutive 1 bits; only 3 is excluded.
 *
 * Follow-ups:
 *   1. How would you list the valid numbers instead of counting them?
 *      Backtrack over bits with a previous-bit state, pruning any branch that creates "11".
 *   2. How would you answer many queries quickly?
 *      Precompute Fibonacci-style bit counts once and reuse them for every query.
 *   3. What if the forbidden pattern is "101" instead of "11"?
 *      Use digit DP with automaton state for the longest matched suffix of the pattern.
 *   4. How would this change for 64-bit inputs?
 *      Extend the DP arrays to 64 positions and scan the long's bits.
 *
 * Related: Count Binary Strings Without Consecutive 1s.
 */
public class NonConsecutiveOnesCounter {

    public static void main(String[] args) {
        int[] inputs = {1, 5, 10};
        int[] expected = {2, 5, 8};

        for (int i = 0; i < inputs.length; i++) {
            int output = findCountWithoutConsecutiveOnes(inputs[i]);
            System.out.printf("num=%d -> %d  expected=%d%n", inputs[i], output, expected[i]);
        }
    }

    /**
     * Intuition: valid binary strings without consecutive 1s follow the same
     * recurrence as Fibonacci numbers. A valid string ending in 0 can follow any
     * shorter valid string, while a valid string ending in 1 can only follow a
     * shorter string ending in 0. The original code first counts all valid strings
     * with the same bit length as num, then scans num's reversed binary form and
     * subtracts valid strings that are structurally larger than num.
     *
     * Algorithm:
     *   1. Reverse num's binary representation so index 0 is the least significant bit.
     *   2. Fill dpZeroEnd and dpOneEnd for each bit length using the no-consecutive-1s recurrence.
     *   3. Start from all valid strings of num's bit length.
     *   4. Scan adjacent bits from most significant toward least, subtracting over-large branches and stopping at "11".
     *
     * Time:  O(log n) - the work is proportional to the number of bits in num.
     * Space: O(log n) - two arrays store counts for each bit position.
     *
     * @param num positive upper bound for the range [0, num]
     * @return count of integers whose binary representation has no consecutive 1s
     */
    public static int findCountWithoutConsecutiveOnes(int num) {
        // Convert number to binary and reverse for easier indexing from LSB to MSB
        String binary = new StringBuilder(Integer.toBinaryString(num)).reverse().toString();
        int bitLength = binary.length();

        /**
         * STEP 1 Compute number of valid binary strings
         * of length `bitLength` with no consecutive 1s
         */
        int[] dpZeroEnd = new int[bitLength]; // Number of valid binary strings ending with '0'
        int[] dpOneEnd = new int[bitLength]; // Number of valid binary strings ending with '1'

        // Base case for length 1 (i = 0)
        dpZeroEnd[0] = 1; // "0"
        dpOneEnd[0] = 1;  // "1"

        // Build DP for all lengths up to bitLength
        for (int i = 1; i < bitLength; i++) {
            dpZeroEnd[i] = dpZeroEnd[i - 1] + dpOneEnd[i - 1]; // Append 0 to both previous 0 and 1
            dpOneEnd[i] = dpZeroEnd[i - 1]; // Append 1 only to previous 0
        }

        // Total valid binary numbers with no consecutive 1s up to `num` of length `bitLength`
        int totalValid = dpZeroEnd[bitLength - 1] + dpOneEnd[bitLength - 1];

        /**
         * STEP 2 (Most Important): Subtract structurally valid binary numbers of the same length
         * that are greater than `num`. These were already counted in `totalValid`
         */
        for (int i = bitLength - 2; i >= 0; i--) {
            char current = binary.charAt(i);
            char next = binary.charAt(i + 1);

            // If we encounter "00", we need to subtract the count of valid numbers that
            // would have had a '1' at position `i` (which were counted in totalValid).
            if (current == '0' && next == '0') {
                totalValid -= dpOneEnd[i];
            }

            // If we encounter "11", it means num itself contains consecutive 1s,making it invalid number.
            // Any number greater than this would also be invalid, so we stop here.
            if (current == '1' && next == '1') {
                break;
            }
        }

        return totalValid;
    }

    /**
     * Brute force approach for small inputs and verification.
     *
     * Algorithm: Direct Enumeration with Binary Check
     * Step 1: Iterate through all numbers from 0 to num
     * Step 2: For each number, convert to binary representation
     * Step 3: Check if binary representation contains consecutive 1s
     * Step 4: Count numbers that don't have consecutive 1s
     * Step 5: Return total count
     *
     * Time Complexity: O(num * log num) where log num is for binary conversion per number
     * Space Complexity: O(log num) for binary string storage
     *
     * @param num the upper bound integer
     * @return count using brute force (suitable only for small num)
     */
    public int findIntegersBruteForce(int num) {
        int count = 0;

        // Step 1: Check each number from 0 to num
        for (int i = 0; i <= num; i++) {
            // Step 2-3: Check if number has consecutive 1s in binary
            if (!hasConsecutiveOnes(i)) {
                count++;
            }
        }

        return count;
    }

    // Helper method to check if a number has consecutive 1s in binary representation
    private boolean hasConsecutiveOnes(int num) {
        String binary = Integer.toBinaryString(num);
        return binary.contains("11");
    }
}