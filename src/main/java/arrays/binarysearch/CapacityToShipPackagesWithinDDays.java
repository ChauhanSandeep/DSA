package arrays.binarysearch;

/**
 * 1011. Capacity To Ship Packages Within D Days
 *
 * Problem: A conveyor belt has packages that must be shipped from one port to another
 * within D days. Find the least weight capacity of the ship to ship all packages within D days.
 *
 * Example:
 * Input: weights = [1,2,3,4,5,6,7,8,9,10], D = 5
 * Output: 15
 * Explanation: Ship capacity 15: Day 1: [1,2,3,4,5], Day 2: [6,7], Day 3: [8], Day 4: [9], Day 5: [10]
 *
 * LeetCode: https://leetcode.com/problems/capacity-to-ship-packages-within-d-days
 *
 * Follow-up questions:
 * Q: What if packages can be split across days?
 * A: Different problem - use greedy approach to fill each day to maximum capacity.
 *
 * Q: How to handle case where we need exactly D days (not within D days)?
 * A: Modify the feasibility check to ensure we use exactly D days.
 *
 * Q: Can we optimize for very large arrays?
 * A: Current solution is already optimal O(n log(sum)), can't improve asymptotically.
 */
public class CapacityToShipPackagesWithinDDays {

    /**
     * Finds the minimum ship capacity to transport all packages within D days.
     *
     * Algorithm: Binary search on capacity
     * - Search space: [max_weight, sum_of_all_weights]
     * - For each capacity, simulate shipping to check if feasible within D days
     * - Use greedy approach: pack as many packages as possible each day
     * - Binary search to find minimum feasible capacity
     *
     * Time Complexity: O(n * log(sum)) where n is number of packages, sum is total weight
     * Space Complexity: O(1)
     */
    public int shipWithinDays(int[] weights, int D) {
        int left = 0, right = 0;

        // Calculate search boundaries
        for (int weight : weights) {
            left = Math.max(left, weight);  // minimum capacity must handle heaviest package
            right += weight;                // maximum capacity is sum of all weights
        }

        // Binary search for minimum feasible capacity
        while (left < right) {
            int mid = left + (right - left) / 2;

            if (canShipWithCapacity(weights, D, mid)) {
                right = mid;  // try smaller capacity
            } else {
                left = mid + 1;  // need larger capacity
            }
        }

        return left;
    }

    // Check if packages can be shipped within D days with given capacity
    private boolean canShipWithCapacity(int[] weights, int D, int capacity) {
        int daysNeeded = 1;
        int currentLoad = 0;

        for (int weight : weights) {
            if (currentLoad + weight > capacity) {
                daysNeeded++;
                currentLoad = weight;

                if (daysNeeded > D) {
                    return false;
                }
            } else {
                currentLoad += weight;
            }
        }

        return true;
    }

    /**
     * Alternative implementation with explicit capacity calculation.
     * More readable for understanding the simulation process.
     */
    public int shipWithinDaysVerbose(int[] weights, int D) {
        int maxWeight = 0;
        int totalWeight = 0;

        for (int weight : weights) {
            maxWeight = Math.max(maxWeight, weight);
            totalWeight += weight;
        }

        for (int capacity = maxWeight; capacity <= totalWeight; capacity++) {
            if (canShipInTime(weights, D, capacity)) {
                return capacity;
            }
        }

        return totalWeight;
    }

    // Verbose feasibility check with day-by-day simulation
    private boolean canShipInTime(int[] weights, int D, int capacity) {
        int day = 1;
        int currentCapacity = capacity;

        for (int weight : weights) {
            if (weight > currentCapacity) {
                day++;
                currentCapacity = capacity;
                if (day > D) {
                    return false;
                }
            }
            currentCapacity -= weight;
        }

        return true;
    }

    /**
     * Optimized version with early termination conditions.
     * Includes additional checks for edge cases.
     */
    public int shipWithinDaysOptimized(int[] weights, int D) {
        if (weights == null || weights.length == 0 || D <= 0) {
            return 0;
        }

        // Edge case: if D >= number of packages, minimum capacity is the heaviest package
        if (D >= weights.length) {
            int maxWeight = 0;
            for (int weight : weights) {
                maxWeight = Math.max(maxWeight, weight);
            }
            return maxWeight;
        }

        int left = 0, right = 0;
        for (int weight : weights) {
            left = Math.max(left, weight);
            right += weight;
        }

        // Edge case: if D = 1, we need capacity for all packages
        if (D == 1) {
            return right;
        }

        while (left < right) {
            int mid = left + (right - left) / 2;
            int daysNeeded = calculateDaysNeeded(weights, mid);

            if (daysNeeded <= D) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    // Calculate exact number of days needed for given capacity
    private int calculateDaysNeeded(int[] weights, int capacity) {
        int days = 1;
        int currentLoad = 0;

        for (int weight : weights) {
            if (currentLoad + weight > capacity) {
                days++;
                currentLoad = weight;
            } else {
                currentLoad += weight;
            }
        }

        return days;
    }
}