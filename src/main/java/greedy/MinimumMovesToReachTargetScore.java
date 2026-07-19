package greedy;

/**
 * Problem: Minimum Moves to Reach Target Score
 *
 * Start at 1 and reach target. One move can increment the current value by 1,
 * and at most maxDoubles moves can double the current value. Return the fewest
 * moves needed.
 *
 * Leetcode: https://leetcode.com/problems/minimum-moves-to-reach-target-score/ (Medium)
 * Rating:   1417 (zerotrac Elo)
 * Pattern:  Greedy | Work backward | Halve whenever it is legal
 *
 * Example:
 *   Input:  target = 19, maxDoubles = 2
 *   Output: 7
 *   Why:    working backward gives 19 -> 18 -> 9 -> 8 -> 4, then three more
 *           decrements reach 1, for seven reversed moves total.
 *
 * Follow-ups:
 *   1. What if multiply by any factor k is allowed?
 *      Work backward and divide by k when divisible; otherwise decrement toward a divisible value.
 *   2. What if operations have different costs?
 *      Use dynamic programming or shortest path on values because move count no longer captures cost.
 *   3. Return the actual forward operations?
 *      Record backward operations and reverse them, swapping divide with double and decrement with increment.
 *   4. What if maxDoubles is unlimited?
 *      The answer is the number of halving/decrement steps in the binary reduction of target.
 *
 * Related: Broken Calculator (991), 2 Keys Keyboard (650).
 */
public class MinimumMovesToReachTargetScore {

    public static void main(String[] args) {
        MinimumMovesToReachTargetScore solver = new MinimumMovesToReachTargetScore();
        int[] targets = {19, 5, 10, 1};
        int[] maxDoubles = {2, 0, 4, 3};
        int[] expected = {7, 4, 4, 0};

        for (int i = 0; i < targets.length; i++) {
            int got = solver.minMoves(targets[i], maxDoubles[i]);
            System.out.printf("target=%d maxDoubles=%d -> %d  expected=%d%n",
                targets[i], maxDoubles[i], got, expected[i]);
        }
    }


    /**
     * Intuition: choosing when to double is hard in the forward direction because
     * it changes how many increments are needed later. Work backward instead.
     * An odd target could not have come from a final double, so it must be
     * decremented. An even target can be halved while doubles remain, and that is
     * always at least as good as undoing many increments one by one. Once doubles
     * are exhausted, only plain decrements remain.
     *
     * Algorithm:
     *   1. Work backward from target while target > 1 and doubles remain.
     *   2. If target is odd, decrement it.
     *   3. If target is even, divide it by 2 and spend one double.
     *   4. Add target - 1 for the remaining decrement-only distance to 1.
     *
     * Time:  O(log target) - each useful double halves the remaining target, with at most one odd decrement before it.
     * Space: O(1) - only counters and the shrinking target are stored.
     *
     * @param target value to reach from 1
     * @param maxDoubles maximum number of double operations allowed
     * @return minimum number of moves needed
     */
    public int minMoves(int target, int maxDoubles) {
        if (target == 1) return 0;

        int moves = 0;

        // Work backwards from target while we have doubles available
        while (maxDoubles > 0 && target > 1) {
            moves++;

            if (target % 2 == 1) {
                // Target is odd, must decrement to make it even
                target--;
            } else {
                // Target is even, use a double operation (divide by 2 in reverse)
                target /= 2;
                maxDoubles--;
            }
        }

        // After exhausting doubles, only increment operations remain
        moves += target - 1;

        return moves;
    }

    /**
     * Alternative implementation with explicit tracking of operation types.
     * Useful for understanding what operations are actually performed.
     *
     * Time Complexity: O(min(log target, maxDoubles))
     * Space Complexity: O(1)
     */
    public int minMovesWithTracking(int target, int maxDoubles) {
        if (target == 1) return 0;

        int moves = 0;
        int doublesUsed = 0;
        int incrementsUsed = 0;

        while (maxDoubles > 0 && target > 1) {
            if (target % 2 == 1) {
                target--;
                incrementsUsed++;
            } else {
                target /= 2;
                doublesUsed++;
                maxDoubles--;
            }
            moves++;
        }

        // Add remaining increments needed
        int remainingIncrements = target - 1;
        incrementsUsed += remainingIncrements;
        moves += remainingIncrements;

        return moves;
    }

    /**
     * Forward simulation approach (less efficient but intuitive).
     * Builds from 1 to target using optimal strategy.
     *
     * Time Complexity: O(target) in worst case
     * Space Complexity: O(1)
     */
    public int minMovesForward(int target, int maxDoubles) {
        if (target == 1) return 0;

        int current = 1;
        int moves = 0;
        int doublesRemaining = maxDoubles;

        while (current < target) {
            if (doublesRemaining > 0 && shouldDouble(current, target, doublesRemaining)) {
                current *= 2;
                doublesRemaining--;
            } else {
                current++;
            }
            moves++;
        }

        return moves;
    }

    /** Decides whether the forward simulation should spend a double now. */
    private boolean shouldDouble(int current, int target, int doublesRemaining) {
        if (doublesRemaining == 0) return false;

        // Simple heuristic: double if it gets us closer to target without overshooting significantly
        int afterDouble = current * 2;
        int afterIncrement = current + 1;

        // If doubling would overshoot by too much, prefer increment
        if (afterDouble > target && afterDouble - target > target - current) {
            return false;
        }

        return afterDouble <= target * 2; // Conservative doubling strategy
    }

    /**
     * Mathematical approach using bit manipulation insights.
     * Uses the fact that optimal strategy involves doubling at powers of 2 when possible.
     *
     * Time Complexity: O(log target)
     * Space Complexity: O(1)
     */
    public int minMovesBitwise(int target, int maxDoubles) {
        if (target == 1) return 0;

        int moves = 0;

        while (maxDoubles > 0 && target > 1) {
            moves++;

            if ((target & 1) == 1) { // target is odd
                target--;
            } else { // target is even
                target >>= 1; // equivalent to target /= 2
                maxDoubles--;
            }
        }

        return moves + target - 1;
    }

    /**
     * Dynamic programming approach for comparison (overkill for this problem).
     * Demonstrates how DP could be used if problem had more complex constraints.
     *
     * Time Complexity: O(target * maxDoubles)
     * Space Complexity: O(target * maxDoubles)
     */
    public int minMovesDP(int target, int maxDoubles) {
        if (target == 1) return 0;

        // dp[i][j] = minimum moves to reach i using at most j doubles
        int[][] dp = new int[target + 1][maxDoubles + 1];

        // Initialize with large values
        for (int i = 0; i <= target; i++) {
            for (int j = 0; j <= maxDoubles; j++) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }

        // Base case
        for (int j = 0; j <= maxDoubles; j++) {
            dp[1][j] = 0;
        }

        // Fill DP table
        for (int i = 2; i <= target; i++) {
            for (int j = 0; j <= maxDoubles; j++) {
                // Option 1: increment from i-1
                if (dp[i - 1][j] != Integer.MAX_VALUE) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j] + 1);
                }

                // Option 2: double from i/2 (if i is even and we have doubles)
                if (i % 2 == 0 && j > 0 && dp[i / 2][j - 1] != Integer.MAX_VALUE) {
                    dp[i][j] = Math.min(dp[i][j], dp[i / 2][j - 1] + 1);
                }
            }
        }

        return dp[target][maxDoubles];
    }

    /**
     * Validation method for input constraints.
     *
     * @param target Target value
     * @param maxDoubles Maximum doubles allowed
     * @return true if inputs are valid
     */
    public boolean validateInputs(int target, int maxDoubles) {
        return target >= 1 && target <= 1_000_000_000 && maxDoubles >= 0 && maxDoubles <= 100;
    }
}