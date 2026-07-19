package arrays;

import java.util.Arrays;
/**
 * Problem: Can Place Flowers
 *
 * A flowerbed is a row of plots where 1 means occupied and 0 means empty. Decide
 * whether n new flowers can be planted without ever placing flowers in adjacent
 * plots. Existing flowers already satisfy the rule.
 *
 * Leetcode: https://leetcode.com/problems/can-place-flowers/ (Easy)
 * Rating:   acceptance 29.2% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Greedy | Local neighbour check
 *
 * Example:
 *   Input:  flowerbed = [1,0,0,0,1], n = 1
 *   Output: true
 *   Why:    the middle plot has empty neighbours on both sides, so planting there
 *           gives [1,0,1,0,1] without breaking the adjacency rule.
 *
 * Follow-ups:
 *   1. Return the maximum number of flowers that can be planted?
 *      Keep the same greedy scan and return the final planted count instead of a boolean.
 *   2. Make the required distance between flowers k plots instead of one?
 *      Track the nearest occupied/planted plot and require a gap of at least k + 1.
 *   3. Handle a circular flowerbed?
 *      Treat the first and last plots as neighbours and special-case whether either is planted.
 *
 * Related: Teemo Attacking (495), Gas Station (134).
 */
public class CanPlaceFlowers {

    public static void main(String[] args) {
        CanPlaceFlowers solver = new CanPlaceFlowers();

        int[][] inputs = { {1, 0, 0, 0, 1}, {1, 0, 0, 0, 1}, {0} };
        int[] flowers = { 1, 2, 1 };
        boolean[] expected = { true, false, true };

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.canPlaceFlowers(inputs[i].clone(), flowers[i]);
            System.out.printf("flowerbed=%s n=%d  ->  %s  expected=%s%n",
                Arrays.toString(inputs[i]), flowers[i], got, expected[i]);
        }
    }

    /**
     * Intuition: planting greedily from left to right is safe because a valid flower at
     * position i can only block position i + 1; it never helps to postpone that flower
     * to a later plot. For each empty plot, check both neighbours as if out-of-bounds
     * edges were empty, plant when possible, and stop once all n flowers are placed.
     *
     * Algorithm:
     *   1. Scan flowerbed while flowers still need to be planted.
     *   2. For each empty plot, check that the left and right neighbours are empty or outside the array.
     *   3. Plant there by writing 1 and decrement n when both neighbours allow it.
     *   4. Return whether n reached 0.
     *
     * Time:  O(m) - each plot is inspected at most once.
     * Space: O(1) - the input flowerbed is modified in place.
     *
     * @param flowerbed row of plots where 1 is occupied and 0 is empty
     * @param n number of new flowers to place
     * @return true if at least n flowers can be planted without adjacency
     */
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        int length = flowerbed.length;

        for (int i = 0; i < length && n > 0; i++) {
            // Check if current position is empty
            if (flowerbed[i] == 0) {
                // Check left neighbor (empty or out of bounds)
                boolean leftEmpty = (i == 0) || (flowerbed[i - 1] == 0);
                // Check right neighbor (empty or out of bounds)
                boolean rightEmpty = (i == length - 1) || (flowerbed[i + 1] == 0);

                // If both neighbors are empty, plant flower
                if (leftEmpty && rightEmpty) {
                    flowerbed[i] = 1;
                    n--;
                }
            }
        }

        return n == 0;
    }

    /**
     * Alternative approach without modifying input array
     * In this approach, when a flower is planted, we skip the next position
     * since we cannot plant adjacent flowers.
     *
     * Time Complexity: O(m) where m is length of flowerbed
     * Space Complexity: O(1) - no extra space used
     */
    public boolean canPlaceFlowersNoModify(int[] flowerbed, int n) {
        int length = flowerbed.length;
        int planted = 0;

        for (int i = 0; i < length && planted < n; i++) {
            if (flowerbed[i] == 0) {
                boolean leftEmpty = (i == 0) || (flowerbed[i - 1] == 0);
                boolean rightEmpty = (i == length - 1) || (flowerbed[i + 1] == 0);

                if (leftEmpty && rightEmpty) {
                    planted++;
                    // Skip next position since we can't plant adjacent
                    i++;
                }
            }
        }

        return planted >= n;
    }
}
