package dynamicprogramming.MiscellaneousDP;

import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Problem: Smallest Sequence With Given Primes
 *
 * Given three prime numbers and k, return the first k positive numbers whose only
 * prime factors come from those primes. The sequence starts with 1 by convention.
 *
 * Source: InterviewBit - Smallest Sequence With Given Primes
 * Pattern:  Dynamic programming | Multiple pointers | Generated sorted sequence
 *
 * Example:
 *   Input:  first = 2, second = 5, third = 11, k = 5
 *   Output: [1,2,4,5,8]
 *   Why:    after 1, the smallest numbers generated only by multiplying by 2, 5,
 *           or 11 are 2, 4, 5, and 8.
 *
 * Follow-ups:
 *   1. Generalize to many primes?
 *      Use one pointer per prime, or a heap when the number of primes is large.
 *   2. Avoid duplicates such as 10 from both 2*5 and 5*2?
 *      Advance every pointer that produced the chosen next value.
 *   3. What if the values can overflow int?
 *      Compute candidates in long and decide whether to cap, throw, or return longs.
 *
 * Related: Ugly Number II (264), Super Ugly Number (313).
 */
public class SmallestPrimeSequence {

    public static void main(String[] args) {
        SmallestPrimeSequence solver = new SmallestPrimeSequence();
        int[][] inputs = {{2, 5, 11, 0}, {2, 5, 11, 5}, {3, 5, 7, 6}};
        String[] expected = {"[]", "[1, 2, 4, 5, 8]", "[1, 3, 5, 7, 9, 15]"};

        for (int i = 0; i < inputs.length; i++) {
            int[] input = inputs[i];
            int[] got = solver.findSmallestSequence(input[0], input[1], input[2], input[3]);
            System.out.printf("primes=[%d,%d,%d] k=%d -> %s  expected=%s%n",
                input[0], input[1], input[2], input[3], Arrays.toString(got), expected[i]);
        }
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
     * Intuition (interview default): result[index] is the sorted sequence built so
     * far. For each prime, a pointer marks the earliest result value whose product
     * with that prime has not yet been consumed. The next sequence value is the
     * smallest of those three products. Advancing every pointer that matches the
     * chosen value keeps duplicates out while preserving sorted order.
     *
     * Algorithm:
     *   1. Start result[0] at 1 and keep one pointer per prime factor.
     *   2. At each index, compute the three candidate products.
     *   3. Store the smallest candidate as the next sequence value.
     *   4. Advance every pointer whose product matched that value to skip duplicates.
     *
     * Time:  O(k) - each output value is produced once with constant candidate work.
     * Space: O(k) - the output array is also the DP state needed for future products.
     *
     * @param first first allowed prime factor
     * @param second second allowed prime factor
     * @param third third allowed prime factor
     * @param k number of sequence values to return
     * @return the first k values in ascending order
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
