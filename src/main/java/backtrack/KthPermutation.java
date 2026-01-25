package backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * PermutationSequence.java
 *
 * The set [1, 2, 3, ..., n] contains a total of n! unique permutations.
 * By listing and labeling all of the permutations in order, we get the following sequence for n = 3:
 * "123", "132", "213", "231", "312", "321"
 * Given n and k, return the kth permutation sequence.
 *
 * Example 1:
 * Input: n = 3, k = 3
 * Output: "213"
 * Explanation:
 * The permutations in order are: "123", "132", "213", "231", "312", "321"
 * The 3rd permutation is "213"
 *
 * LeetCode Link: https://leetcode.com/problems/permutation-sequence/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How would you find the lexicographic rank of a given permutation?
 *    Answer: Reverse the process - count smaller elements and multiply by factorials.
 *    Related: Calculate permutation index from sequence
 *
 * 2. What if we need to generate all permutations between two given permutations?
 *    Answer: Find ranks of both permutations, iterate through intermediate ranks.
 *    Related: LeetCode 31 - Next Permutation
 *
 * 3. How to handle permutations with repeated elements?
 *    Answer: Modify factorials to account for repetitions using multinomial coefficients.
 *
 * 4. What if we need the kth permutation in different ordering (not lexicographic)?
 *    Answer: Apply custom comparison function and use similar factorial-based calculation.
 *    Related: Custom permutation ordering problems
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class KthPermutation {
    public static void main(String[] args) {
        int n = 4, k = 17;
        String result = getKthPermutationRecursiveApproach(n, k);
        System.out.println("The " + k + "-th permutation of sequence 1 to " + n + " is: " + result);
    }

    /**
     * Returns the k-th permutation of numbers from 1 to n.
     *
     * Approach:
     * - Compute (n-1)! to determine the size of each "block" of permutations.
     * - Use the k-th index to determine which number should be fixed in each step.
     * - Reduce k and adjust the factorial dynamically to determine subsequent numbers.
     * - Remove used numbers to avoid repetition.
     *
     * Step-by-step trace for n = 4, k = 17 (zero-based k = 16):
     *
     * |------|-------------------|-----------|-----|------------------------------|---------------|------------------------|
     * | Step | Available Numbers | Factorial | k   | PickedIndex = k / factorial  | Picked Digit  | New k = k % factorial  |
     * |------|-------------------|-----------|-----|------------------------------|---------------|------------------------|
     * | 1    | [1, 2, 3, 4]      |(4-1)! = 6 | 16  | 2                            | 3             | 16 % 6 = 4             |
     * | 2    | [1, 2, 4]         |(3-1)! = 2 | 4   | 2                            | 4             | 4 % 2 = 0              |
     * | 3    | [1, 2]            |(2-1)! = 1 | 0   | 0                            | 1             | 0                      |
     * | 4    | [2]               |(1-1)! = 1 | 0   | 0                            | 2             | 0                      |
     * |------|-------------------|-----------|-----|------------------------------|---------------|------------------------|
     * Final Permutation: "3412"
     *
     * Time Complexity:  O(n) → We iterate through `n` elements and perform constant-time operations.
     * Space Complexity: O(n) → We store `n` elements in a list.
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
        while (true) {
            int index = k / factorial;
            permutation.append(availableNumbers.get(index));
            availableNumbers.remove(index);

            if (availableNumbers.isEmpty()) break;

            k %= factorial;
            factorial /= availableNumbers.size();  // Adjust factorial dynamically
        }

        return permutation.toString();
    }

    /**
     * Optimized recursive approach that stops early once kth permutation is found.
     * Avoids generating all n! permutations when we only need the kth one.
     *
     * Steps:
     * 1. Use a boolean array to track used numbers.
     * 2. Build the current permutation step-by-step.
     * 3. Maintain a counter to track how many permutations have been generated.
     * 4. Stop recursion and return the result once the counter matches k.
     *
     * Time Complexity: O(k * n) in best case, O(n! * n) in worst case
     * Because we may need to generate up to k permutations, each taking O(n) time to build.
     * In worst case where k is the last permutation, we may generate all n! permutations, leading to O(n! * n).
     *
     * Space Complexity: O(n) for recursion stack + current permutation
     */
    public static String getKthPermutationRecursiveApproach(int num, int k) {
        boolean[] used = new boolean[num + 1];
        StringBuilder currentPermutation = new StringBuilder();
        int[] counter = {0}; // Use array to make it mutable in recursion

        String result = generatePermutationRec(num, k, currentPermutation, used, counter);
        return result != null ? result : "";
    }

    // Helper method for early stopping approach
    private static String generatePermutationRec(int num, int k, StringBuilder currentPermutation, boolean[] used, int[] counter) {
        // Base case: complete permutation found
        if (currentPermutation.length() == num) {
            System.out.println("Generated permutation: " + currentPermutation.toString());
            counter[0]++;
            if (counter[0] == k) {
                return currentPermutation.toString(); // Found kth permutation!
            }
            return null; // Continue searching
        }

        // Try each unused number
        // Start from 1 to n to maintain lexicographical order
        for (int i = 1; i <= num; i++) {
            if (!used[i]) {
                // Choose
                used[i] = true;
                currentPermutation.append(i);

                // Recurse
                String result = generatePermutationRec(num, k, currentPermutation, used, counter);
                if (result != null) {
                    return result; // Found it, propagate up
                }

                // Backtrack
                currentPermutation.deleteCharAt(currentPermutation.length() - 1);
                used[i] = false;
            }
        }

        return null; // Not found in this branch
    }
}
