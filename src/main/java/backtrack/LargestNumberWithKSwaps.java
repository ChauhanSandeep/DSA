package backtrack;

/**
 * Problem: Largest Number in K Swaps
 *
 * Given a number represented as a string and an integer `k`, find the largest number possible by making at most `k` swaps of digits.
 *
 * Example:
 * Input: number = "934651", k = 2
 * Output: "965431"
 *
 * Link: https://www.geeksforgeeks.org/find-maximum-number-possible-by-doing-at-most-k-swaps/
 *
 * Follow-up Questions (Asked in FAANG interviews):
 * 1. Can this be solved using a greedy algorithm?
 *    - No. A greedy approach might give a suboptimal solution due to premature swaps. Backtracking is needed to explore all promising branches.
 * 2. Can we reduce the recursive calls using memoization?
 *    - Not directly, since the problem state is highly dynamic (string + remaining swaps) and memoization overhead may outweigh benefits.
 * 3. Can this be modified to return the minimum number instead?
 *    - Yes. Similar logic applies, but we aim to minimize digits rather than maximize.
 */
public class LargestNumberWithKSwaps {

  /**
   * Main method to find the largest number after at most K swaps.
   *
   * Algorithm:
   * - Use backtracking to explore all swap options.
   * - At each recursive level, pick the max digit from remaining suffix.
   * - Swap only if it improves the number and we have swaps left.
   * - Track the max number globally.
   *
   * Time Complexity: O((n^2)^k) in worst case where n is number of digits.
   * Space Complexity: O(n) for recursion depth.
   *
   * @param numberStr the input number represented as a string
   * @param maxSwaps  maximum number of swaps allowed
   * @return the largest possible number as a string after at most k swaps
   */
  public String findLargestNumberAfterKSwaps(String numberStr, int maxSwaps) {
    if (numberStr == null || numberStr.length() == 0 || maxSwaps <= 0) {
      return numberStr;
    }

    char[] digits = numberStr.toCharArray();
    MAX_NUMBER = numberStr;

    backtrack(digits, maxSwaps, 0);
    return MAX_NUMBER;
  }

  private String MAX_NUMBER;

  /**
   * Recursive backtracking function to try all valid swaps.
   *
   * @param digits      current digit array
   * @param remainingSwaps remaining swap operations
   * @param currentIndex current position to fix
   */
  private void backtrack(char[] digits, int remainingSwaps, int currentIndex) {
    if (remainingSwaps == 0 || currentIndex == digits.length) {
      return;
    }

    // Find the maximum digit from current index to the end
    char maxDigitOnRightOfCurrentIndex = findMaxDigitFromIndex(digits, currentIndex);

    // If max digit to right is not the same as current digit, we can swap
    if (maxDigitOnRightOfCurrentIndex != digits[currentIndex]) {
      // this is done before the for loop because we want to swap only if the current digit is not the max
      remainingSwaps--;
    }

    // Try swapping with all occurrences of the max digit found
    for (int i = digits.length - 1; i >= currentIndex; i--) {
      if (digits[i] == maxDigitOnRightOfCurrentIndex) {
        swap(digits, currentIndex, i);

        String tentativeMax = new String(digits);
        if (tentativeMax.compareTo(MAX_NUMBER) > 0) {
          MAX_NUMBER = tentativeMax;
        }

        backtrack(digits, remainingSwaps, currentIndex + 1);

        // Backtrack
        swap(digits, currentIndex, i);
      }
    }
  }

  /**
   * Finds the maximum digit from the given index to the end of the array.
   */
  private char findMaxDigitFromIndex(char[] digits, int index) {
    char maxDigit = digits[index];
    for (int i = index + 1; i < digits.length; i++) {
      if (digits[i] > maxDigit) {
        maxDigit = digits[i];
      }
    }
    return maxDigit;
  }

  /**
   * Utility method to swap characters in the digit array.
   */
  private void swap(char[] digits, int i, int j) {
    char temp = digits[i];
    digits[i] = digits[j];
    digits[j] = temp;
  }

  // Example usage
  public static void main(String[] args) {
    LargestNumberWithKSwaps solver = new LargestNumberWithKSwaps();
    System.out.println(solver.findLargestNumberAfterKSwaps("934651", 2)); // Output: 965431
  }
}