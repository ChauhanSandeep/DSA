package dynamicprogramming.MiscellaneousDP;

import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Problem: Smallest Prime Sequence
 *
 * Given three prime numbers `first`, `second`, and `third`, and an integer `k`,
 * find the first `k` smallest numbers that have only these three as their prime factors (no others).
 *
 * Example:
 * Input: first = 2, second = 5, third = 11, k = 5
 * Output: [1, 2, 4, 5, 8]
 * Explanation: The sequence starts from 1, then multiplies by prime factors to generate next smallest numbers.
 *
 * Interviewbit: https://www.interviewbit.com/problems/smallest-sequence-with-given-primes/
 *
 * Follow-up Questions (FAANG-style):
 * 1. How would you generalize this for N primes instead of 3?
 *    - Use an array of pointers and heap to pick the next min, same as Super Ugly Number.
 * 2. How to ensure avoid duplicates in the sequence?
 *    - Always increment indexes only if next candidate equals the result (see below).
 * 3. Can you generate the numbers in O(1) space?
 *    - No, you need O(k) to hold and generate sequence for unique factors.
 * 4. What if k is extremely large?
 *    - Use streaming/online heap approach, possibly outputting numbers on the fly.
 */
public class SmallestPrimeSequence {

    public static void main(String[] args) {
        int[] result = new SmallestPrimeSequence().findSmallestSequence(2, 5, 11, 5);
        System.out.println(Arrays.toString(result)); // Output: [1, 2, 4, 5, 8]
    }

    /**
     * Solves using Min-Heap to always pick the smallest candidate.
     * 
     * Steps:
     * 1. Start with 1 in the heap and a set to track visited numbers (avoid duplicates)
     * 2. Poll the smallest number from heap, add it to result
     * 3. Generate new candidates by multiplying with each prime factor
     * 4. Add new candidates to heap if not already visited
     * 5. Repeat until we have k numbers
     * 
     * Time Complexity: O(k log k) - k insertions/deletions from heap
     * Space Complexity: O(k) - heap and visited set
     */
    public int[] findSmallestSequenceUsingHeap(int first, int second, int third, int k) {
        if (k <= 0) return new int[0];
        
        int[] result = new int[k];
        PriorityQueue<Long> minHeap = new PriorityQueue<>();
        Set<Long> visited = new HashSet<>();
        
        minHeap.offer(1L);
        visited.add(1L);
        
        for (int i = 0; i < k; i++) {
            long current = minHeap.poll();
            result[i] = (int) current;
            
            // Generate new candidates by multiplying with each prime
            long[] candidates = {current * first, current * second, current * third};
            
            for (long candidate : candidates) {
                if (!visited.contains(candidate)) {
                    minHeap.offer(candidate);
                    visited.add(candidate);
                }
            }
        }
        
        return result;
    }

    /**
     * Finds the first `k` smallest numbers that only have `first`, `second`, and `third` as prime factors.
     *
     * Steps of Solution:
     * - Start with 1 (by convention, first element in such sequences).
     * - For every iteration, select the next smallest by multiplying every element by prime and taking min.
     * - Move (increment) each pointer whose prime times result equals minimum generated.
     * - Avoid duplicates by incrementing all pointers that lead to current min.
     *
     * Example Walkthrough for first=2, second=5, third=11, k=5:
     * i	  Next candidates	            NextMin	 Update	    New pointers
     * 0		2*1=2,  5*1=5, 11*1=11	    2	       Add 2	    firstIndex++ (now 1)
     * 1		2*2=4,  5*1=5, 11*1=11	    4	       Add 4	    firstIndex++ (now 2)
     * 2		2*4=8,  5*1=5, 11*1=11	    5	       Add 5	    secondIndex++ (now 1)
     * 3		2*4=8,  5*2=10, 11*1=11	    8	       Add 8	    firstIndex++ (now 3)
     * 4		2*5=10, 5*2=10, 11*1=11	    10	     Add 10	    firstIndex++, secondIndex++
     *
     * Time Complexity: O(k) to generate k numbers
     * Space Complexity: O(k) to store k results
     */
    public int[] findSmallestSequence(int first, int second, int third, int k) {
        if (k <= 0) return new int[0];

        int[] result = new int[k];
        result[0] = 1; // Start sequence with 1

        // firstIndex, secondIndex, thirdIndex track the position in result for each factor
        int firstIndex = 0, secondIndex = 0, thirdIndex = 0;
        for (int i = 1; i < k; i++) {
            int candidate1 = first * result[firstIndex];
            int candidate2 = second * result[secondIndex];
            int candidate3 = third * result[thirdIndex];

            int nextMin = Math.min(candidate1, Math.min(candidate2, candidate3));
            result[i] = nextMin;

            if (nextMin == candidate1) firstIndex++;
            if (nextMin == candidate2) secondIndex++;
            if (nextMin == candidate3) thirdIndex++;
        }
        return result;
    }
}
