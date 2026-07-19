package arrays.greedy;

import java.util.*;

/**
 * Problem: Maximum Units on a Truck
 *
 * Each box type gives a number of boxes and units per box. Load at most
 * truckSize boxes so the total units on the truck is as large as possible.
 *
 * Leetcode: https://leetcode.com/problems/maximum-units-on-a-truck/ (Easy)
 * Rating:   acceptance 74.3% (Easy) - contest rating 1310
 * Pattern:  Greedy | Sorting by value density
 *
 * Example:
 *   Input:  boxTypes = [[1,3],[2,2],[3,1]], truckSize = 4
 *   Output: 8
 *   Why:    take the 3-unit box, both 2-unit boxes, and one 1-unit box.
 *
 * Follow-ups:
 *   1. What if boxes also have weights?
 *      This becomes 0/1 or bounded knapsack instead of a pure greedy count.
 *   2. What if boxes can be split fractionally?
 *      Sort by units per box and take fractional capacity like fractional knapsack.
 *   3. What if units per box are in a small fixed range?
 *      Counting sort or buckets can reduce sorting to linear time.
 *
 * Related: Assign Cookies (455), Maximum Bags With Full Capacity of Rocks (2279).
 */
public class MaximumUnitsOnATruck {

    public static void main(String[] args) {
        MaximumUnitsOnATruck solver = new MaximumUnitsOnATruck();
        int[][][] boxTypes = {
            {{1, 3}, {2, 2}, {3, 1}},
            {{5, 10}, {2, 5}, {4, 7}, {3, 9}},
            {}
        };
        int[] truckSizes = {4, 10, 3};
        int[] expected = {8, 91, 0};

        for (int i = 0; i < boxTypes.length; i++) {
            int[][] input = Arrays.stream(boxTypes[i]).map(int[]::clone).toArray(int[][]::new);
            int got = solver.maximumUnits(input, truckSizes[i]);
            System.out.printf("boxTypes=%s truckSize=%d -> %d  expected=%d%n",
                Arrays.deepToString(boxTypes[i]), truckSizes[i], got, expected[i]);
        }
    }


    /**
     * Intuition: every box consumes exactly one truck slot, so only units per box
     * matters. Loading higher-unit boxes first can never hurt; replacing any lower
     * unit box with a higher-unit box increases or preserves the total.
     *
     * Algorithm:
     *   1. Sort boxTypes by units per box in descending order.
     *   2. Visit each type and take as many boxes as remainingCapacity allows.
     *   3. Add boxesToTake * unitsPerBox and stop once the truck is full.
     *
     * Time:  O(n log n) - sorting dominates the scan over box types.
     * Space: O(1) - aside from sort implementation details, only counters are used.
     *
     * @param boxTypes boxTypes[i] = [numberOfBoxes, unitsPerBox]
     * @param truckSize maximum number of boxes the truck can carry
     * @return maximum units that can be loaded
     */
    public int maximumUnits(int[][] boxTypes, int truckSize) {
        if (boxTypes == null || boxTypes.length == 0 || truckSize <= 0) {
            return 0;
        }

        // Sort by units per box in descending order (greedy: take most valuable first)
        Arrays.sort(boxTypes, (a, b) -> Integer.compare(b[1], a[1]));

        int totalUnits = 0;
        int remainingCapacity = truckSize;

        // Process each box type from most to least valuable
        for (int[] boxType : boxTypes) {
            int availableBoxes = boxType[0];
            int unitsPerBox = boxType[1];

            // Take as many boxes as possible from current type
            int boxesToTake = Math.min(remainingCapacity, availableBoxes);

            // Add units from taken boxes
            totalUnits += boxesToTake * unitsPerBox;

            // Update remaining truck capacity
            remainingCapacity -= boxesToTake;

            // Early termination: truck is full
            if (remainingCapacity == 0) {
                break;
            }
        }

        return totalUnits;
    }

    /**
     * Alternative approach using priority queue (max heap) for dynamic processing.
     * Useful when box types might be processed dynamically or need different ordering.
     *
     * Algorithm:
     * 1. Build max heap based on units per box value
     * 2. Continuously extract box type with highest units per box
     * 3. Take as many boxes as possible from extracted type
     * 4. Continue until truck is full or heap is empty
     *
     * Time Complexity: O(n log n) where n is number of box types
     * Space Complexity: O(n) for the priority queue
     *
     * @param boxTypes 2D array where boxTypes[i] = [numberOfBoxes, unitsPerBox]
     * @param truckSize maximum number of boxes truck can carry
     * @return maximum total units that can be loaded on truck
     */
    public int maximumUnitsWithHeap(int[][] boxTypes, int truckSize) {
        if (boxTypes == null || boxTypes.length == 0 || truckSize <= 0) {
            return 0;
        }

        // Create max heap based on units per box (second element of each array)
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) ->
            Integer.compare(b[1], a[1]));

        // Add all box types to heap
        for (int[] boxType : boxTypes) {
            maxHeap.offer(boxType);
        }

        int totalUnits = 0;
        int remainingCapacity = truckSize;

        // Process box types in order of decreasing units per box
        while (!maxHeap.isEmpty() && remainingCapacity > 0) {
            int[] currentBoxType = maxHeap.poll();
            int availableBoxes = currentBoxType[0];
            int unitsPerBox = currentBoxType[1];

            // Take as many boxes as possible from current type
            int boxesToTake = Math.min(remainingCapacity, availableBoxes);

            // Add units and update capacity
            totalUnits += boxesToTake * unitsPerBox;
            remainingCapacity -= boxesToTake;
        }

        return totalUnits;
    }
}

