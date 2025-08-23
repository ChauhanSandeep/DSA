package com.sandeep.frazsheet.greedy;

import java.util.PriorityQueue;

/**
 * Problem: Construct Target Array With Multiple Sums
 * 
 * You are given an array target of n integers. From a starting array arr consisting of n 1's,
 * you may perform the following procedure:
 * - let x be the sum of all elements currently in your array.
 * - choose index i, such that 0 <= i < target.size and set the value of arr at index i to x.
 * - you may repeat this procedure as many times as needed.
 * Return true if it is possible to construct the target array from arr, otherwise return false.
 * 
 * Example:
 * Input: target = [9,3,5]
 * Output: true
 * Explanation: Start with [1, 1, 1], sum = 3 choose index 1 -> [1, 3, 1], sum = 5 choose index 2 -> [1, 3, 5], sum = 9 choose index 0 -> [9, 3, 5]
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
 */
public class ConstructTargetArrayWithMultipleSums {
    
    /**
     * Determines if target array can be constructed using reverse simulation with max heap.
     * 
     * Algorithm:
     * 1. Work backwards from target to [1,1,...,1]
     * 2. Use max heap to always process the largest element
     * 3. For largest element x, calculate what it was before: x % (sum - x)
     * 4. Continue until all elements become 1 or impossibility is detected
     * 
     * Time Complexity: O(n log n + log(max_target) * log n) where n is array length
     * Space Complexity: O(n) for the max heap
     * 
     * @param target Target array to construct
     * @return true if construction is possible, false otherwise
     */
    public boolean isPossible(int[] target) {
        if (target == null || target.length == 0) return false;
        if (target.length == 1) return target[0] == 1;
        
        // Use max heap to efficiently get the largest element
        PriorityQueue<Long> maxHeap = new PriorityQueue<>((a, b) -> Long.compare(b, a));
        long totalSum = 0;
        
        // Initialize heap and calculate total sum
        for (int num : target) {
            maxHeap.offer((long) num);
            totalSum += num;
        }
        
        while (maxHeap.peek() > 1) {
            long maxElement = maxHeap.poll();
            long remainingSum = totalSum - maxElement;
            
            // Special case: if remaining sum is 1, we can always construct
            if (remainingSum == 1) {
                return true;
            }
            
            // Check for impossible cases
            if (remainingSum >= maxElement || remainingSum == 0) {
                return false;
            }
            
            // Calculate what the max element was before the last operation
            long previousValue = maxElement % remainingSum;
            
            // If modulo gives 0, it means we can't reverse the operation
            if (previousValue == 0) {
                return false;
            }
            
            // Update heap and total sum
            maxHeap.offer(previousValue);
            totalSum = remainingSum + previousValue;
        }
        
        return true;
    }
    
    /**
     * Optimized version that handles edge cases more efficiently.
     * Uses mathematical optimization to avoid TLE for large numbers.
     * 
     * Time Complexity: O(n log n + log(max_target) * log n)
     * Space Complexity: O(n)
     */
    public boolean isPossibleOptimized(int[] target) {
        if (target.length == 1) return target[0] == 1;
        
        PriorityQueue<Long> maxHeap = new PriorityQueue<>((a, b) -> Long.compare(b, a));
        long totalSum = 0;
        
        for (int num : target) {
            maxHeap.offer((long) num);
            totalSum += num;
        }
        
        while (maxHeap.peek() > 1) {
            long maxElement = maxHeap.poll();
            long remainingSum = totalSum - maxElement;
            
            if (remainingSum == 1) return true;
            if (remainingSum >= maxElement || remainingSum == 0) return false;
            
            // Optimization: instead of repeatedly subtracting, use modulo
            long previousValue = maxElement % remainingSum;
            
            // Handle case where maxElement is exactly k * remainingSum
            if (previousValue == 0) {
                // Check if maxElement was created by adding remainingSum to 1
                if (maxElement == remainingSum + 1) {
                    previousValue = 1;
                } else {
                    return false;
                }
            }
            
            maxHeap.offer(previousValue);
            totalSum = remainingSum + previousValue;
        }
        
        return true;
    }
    
    /**
     * Alternative recursive approach for understanding the problem structure.
     * Not recommended for large inputs due to stack overflow risk.
     */
    public boolean isPossibleRecursive(int[] target) {
        return canConstruct(target, calculateSum(target));
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
        
        long maxElement = arr[maxIndex];
        long remainingSum = sum - maxElement;
        
        if (remainingSum >= maxElement || remainingSum <= 0) {
            return false;
        }
        
        long previousValue = maxElement % remainingSum;
        if (previousValue == 0) return false;
        
        // Create new array with updated value
        int[] newArr = new int[arr.length];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        newArr[maxIndex] = (int) previousValue;
        
        return canConstruct(newArr, remainingSum + previousValue);
    }
    
    // Helper method to calculate array sum
    private long calculateSum(int[] arr) {
        long sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return sum;
    }
}