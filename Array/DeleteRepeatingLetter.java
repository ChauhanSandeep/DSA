package Array;

/**
 * Given a string s and an array of integers cost where cost[i] is the cost of deleting the ith character in s.
 * Return the minimum cost of deletions such that there are no two identical letters next to each other.
 *
 * Approach:
 * - Iterate through the string while tracking consecutive duplicate characters.
 * - Keep the highest-cost character in each duplicate sequence.
 * - Sum up the deletion costs of the rest.
 *
 * Time Complexity: O(N) - Single pass through the string.
 * Space Complexity: O(1) - Uses only a few variables.
 *
 * LeetCode: https://leetcode.com/problems/minimum-deletion-cost-to-avoid-repeating-letters/
 */
public class DeleteRepeatingLetter {
    public static void main(String[] args) {
        int[] cost = {1, 2, 3, 4, 5};
        System.out.println("Minimum deletion cost: " + new DeleteRepeatingLetter().minDeletionCost("abaac", cost));
    }

    /**
     * Calculates the minimum deletion cost to remove adjacent duplicate letters.
     *
     * @param str  Input string.
     * @param cost Deletion cost for each character.
     * @return Minimum total deletion cost.
     */
    public int minDeletionCost(String str, int[] cost) {
        int len = str.length();
        int currentGroupCost = 0;
        int maxCostInGroup = 0;
        int totalDeletionCost = 0;

        for(int i=0; i<len; i++) {
            if(i == 0 || str.charAt(i-1) == str.charAt(i)) {
                // still in same character group
                currentGroupCost += cost[i];
                maxCostInGroup = Math.max(maxCostInGroup, cost[i]);
            } else {
                totalDeletionCost = totalDeletionCost + (currentGroupCost - maxCostInGroup);
                currentGroupCost = cost[i];
                maxCostInGroup = cost[i];
            }
        }
        totalDeletionCost = totalDeletionCost + (currentGroupCost - maxCostInGroup);
        return totalDeletionCost;
    }
}