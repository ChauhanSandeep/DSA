package arrays;

import java.util.Arrays;
/**
 * Problem: Minimum Deletion Cost to Avoid Repeating Letters
 *
 * Each character in a string has a deletion cost. Delete characters so that no
 * two adjacent remaining characters are equal, and return the minimum total cost
 * paid. Only consecutive groups of the same character matter.
 *
 * Leetcode: https://leetcode.com/problems/minimum-time-to-make-rope-colorful/ (Medium)
 * Rating:   1574 (zerotrac Elo)
 * Pattern:  Array | Greedy | Consecutive group compression
 *
 * Example:
 *   Input:  s = "abaac", cost = [1,2,3,4,5]
 *   Output: 3
 *   Why:    the only bad group is "aa" with costs 3 and 4, so deleting the
 *           cheaper one costs 3 and leaves no equal neighbours.
 *
 * Follow-ups:
 *   1. Keep the minimum-cost character in each group instead?
 *      Then you are maximizing deletion cost, so delete everything except the cheapest item.
 *   2. Allow at most k deletions total?
 *      Use dynamic programming over position, previous kept character, and deletions used.
 *   3. Remove groups of length three or more instead of adjacent pairs?
 *      Process each equal-character run and keep up to two highest-cost characters.
 *
 * Related: Remove All Adjacent Duplicates in String (1047), Candy Crush (723).
 */
public class DeleteRepeatingLetter {

    public static void main(String[] args) {
        DeleteRepeatingLetter solver = new DeleteRepeatingLetter();

        String[] strings = { "abaac", "abc", "aabaa" };
        int[][] costs = { {1, 2, 3, 4, 5}, {1, 2, 3}, {1, 2, 3, 4, 1} };
        int[] expected = { 3, 0, 2 };

        for (int i = 0; i < strings.length; i++) {
            int got = solver.minDeletionCost(strings[i], costs[i]);
            System.out.printf("s=%s cost=%s  ->  %d  expected=%d%n",
                strings[i], Arrays.toString(costs[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: only consecutive equal-character groups create conflicts. In one such
     * group, all but one character must be deleted, and keeping the most expensive
     * character minimizes deletion cost. Track the total cost and maximum cost of the
     * current group, then pay total minus maximum whenever the group ends.
     *
     * Algorithm:
     *   1. Scan the string while accumulating currentGroupCost and maxCostInGroup.
     *   2. Continue the group while adjacent characters are equal.
     *   3. When a new character starts, add currentGroupCost - maxCostInGroup to the answer.
     *   4. Process the final group after the loop and return the total deletion cost.
     *
     * Time:  O(n) - each character and cost is processed once.
     * Space: O(1) - only current group totals are stored.
     *
     * @param str input string whose adjacent equal letters must be separated
     * @param cost deletion cost for each character
     * @return minimum total deletion cost
     */
    public int minDeletionCost(String str, int[] cost) {
        int strLength = str.length();
        int currentGroupCost = 0;
        int maxCostInGroup = 0;
        int totalDeletionCost = 0;

        for (int i = 0; i < strLength; i++) {
            if (i == 0 || str.charAt(i - 1) == str.charAt(i)) {
                // Still in the same consecutive character group
                currentGroupCost += cost[i];
                maxCostInGroup = Math.max(maxCostInGroup, cost[i]);
            } else {
                // New character - finalize previous group
                totalDeletionCost += (currentGroupCost - maxCostInGroup);
                currentGroupCost = cost[i];
                maxCostInGroup = cost[i];
            }
        }
        
        // Process the final group
        totalDeletionCost += (currentGroupCost - maxCostInGroup);
        return totalDeletionCost;
    }
}
