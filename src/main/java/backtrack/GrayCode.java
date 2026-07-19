package backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Gray Code
 *
 * Gray code is a binary ordering in which two successive values differ in only
 * one bit. Given a non-negative integer n (the number of bits), return the Gray
 * code sequence, starting from 0.
 *
 * Leetcode: https://leetcode.com/problems/gray-code/
 * Rating:   acceptance 65.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Bit manipulation | Reflected binary code
 *
 * Example:
 *   Input:  n = 2
 *   Output: [0, 1, 3, 2]
 *   Why:    in binary that is 00, 01, 11, 10 - every neighbouring pair flips
 *           exactly one bit, and the last (10) also differs from the first (00)
 *           by one bit, so the sequence is a valid single-bit-change ordering.
 *
 * Follow-ups:
 *   1. Produce the i-th Gray code without generating the prefix?
 *      Use i ^ (i >> 1), which converts binary rank directly to Gray rank.
 *   2. Convert a Gray-coded value back to its binary rank?
 *      Repeatedly xor the value with itself shifted right until the shift becomes 0.
 *   3. Generate a cyclic Hamiltonian path for a restricted bit graph?
 *      This becomes a graph backtracking problem instead of the simple reflection formula.
 *   4. Stream values for very large n?
 *      Iterate rank from 0 upward and emit rank ^ (rank >> 1) lazily.
 *
 * Related: Circular Permutation in Binary Representation (1238).
 *
 *   Approach                Method                     Time    Space (extra)
 *   ----------------------  -------------------------  ------  -------------
 *   Reflection strings      generateGrayCodeRecursive  O(2^n)  O(2^n)
 *   Direct bit formula      generateGrayCode           O(2^n)  O(1)
 */
public class GrayCode {

    /**
     * Intuition: build the n-bit sequence out of the (n-1)-bit one by
     * "reflecting" it. Take the shorter sequence, write it once with a 0 stuck on
     * the front, then write it again in reverse with a 1 on the front. Within each
     * half only the low bits change (they already form a valid Gray sequence), and
     * at the seam between the halves the mirror image means the low bits are
     * identical, so only the new leading bit flips - so every neighbour, including
     * the join, differs by exactly one bit.
     *
     * Algorithm:
     *   1. Recursively build the reflected binary strings for n bits.
     *   2. First half: copy the previous sequence in order, prefixing each with 0.
     *   3. Second half: copy the previous sequence in reverse, prefixing each
     *      with 1 (the reversal is what keeps the seam a single-bit change).
     *   4. Parse each binary string into its integer value.
     *
     * Time:  O(2^n * n) - there are 2^n codes and building/parsing each one is an
     *        n-character string, so O(n) work per code.
     * Space: O(2^n * n) for the intermediate strings.
     *
     * @param bitsCount number of bits
     * @return Gray code sequence as integers
     */
    public static List<Integer> generateGrayCodeRecursive(int bitsCount) {
        List<Integer> grayCodes = new ArrayList<>();
        if (bitsCount < 0) return grayCodes;

        List<String> binaryCodes = buildReflectedCodes(bitsCount);
        for (String binaryCode : binaryCodes) {
            grayCodes.add(Integer.parseInt(binaryCode, 2));
        }
        return grayCodes;
    }

    /** Builds the reflected binary Gray codes for n bits as zero/one strings. */
    private static List<String> buildReflectedCodes(int bitsCount) {
        List<String> codes = new ArrayList<>();
        if (bitsCount == 0) {
            codes.add("0");
            return codes;
        }

        List<String> previousCodes = buildReflectedCodes(bitsCount - 1);
        for (String code : previousCodes) codes.add("0" + code);
        for (int i = previousCodes.size() - 1; i >= 0; i--) codes.add("1" + previousCodes.get(i));
        return codes;
    }

    /**
     * Intuition (interview default): there is a direct formula, so no recursion is
     * needed. If you count normally in binary, going from one number to the next
     * can flip several bits at once (a carry ripples through a run of 1s). The
     * trick rank ^ (rank >> 1) xors each number with itself shifted right by one:
     * that cancels every bit that matches its higher neighbour and keeps only the
     * places where bits change, which for consecutive numbers always works out to
     * a single differing bit. So we can just count 0, 1, 2, ... and convert each.
     *
     * Algorithm:
     *   1. Reject a negative bit count.
     *   2. The sequence has 2^n values, so compute that size once.
     *   3. For each rank from 0 upward, append rank ^ (rank >> 1).
     *
     * Time:  O(2^n) - one Gray value per rank, and each is a single O(1) xor/shift.
     * Space: O(1) extra beyond the output.
     *
     * @param bitsCount number of bits
     * @return Gray code sequence as integers
     */
    public static List<Integer> generateGrayCode(int bitsCount) {
        List<Integer> grayCodes = new ArrayList<>();
        if (bitsCount < 0) return grayCodes;

        int sequenceSize = 1 << bitsCount;
        for (int rank = 0; rank < sequenceSize; rank++) {
            grayCodes.add(rank ^ (rank >> 1));
        }
        return grayCodes;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        int[] inputs = {0, 2, 3};
        String[] expected = {
            "[0]",
            "[0, 1, 3, 2]",
            "[0, 1, 3, 2, 6, 7, 5, 4]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<Integer> got = generateGrayCode(inputs[i]);
            System.out.printf("n=%d  ->  %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }
}
