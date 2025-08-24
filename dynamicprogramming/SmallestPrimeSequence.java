package dynamicprogramming;

import java.util.Arrays;

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
 * Related LeetCode Problem:
 * - Super Ugly Number: https://leetcode.com/problems/super-ugly-number/
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
     *
     * @param first  First prime factor
     * @param second Second prime factor
     * @param third  Third prime factor
     * @param k      Number of elements to generate
     * @return Array of first `k` elements in the sequence
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
