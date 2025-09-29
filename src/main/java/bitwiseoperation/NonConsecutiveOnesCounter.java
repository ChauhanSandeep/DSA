package bitwiseoperation;

/**
 * Non-negative Integers without Consecutive Ones
 *
 * Problem Statement:
 * Given a positive integer n, return the number of integers in the range [0, n] whose binary
 * representations do not contain consecutive ones (i.e., no "11" pattern exists).
 *
 * Example:
 * Input: n = 5
 * Output: 5
 * Explanation: Here are the non-negative integers <= 5 with their binary representations:
 * 0 : 0     (valid - no consecutive ones)
 * 1 : 1     (valid - single 1)
 * 2 : 10    (valid - no consecutive ones)
 * 3 : 11    (invalid - has consecutive ones "11")
 * 4 : 100   (valid - no consecutive ones)
 * 5 : 101   (valid - no consecutive ones)
 * Among them, only integer 3 violates the rule, so answer is 5.
 *
 * Input: n = 1
 * Output: 2
 * Explanation: Valid numbers are 0 (binary: 0) and 1 (binary: 1)
 *
 * LeetCode Link: https://leetcode.com/problems/non-negative-integers-without-consecutive-ones
 *
 * Follow-up Questions:
 * 1. What if we need to find the actual numbers instead of just count?
 *    Answer: Modify DP to track actual valid numbers instead of just counting them.
 * 2. How would you handle negative numbers in the range?
 *    Answer: Extend algorithm to handle two's complement representation with sign bit considerations.
 * 3. What about consecutive zeros restriction instead of ones?
 *    Answer: Similar DP approach but with different state transitions for handling "00" patterns.
 * 4. How to optimize space for very large n values?
 *    Answer: Use rolling arrays since we only need previous few Fibonacci values.
 */
public class NonConsecutiveOnesCounter {

    public static void main(String[] args) {
        System.out.println(findCountWithoutConsecutiveOnes(10)); // Expected Output: 8
    }

    /**
     * Returns the count of non-negative integers less than or equal to `num`
     * whose binary representation does **not** contain consecutive 1s.
     *
     * 🔹 Intuition:
     * We're counting valid binary numbers ≤ num that avoid "11".
     * Rather than generate each number, we:
     * - Use DP to precompute how many valid binary numbers exist for each bit length.
     * - Traverse the binary representation of `num`, deciding which branches of the binary "decision tree" are valid.
     *
     * Approach:
     * 1. Precompute:
     *    - Use DP to fill arrays:
     *      - `dpZeroEnd[i]`: valid binary strings of length i+1 ending in '0'
     *      - `dpOneEnd[i]`: valid binary strings of length i+1 ending in '1'
     *    - This is based on the Fibonacci recurrence:
     *      `dpZeroEnd[i] = dpZeroEnd[i-1] + dpOneEnd[i-1]`, `dpOneEnd[i] = dpZeroEnd[i-1]`
     * 2. Traverse the bits of `num` (from most to least significant):
     *    - If you encounter "00" at index `i` and `i+1`: `num` is valid, but we must subtract counts of valid numbers
     *      that would have had '1' at `i` (which were counted in total).
     *    - If you encounter "11": `num` itself is invalid → break early, return result so far
     * 3. If traversal completes without hitting "11", `num` itself is valid → include it in the result
     *
     * Time Complexity: O(log n) — Proportional to the number of bits in `num`. Operations on size of binary representation are logarithmic.
     * Space Complexity: O(log n) — for the DP arrays
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