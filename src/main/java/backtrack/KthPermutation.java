package backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Permutation Sequence
 *
 * The numbers 1..n have n! lexicographic permutations. Given n and a 1-based
 * rank k, return the k-th permutation without listing all permutations first.
 *
 * Leetcode: https://leetcode.com/problems/permutation-sequence/
 * Rating:   acceptance 53.6% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Factorial number system | K-th permutation
 *
 * Example:
 *   Input:  n = 4, k = 17
 *   Output: "3412"
 *   Why:    permutations for n = 4 come in blocks of 6 by first digit, and
 *           rank 17 lands in the block starting with 3, then narrows to 4, 1, 2.
 *
 * Follow-ups:
 *   1. Find the lexicographic rank of a given permutation?
 *      Reverse the factorial-number-system process by counting unused smaller digits at each position.
 *   2. Support duplicate digits?
 *      Divide block counts by duplicate factorials using multinomial coefficients.
 *   3. Return the next k permutations after a given permutation?
 *      Convert the start permutation to rank, then unrank rank+1 through rank+k.
 *   4. Make removals O(log n) for large n?
 *      Replace ArrayList removal with a Fenwick tree over unused positions.
 *
 * Related: Next Permutation (31), Permutations (46), Permutations II (47).
 *
 *   Approach                Method                              Time      Space (extra)
 *   ----------------------  ----------------------------------  --------  -------------
 *   Backtracking rank scan  getKthPermutationRecursiveApproach O(k*n)    O(n)
 *   Factorial unranking     getKthPermutation                  O(n^2)    O(n)
 */
public class KthPermutation {

    /**
     * Intuition: the most direct way to understand the k-th permutation is to
     * generate permutations in lexicographic order and count them. It is not the
     * optimal interview solution, but it is a useful baseline: the first complete
     * permutation is rank 1, the next is rank 2, and so on. Because we try numbers
     * from small to large at every position, the DFS naturally produces the same
     * order the problem asks about, so stopping at count k is correct.
     *
     * Algorithm:
     *   1. Reject n/k values where k is outside the range 1..n!.
     *   2. Build permutations one position at a time, trying unused numbers from 1 to n.
     *   3. When a full permutation is built, increment the generated count and
     *      return it only if this is the k-th one.
     *   4. After trying a number in the current position, remove it and mark it unused
     *      so the next branch starts from a clean state.
     *
     * Time:  O(k*n), worst-case O(n!*n) - we may build k complete length-n permutations before stopping.
     * Space: O(n) recursion depth, used array, and current permutation.
     *
     * @param n size of the sequence 1..n
     * @param k 1-based permutation rank
     * @return the k-th permutation, or empty string if k is invalid
     */
    public static String getKthPermutationRecursiveApproach(int n, int k) {
        if (n <= 0 || k <= 0 || k > factorial(n)) return "";

        boolean[] used = new boolean[n + 1];
        int[] generatedCount = {0};
        return findKthPermutation(n, k, new StringBuilder(), used, generatedCount);
    }

    /** Generates permutations in lexicographic order until the requested rank is reached. */
    private static String findKthPermutation(int n, int k, StringBuilder currentPermutation,
                                             boolean[] used, int[] generatedCount) {
        if (currentPermutation.length() == n) {
            generatedCount[0]++;
            return generatedCount[0] == k ? currentPermutation.toString() : null;
        }

        for (int number = 1; number <= n; number++) {
            if (used[number]) continue;

            // select -> mark -> work -> unmark
            used[number] = true;
            currentPermutation.append(number);
            String found = findKthPermutation(n, k, currentPermutation, used, generatedCount);
            if (found != null) return found;
            currentPermutation.deleteCharAt(currentPermutation.length() - 1);
            used[number] = false;
        }
        return null;
    }

    /**
     * Intuition (interview default): lexicographic permutations come in equal-size
     * blocks. For n digits, fixing the first digit leaves (n-1)! endings, so each
     * possible first digit owns a block of that size. The rank k tells us which
     * block to jump into, which digit to remove, and what the rank is inside the
     * smaller remaining problem. Repeating that unranking step chooses the whole
     * permutation without generating the earlier ones.
     *
     * Algorithm:
     *   1. Compute n! and return an empty string if k is not a valid 1-based rank.
     *   2. Store the available digits in sorted order so block indexes match lexicographic order.
     *   3. Convert k to a zero-based rank, because integer division then points at the correct block.
     *   4. For each position, divide by the current block size to choose a digit,
     *      remove that digit, and keep only the remainder rank inside the chosen block.
     *
     * Time:  O(n^2) - we choose n digits, and each ArrayList removal shifts the remaining suffix.
     * Space: O(n) for the available digits and output builder.
     *
     * @param n size of the sequence 1..n
     * @param k 1-based permutation rank
     * @return the k-th permutation, or empty string if k is invalid
     */
    public static String getKthPermutation(int n, int k) {
        if (n <= 0 || k <= 0) return "";

        int totalPermutations = factorial(n);
        if (k > totalPermutations) return "";

        List<Integer> availableNumbers = new ArrayList<>();
        for (int number = 1; number <= n; number++) availableNumbers.add(number);

        StringBuilder permutation = new StringBuilder();
        int rank = k - 1;
        int blockSize = totalPermutations;

        while (!availableNumbers.isEmpty()) {
            blockSize /= availableNumbers.size();
            int chosenIndex = rank / blockSize;
            permutation.append(availableNumbers.remove(chosenIndex));
            rank %= blockSize;
        }
        return permutation.toString();
    }

    /** Returns n! for small interview constraints. */
    private static int factorial(int n) {
        int value = 1;
        for (int number = 2; number <= n; number++) value *= number;
        return value;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        int[] ns = {3, 4, 1};
        int[] ks = {3, 17, 1};
        String[] expected = {"213", "3412", "1"};

        for (int i = 0; i < ns.length; i++) {
            String got = getKthPermutation(ns[i], ks[i]);
            System.out.printf("n=%d k=%d  ->  %s  expected=%s%n",
                ns[i], ks[i], got, expected[i]);
        }
    }
}
