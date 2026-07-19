package backtrack;

/**
 * Problem: Largest Number With K Swaps
 *
 * Given a non-negative integer as a string and an integer k, return the largest
 * number that can be formed using at most k swaps of any two digits. The number
 * length stays fixed, so lexicographic comparison matches numeric comparison.
 *
 * Pattern:  Backtracking | Greedy candidate choice | Try all best swaps
 *
 * Example:
 *   Input:  number = "934651", k = 2
 *   Output: "965341"
 *   Why:    swapping 3 with 6 and then 4 with 5 gives 965341; with only two
 *           swaps, no larger digit can be moved earlier without losing that prefix.
 *
 * Follow-ups:
 *   1. Return the minimum number instead?
 *      Mirror the search by choosing the smallest suffix digit, while preserving leading-zero rules if needed.
 *   2. Find the fewest swaps needed to reach the maximum possible number?
 *      BFS by swap distance gives shortest swaps, or DFS with iterative deepening.
 *   3. Handle very large k and many repeated digits efficiently?
 *      Memoize (digitString, remainingSwaps, index) states and skip swapping equal digits.
 *   4. Return all maximum numbers reachable in exactly k swaps?
 *      Continue search after finding a max and collect ties at depth k.
 */
public class LargestNumberWithKSwaps {
    private String bestNumber;

    /**
     * Intuition: to make the number as large as possible, the leftmost digits
     * matter the most. At position i, the best improvement is to bring the largest
     * digit from the suffix into that position. If that largest digit appears more
     * than once, different occurrences can leave different suffixes, so we try all
     * of those best swaps. This is still backtracking, but it prunes away swaps
     * that could never improve the current most-important position.
     *
     * Algorithm:
     *   1. Return the original string for null, empty, or zero-swap input.
     *   2. Track the best number seen while recursively fixing one index at a time.
     *   3. Find the maximum digit from the current index to the end; spend a swap
     *      only if that digit is bigger than the current digit.
     *   4. Swap the current index with each occurrence of that maximum digit, update
     *      the best answer, recurse to the next index, and then swap back.
     *
     * Time:  O(n^k) - for each of k improving positions, tied max digits can create up to n branches.
     * Space: O(n) recursion depth and digit array.
     *
     * @param numberStr input number represented as digits
     * @param maxSwaps maximum swaps allowed
     * @return largest number reachable with at most maxSwaps swaps
     */
    public String findLargestNumberAfterKSwaps(String numberStr, int maxSwaps) {
        if (numberStr == null || numberStr.length() == 0 || maxSwaps <= 0) return numberStr;

        char[] digits = numberStr.toCharArray();
        bestNumber = numberStr;
        backtrack(digits, maxSwaps, 0);
        return bestNumber;
    }

    /** Fixes one digit position at a time by trying every best suffix swap. */
    private void backtrack(char[] digits, int remainingSwaps, int currentIndex) {
        if (remainingSwaps == 0 || currentIndex == digits.length) return;

        char maxDigitInSuffix = findMaxDigitFromIndex(digits, currentIndex);
        int swapsAfterChoosing = maxDigitInSuffix == digits[currentIndex] ? remainingSwaps : remainingSwaps - 1;

        for (int i = digits.length - 1; i >= currentIndex; i--) {
            if (digits[i] != maxDigitInSuffix) continue;

            swap(digits, currentIndex, i);
            String candidateNumber = new String(digits);
            if (candidateNumber.compareTo(bestNumber) > 0) bestNumber = candidateNumber;
            backtrack(digits, swapsAfterChoosing, currentIndex + 1);
            swap(digits, currentIndex, i);
        }
    }

    /** Finds the largest digit in digits[index..end]. */
    private char findMaxDigitFromIndex(char[] digits, int index) {
        char maxDigit = digits[index];
        for (int i = index + 1; i < digits.length; i++) {
            if (digits[i] > maxDigit) maxDigit = digits[i];
        }
        return maxDigit;
    }

    /** Swaps two digit positions in place. */
    private void swap(char[] digits, int i, int j) {
        char swapTemp = digits[i];
        digits[i] = digits[j];
        digits[j] = swapTemp;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        LargestNumberWithKSwaps solver = new LargestNumberWithKSwaps();

        String[] numbers = {"934651", "1234", "129814999"};
        int[] swaps = {2, 0, 4};
        String[] expected = {"965341", "1234", "999984211"};

        for (int i = 0; i < numbers.length; i++) {
            String got = solver.findLargestNumberAfterKSwaps(numbers[i], swaps[i]);
            System.out.printf("number=%s k=%d  ->  %s  expected=%s%n",
                numbers[i], swaps[i], got, expected[i]);
        }
    }
}
