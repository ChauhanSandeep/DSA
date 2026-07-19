package strings.greedy;

/**
 * Problem: Minimum Time to Make Rope Colorful
 *
 * Balloons sit on a rope, each with a color and a removal cost. Remove balloons
 * so no two adjacent remaining balloons have the same color, while minimizing
 * the total removal time.
 *
 * Leetcode: https://leetcode.com/problems/minimum-time-to-make-rope-colorful/ (Medium)
 * Rating:   acceptance 63.5% (Medium), contest rating 1574
 * Pattern:  Greedy | Consecutive groups | Keep the most expensive item
 *
 * Example:
 *   Input:  colors = "aabaa", neededTime = [1,3,2,1,1]
 *   Output: 2
 *   Why:    remove the cheaper balloon from each duplicate run: one 'a' from "aa" and one from the final "aa".
 *
 * Follow-ups:
 *   1. Allow at most k equal colors in a row?
 *      For each run, keep the k largest costs and remove the rest.
 *   2. Return which indices to remove?
 *      Track indices inside each run and exclude the index with maximum cost.
 *   3. What if removal costs change after each deletion?
 *      The independent-run greedy breaks; model the process with a priority queue or simulation.
 *   4. Can the balloons be rearranged instead of removed?
 *      It becomes a rearrangement problem similar to Reorganize String with weighted deletions.
 *
 * Related: Remove All Adjacent Duplicates In String (1047), Remove All Adjacent Duplicates in String II (1209).
 */
public class MinimumTimeToMakeRopeColorful {

    public static void main(String[] args) {
        MinimumTimeToMakeRopeColorful solver = new MinimumTimeToMakeRopeColorful();
        String[] colors = {"aabaa", "abc", "aaabbbabbbb"};
        int[][] times = { {1, 3, 2, 1, 1}, {1, 2, 3}, {3, 5, 10, 7, 5, 3, 5, 5, 4, 8, 1} };
        int[] expected = {2, 0, 26};

        for (int i = 0; i < colors.length; i++) {
            int got = solver.minCost(colors[i], times[i]);
            System.out.printf("colors=%s neededTime=%s -> %d  expected=%d%n",
                colors[i], java.util.Arrays.toString(times[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: duplicate colors only conflict within a consecutive run. For one
     * run of the same color, at most one balloon can remain, so the cheapest choice
     * is to keep the balloon with the largest removal time and pay for all others.
     *
     * Algorithm:
     *   1. Scan the string run by run.
     *   2. For each equal-color run, accumulate the total removal time and the maximum time.
     *   3. If the run has more than one balloon, add total minus maximum to the answer.
     *   4. Continue from the first different color and return the accumulated cost.
     *
     * Time:  O(n) - each balloon is visited once.
     * Space: O(1) - only counters for the current run are stored.
     *
     * @param colors balloon colors in rope order
     * @param neededTime removal time for each balloon
     * @return minimum total time needed to remove adjacent equal colors
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
