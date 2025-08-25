package strings;

/**
 * LeetCode 1578. Minimum Time to Make Rope Colorful
 *
 * Alice has n balloons arranged on a rope. You are given a 0-indexed string colors
 * where colors[i] is the color of the ith balloon. Alice wants the rope to be colorful,
 * meaning no two consecutive balloons are of the same color.
 *
 * The time needed to remove the ith balloon is neededTime[i]. Return the minimum time
 * to make the rope colorful.
 *
 * Example 1:
 * Input: colors = "aabaa", neededTime = [1,3,2,1,1]
 * Output: 3
 * Explanation: Remove balloons at indices 0 and 4 (cost 1+1=2) or remove balloon at index 1 (cost 3).
 * The minimum cost is 3.
 *
 * LeetCode Link: https://leetcode.com/problems/minimum-time-to-make-rope-colorful/
 *
 * Follow-up Questions:
 * - How would you modify for k consecutive same colors? (Extend grouping logic)
 * - Can you optimize for very large arrays with few color changes? (Skip processing if no consecutive duplicates)
 * - How would you handle multiple color strings? (Process each string independently)
 * - What if we want to maximize remaining balloons instead of minimize time? (Keep maximum time balloon in each group)
 */
public class MinimumTimeToMakeRopeColorful {

    /**
     * Finds minimum time to remove balloons to make rope colorful.
     *
     * Algorithm:
     * 1. Iterate through the colors array
     * 2. When consecutive balloons have same color, group them together
     * 3. For each group, keep the balloon with maximum removal time
     * 4. Remove all other balloons in the group (add their times to result)
     * 5. Continue until all consecutive duplicates are handled
     *
     * Time Complexity: O(n) where n is number of balloons
     * Space Complexity: O(1) - only uses constant extra space
     *
     * @param colors String representing colors of balloons
     * @param neededTime Array of time needed to remove each balloon
     * @return Minimum time to make rope colorful
     */
    public int minCost(String colors, int[] neededTime) {
        if (colors == null || colors.length() <= 1) {
            return 0;
        }

        int totalCost = 0;
        int i = 0;

        while (i < colors.length()) {
            char currentColor = colors.charAt(i);

            // Find all consecutive balloons with same color
            int groupSum = neededTime[i];
            int maxTimeInGroup = neededTime[i];
            int j = i + 1;

            while (j < colors.length() && colors.charAt(j) == currentColor) {
                groupSum += neededTime[j];
                maxTimeInGroup = Math.max(maxTimeInGroup, neededTime[j]);
                j++;
            }

            // If group has more than 1 balloon, remove all except the one with max time
            if (j > i + 1) {
                totalCost += groupSum - maxTimeInGroup;
            }

            i = j;
        }

        return totalCost;
    }

    /**
     * Alternative single-pass approach without grouping.
     */
    public int minCostOptimized(String colors, int[] neededTime) {
        int totalCost = 0;

        for (int i = 1; i < colors.length(); i++) {
            if (colors.charAt(i) == colors.charAt(i - 1)) {
                // Remove the balloon with smaller time
                totalCost += Math.min(neededTime[i], neededTime[i - 1]);

                // Keep the larger time for next comparison
                neededTime[i] = Math.max(neededTime[i], neededTime[i - 1]);
            }
        }

        return totalCost;
    }
}
