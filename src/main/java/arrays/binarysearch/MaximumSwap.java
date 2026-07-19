package arrays.binarysearch;

/**
 * Problem: Maximum Swap
 *
 * Given a non-negative integer, swap at most two digits once to produce the largest possible value. If no improving swap exists, return the number unchanged.
 *
 * Leetcode: https://leetcode.com/problems/maximum-swap/ (Medium)
 * Rating:   acceptance 52.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Last digit occurrence | Most significant improvement
 *
 * Example:
 *   Input:  num = 2736
 *   Output: 7236
 *   Why:    swapping the leading 2 with the later 7 creates the best prefix.
 *
 * Follow-ups:
 *   1. Allow k swaps? Use backtracking with pruning or repeated greedy placement.
 *   2. Second-largest one-swap value? Enumerate swaps and track top distinct values.
 *   3. Very large number string? Keep the digit-array logic and return a string.
 *   4. Minimize with one swap? Choose the first improvable digit and smallest later digit.
 *
 * Related: Next Permutation (31), Largest Number (179).
 */
public class MaximumSwap {

    public static void main(String[] args) {
        MaximumSwap solver = new MaximumSwap();
        int[] inputs = { 2736, 9973, 98368 };
        int[] expected = { 7236, 9973, 98863 };
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.maximumSwap(inputs[i]);
            int alt = solver.maximumSwapAlternative(inputs[i]);
            System.out.printf("num=%d -> greedy=%d alternative=%d  expected=%d%n", inputs[i], got, alt, expected[i]);
        }
    }


        /**
     * Intuition: Improving an earlier digit dominates any later improvement. Store each digit's last index, then make the first position that can be upgraded as large as possible.
     *
     * Algorithm:
     *   1. Return num for single-digit input.
     *   2. Convert to digits and record lastIndex for 0..9.
     *   3. Scan positions left to right and try larger digits from 9 down.
     *   4. Swap with a later larger digit and return immediately.
     *
     * Time:  O(n) - each digit checks at most ten candidates.
     * Space: O(n) - the digit array is stored.
     *
     * @param num non-negative integer
     * @return maximum value after at most one swap
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

    /** Swaps two digit characters in place. */
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

