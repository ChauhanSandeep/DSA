package backtrack;

import java.util.Arrays;

/**
 * Problem: Matchsticks to Square
 *
 * Given matchstick lengths, decide whether every stick can be used exactly once
 * to form a square. Sticks cannot be broken, so the task is assigning each
 * length to one of four sides with equal final sums.
 *
 * Leetcode: https://leetcode.com/problems/matchsticks-to-square/
 * Rating:   acceptance 42.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Four-bucket partition | Symmetry pruning
 *
 * Example:
 *   Input:  [1,1,2,2,2]
 *   Output: true
 *   Why:    the total length is 8, so each side must be 2; the sticks can make
 *           [2], [2], [1,1], and [2] using every stick once.
 *
 * Follow-ups:
 *   1. Return the actual four sides?
 *      Carry four lists beside the side sums and copy them at the successful leaf.
 *   2. Generalize to a rectangle?
 *      Use two target side lengths, with two buckets for each target.
 *   3. Avoid repeated DFS states?
 *      Memoize by (usedMask, currentSideSum, completedSides) or use subset DP.
 *   4. Count all distinct squares?
 *      Canonicalize side order and skip equal side-sum branches to avoid permutation duplicates.
 *
 * Related: Partition to K Equal Sum Subsets (698), Fair Distribution of Cookies (2305).
 */
public class MatchsticksToSquare {
    private static final int SIDE_COUNT = 4;

    /**
     * Intuition: making a square is the same as splitting all sticks into four
     * groups with the same sum. The target side length is forced by the total sum,
     * so every decision is just, "which side should this stick join?" Trying long
     * sticks first makes impossible choices fail sooner. Also, the four sides have
     * no names; putting a stick onto two sides with the same current length creates
     * the same state, so those symmetric choices can be skipped.
     *
     * Algorithm:
     *   1. Reject inputs with fewer than four sticks or a total length not divisible by four.
     *   2. Sort the sticks and fail early if the largest stick is longer than the target side.
     *   3. Starting from the largest stick, try placing it on any side that would not exceed the target.
     *   4. Skip a side when an earlier side already has the same current length,
     *      because that branch would be a duplicate with sides renamed.
     *   5. Add the stick to a side, recurse to the next stick, and remove it if that placement fails.
     *
     * Time:  O(4^n) - each stick may be tried on four sides before pruning removes bad branches.
     * Space: O(n) recursion depth plus four side sums.
     *
     * @param matchsticks lengths of all sticks
     * @return true if the sticks can form one square
     */
    public boolean makesquare(int[] matchsticks) {
        if (matchsticks == null || matchsticks.length < SIDE_COUNT) return false;

        int totalLength = 0;
        for (int stick : matchsticks) totalLength += stick;
        if (totalLength % SIDE_COUNT != 0) return false;

        int targetSideLength = totalLength / SIDE_COUNT;
        Arrays.sort(matchsticks);
        if (matchsticks[matchsticks.length - 1] > targetSideLength) return false;

        return canBuildSquare(matchsticks, matchsticks.length - 1, new int[SIDE_COUNT], targetSideLength);
    }

    /** Assigns matchsticks to side sums from largest to smallest. */
    private boolean canBuildSquare(int[] matchsticks, int index, int[] sideLengths, int targetSideLength) {
        if (index == -1) {
            for (int sideLength : sideLengths) {
                if (sideLength != targetSideLength) return false;
            }
            return true;
        }

        int currentStick = matchsticks[index];
        for (int side = 0; side < SIDE_COUNT; side++) {
            if (sideLengths[side] + currentStick > targetSideLength) continue;
            if (hasEquivalentEarlierSide(sideLengths, side)) continue;

            sideLengths[side] += currentStick;
            if (canBuildSquare(matchsticks, index - 1, sideLengths, targetSideLength)) return true;
            sideLengths[side] -= currentStick;
        }
        return false;
    }

    /** Detects whether this side choice duplicates an earlier side with the same current length. */
    private boolean hasEquivalentEarlierSide(int[] sideLengths, int side) {
        for (int previousSide = 0; previousSide < side; previousSide++) {
            if (sideLengths[previousSide] == sideLengths[side]) return true;
        }
        return false;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        MatchsticksToSquare solver = new MatchsticksToSquare();

        int[][] inputs = {
            {1, 1, 2, 2, 2},
            {3, 3, 3, 3, 4},
            {}
        };
        boolean[] expected = {true, false, false};

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.makesquare(inputs[i].clone());
            System.out.printf("matchsticks=%s  ->  %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }
}
