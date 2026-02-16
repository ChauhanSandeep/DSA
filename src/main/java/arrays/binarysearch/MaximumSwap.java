package arrays.binarysearch;

/**
 * You are given a non-negative integer num. You can swap two digits at most once to get the maximum valued number.
 * Return the maximum valued number you can get.
 *
 * Example:
 * Input: num = 2736
 * Output: 7236
 * Explanation:
 * - Digits: [2, 7, 3, 6].
 * - By swapping 2 (index 0) with 7 (index 1), we obtain 7236, which is the maximum value achievable with at most one swap.
 *
 * LeetCode link:
 * https://leetcode.com/problems/maximum-swap/
 *
 * Likely follow-up questions (with brief hints):
 * 1) How would you modify the solution to find the second-largest number obtainable with at most one swap?
 *    - Idea: Enumerate all valid single swaps (O(n^2)), track distinct results, and return the second-largest; or reason about the next-best swap given the optimal one.
 *    - Related problem: "Next Permutation" (https://leetcode.com/problems/next-permutation/).
 *
 * 2) What if you are allowed up to K swaps instead of at most one?
 *    - Idea: This becomes closer to a constrained maximum permutation problem. A common approach is DFS/Backtracking with pruning or a greedy approach over K iterations, but complexity can grow quickly.
 *    - Related problem: variants of "Largest Number" and state-search problems with swap-limited permutations.
 *
 * 3) What if the number is given as a string and may be very large (beyond 32/64-bit ranges)?
 *    - Idea: Keep the same digit-array logic but operate purely on characters/arrays without numeric conversions. Time and space remain O(n).
 *
 * 4) What if you had to minimize the number with at most one swap (no leading zeros allowed)?
 *    - Idea: Similar greedy logic but from left to right, try to swap with the smallest possible digit occurring later, with constraints to avoid leading zeros.
 *    - Related problem: "Smallest Number" with one swap on digits.
 */
public class MaximumSwap {

    /**
     * Greedy optimal solution.
     *
     * Core idea:
     * 1. Convert the integer num into an array of its digits.
     * 2. Precompute an array lastIndex[10] where lastIndex[d] is the last (rightmost) index of digit d in the digits array.
     * 3. Traverse the digits from left to right (most significant to least significant):
     *    - For the current index i and digit digits[i], we attempt to find a larger digit to its right.
     *    - Specifically, check digits from 9 down to digits[i] + 1:
     *      - If there exists a digit d (from 9 downwards) such that lastIndex[d] > i, then:
     *        - Swap digits[i] with digits[lastIndex[d]].
     *        - Convert the digit array back into an integer and return it immediately.
     *    - Because we scan from the most significant side and choose the largest possible digit to swap with, the first such swap gives the maximum result.
     * 4. If no beneficial swap is found for any index, return num unchanged.
     *
     * Why this is optimal:
     * - We always prioritize improving higher (more significant) positions first, which has a greater impact on the numeric value.
     * - For each position, we swap with the largest possible digit that appears later, ensuring maximal gain at that position.
     *
     * Time Complexity:
     * - O(n) where n is the number of digits (at most ~10 for 32-bit int), since we:
     *   - Build lastIndex in O(n),
     *   - Then for each digit, scan at most 10 candidate digits (0–9).
     *
     * Space Complexity:
     * - O(n) for the digit array plus O(1) for the lastIndex array (fixed size 10).
     *
     * Edge cases handled:
     * - num has a single digit (no swap possible, return as is).
     * - num already forms the maximum possible number (no swap improves it, return as is), e.g., 9973.
     * - num is 0 or contains repeated digits.
     *
     * @param num the non-negative integer whose digits can be swapped at most once
     * @return the maximum number obtainable by performing at most one swap of two digits
     */
    public int maximumSwap(int num) {
        // If num has only one digit, swapping cannot change its value.
        if (num < 10) {
            return num;
        }

        char[] digitsArr = Integer.toString(num).toCharArray();
        int length = digitsArr.length;

        // lastIndex[d] will store the last (rightmost) index of digit d in digits.
        int[] lastIndex = new int[10];
        for (int i = 0; i < length; i++) {
            int d = digitsArr[i] - '0';
            lastIndex[d] = i;
        }

        // Traverse from left to right, and for each digit try to find a larger digit to swap with.
        for (int i = 0; i < length; i++) {
            int firstCandidate = digitsArr[i] - '0';

            // Try to find a larger digit than firstCandidate, starting from 9 down to firstCandidate + 1.
            for (int secondCandidate = 9; secondCandidate > firstCandidate; secondCandidate--) {
                int secondCandidateIndex = lastIndex[secondCandidate];
                // If candidate digit exists to the right of i, perform the swap.
                if (secondCandidateIndex > i) {
                    swap(digitsArr, i, secondCandidateIndex);
                    return Integer.parseInt(new String(digitsArr));
                }
            }
        }

        // If no better swap was found, num is already the maximum possible.
        return num;
    }

    // Helper method to swap two characters in an array of digits.
    private void swap(char[] digitsArr, int i, int j) {
        char temp = digitsArr[i];
        digitsArr[i] = digitsArr[j];
        digitsArr[j] = temp;
    }

    /**
     * Alternative approach using explicit scanning from the right to determine swap positions.
     *
     * Idea (still O(n), but slightly different perspective):
     * - Traverse the digits array from right to left, tracking:
     *   - maxDigit: the maximum digit seen so far.
     *   - maxIndex: the index of maxDigit.
     *   - leftIndex, rightIndex: indices of the best pair of digits to swap.
     * - For each digit at position i when scanning from right to left:
     *   - If digits[i] is greater than maxDigit, update maxDigit and maxIndex to this position.
     *   - If digits[i] is smaller than maxDigit, record a potential swap pair:
     *     - leftIndex = i (a smaller digit on the left),
     *     - rightIndex = maxIndex (a larger digit on the right).
     *   - To maximize the final number, we keep updating leftIndex/rightIndex so that:
     *     - leftIndex is as far left as possible,
     *     - rightIndex corresponds to the best (rightmost) occurrence of the larger digit.
     * - After the scan, if leftIndex is not -1, perform exactly one swap; otherwise, no swap is needed.
     *
     * This method is conceptually similar to the lastIndex approach but uses a single pass and running max.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n) for the digit array, O(1) extra space.
     *
     * @param num the non-negative integer whose digits can be swapped at most once
     * @return the maximum number obtainable using at most one swap, computed with the alternative scanning approach
     */
    public int maximumSwapAlternative(int num) {
        if (num < 10) {
            return num;
        }

        char[] digitsArr = Integer.toString(num).toCharArray();
        int length = digitsArr.length;

        int maxDigit = -1;
        int maxIndex = -1;
        int leftIndex = -1;
        int rightIndex = -1;

        // Scan from right to left, maintaining the maximum digit seen so far.
        for (int i = length - 1; i >= 0; i--) {
            int currentDigit = digitsArr[i] - '0';

            if (currentDigit > maxDigit) {
                // New maximum digit seen; update maxDigit and its index.
                maxDigit = currentDigit;
                maxIndex = i;
            } else if (currentDigit < maxDigit) {
                // Current digit is smaller than a digit to its right: potential swap candidate.
                leftIndex = i;
                rightIndex = maxIndex;
            }
        }

        // If we found a beneficial swap, perform it.
        if (leftIndex != -1) {
            swap(digitsArr, leftIndex, rightIndex);
            return Integer.parseInt(new String(digitsArr));
        }

        return num;
    }
}

