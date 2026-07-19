package greedy;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Problem: Construct Target Array With Multiple Sums
 *
 * Start with an array of all 1s. In one operation, choose one index and replace
 * its value with the current total sum of the array. Given a target array,
 * decide whether some sequence of operations can construct it.
 *
 * Leetcode: https://leetcode.com/problems/construct-target-array-with-multiple-sums/ (Hard)
 * Rating:   2015 (zerotrac Elo)
 * Pattern:  Greedy | Reverse simulation | Max heap
 *
 * Example:
 *   Input:  target = [9,3,5]
 *   Output: true
 *   Why:    the target can be reversed by changing 9 to 1, then 5 to 1,
 *           then 3 to 1, which reaches the starting array [1,1,1].
 *
 * Follow-ups:
 *   1. Return the actual sequence of operations?
 *      Record each reverse replacement, then reverse that log to get the forward operations.
 *   2. How would you avoid repeated subtraction on very large values?
 *      Replace repeated max - rest steps with max % rest, while validating zero remainders.
 *   3. What if values exceed 64-bit integers?
 *      Use BigInteger and keep the same reverse max-versus-rest invariant.
 *   4. What if the starting array has custom positive values?
 *      Stop the reverse simulation at those values and validate every rollback against them.
 *
 * Related: Patching Array (330), Reach a Number (754).
 */
public class ConstructTargetArrayWithMultipleSums {

    public static void main(String[] args) {
        ConstructTargetArrayWithMultipleSums solver = new ConstructTargetArrayWithMultipleSums();
        int[][] inputs = { {9, 3, 5}, {1, 1, 1, 2}, {8, 5}, {1}, {2} };
        boolean[] expected = {true, false, true, true, false};

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.isPossibleWithoutModulo(inputs[i].clone());
            System.out.printf("target=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


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

    /** Recursively rolls back the largest value toward an all-ones array. */
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
     * Intuition: forward construction branches at every operation, but the last
     * forward write is forced when viewed backward: it must be the current
     * maximum value. If the rest of the array sums to remainingSum, the previous
     * value at that index was maxValue - remainingSum. Repeating that rollback
     * either reaches all 1s or exposes an impossible state where the maximum
     * cannot have been produced by adding the rest.
     *
     * Algorithm:
     *   1. Put every target value into a max heap and track the total sum.
     *   2. Repeatedly remove the maximum value while it is greater than 1.
     *   3. Compute remainingSum and reject states where the maximum cannot shrink.
     *   4. Replace the maximum with maxValue - remainingSum, update the sum, and continue.
     *
     * Time:  O(t log n) - t reverse operations are simulated, each with one heap remove and insert.
     * Space: O(n) - the priority queue stores all target values.
     *
     * @param target desired positive target array
     * @return true if the target can be constructed from all 1s
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