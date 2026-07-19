package arrays.greedy;

import java.util.Arrays;

/**
 * Problem: Minimum Domino Rotations For Equal Row
 *
 * Each domino has a top and bottom value. You may rotate any domino, and the
 * goal is to make either all tops equal or all bottoms equal using the fewest
 * rotations, returning -1 if no value can fill a row.
 *
 * Leetcode: https://leetcode.com/problems/minimum-domino-rotations-for-equal-row/ (Medium)
 * Rating:   acceptance 54.4% (Medium) - contest rating 1541
 * Pattern:  Greedy | Candidate pruning | Constant-space counting
 *
 * Example:
 *   Input:  tops = [2,1,2,4,2,2], bottoms = [5,2,6,2,3,2]
 *   Output: 2
 *   Why:    rotating dominoes 1 and 3 makes every top value equal to 2.
 *
 * Follow-ups:
 *   1. What if domino values are not limited to 1..6?
 *      The first-domino candidate check still works without fixed-size arrays.
 *   2. Return all optimal target rows?
 *      Evaluate every valid candidate and collect those with the minimum rotations.
 *   3. What if dominoes can also be reordered?
 *      Reordering does not affect row equality, only the available top/bottom values matter.
 *
 * Related: Couples Holding Hands (765), Minimum Moves to Equal Array Elements II (462).
 */
public class MinimumDominoRotationsForEqualRow {

    public static void main(String[] args) {
        MinimumDominoRotationsForEqualRow solver = new MinimumDominoRotationsForEqualRow();
        int[][] tops = {{2, 1, 2, 4, 2, 2}, {3, 5, 1, 2, 3}};
        int[][] bottoms = {{5, 2, 6, 2, 3, 2}, {3, 6, 3, 3, 4}};
        int[] expected = {2, -1};

        for (int i = 0; i < tops.length; i++) {
            int got = solver.minDominoRotations(tops[i], bottoms[i]);
            System.out.printf("tops=%s bottoms=%s -> %d  expected=%d%n",
                Arrays.toString(tops[i]), Arrays.toString(bottoms[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: if a value can fill an entire row, it must appear in the first
     * domino, either on top or bottom. That leaves only two candidates to test.
     * For each candidate, count the cheaper direction: rotate into all tops or
     * rotate into all bottoms.
     *
     * Algorithm:
     *   1. Use tops[0] and bottoms[0] as the only possible target candidates.
     *   2. For each candidate, verify every domino contains it.
     *   3. Return the smaller valid rotation count, or -1 if neither candidate works.
     *
     * Time:  O(n) - at most two linear candidate checks.
     * Space: O(1) - only counters and candidate values are used.
     *
     * @param tops top values of the dominoes
     * @param bottoms bottom values of the dominoes
     * @return minimum rotations needed, or -1 if no equal row is possible
     */
    public int minDominoRotations(int[] tops, int[] bottoms) {
        if (tops == null || bottoms == null || tops.length != bottoms.length) {
            return -1;
        }

        int length = tops.length;

        // Try both values from the first domino as potential targets
        int candidate1 = tops[0];
        int candidate2 = bottoms[0];

        // Check minimum rotations for first candidate
        int rotationsForCandidate1 = getMinRotations(tops, bottoms, candidate1);

        // Check minimum rotations for second candidate (if different)
        int rotationsForCandidate2 = getMinRotations(tops, bottoms, candidate2);

        // Return minimum of valid rotations, or -1 if none possible
        int minRotations = Math.min(rotationsForCandidate1, rotationsForCandidate2);
        return minRotations == Integer.MAX_VALUE ? -1 : minRotations;
    }

    // Helper method to calculate minimum rotations for a target value
    private int getMinRotations(int[] tops, int[] bottoms, int target) {
        int rotationsToMakeAllTopsEqual = 0;
        int rotationsToMakeAllBottomsEqual = 0;

        for (int i = 0; i < tops.length; i++) {
            // Check if target value exists in current domino
            if (tops[i] != target && bottoms[i] != target) {
                return Integer.MAX_VALUE; // Impossible to achieve target
            }

            // Count rotations needed for all tops to be target
            if (tops[i] != target) {
                rotationsToMakeAllTopsEqual++; // Need to rotate this domino
            }

            // Count rotations needed for all bottoms to be target
            if (bottoms[i] != target) {
                rotationsToMakeAllBottomsEqual++; // Need to rotate this domino
            }
        }

        // Return minimum rotations between making all tops or all bottoms equal
        return Math.min(rotationsToMakeAllTopsEqual, rotationsToMakeAllBottomsEqual);
    }

    /**
     * Alternative approach using frequency counting for all possible values.
     * More comprehensive but slightly less efficient for this specific problem.
     *
     * Algorithm:
     * 1. Count frequency of each value (1-6) in tops, bottoms, and combined
     * 2. For each value that appears in all dominoes, calculate minimum rotations
     * 3. A value appears in all dominoes if: topCount + bottomCount - bothCount = n
     * 4. Return minimum rotations among all valid target values
     *
     * Time Complexity: O(n) where n is number of dominoes
     * Space Complexity: O(1) using fixed-size arrays for counting
     *
     * @param tops array representing top values of dominoes
     * @param bottoms array representing bottom values of dominoes
     * @return minimum rotations needed, or -1 if impossible
     */
    public int minDominoRotationsAlternative(int[] tops, int[] bottoms) {
        if (tops == null || bottoms == null || tops.length != bottoms.length) {
            return -1;
        }

        int length = tops.length;
        int[] topCount = new int[7];     // Count of each value in tops (1-6)
        int[] bottomCount = new int[7];  // Count of each value in bottoms (1-6)
        int[] bothCount = new int[7];    // Count where same value appears in both

        // Count frequencies
        for (int i = 0; i < length; i++) {
            topCount[tops[i]]++;
            bottomCount[bottoms[i]]++;
            if (tops[i] == bottoms[i]) {
                bothCount[tops[i]]++;
            }
        }

        int result = Integer.MAX_VALUE;

        // Check each possible value (1-6)
        for (int value = 1; value <= 6; value++) {
            // Check if this value can fill entire row
            if (topCount[value] + bottomCount[value] - bothCount[value] == length) {
                // Calculate rotations needed
                int rotationsForTops = length - topCount[value];
                int rotationsForBottoms = length - bottomCount[value];

                result = Math.min(result, Math.min(rotationsForTops, rotationsForBottoms));
            }
        }

        return result == Integer.MAX_VALUE ? -1 : result;
    }
}
