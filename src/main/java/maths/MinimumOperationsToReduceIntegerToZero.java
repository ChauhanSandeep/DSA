package maths;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Minimum Operations to Reduce an Integer to 0
 *
 * Given a positive integer n, one operation may add or subtract any power of 2.
 * Return the minimum number of operations needed to reduce n to 0.
 *
 * Leetcode: https://leetcode.com/problems/minimum-operations-to-reduce-an-integer-to-0/ (Medium)
 * Rating:   1649 (zerotrac Elo)
 * Pattern:  Math | Recursion with memoization | Binary carry choices
 *
 * Example:
 *   Input:  n = 39
 *   Output: 3
 *   Why:    39 + 1 = 40, 40 - 8 = 32, and 32 - 32 = 0.
 *
 * Follow-ups:
 *   1. What if only subtraction is allowed?
 *      The answer becomes the number of set bits in n.
 *   2. What if powers of 3 are allowed instead of powers of 2?
 *      Use balanced ternary ideas to decide whether to round each digit up or down.
 *   3. How would you reconstruct the actual operations?
 *      Store which branch wins for each memoized n and replay those choices.
 *
 * Related: Integer Replacement (397), Number of 1 Bits (191).
 */

public class MinimumOperationsToReduceIntegerToZero {

        /**
     * Intuition: inspect n from the lowest binary bit upward. If n is even, the
     * lowest bit is already clear and the same problem remains after dividing by
     * 2. If n is odd, one operation must either subtract 1 or add 1 before that
     * division; memoization keeps overlapping odd branches from being recomputed.
     *
     * Algorithm:
     *   1. Start the recursive solve with an empty memo map.
     *   2. In solve, return 0 for n == 0 and 1 for n == 1.
     *   3. For even n, recurse on n / 2.
     *   4. For odd n, take 1 + min(solve((n - 1) / 2), solve((n + 1) / 2)).
     *   5. Cache each computed n before returning it.
     *
     * Time:  O(log n) - each recursive branch quickly halves the value.
     * Space: O(log n) - memo entries and recursion depth follow the bit length.
     *
     * @param n positive integer to reduce to zero
     * @return minimum number of operations
     */

    public int minOperations(int n) {
        return solve(n, new HashMap<>());
    }

        /** Recursively computes the best operation count for a reduced subproblem. */

    private int solve(int n, Map<Integer, Integer> memo) {
        if (n == 0) return 0;
        // ✅ guard self-recursion: for odd n, (n+1)/2 == n when n == 1
        if (n == 1) return 1;

        Integer cached = memo.get(n);
        if (cached != null) return cached;

        int best;
        if (n % 2 == 0) {
            // --- Step 1: even level is already clean — defer upward -----
            best = solve(n / 2, memo);
        } else {
            // --- Step 2: odd level — pay one op, try both ways to clear -
            int subtractOne = solve((n - 1) / 2, memo);
            // ✅ +1 can collapse a long run of odd levels into one higher power
            int addOne      = solve((n + 1) / 2, memo);
            best = 1 + Math.min(subtractOne, addOne);
        }

        memo.put(n, best);
        return best;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        MinimumOperationsToReduceIntegerToZero solver = new MinimumOperationsToReduceIntegerToZero();
        int[] inputs = { 1, 8, 39, 54 };
        int[] expected = { 1, 1, 3, 3 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.minOperations(inputs[i]);
            System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }
}


