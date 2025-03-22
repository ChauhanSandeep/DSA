package StackQueue;

/**
 * Gas Station Problem - Find the starting gas station index from where a circular trip can be completed.
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
     * Determines if a circular journey can be completed starting from a specific gas station.
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
