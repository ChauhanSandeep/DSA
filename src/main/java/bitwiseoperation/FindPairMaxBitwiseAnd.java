package bitwiseoperation;

/**
 * 🔍 Problem Statement:
 * Given an array of integers, find the maximum AND value of any two elements.
 *
 * Example:
 * Input: [5, 8, 7, 2]
 * Output: 5
 * Explanation: The maximum AND is obtained from (5 & 7) = 5.
 *
 * Similar Problem:
 * - Not a direct LeetCode problem, but a variation can be found here:
 *   https://www.geeksforgeeks.org/find-pair-max-bitwise-and/
 *
 * Follow-up Questions:
 * 1. What if you need to find the actual pair of indices instead of just the value?
 *    Answer: Store indices along with values during the filtering process. Return
 *    the indices of any two elements that match the maximum pattern found.
 *
 * 2. How would you extend this to find triplets with maximum bitwise AND?
 *    Answer: Similar bit-by-bit approach but require at least 3 elements matching
 *    the pattern at each step. Time complexity becomes O(N * log(MAX)).
 *
 * 3. What if you need to find k pairs with maximum bitwise AND values?
 *    Answer: After finding maximum AND value, iterate through array to find all
 *    pairs matching this value. If fewer than k pairs exist, continue with next
 *    lower AND value.
 *
 * 4. How would you solve if array can contain negative numbers?
 *    Answer: Handle sign bit separately. Negative numbers in two's complement have
 *    MSB as 1. Consider pairs of negatives separately from pairs of positives.
 *
 * 5. Can you optimize for sparse arrays where most elements are very different?
 *    Answer: Use hash map to group elements by their MSB positions. Only check
 *    pairs within groups that share same high-order bits.
 */
public class FindPairMaxBitwiseAnd {

    public static void main(String[] args) {
        int[] input = {5, 8, 7, 2};
        int maxAndValue = new FindPairMaxBitwiseAnd().maxBitwiseAnd(input);
        System.out.println("Maximum AND Value: " + maxAndValue);
    }

    /**
     * Finds maximum bitwise AND value using bit-by-bit pattern matching.
     *
     * Algorithm:
     * 1. Start from MSB (most significant bit) and work towards LSB
     * 2. At each bit position, check if at least 2 elements have that bit set
     * 3. If yes, add that bit to result pattern and filter elements
     * 4. Keep only elements that match the current pattern
     * 5. Continue until all 32 bits are processed
     *
     * Key insight: For maximum AND, we want to set as many high-order bits as
     * possible. If at least 2 elements share a bit pattern, that pattern can be
     * part of the result. We greedily select bits from MSB to LSB.
     *
     * Time Complexity: O(N * log(MAX)) where N is array length and MAX is maximum
     * element value. We iterate through 32 bits (log MAX) and for each bit, scan
     * through remaining candidates.
     *
     * Space Complexity: O(1) as we only use a few variables regardless of input size.
     *
     * @param nums array of positive integers
     * @return maximum bitwise AND value achievable by any pair
     */
    public int maxBitwiseAnd(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }

        int result = 0;
        int candidateCount = nums.length;

        // Process each bit from MSB (31) to LSB (0)
        for (int bit = 31; bit >= 0; bit--) {
            int pattern = result | (1 << bit); // Candidate pattern with current bit set

            // Count how many elements match the current pattern
            int matchCount = 0;
          for (int num : nums) {
            if ((num & pattern) == pattern) { // Check if num has all bits of pattern set
              matchCount++;
            }
          }

            // If at least 2 elements have this bit set, include it in result
            if (matchCount >= 2) {
                result = pattern;
            }
        }

        return result;
    }

    /**
     * Alternate Brute-force method (for interview comparison):
     * Compares all pairs to find max AND.
     *
     * @param nums Input array
     * @return Max AND value from all pairs
     *
     * Time Complexity: O(n²)
     * Space Complexity: O(1)
     */
    public int bruteForceMaxAnd(int[] nums) {
        int maxAnd = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                maxAnd = Math.max(maxAnd, nums[i] & nums[j]);
            }
        }
        return maxAnd;
    }
}