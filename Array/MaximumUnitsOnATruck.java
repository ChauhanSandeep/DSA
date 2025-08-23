package Array;

import java.util.*;

/**
 * Maximum Units On A Truck
 * 
 * Problem: Given box types with [numberOfBoxes, unitsPerBox] and truck capacity,
 * maximize total units that can be loaded. Use greedy approach.
 * 
 * Example: boxTypes = [[1,3],[2,2],[3,1]], truckSize = 4 -> Output: 8
 * Take 1 box of type [1,3] and 3 boxes of type [2,2]: 1*3 + 3*2 = 9. Wait, that's wrong.
 * Take 1 box of [1,3] and 2 boxes of [2,2] and 1 box of [3,1]: 1*3 + 2*2 + 1*1 = 8
 * 
 * LeetCode: https://leetcode.com/problems/maximum-units-on-a-truck
 * 
 * Follow-up Questions:
 * - What if boxes have weights and truck has weight limit? (Add weight constraint)
 * - How to track which box types were selected? (Store selection in result)
 * - What if we want to minimize units instead? (Sort in ascending order of units per box)
 */
public class MaximumUnitsOnATruck {

    /**
     * Maximizes units loaded on truck using greedy approach.
     * 
     * Algorithm:
     * 1. Sort box types by units per box in descending order
     * 2. Greedily select boxes with highest units per box first
     * 3. For each box type, take as many as possible within remaining capacity
     * 4. Continue until truck is full or no more boxes available
     * 
     * Time Complexity: O(n log n) where n is number of box types
     * Space Complexity: O(1) if sorting in-place, O(n) if creating copy
     * 
     * @param boxTypes array of [numberOfBoxes, unitsPerBox]
     * @param truckSize maximum number of boxes truck can carry
     * @return maximum units that can be loaded
     */
    public int maximumUnits(int[][] boxTypes, int truckSize) {
        // Sort by units per box in descending order (greedy choice)
        Arrays.sort(boxTypes, (a, b) -> Integer.compare(b[1], a[1]));

        int totalUnits = 0;
        int remainingCapacity = truckSize;

        for (int[] boxType : boxTypes) {
            int numberOfBoxes = boxType[0];
            int unitsPerBox = boxType[1];

            // Take as many boxes of this type as possible
            int boxesToTake = Math.min(numberOfBoxes, remainingCapacity);
            totalUnits += boxesToTake * unitsPerBox;
            remainingCapacity -= boxesToTake;

            // Stop if truck is full
            if (remainingCapacity == 0) {
                break;
            }
        }

        return totalUnits;
    }

    /**
     * Priority queue approach (alternative greedy implementation)
     * Time Complexity: O(n log n), Space Complexity: O(n)
     */
    public int maximumUnitsPriorityQueue(int[][] boxTypes, int truckSize) {
        // Use max heap based on units per box
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> 
            Integer.compare(b[1], a[1]));

        // Add all box types to heap
        for (int[] boxType : boxTypes) {
            maxHeap.offer(boxType);
        }

        int totalUnits = 0;
        int remainingCapacity = truckSize;

        while (!maxHeap.isEmpty() && remainingCapacity > 0) {
            int[] currentBoxType = maxHeap.poll();
            int numberOfBoxes = currentBoxType[0];
            int unitsPerBox = currentBoxType[1];

            int boxesToTake = Math.min(numberOfBoxes, remainingCapacity);
            totalUnits += boxesToTake * unitsPerBox;
            remainingCapacity -= boxesToTake;
        }

        return totalUnits;
    }

    /**
     * Without modifying input array (creates copy for sorting)
     * Time Complexity: O(n log n), Space Complexity: O(n)
     */
    public int maximumUnitsWithoutModify(int[][] boxTypes, int truckSize) {
        // Create copy to avoid modifying original array
        int[][] sortedBoxTypes = Arrays.copyOf(boxTypes, boxTypes.length);
        for (int i = 0; i < boxTypes.length; i++) {
            sortedBoxTypes[i] = Arrays.copyOf(boxTypes[i], boxTypes[i].length);
        }

        Arrays.sort(sortedBoxTypes, (a, b) -> Integer.compare(b[1], a[1]));

        int totalUnits = 0;
        int remainingCapacity = truckSize;

        for (int[] boxType : sortedBoxTypes) {
            if (remainingCapacity == 0) break;

            int boxesToTake = Math.min(boxType[0], remainingCapacity);
            totalUnits += boxesToTake * boxType[1];
            remainingCapacity -= boxesToTake;
        }

        return totalUnits;
    }

    /**
     * Helper method to track selection details (for debugging/analysis)
     */
    public List<int[]> getOptimalSelection(int[][] boxTypes, int truckSize) {
        Arrays.sort(boxTypes, (a, b) -> Integer.compare(b[1], a[1]));

        List<int[]> selection = new ArrayList<>();
        int remainingCapacity = truckSize;

        for (int[] boxType : boxTypes) {
            if (remainingCapacity == 0) break;

            int boxesToTake = Math.min(boxType[0], remainingCapacity);
            if (boxesToTake > 0) {
                selection.add(new int[]{boxesToTake, boxType[1]});
                remainingCapacity -= boxesToTake;
            }
        }

        return selection;
    }
}
