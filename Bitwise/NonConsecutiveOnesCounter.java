package Bitwise;

/**
 * ✅ Problem: Non-negative Integers Without Consecutive Ones
 *
 * Given a number `num`, count how many non-negative integers less than or equal to `num`
 * do **not** contain **consecutive 1s** in their binary representation.
 *
 * 🔹 Example:
 * Input: num = 10 (Binary: 1010)
 * Output: 8
 * Explanation:
 * Valid numbers (no consecutive 1s): 0, 1, 2, 4, 5, 8, 9, 10
 *
 * 🔗 Leetcode: https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/
 *
 * 🔁 Follow-up Questions:
 * 1. Can you generate the actual list of such numbers instead of just the count?
 *    ➤ Yes, recursively generate numbers using backtracking and binary prefix building.
 * 2. What if we want to exclude numbers that start with 0s?
 *    ➤ Binary representation inherently does not start with 0s. Ignore leading 0s.
 * 3. Can you do this with constant space?
 *    ➤ Only if you reuse DP arrays or unroll the recurrence relation.
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
     * We're counting valid binary numbers ≤ num that **avoid "11"**.
     * Rather than generate each number, we:
     * - Use DP to precompute how many valid binary numbers exist for each bit length.
     * - Traverse the binary representation of `num`, deciding which branches of the binary "decision tree" are valid.
     *
     * 🔹 Approach:
     * 1. Precompute:
     *    - Use DP to fill arrays:
     *      - `dpZeroEnd[i]`: valid binary strings of length i+1 ending in '0'
     *      - `dpOneEnd[i]`: valid binary strings of length i+1 ending in '1'
     *    - This is based on the Fibonacci recurrence:
     *      `dpZeroEnd[i] = dpZeroEnd[i-1] + dpOneEnd[i-1]`, `dpOneEnd[i] = dpZeroEnd[i-1]`
     * 2. Traverse the bits of `num` (from most to least significant):
     *    - If you encounter "00" at index `i` and `i+1`: you overcounted paths that took a '1' here → subtract `dpOneEnd[i]`
     *    - If you encounter "11": `num` itself is invalid → break early, return result so far
     * 3. If traversal completes without hitting "11", `num` itself is valid → include it in the result
     *
     * 🔹 Time Complexity: O(log n) — proportional to the number of bits in `num`
     * 🔹 Space Complexity: O(log n) — for the DP arrays
     */
    public static int findCountWithoutConsecutiveOnes(int num) {
        // Convert number to binary and reverse for easier indexing from LSB to MSB
        String binary = new StringBuilder(Integer.toBinaryString(num)).reverse().toString();
        int bitLength = binary.length();

        /**
         * _______  _______  _______  _______    ____
         * |       ||       ||       ||       |  |    |
         * |  _____||_     _||    ___||    _  |   |   |
         * | |_____   |   |  |   |___ |   |_| |   |   |
         * |_____  |  |   |  |    ___||    ___|   |   |
         *  _____| |  |   |  |   |___ |   |       |   |
         * |_______|  |___|  |_______||___|       |___|
         * Compute number of valid binary strings
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
         *  _______  _______  _______  _______    _______
         * |       ||       ||       ||       |  |       |
         * |  _____||_     _||    ___||    _  |  |____   |
         * | |_____   |   |  |   |___ |   |_| |   ____|  |
         * |_____  |  |   |  |    ___||    ___|  | ______|
         *  _____| |  |   |  |   |___ |   |      | |_____
         * |_______|  |___|  |_______||___|      |_______|
         *
         * STEP 2 (Most Important): Subtract structurally valid binary numbers of the same length
         * that are greater than `num`. These were already counted in `totalValid`
         */
        for (int i = bitLength - 2; i >= 0; i--) {
            char current = binary.charAt(i);
            char next = binary.charAt(i + 1);

            // If we encounter "00", we need to subtract the count of valid numbers
            // that took a '1' at this position (i), while `num` took a '0',
            // because those numbers are now known to be > num and were incorrectly included.
            if (current == '0' && next == '0') {
                totalValid -= dpOneEnd[i];
            }

            // If we encounter "11", it means `num` itself contains consecutive 1s,
            // making it invalid. Any number with the same prefix from this point onward
            // would also contain consecutive 1s, so we stop counting beyond this point.
            if (current == '1' && next == '1') {
                break;
            }
        }

        return totalValid;
    }
}