package arrays.greedy;

/**
 * Problem: Minimum Domino Rotations For Equal Row
 *
 * In a row of dominoes, tops[i] and bottoms[i] represent the top and bottom halves
 * of the ith domino. A domino is a tile with two numbers from 1 to 6 - one on each
 * half of the tile. We may rotate the ith domino, so that tops[i] and bottoms[i]
 * swap values.
 *
 * Return the minimum number of rotations so that all the values in tops are the same,
 * or all the values in bottoms are the same. If it cannot be done, return -1.
 *
 * Example:
 * Input:
 * tops =    [2,1,2,4,2,2],
 * bottoms = [5,2,6,2,3,2]
 * Output: 2
 * Explanation:
 * We can rotate the second and fourth dominoes to make all top values equal to 2.
 * After:  tops=[2,2,2,2,2,2], bottoms=[5,1,6,4,3,2]
 *
 * Input: 
 * tops =    [3, 5, 1, 2, 3], 
 * bottoms = [3, 6, 3, 3, 4]
 * Output: -1
 * Explanation: No single value can fill an entire row after rotations.
 *
 * LeetCode: https://leetcode.com/problems/minimum-domino-rotations-for-equal-row
 *
 * Follow-up Questions:
 * 1. Q: What if dominoes could have values outside 1-6 range?
 *    A: The algorithm works the same, just need to adjust data structures accordingly.
 *
 * 2. Q: How would you handle the case where multiple solutions exist with same minimum?
 *    A: Current solution returns the minimum count. Could be modified to return all optimal configurations.
 *
 * 3. Q: What if we could rearrange dominoes in addition to rotating them?
 *    A: That would be a different problem requiring sorting/greedy approach for placement.
 *
 * 4. Q: How would you optimize for very large arrays?
 *    A: Current O(n) solution is already optimal. Could use early termination optimizations.
 *
 * Related Problems:
 * - Best Meeting Point: https://leetcode.com/problems/best-meeting-point/
 * - Minimum Moves to Equal Array Elements II: https://leetcode.com/problems/minimum-moves-to-equal-array-elements-ii/
 * - Flip String to Monotone Increasing: https://leetcode.com/problems/flip-string-to-monotone-increasing/
 * LeetCode Contest Rating: 1541
 */
public class MinimumDominoRotationsForEqualRow {

    /**
     * Finds minimum rotations by checking candidate values from first domino.
     *
     * Algorithm:
     * 1. For a solution to exist, the target value must appear in every domino
     * 2. The only possible target values are tops[0] and bottoms[0] (from first domino)
     * 3. For each candidate, check if it appears in all dominoes (top or bottom)
     * 4. Count rotations needed to make all tops or all bottoms equal to candidate
     * 5. Return minimum rotations among all valid configurations
     *
     * Time Complexity: O(n) where n is number of dominoes
     * Space Complexity: O(1) using constant extra space
     *
     * @param tops array representing top values of dominoes
     * @param bottoms array representing bottom values of dominoes
     * @return minimum rotations needed, or -1 if impossible
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
