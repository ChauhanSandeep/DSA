package BinarySearch;

import java.util.Collections;
import java.util.PriorityQueue;


/**
 * You are given an integer array nums where the ith bag contains nums[i] balls. You are also given an integer maxOperations.
 *
 * You can perform the following operation at most maxOperations times:
 * - Take any bag of balls and divide it into two new bags with a positive number of balls.
 *
 * Your penalty is the maximum number of balls in a bag. You want to minimize your penalty after the operations.
 *
 * Return the minimum possible penalty after performing the operations.
 *
 * Example 1:
 * Input: nums = [9], maxOperations = 2
 * Output: 3
 * Explanation:
 * - Divide the bag with 9 balls into two bags of sizes 6 and 3. [9] -> [6,3].
 * - Divide the bag with 6 balls into two bags of size 3 each. [6,3] -> [3,3,3].
 * The bag with the most number of balls has 3 balls, so your penalty is 3.
 *
 * Example 2:
 * Input: nums = [2,4,8,2], maxOperations = 4
 * Output: 2
 * Explanation:
 * - Divide the bag with 8 balls into two bags of 4s. [2,4,8,2] -> [2,4,4,4,2].
 * - Divide the bag with 4 balls into two bags of 2s. [2,4,4,4,2] -> [2,2,2,4,4,2].
 * - Divide the bag with 4 balls into two bags of 2s. [2,2,2,4,4,2] -> [2,2,2,2,2,4,2].
 * - Divide the bag with 4 balls into two bags of 2s. [2,2,2,2,2,4,2] -> [2,2,2,2,2,2,2,2].
 * The bag with the most number of balls has 2 balls, so your penalty is 2.
 *
 * LeetCode: https://leetcode.com/problems/minimum-limit-of-balls-in-a-bag/
 *
 * Follow-up Questions:
 * 1. How would you handle very large input sizes (e.g., 10^5 bags)?
 *    - The binary search approach is efficient with O(n log(max(nums))) time complexity.
 * 2. What if we need to minimize the sum of squares of balls in each bag instead of the maximum?
 *    - That would require a different approach, possibly using priority queues to always split the largest bag.
 * 3. How would you modify the solution to also return the final configuration of bags?
 *    - We could track the splits made during the binary search to reconstruct the final configuration.
 *
 * Related Problems:
 * - Koko Eating Bananas (https://leetcode.com/problems/koko-eating-bananas/)
 * - Split Array Largest Sum (https://leetcode.com/problems/split-array-largest-sum/)
 */
public class MinimumLimitOfBallsInABag {
    /**
     * Calculates the minimum possible penalty after performing at most maxOperations operations.
     *
     * @param nums Array representing number of balls in each bag
     * @param maxOperations Maximum number of operations allowed
     * @return Minimum possible maximum number of balls in any bag after operations
     */
    public int minimumSize(int[] nums, int maxOperations) {
        int left = 1;
        int right = 0;

        // Find the maximum possible value in the array as the upper bound
        for (int num : nums) {
            right = Math.max(right, num);
        }

        int result = right;

        // Binary search for the minimum possible penalty
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (isPossible(nums, maxOperations, mid)) {
                result = mid;
                right = mid - 1; // Try for a smaller penalty
            } else {
                left = mid + 1;  // Need a larger penalty
            }
        }

        return result;
    }

    /**
     * Checks if it's possible to achieve the given penalty with at most maxOperations.
     *
     * @param nums Array of balls in each bag
     * @param maxOperations Maximum allowed operations
     * @param penalty Target maximum balls in any bag
     * @return true if possible, false otherwise
     */
    private boolean isPossible(int[] nums, int maxOperations, int penalty) {
        int operationsNeeded = 0;

        for (int num : nums) {
            // For each bag, calculate how many operations are needed to reduce it to at most 'penalty'
            // (num - 1) / penalty gives the number of splits needed
            operationsNeeded += (num - 1) / penalty;

            // Early termination if we've already exceeded maxOperations
            if (operationsNeeded > maxOperations) {
                return false;
            }
        }

        return operationsNeeded <= maxOperations;
    }

    /**
     * Alternative solution using priority queue (less efficient but more intuitive)
     */
    public int minimumSizePriorityQueue(int[] nums, int maxOperations) {
        // Max-heap to always get the bag with most balls
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (int num : nums) {
            maxHeap.offer(num);
        }

        while (maxOperations > 0 && !maxHeap.isEmpty()) {
            int current = maxHeap.poll();

            // If the current maximum is 1, no more operations will help
            if (current == 1) {
                return 1;
            }

            // Split the current bag into two parts
            int half = current / 2;
            maxHeap.offer(half);
            maxHeap.offer(current - half);

            maxOperations--;
        }

        // The maximum in the heap is our answer
        return maxHeap.isEmpty() ? 0 : maxHeap.peek();
    }
}
