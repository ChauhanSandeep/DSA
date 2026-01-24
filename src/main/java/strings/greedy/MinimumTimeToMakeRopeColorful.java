package strings.greedy;

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
 * Explanation: Remove balloons at indices 0 and 4 (cost 1+1=2) to make "aba".
 *
 * LeetCode Link: https://leetcode.com/problems/minimum-time-to-make-rope-colorful/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if we can rearrange balloons instead of just removing them?
 *    Answer: Use greedy sorting to place same colors non-adjacently, minimizing total cost.
 * 2. How would you handle 3D rope (grid) where no adjacent cells can have same color?
 *    Answer: Apply same principle with 4-directional or 8-directional adjacency checks.
 * 3. What if removal costs change dynamically during the process?
 *    Answer: Use priority queue to always process most expensive operations last.
 * 4. How to modify for "at most k consecutive same colors allowed"?
 *    Answer: Extend grouping logic to only remove when group size > k, keep k most expensive.
 *
 * Related Problems:
 * - LeetCode 1047: Remove All Adjacent Duplicates In String
 * - LeetCode 1209: Remove All Adjacent Duplicates in String II
 * - LeetCode 1578: Minimum Deletion Cost to Avoid Repeating Letters (same problem)
 * LeetCode Contest Rating: 1574
 **/
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

        int minTotalTime = 0;
        int index = 0;

        while (index < colors.length()) {
            char groupColor = colors.charAt(index);

            // Find all consecutive balloons with same color
            int groupTotalTime = neededTime[index];
            int maxTimeInGroup = neededTime[index];
            int nextIndex = index + 1;

            while (nextIndex < colors.length() && colors.charAt(nextIndex) == groupColor) {
                groupTotalTime += neededTime[nextIndex];
                maxTimeInGroup = Math.max(maxTimeInGroup, neededTime[nextIndex]);
                nextIndex++;
            }

            // If group has more than 1 balloon, remove all except the one with max time
            if (nextIndex > index + 1) {
                minTotalTime += groupTotalTime - maxTimeInGroup;
            }

            index = nextIndex;
        }

        return minTotalTime;
    }

    /**
     * Alternative single-pass approach without grouping.
     * Intuition:
     * When we find two consecutive balloons of the same color,
     * we can immediately decide to remove the one with the smaller needed time.
     * This way, we don't need to explicitly group all consecutive same colors.
     *
     * Algorithm:
     * 1. Iterate through the colors array starting from the second balloon
     * 2. If current balloon color matches previous, add the smaller needed time to total cost
     *    and keep the larger needed time for future comparisons
     * 3. Continue until end of array
     *
     * Time Complexity: O(n) where n is number of balloons
     * Space Complexity: O(1) - only uses constant extra space
     *
     * @param colors String representing colors of balloons
     * @param neededTime Array of time needed to remove each balloon
     * @return Minimum time to make rope colorful
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
