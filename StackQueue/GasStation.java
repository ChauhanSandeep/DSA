package StackQueue;

/**
 * Given two integer arrays gas and cost, return the starting gas station's index
 * if you can travel around the circuit once in the clockwise direction,
 * otherwise return -1. If there exists a solution, it is guaranteed to be unique.
 *
 * <p>LeetCode Problem Link:
 * <a href="https://leetcode.com/problems/gas-station/">https://leetcode.com/problems/gas-station/</a>
 * </p>
 *
 * <p><b>Approach:</b></p>
 * - If the total gas available is less than the total cost, completing the circuit is impossible.
 * - Traverse the gas stations while maintaining a running balance of available fuel.
 * - Reset the starting station when the fuel balance goes negative.
 *
 * <p><b>Time Complexity:</b> O(N) (Single pass through the arrays)
 * <br><b>Space Complexity:</b> O(1) (Constant extra space usage)
 */
public class GasStation {

    /**
     * We can complete the circuit only if the total gas is greater than or equal to total cost.
     * So we track that first. Then, we check where the gas tank becomes negative — which means we can’t start from that segment.
     * We reset the starting station and try again. This greedy approach works in a single pass.
     *
     * @param gas  Array where gas[i] represents the fuel available at station i.
     * @param cost Array where cost[i] represents the fuel required to travel from station i to i+1.
     * @return The starting gas station index if the trip is possible; otherwise, -1.
     */
    public static int canCompleteCircuit(final int[] gas, final int[] cost) {
        int totalFuelBalance = 0; // Net fuel balance for the entire trip
        int currentFuelBalance = 0; // Fuel balance for the current journey
        int startIndex = 0; // Potential start index

        for (int i = 0; i < gas.length; i++) {
            int fuelGain = gas[i] - cost[i];
            totalFuelBalance += fuelGain;
            currentFuelBalance += fuelGain;

            // If at any point the current balance goes negative, reset start index
            if (currentFuelBalance < 0) {
                startIndex = i + 1; // Move start to the next station
                currentFuelBalance = 0; // Reset the fuel balance
            }
            // we don't need to loop over to the starting position because
            // - We’re guaranteed that if the total surplus is non-negative, there is a point where the journey can complete.
            // •	By linear traversal and greedy resets, we eventually land at that point.
        }

        // If total fuel balance is negative, no solution exists
        return totalFuelBalance >= 0 ? startIndex : -1;
    }

    public static void main(String[] args) {
        int[] gas = {1, 2, 3, 4, 5};
        int[] cost = {3, 4, 5, 1, 2};

        int startIndex = canCompleteCircuit(gas, cost);
        System.out.println("Start Index: " + startIndex); // Expected Output: 3
    }
}
