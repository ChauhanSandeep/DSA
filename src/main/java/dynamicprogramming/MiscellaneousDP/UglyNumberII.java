package dynamicprogramming.MiscellaneousDP;

/**
 * Problem: Ugly Number II
 *
 * Ugly numbers are positive integers whose only prime factors are 2, 3, and 5.
 * Given n, return the nth ugly number in increasing order, counting 1 as the
 * first ugly number.
 *
 * Leetcode: https://leetcode.com/problems/ugly-number-ii/
 * Rating:   acceptance 49.7% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic programming | Three pointers | Generated sorted sequence
 *
 * Example:
 *   Input:  n = 10
 *   Output: 12
 *   Why:    the first ten ugly numbers are 1,2,3,4,5,6,8,9,10,12, so the tenth is 12.
 *
 * Follow-ups:
 *   1. What if the allowed primes are an arbitrary list?
 *      Use the same pointer idea with one pointer per prime, as in Super Ugly Number.
 *   2. What if n is so large that the value overflows int?
 *      Store candidates in long or BigInteger and decide how the API should handle overflow.
 *   3. Can this be generated lazily?
 *      Keep the DP list and pointers as iterator state and emit one next value at a time.
 *
 * Related: Ugly Number (263), Super Ugly Number (313), Ugly Number III (1201).
 */
public class UglyNumberII {
/**
     * Intuition: dp[index] is the sorted list of ugly numbers discovered so far.
     * The next ugly number must be some earlier ugly number multiplied by 2, 3, or
     * 5, so three pointers track the smallest unused product for each factor. We
     * append the minimum candidate and advance every pointer that produced it; this
     * keeps the sequence sorted and removes duplicates like 6 from 2*3 and 3*2.
     *
     * Algorithm:
     *   1. Return 0 for non-positive n, then seed ugly[0] = 1.
     *   2. Track p2, p3, and p5 as the next multipliers for 2, 3, and 5.
     *   3. Repeatedly write the minimum of ugly[p2]*2, ugly[p3]*3, and ugly[p5]*5.
     *   4. Advance every pointer that produced the chosen value and return ugly[n-1].
     *
     * Time:  O(n) - each ugly number is produced once and each pointer only moves forward.
     * Space: O(n) - the DP array stores the first n ugly numbers for future multiples.
     *
     * @param n one-based position in the ugly-number sequence
     * @return the nth ugly number, or 0 for non-positive n
     */
    public int nthUglyNumber(int n) {
        if (n <= 0) return 0;

        int[] ugly = new int[n];
        ugly[0] = 1;

        // Pointers for 2, 3, 5
        int p2 = 0, p3 = 0, p5 = 0;

        for (int i = 1; i < n; i++) {
            // Find the next ugly number by multiplying with 2, 3, or 5
            int next2 = ugly[p2] * 2;
            int next3 = ugly[p3] * 3;
            int next5 = ugly[p5] * 5;

            // Get the minimum of the three candidates
            int nextUgly = Math.min(next2, Math.min(next3, next5));
            ugly[i] = nextUgly;

            // Move the pointers forward if their product equals the current ugly number
            if (nextUgly == next2) p2++;
            if (nextUgly == next3) p3++;
            if (nextUgly == next5) p5++;
        }

        return ugly[n - 1];
    }

    public static void main(String[] args) {
        UglyNumberII solver = new UglyNumberII();
        int[] inputs = {0, 1, 10, 15};
        int[] expected = {0, 1, 12, 24};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.nthUglyNumber(inputs[i]);
            System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }
}
