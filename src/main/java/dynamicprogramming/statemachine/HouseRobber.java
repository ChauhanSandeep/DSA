package dynamicprogramming.statemachine;

/**
 * Problem: House Robber (LeetCode #198)
 *
 * Problem Statement:
 * You are a professional robber planning to rob houses along a street. Each house has a certain
 * amount of money stashed. The only constraint stopping you from robbing each of them is that
 * adjacent houses have security systems connected, and it will automatically contact the police
 * if two adjacent houses are broken into on the same night.
 *
 * Example 1:
 * Input: houseWeights = [1,2,3,1]
 * Output: 4
 * Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
 * Total amount you can rob = 1 + 3 = 4.
 *
 * Example 2:
 * Input: houseWeights = [2,7,9,3,1]
 * Output: 12
 * Explanation: Rob house 1 (money = 2), rob house 3 (money = 9), and rob house 5 (money = 1).
 * Total amount you can rob = 2 + 9 + 1 = 12.
 *
 * Approach:
 * We can solve this problem using dynamic programming. At each house, the robber has two choices:
 * 1. Rob the current house and add its value to the amount robbed from houses before the previous one
 * 2. Skip the current house and keep the maximum amount robbed up to the previous house
 *
 * The recurrence relation is:
 * dp[i] = max(dp[i-1], dp[i-2] + houseWeights[i])
 *
 * We can optimize space by only keeping track of the last two values since we only need them to
 * compute the current value.
 *
 * Time Complexity: O(n) where n is the number of houses
 * Space Complexity: O(1) - We only use constant extra space
 *
 * Follow-up Questions:
 * 1. What if the houses are arranged in a circle?
 *    Answer: We can solve it by taking the maximum of two cases: rob houses[0..n-2] or rob houses[1..n-1],
 *    since the first and last houses are adjacent in a circle.
 *
 * 3. How would you modify your solution to also return which houses to rob?
 *    Answer: We can modify the solution to keep track of the chosen houses by maintaining an array
 *    that stores the indices of the robbed houses at each step. We would need to backtrack through
 *    the DP array to reconstruct the solution.
 *
 * LeetCode: https://leetcode.com/problems/house-robber/
 */
public class HouseRobber {

    /**
     * Calculates the maximum amount of money that can be robbed without alerting the police
     *
     * @param houseWeights Array representing the amount of money in each house
     * @return Maximum amount of money that can be robbed
     */
    public int rob(int[] houseWeights) {
        // Handle edge cases
        if (houseWeights == null || houseWeights.length == 0) {
            return 0;
        }
        if (houseWeights.length == 1) {
            return houseWeights[0];
        }

        // Initialize variables to store the maximum amount robbed up to the previous two houses
        int maxRobbedUpToPreviousHouse = Math.max(houseWeights[0], houseWeights[1]);
        int maxRobbedUpToTwoHousesBack = houseWeights[0];

        // If there are only two houses, return the maximum of the two
        if (houseWeights.length == 2) {
            return maxRobbedUpToPreviousHouse;
        }

        // Calculate maximum amount for each house
        for (int currentHouse = 2; currentHouse < houseWeights.length; currentHouse++) {
            // Current maximum is either:
            // 1. Rob current house + amount from two houses back, or
            // 2. Skip current house and keep amount from previous house
            int currentMax = Math.max(
                maxRobbedUpToTwoHousesBack + houseWeights[currentHouse],
                maxRobbedUpToPreviousHouse
            );

            // Update for next iteration
            maxRobbedUpToTwoHousesBack = maxRobbedUpToPreviousHouse;
            maxRobbedUpToPreviousHouse = currentMax;
        }

        return maxRobbedUpToPreviousHouse;
    }

    /**
     * Alternative solution using dynamic programming with O(n) space
     * This version is more straightforward but uses O(n) space
     *
     * @param houseWeights Array representing the amount of money in each house
     * @return Maximum amount of money that can be robbed
     */
    public int robWithDPArray(int[] houseWeights) {
        // Handle edge cases
        if (houseWeights == null || houseWeights.length == 0) {
            return 0;
        }
        if (houseWeights.length == 1) {
            return houseWeights[0];
        }

        // dp[i] represents the maximum amount that can be robbed up to house i
        int[] dp = new int[houseWeights.length];
        dp[0] = houseWeights[0];
        dp[1] = Math.max(houseWeights[0], houseWeights[1]);

        // Fill the dp array
        for (int i = 2; i < houseWeights.length; i++) {
            dp[i] = Math.max(dp[i-1], dp[i-2] + houseWeights[i]);
        }

        return dp[houseWeights.length - 1];
    }

    /**
     * Solution for the follow-up question where houses are arranged in a circle
     *
     * @param houseWeights Array representing the amount of money in each house
     * @return Maximum amount of money that can be robbed without robbing adjacent houses
     */
    public int robCircularHouses(int[] houseWeights) {
        // Handle edge cases
        if (houseWeights == null || houseWeights.length == 0) {
            return 0;
        }
        if (houseWeights.length == 1) {
            return houseWeights[0];
        }

        // The solution is the maximum of two cases:
        // 1. Rob houses[0..n-2] (exclude last house)
        // 2. Rob houses[1..n-1] (exclude first house)
        return Math.max(
            robHelper(houseWeights, 0, houseWeights.length - 2),
            robHelper(houseWeights, 1, houseWeights.length - 1)
        );
    }

    /**
     * Helper method to calculate maximum robbery amount for houses from start to end index
     */
    private int robHelper(int[] houseWeights, int start, int end) {
        int prevMax = 0;
        int currMax = 0;

        for (int i = start; i <= end; i++) {
            int temp = currMax;
            currMax = Math.max(prevMax + houseWeights[i], currMax);
            prevMax = temp;
        }

        return currMax;
    }
}
