package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Capacity To Ship Packages Within D Days
 *
 * Packages must be shipped in order within D days. Return the smallest ship capacity that can carry each day's contiguous package group without exceeding capacity.
 *
 * Leetcode: https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/ (Medium)
 * Rating:   zerotrac 1725 (Q3, weekly-128)
 * Pattern:  Binary search on answer | Greedy feasibility | Minimum feasible capacity
 *
 * Example:
 *   Input:  weights = [1,2,3,4,5,6,7,8,9,10], D = 5
 *   Output: 15
 *   Why:    capacity 15 finishes in five days, while smaller capacities require more days.
 *
 * Follow-ups:
 *   1. Can package order change? Then this becomes a scheduling/bin-packing variant.
 *   2. Need exactly D non-empty days? The same capacity works when a feasible split can be refined.
 *   3. Many D queries? Precompute prefix/jump data or rerun the monotonic check per query.
 *   4. Streaming weights? Track max and sum online, but exact search waits for the stream end.
 *
 * Related: Koko Eating Bananas (875), Split Array Largest Sum (410).
 */
public class CapacityToShipPackagesWithinDDays {

    public static void main(String[] args) {
        CapacityToShipPackagesWithinDDays solver = new CapacityToShipPackagesWithinDDays();
        int[][] weights = { {1,2,3,4,5,6,7,8,9,10}, {3,2,2,4,1,4}, {1,2,3} };
        int[] days = { 5, 3, 3 };
        int[] expected = { 15, 6, 3 };
        for (int i = 0; i < weights.length; i++) {
            int got = solver.shipWithinDays(weights[i], days[i]);
            System.out.printf("weights=%s days=%d -> %d  expected=%d%n", Arrays.toString(weights[i]), days[i], got, expected[i]);
        }
    }


        /**
     * Intuition: Capacity is monotonic: if one capacity works, every larger capacity works. Greedy simulation tells whether a candidate capacity can ship in D days.
     *
     * Algorithm:
     *   1. Set left to the heaviest package and right to the total weight.
     *   2. Try mid as the candidate capacity.
     *   3. If mid is feasible, keep the left half including mid.
     *   4. Otherwise raise left above mid.
     *
     * Time:  O(n log S) - each check scans all weights across the sum range.
     * Space: O(1) - only counters and bounds are used.
     *
     * @param weights package weights in order
     * @param D maximum allowed days
     * @return minimum feasible capacity
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

    /** Returns whether capacity can ship all packages within D days. */
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

    /** Simulates remaining day capacity for the verbose solution. */
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

    /** Counts days required for a given ship capacity. */
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