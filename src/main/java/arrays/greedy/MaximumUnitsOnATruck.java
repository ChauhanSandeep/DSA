package arrays.greedy;

import java.util.*;

/**
 * Problem: Maximum Units on a Truck
 *
 * You are assigned to put some amount of boxes onto one truck. You are given a 2D array
 * boxTypes, where boxTypes[i] = [numberOfBoxesi, numberOfUnitsPerBoxi]:
 * - numberOfBoxesi is the number of boxes of type i
 * - numberOfUnitsPerBoxi is the number of units in each box of type i
 *
 * You are also given an integer truckSize, which is the maximum number of boxes that
 * can be put on the truck. You can choose any boxes to put on the truck as long as
 * the number of boxes does not exceed truckSize.
 *
 * Return the maximum total number of units that can be put on the truck.
 *
 * Example:
 * Input: boxTypes = [[1,3],[2,2],[3,1]], truckSize = 4
 * Output: 8
 * Explanation:
 * - 1 box of first type (3 units each) = 3 units
 * - 2 boxes of second type (2 units each) = 4 units
 * - 1 box of third type (1 unit each) = 1 unit
 * Total = 3 + 4 + 1 = 8 units using 4 boxes
 *
 * Input: boxTypes = [[5,10],[2,5],[4,7],[3,9]], truckSize = 10
 * Output: 91
 * Explanation: Take all 5 boxes of type [5,10] (50 units), all 3 boxes of type [3,9] (27 units),
 * and 2 boxes of type [4,7] (14 units) = 91 units using 10 boxes
 *
 * LeetCode: https://leetcode.com/problems/maximum-units-on-a-truck
 *
 * Follow-up Questions:
 * 1. Q: What if boxes had weight constraints in addition to count constraints?
 *    A: Would become a more complex knapsack problem requiring dynamic programming.
 *
 * 2. Q: How would you handle fractional boxes (can take part of a box)?
 *    A: This becomes the classic fractional knapsack problem with same greedy approach.
 *
 * 3. Q: What if different box types had different loading costs?
 *    A: Would need to consider cost-benefit ratio instead of just units per box.
 *
 * 4. Q: How would you optimize for very large datasets?
 *    A: Current O(n log n) solution is optimal. Could use counting sort if units are small range.
 *
 * Related Problems:
 * - Fractional Knapsack: Classic greedy algorithm problem
 * - Maximum Bags With Full Capacity of Rocks: https://leetcode.com/problems/maximum-bags-with-full-capacity-of-rocks/
 * - Assign Cookies: https://leetcode.com/problems/assign-cookies/
 */
public class MaximumUnitsOnATruck {

    /**
     * Finds maximum units using greedy approach with sorting by units per box.
     *
     * Algorithm:
     * 1. Sort box types by units per box in descending order (most valuable first)
     * 2. Greedily take as many boxes as possible from each type, starting with highest value
     * 3. For each box type, take min(available boxes, remaining truck capacity)
     * 4. Update total units and remaining truck capacity
     * 5. Stop when truck is full or no more boxes available
     *
     * Time Complexity: O(n log n) where n is number of box types (dominated by sorting)
     * Space Complexity: O(1) if sorting in-place, O(n) if using additional space for sorting
     *
     * @param boxTypes 2D array where boxTypes[i] = [numberOfBoxes, unitsPerBox]
     * @param truckSize maximum number of boxes truck can carry
     * @return maximum total units that can be loaded on truck
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

