package arrays.binarysearch;

import java.util.Arrays;

import java.util.Collections;
import java.util.PriorityQueue;


/**
 * Problem: Minimum Limit of Balls in a Bag
 *
 * Each operation splits one bag into two positive-size bags. Minimize the final penalty, defined as the maximum number of balls in any bag.
 *
 * Leetcode: https://leetcode.com/problems/minimum-limit-of-balls-in-a-bag/ (Medium)
 * Rating:   zerotrac 1940 (Q3, weekly-228)
 * Pattern:  Binary search on answer | Split-count feasibility | Minimum feasible penalty
 *
 * Example:
 *   Input:  nums = [9], maxOperations = 2
 *   Output: 3
 *   Why:    9 can become [3,3,3] using two splits, and penalty below 3 is impossible.
 *
 * Follow-ups:
 *   1. Return a configuration? Materialize splits after finding the penalty.
 *   2. Minimize average size? The objective is different and this predicate no longer applies.
 *   3. Split into more than two bags per operation? Recompute the operations-needed formula.
 *   4. Online bag additions? Maintain bounds and rerun the feasibility search.
 *
 * Related: Koko Eating Bananas (875), Split Array Largest Sum (410).
 */
public class MinimumLimitOfBallsInABag {

    public static void main(String[] args) {
        MinimumLimitOfBallsInABag solver = new MinimumLimitOfBallsInABag();
        int[][] inputs = { {9}, {2,4,8,2}, {7,17} };
        int[] operations = { 2, 4, 2 };
        int[] expected = { 3, 2, 7 };
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.minimumSize(inputs[i], operations[i]);
            System.out.printf("nums=%s maxOperations=%d -> %d  expected=%d%n", Arrays.toString(inputs[i]), operations[i], got, expected[i]);
        }
    }

        /**
     * Intuition: Penalty feasibility is monotonic. For a candidate penalty, (num - 1) / penalty gives the splits needed for each bag.
     *
     * Algorithm:
     *   1. Search penalties from 1 to the largest bag.
     *   2. Count operations needed for mid.
     *   3. If feasible, record mid and search smaller penalties.
     *   4. Otherwise search larger penalties.
     *
     * Time:  O(n log M) - each check scans bags across max bag M.
     * Space: O(1) - only counters and bounds are stored.
     *
     * @param nums balls in each bag
     * @param maxOperations maximum splits
     * @return minimum possible penalty
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

        /** Returns whether penalty is reachable within maxOperations. */
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
