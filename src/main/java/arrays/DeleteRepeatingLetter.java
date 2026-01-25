package arrays;

/**
 * Minimum Deletion Cost to Avoid Repeating Letters
 *
 * Problem:
 * Given a string s and an array of integers cost where cost[i] is the cost of deleting the ith character in s,
 * return the minimum cost of deletions such that there are no two identical consecutive letters.
 *
 * Example:
 * Input: s = "abaac", cost = [1,2,3,4,5]
 * Output: 3
 * Explanation: Delete 'a' at index 2 (cost 3) to get "abac" with no consecutive duplicates.
 *
 * Constraints:
 * - 1 <= s.length == cost.length <= 10^5
 * - 1 <= cost[i] <= 10^4
 * - s contains only lowercase English letters
 *
 * LeetCode: https://leetcode.com/problems/minimum-deletion-cost-to-avoid-repeating-letters/
 *
 * Follow-up Questions:
 * Q1: What if we need to maximize the cost of deletions instead (keep minimum cost characters)?
 * A1: For each consecutive group, keep the minimum cost character and delete the rest.
 *
 * Q2: How would you handle the case where we can delete at most k characters?
 * A2: Use dynamic programming with state tracking remaining deletions, or greedy approach prioritizing high-cost groups.
 *
 * Q3: What if adjacent characters can differ by at most 1 in ASCII value (e.g., 'a' and 'b' are too close)?
 * A3: Modify the grouping condition to check if abs(s[i] - s[i-1]) <= 1 instead of equality.
 *
 * Q4: Can we solve this problem if we're allowed to swap characters instead of delete them?
 * A4: Use a different greedy approach - try to break consecutive sequences by swapping with distant characters.
 *
 * Q5: How would you extend this to remove groups of 3 or more consecutive identical characters?
 * A5: Same approach but only process groups where count >= 3, keeping the highest-cost character in each group.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class DeleteRepeatingLetter {
    public static void main(String[] args) {
        int[] cost = {1, 2, 3, 4, 5};
        System.out.println("Minimum deletion cost: " + new DeleteRepeatingLetter().minDeletionCost("abaac", cost));
    }

    /**
     * Calculates the minimum deletion cost to remove all adjacent duplicate letters.
     *
     * Algorithm:
     * 1. Iterate through the string tracking consecutive duplicate character groups
     * 2. For each group of duplicates, sum up all costs and track the maximum cost
     * 3. Delete all characters except the one with maximum cost (keep highest cost)
     * 4. Add (groupSum - maxCost) to total deletion cost
     * 5. Process final group after loop ends
     *
     * Key Insight: For each group of consecutive duplicates, keep the highest-cost character
     * and delete all others to minimize total deletion cost.
     *
     * Time Complexity: O(N) - single pass through the string
     * Space Complexity: O(1) - only uses constant extra variables
     *
     * @param str Input string containing lowercase letters
     * @param cost Array where cost[i] is the deletion cost of str.charAt(i)
     * @return Minimum total cost to remove all adjacent duplicates
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