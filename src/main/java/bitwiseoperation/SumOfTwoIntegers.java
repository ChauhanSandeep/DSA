package bitwiseoperation;

/**
 * Problem: Sum of Two Integers
 *
 * Given two integers, return their sum without using the + or - operators in the
 * solution method. Bit operations simulate column-by-column binary addition.
 *
 * Leetcode: https://leetcode.com/problems/sum-of-two-integers/ (Medium)
 * Rating:   acceptance 55.9% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Bit manipulation | XOR without carry | AND carry propagation
 *
 * Example:
 *   Input:  a = 1, b = 2
 *   Output: 3
 *   Why:    XOR writes the no-carry sum bits, and shifted AND carries add the
 *           columns where both inputs had a 1.
 *
 * Follow-ups:
 *   1. How would you subtract without using -?
 *      Add the two's-complement negation of b, computed from ~b and 1.
 *   2. How would you add long values?
 *      Use the same XOR and shifted-carry loop with long operands.
 *   3. How should overflow be handled?
 *      Either accept fixed-width wrapping or detect it by comparing operand and result signs.
 *   4. How can sign-bit carry be handled?
 *      Let the fixed 32-bit carry propagation finish naturally.
 */
public class SumOfTwoIntegers {
    /**
     * Intuition: binary addition separates the visible sum bits from the carry
     * bits. XOR gives the sum without carry because different bits become 1 and
     * equal bits become 0. AND finds the positions where both inputs have 1, and
     * shifting that carry left moves it to the next column.
     *
     * Algorithm:
     *   1. While b still contains carry bits, compute carry as a & b.
     *   2. Replace a with the no-carry sum a ^ b.
     *   3. Shift carry left into b for the next iteration.
     *
     * Time:  O(1) - Java int has a fixed 32-bit width.
     * Space: O(1) - only a, b, and carry are stored.
     *
     * @param a first integer
     * @param b second integer
     * @return computed sum using fixed-width two's-complement arithmetic
     */
    public int getSum(int a, int b) {
        // Iterate until there is no carry
        while (b != 0) {
            // Calculate the carry - this will have bits set to 1 if there's a carry
            int carry = a & b;

            // Sum of bits of a and b where at least one of the bits is not set
            a = a ^ b;

            // Carry is shifted by one so that adding it to a gives the required sum
            b = carry << 1;

        }

        return a;
    }

    public static void main(String[] args) {
        SumOfTwoIntegers solver = new SumOfTwoIntegers();

        int[][] inputs = {
            {1, 2},
            {5, 7},
            {0, 0},
            {-1, 1},
            {Integer.MAX_VALUE, 1},
            {1 << 30, 1 << 30}
        };
        int[] expected = {3, 12, 0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.getSum(inputs[i][0], inputs[i][1]);
            System.out.printf("a=%d b=%d -> %d  expected=%d%n",
                inputs[i][0], inputs[i][1], output, expected[i]);
        }
    }
}
