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
     * Intuition: solve n bits using the answer for n-1 bits - a build-up from the
     * smallest case. Say you already have a valid Gray sequence on n-1 bits. How
     * do you add one more bit and keep the "neighbours differ in one bit"
     * property? Reflect it: write the shorter sequence as-is with a 0 glued to the
     * front, then write it AGAIN in reverse with a 1 glued to the front. Inside
     * each half the leading bit is fixed and the low bits were already a Gray
     * sequence, so those neighbours are fine. The only brand-new adjacency is the
     * seam where the two halves meet - and because the second half is the mirror
     * image, the two codes touching at the seam share identical low bits and
     * differ only in that fresh leading bit. Base case: 0 bits is just {"0"}. Every
     * neighbour, including the seam, is a single-bit change by construction.
     *
     * Time:  O(2^n * n) - there are 2^n codes and building or parsing each one
     *        touches an n-character string, so O(n) work apiece.
     * Space: O(2^n * n) to hold the intermediate binary strings.
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
     * Intuition (interview default): the reflection idea is neat, but there is a
     * closed form that drops the recursion entirely. Why not just count
     * 0, 1, 2, ... in ordinary binary? Because a normal +1 can flip many bits at
     * once - a carry ripples through a run of trailing 1s (011 -> 100 flips
     * three). We need each step to flip exactly one bit. The formula
     * rank ^ (rank >> 1) delivers that: xor-ing a number with itself shifted right
     * by one keeps a bit only where it differs from its higher neighbour, and for
     * two consecutive integers that always comes out to a single differing bit. So
     * we count 0, 1, 2, ... and map each through the formula - a valid Gray
     * sequence with no bookkeeping at all.
     *
     * Time:  O(2^n) - one Gray value per rank, each just an O(1) xor and shift.
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
