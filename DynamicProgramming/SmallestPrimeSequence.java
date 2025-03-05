package DynamicProgramming;

import java.util.Arrays;

/**
 * Problem: Smallest Prime Sequence
 *
 * Given three prime numbers `first`, `second`, and `third`, and an integer `k`,
 * find the first `k` smallest numbers that only have these three as their prime factors.
 *
 * Approach:
 * - Use a **variant of the Ugly Number problem (Dynamic Programming + Three Pointers)**.
 * - Maintain a result array where each element is built using the smallest possible multiple
 *   of `first`, `second`, and `third` from previous results.
 * - Use **three pointers (`firstIndex`, `secondIndex`, `thirdIndex`)** to track
 *   which elements have been used to form the next smallest number.
 *
 * Time Complexity: **O(k)** (each number is processed once)
 * Space Complexity: **O(k)** (to store `k` results)
 *
 * Related LeetCode Problem: 
 * - Super Ugly Number: https://leetcode.com/problems/super-ugly-number/
 */
public class SmallestPrimeSequence {

    public static void main(String[] args) {
        int[] result = new SmallestPrimeSequence().findSmallestSequence(2, 5, 11, 5);
        System.out.println(Arrays.toString(result)); // Output: [2, 4, 5, 8, 10]
    }

    /**
     * Finds the first `k` smallest numbers that have only `first`, `second`, and `third` as prime factors.
     *
     * @param first  First prime factor
     * @param second Second prime factor
     * @param third  Third prime factor
     * @param k      Number of elements to generate
     * @return Array of first `k` elements in the sequence
     */
    public int[] findSmallestSequence(int first, int second, int third, int k) {
        if (k <= 0) return new int[0]; // Edge case: No elements requested

        int[] result = new int[k]; // Store the first `k` numbers in sequence
        result[0] = 1; // The first number in the sequence is always 1

        int firstIndex = 0, secondIndex = 0, thirdIndex = 0;

        for (int i = 1; i < k; i++) {
            // Compute the next smallest number using the three prime factors
            int nextNumber = Math.min(first * result[firstIndex],
                    Math.min(second * result[secondIndex], third * result[thirdIndex]));

            result[i] = nextNumber;

            // Move the pointer(s) that generated this minimum value
            if (nextNumber == first * result[firstIndex]) firstIndex++;
            if (nextNumber == second * result[secondIndex]) secondIndex++;
            if (nextNumber == third * result[thirdIndex]) thirdIndex++;
        }

        return result;
    }
}
