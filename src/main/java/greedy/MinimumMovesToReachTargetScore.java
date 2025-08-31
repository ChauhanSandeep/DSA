package greedy;

/**
 * Problem: Minimum Moves to Reach Target Score
 *
 * You are playing a game with integers. You start with the integer 1 and you want to reach the
 * integer target. In one move, you can either:
 * - Increment the current integer by one (i.e., x = x + 1).
 * - Double the current integer (i.e., x = 2 * x).
 * You can use the increment operation any number of times, however, you can only use the double
 * operation at most maxDoubles times. Given the two integers target and maxDoubles, return the
 * minimum number of moves needed to reach target starting with 1.
 *
 * Example:
 * Input: target = 19, maxDoubles = 2
 * Output: 7
 * Explanation: Initially, x = 1. Increment 3 times so x = 4. Double once so x = 8.
 * Increment once so x = 9. Double again so x = 18. Increment once so x = 19.
 *
 * LeetCode: https://leetcode.com/problems/minimum-moves-to-reach-target-score
 *
 * Follow-up Questions:
 * 1. What if we could also divide by 2 (reverse of doubling)?
 *    Answer: Work backwards from target, using division when possible and subtraction when not.
 *
 * 2. How would you handle the case where we can multiply by any factor k?
 *    Answer: Similar backwards approach, but check all possible factors at each step.
 *
 * 3. What if there were costs associated with different operations?
 *    Answer: Use dynamic programming with cost optimization instead of simple move counting.
 *    Related: https://leetcode.com/problems/2-keys-keyboard/
 */
public class MinimumMovesToReachTargetScore {

    /**
     * Finds minimum moves using greedy backwards approach.
     *
     * Algorithm:
     * 1. Work backwards from target to 1
     * 2. If target is even and we have doubles left, divide by 2 (reverse of doubling)
     * 3. If target is odd or no doubles left, decrement by 1
     * 4. Count each operation as one move
     * 5. After using all doubles, remaining distance is target - 1
     *
     * Time Complexity: O(min(log target, maxDoubles)) - each double halves the target
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param target Target value to reach
     * @param maxDoubles Maximum number of double operations allowed
     * @return Minimum number of moves needed
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

    // Helper method to decide when to use double in forward approach
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