package Backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode: https://leetcode.com/problems/permutation-sequence/
 *
 * Problem: Given `n`, which represents a sequence [1, 2, ..., n], find the `k`-th lexicographically smallest permutation.
 *
 * Approach:
 * - Compute (n-1)! to determine the size of each "block" of permutations.
 * - Use the k-th index to determine which number should be fixed in each step.
 * - Reduce k and adjust the factorial dynamically to determine subsequent numbers.
 * - Remove used numbers to avoid repetition.
 *
 * Time Complexity:  O(n) → We iterate through `n` elements and perform constant-time operations.
 * Space Complexity: O(n) → We store `n` elements in a list.
 */
public class KthPermutation {
    public static void main(String[] args) {
        int n = 4, k = 17;
        String result = getKthPermutation(n, k);
        System.out.println("The " + k + "-th permutation of sequence 1 to " + n + " is: " + result);
    }

    /**
     * Returns the k-th permutation of numbers from 1 to n.
     *
     * @param n The size of the sequence (1, 2, ..., n).
     * @param k The k-th permutation to return.
     * @return The k-th lexicographically smallest permutation as a string.
     */
    public static String getKthPermutation(int n, int k) {
        List<Integer> availableNumbers = new ArrayList<>();
        int factorial = 1;  // Stores (n-1)! for partitioning
        StringBuilder permutation = new StringBuilder();

        // Populate the list with numbers from 1 to n and compute (n-1)!
        for (int i = 1; i < n; i++) {
            factorial *= i;
            availableNumbers.add(i);
        }
        availableNumbers.add(n);

        // Convert k to zero-based index
        k--;

        // Generate the k-th permutation
        while (!availableNumbers.isEmpty()) {
            int index = k / factorial;
            permutation.append(availableNumbers.get(index));
            availableNumbers.remove(index);

            if (availableNumbers.isEmpty()) break;

            k %= factorial;
            factorial /= availableNumbers.size();  // Adjust factorial dynamically
        }

        return permutation.toString();
    }
}
