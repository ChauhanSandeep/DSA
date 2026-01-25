package greedy;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 *
 * You are given an array target of n integers. From a starting array arr consisting of n 1's, 
 * you may perform the following procedure:
 * - Let x be the sum of all elements currently in your array.
 * - Choose index i, such that 0 <= i < n, and set the value of arr at index i to x.
 * - You may repeat this procedure as many times as needed.
 * 
 * Return true if it is possible to construct the target array from arr, otherwise return false.
 *
 * Example 1:
 * Input: target = [9,3,5]
 * Output: true
 * Explanation:
 * Start with arr = [1,1,1], sum = 3
 * Choose index 1: arr = [1,3,1], sum = 5
 * Choose index 2: arr = [1,3,5], sum = 9
 * Choose index 0: arr = [9,3,5], sum = 17
 * We successfully constructed the target array.
 *
 *
 * LeetCode: https://leetcode.com/problems/construct-target-array-with-multiple-sums
 *
 * Follow-up Questions:
 * 1. What if we need to find the actual sequence of operations?
 *    Answer: Track the operations during the reverse simulation and return them in reverse order.
 *
 * 2. How would you handle very large numbers that could cause overflow?
 *    Answer: Use BigInteger or implement modular arithmetic for large number operations.
 *
 * 3. Can this be extended to allow subtraction operations as well?
 *    Answer: Yes, but the problem becomes more complex as we need to track negative values.
 *    Related: https://leetcode.com/problems/reach-a-number/
 *
 * @author Sandeep
 * LeetCode Contest Rating: 2015
 */
public class ConstructTargetArrayWithMultipleSums {

    /**
     * Recursive approach for understanding the problem structure.
     * Not recommended for large inputs due to stack overflow risk.
     *
     * Intuition:
     * - Instead of going from an array of 1's to target, we reverse the process.
     * - We understand from examples that the previous state can be derived by
     *      - Finding the largest element
     *      - Replacing it with its previous value (current value - sum of other elements)
     * - We repeatedly replace the largest element with its previous value until all elements are 1.
     *
     * Time Complexity: O(n^2) in worst case due to array copying
     * Space Complexity: O(n) for recursion stack
     */
    public boolean isPossibleRecursive(int[] target) {
        int sum = 0;
        for (int num : target) {
            sum += num;
        }
        return canConstruct(target, sum);
    }

    // Helper method for recursive approach
    private boolean canConstruct(int[] arr, long sum) {
        // Base case: all elements are 1
        boolean allOnes = true;
        for (int num : arr) {
            if (num != 1) {
                allOnes = false;
                break;
            }
        }
        if (allOnes) return true;

        // Find the maximum element
        int maxIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[maxIndex]) {
                maxIndex = i;
            }
        }

        long maxValue = arr[maxIndex];
        long remainingSum = sum - maxValue;

        if (remainingSum >= maxValue || remainingSum <= 0) {
            return false;
        }

        long previousValue = maxValue % remainingSum;// modulo is used instead of - because there can be multiple additions
        if (previousValue == 0) return false;

        // Create new array with updated value
        int[] newArr = new int[arr.length];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        newArr[maxIndex] = (int) previousValue;

        return canConstruct(newArr, remainingSum + previousValue);
    }
    
    /**
     * Optimized Method : Determines if target array can be constructed using reverse simulation with max heap.
     *
     * Algorithm:
     *  1. Key insight: Work backwards from target to [1,1,...,1].
     *     - In each step, the maximum element must have been the one that was replaced.
     *     - To reverse: max_element = previous_max, so previous_max = max_element - (sum - max_element)
     *  2. Use max heap (priority queue) to efficiently get largest element.
     *  3. Calculate total sum of all elements.
     *  4. Repeatedly:
     *     a. Extract max element from heap
     *     b. Calculate sum of other elements: remainingSum = totalSum - maxValue
     *     c. Reverse the operation: previousValue = maxValue - remainingSum
     *     d. Handle edge cases: if remainingSum <= 0 or previousValue <= 0, return false
     *     e. Update sum and reinsert previous value into heap
     *  5. Continue until all elements become 1.
     *
     * Key Insight:
     * Working forward is exponential (many choices at each step). Working backward is unique:
     * only the maximum element could have been the one replaced in the last step.
     * 
     *
     * Algorithm: Reverse Simulation with Max Heap 
     * Time Complexity: O(n * max(target)), can be very slow for large values.
     * Space Complexity: O(n) for the priority queue.
     */
    public boolean isPossibleWithoutModulo(int[] target) {
        if (target.length == 1) {
            return target[0] == 1;
        }
        
        PriorityQueue<Long> maxHeap = new PriorityQueue<>((a, b) -> Long.compare(b, a));
        long totalSum = 0;
        
        for (int value : target) {
            totalSum += value;
            maxHeap.offer((long) value);
        }
        
        while (maxHeap.peek() > 1) {
            long maxValue = maxHeap.poll();
            long remainingSum = totalSum - maxValue;
            
               
            if (remainingSum <= 0 || maxValue <= remainingSum) {
                return false;
            }
            
            // Go back one step at a time
            long previousValue = maxValue - remainingSum;
            
            // Check if previous value is valid
            if (previousValue <= 0) {
                return false;
            }
            
            totalSum = totalSum - maxValue + previousValue;
            // Insert value of previous step into heap
            maxHeap.offer(previousValue);
        }
        
        return true;
    }
}