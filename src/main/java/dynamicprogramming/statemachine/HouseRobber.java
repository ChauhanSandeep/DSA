package dynamicprogramming.statemachine;

import java.util.Arrays;

/**
 * Problem: House Robber
 *
 * Given money in a row of houses, choose houses to rob without choosing two
 * adjacent houses. Return the maximum money that can be robbed without alerting
 * the police.
 *
 * Leetcode: https://leetcode.com/problems/house-robber/
 * Rating:   acceptance 53.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | State machine | Take or skip adjacent items
 *
 * Example:
 *   Input:  nums = [2,7,9,3,1]
 *   Output: 12
 *   Why:    robbing 2, 9, and 1 avoids adjacent houses and earns 12, which beats every other valid choice.
 *
 * Follow-ups:
 *   1. What if houses are arranged in a circle?
 *      Solve two linear cases: exclude the first house or exclude the last house.
 *   2. Can you return which houses were robbed?
 *      Store take/skip decisions in a DP array and backtrack from the final index.
 *   3. What if this is a binary tree of houses?
 *      Use tree DP with states for robbing or skipping each node.
 *
 * Related: House Robber II (213), House Robber III (337), Delete and Earn (740).
 */
public class HouseRobber {

        /**
     * Intuition: at each house, the only conflict is the immediately previous house.
     * The best total ending here is either the old best after skipping this house or
     * this house's value plus the best total from two houses back.
     *
     * Algorithm:
     *   1. Handle empty and one-house inputs.
     *   2. Keep previousHouse and currentHouse as rolling DP states.
     *   3. For each value, choose between robbing it and skipping it.
     *   4. Return the final currentHouse state.
     *
     * Time:  O(n) - one pass over the houses.
     * Space: O(1) - only two rolling states are kept.
     *
     * @param houseWeights money in each house
     * @return maximum amount that can be robbed without adjacent houses
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

        /** Robs the linear range from start through end with rolling states. */
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


    public static void main(String[] args) {
        HouseRobber solver = new HouseRobber();
        int[][] inputs = { {}, {5}, {1, 2, 3, 1}, {2, 7, 9, 3, 1} };
        int[] expected = {0, 5, 4, 12};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.rob(inputs[i]);
            System.out.printf("houses=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

}
