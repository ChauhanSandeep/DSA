package maths;

import java.util.HashMap;
import java.util.Map;

/**
 * ✅ Problem: Minimum Operations to Reduce an Integer to 0
 *
 * Given a positive integer `n`, in one operation you may add OR subtract any
 * power of 2 (1, 2, 4, 8, ...) to/from `n`. Return the minimum number of
 * operations required to make `n == 0`.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/minimum-operations-to-reduce-an-integer-to-0/   (Medium)
 * 🏷️ Pattern:  Math · Recursion + memoization · Greedy with lookahead
 *
 * 🧪 Example:
 *   Input:  n = 39
 *   Output: 3            // 39 + 1 = 40, 40 - 8 = 32, 32 - 32 = 0
 *
 * 🚧 Edge cases to remember:
 *   - n is itself a power of 2          → answer is 1, not 0
 *   - n = 1                             → answer is 1
 *   - long trailing run of odd levels   → "+1" wins over "-1" (e.g. 7 = 8 - 1)
 *   - isolated odd level                → "-1" wins (e.g. 5 = 4 + 1)
 *
 * 🔍 Follow-ups:
 *   1. Subtraction only allowed?   → answer is the count of set bits of n.
 *   2. Powers of 3 instead of 2?   → balanced ternary representation of n.
 *   3. Reconstruct the actual ops? → carry the chosen branch at each step.
 *
 * 🔁 Related: Number of 1 Bits (191), Integer Replacement (397).
 */
public class MinimumOperationsToReduceIntegerToZero {

    /**
     * 🧠 Intuition: think of `n` digit-by-digit in base 2 from the bottom up.
     * The lowest "level" must be cleared before any larger op can finish the
     * job. If `n` is even, that level is already 0 — defer the work to `n/2`.
     * If `n` is odd, we must spend exactly one op now, and the only two moves
     * that clear the 1's place are `-1` (kill it) or `+1` (carry it up, which
     * may collapse a long run of odd levels into one higher power, e.g.
     * 7 → 8). After the op `n` is even, so halve and recurse. Memoize because
     * both branches of an odd `n` can land on overlapping subproblems.
     *
     * Algorithm:
     *   1. f(0) = 0 — nothing to do.
     *   2. If n is even, f(n) = f(n / 2) — defer to the next level.
     *   3. If n is odd, f(n) = 1 + min(f((n - 1) / 2), f((n + 1) / 2)).
     *   4. Memoize on n to avoid recomputing shared subproblems.
     *
     * Time:  O(log n) — each recursive frame at least halves n.
     * Space: O(log n) — recursion stack + memo entries.
     *
     * @param n positive integer to reduce to 0
     * @return minimum number of add/subtract-power-of-2 operations
     */
    public int minOperations(int n) {
        return solve(n, new HashMap<>());
    }

    /**
     * Recursive worker for {@link #minOperations(int)}.
     *
     * 🧠 Intuition: same as the public method — even `n` is "free" (defer),
     * odd `n` costs one op and forks into `-1` vs `+1` before halving.
     *
     * Algorithm:
     *   1. Return 0 for the base case n == 0.
     *   2. Return cached value if present.
     *   3. Even n  → recurse on n / 2.
     *   4. Odd  n  → 1 + min(recurse on (n-1)/2, recurse on (n+1)/2).
     *
     * Time:  O(log n).
     * Space: O(log n).
     *
     * @param n    current value being reduced (n &gt;= 0)
     * @param memo cache from value to its minimum operation count
     * @return minimum operations to reduce `n` to 0
     */
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
        MinimumOperationsToReduceIntegerToZero solver =
            new MinimumOperationsToReduceIntegerToZero();

        int[] inputs   = {  1,  2,  3,  7,  8, 39, 54, 100000 };
        int[] expected = {  1,  1,  2,  2,  1,  3,  3,      6 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.minOperations(inputs[i]);
            System.out.printf("n=%-6d  ->  %d  expected=%d%n",
                inputs[i], got, expected[i]);
        }
    }
}


