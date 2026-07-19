package arrays.greedy;

/**
 * Problem: Flip String to Monotone Increasing
 *
 * A binary string is monotone increasing when every 0 appears before every 1.
 * Flip the fewest characters so the input can be split into a left block of 0s
 * and a right block of 1s.
 *
 * Leetcode: https://leetcode.com/problems/flip-string-to-monotone-increasing/ (Medium)
 * Rating:   acceptance 63.1% (Medium) - contest rating 1602
 * Pattern:  Dynamic programming | Prefix state | Greedy choice per zero
 *
 * Example:
 *   Input:  s = "010110"
 *   Output: 2
 *   Why:    flipping to "011111" or "000111" makes all 0s come before all 1s.
 *
 * Follow-ups:
 *   1. Return the resulting string, not just the flip count?
 *      Store parent choices for each position and reconstruct the final string.
 *   2. What if 0->1 and 1->0 have different costs?
 *      Replace the unit increments with the corresponding weighted transition costs.
 *   3. What if the target is monotone decreasing?
 *      Mirror the state by counting zeros instead of ones.
 *
 * Related: Minimum Deletions to Make String Balanced (1653).
 */
public class MinFlipsToMonotoneIncreasing {

  public static void main(String[] args) {
    String[] inputs = {"00110", "010110", "00000"};
    int[] expected = {1, 2, 0};

    for (int i = 0; i < inputs.length; i++) {
      int got = minFlipsMonoIncr(inputs[i]);
      System.out.printf("s=%s -> %d  expected=%d%n", inputs[i], got, expected[i]);
    }
  }



  /**
   * Intuition: after a prefix has been processed, either keep previous 1s and
   * flip a new 0 to 1, or flip all previous 1s to 0 and keep that 0. The running
   * onesCount is the cost of the second option; flipCount + 1 is the first.
   *
   * Algorithm:
   *   1. Track onesCount and the best flipCount for the processed prefix.
   *   2. Increment onesCount for every '1'.
   *   3. For every '0', keep the cheaper of flipping this 0 or all previous 1s.
   *
   * Time:  O(n) - one pass over the string.
   * Space: O(1) - only two counters are kept.
   *
   * @param input binary string consisting only of '0' and '1'
   * @return minimum flips needed to make input monotone increasing
   */
  public static int minFlipsMonoIncrSplitPoint(String input) {
    int length = input.length();

    // Count total zeros and ones
    int totalOnes = 0;
    for (char ch : input.toCharArray()) {
      if (ch == '1') totalOnes++;
    }

    int minFlips = totalOnes;  // Case: all '0's (flip all '1's)

    int onesOnLeft = 0;

    // Try each split point
    for (int i = 0; i < length; i++) {
      if (input.charAt(i) == '1') {
        onesOnLeft++;
      }

      // Split after position i: flip all '1's before i+1, flip all '0's after i
      int remainingLength = length - i - 1;
      int onesOnRight = totalOnes - onesOnLeft;

      int zerosOnRight = remainingLength - onesOnRight;
      int flipsNeeded = onesOnLeft + zerosOnRight;
      minFlips = Math.min(minFlips, flipsNeeded);
    }

    return minFlips;
  }

  /**
   * Finds minimum flips using dynamic programming with state tracking.
   *
   * Algorithm:
   * 1. Track count of '1's seen so far (onesCount)
   * 2. Track minimum flips needed to make string monotone up to current position
   * 3. For each character:
   *    - If '1': increment onesCount (no flip needed, extends monotone sequence)
   *    - If '0': choose minimum between:
   *      a) Flip current '0' to '1' (flipCount + 1)
   *      b) Flip all previous '1's to '0's (onesCount)
   *
   * Key insight: At any position, we have two choices for a '0':
   * - Keep all previous '1's and flip this '0' to '1'
   * - Flip all previous '1's to '0's and keep this '0'
   * We take the minimum cost option.
   *
   * Time Complexity: O(N) where N is string length. Single pass through string.
   *
   * Space Complexity: O(1) using only two variables for tracking state.
   *
   * @param input binary string consisting of '0's and '1's
   * @return minimum number of flips to make string monotone increasing
   */
  public static int minFlipsMonoIncr(String input) {
    int onesCount = 0;   // Count of '1's seen so far
    int flipCount = 0;   // Minimum flips needed up to current position

    for (char ch : input.toCharArray()) {
      if (ch == '1') {
        onesCount++;
      } else {
        // Character is '0', choose minimum cost:
        // 1. Flip this '0' to '1' (flipCount + 1)
        // 2. Flip all previous '1's to '0's (onesCount)
        flipCount = Math.min(flipCount + 1, onesCount);
      }
    }

    return flipCount;
  }

  /**
   * Verbose version with detailed step-by-step explanation for learning.
   *
   * Time Complexity: O(N) where N is string length.
   * Space Complexity: O(1) using only constant extra variables.
   *
   * @param input binary string consisting of '0's and '1's
   * @return minimum number of flips to make string monotone increasing
   */
  public static int minFlipsMonoIncrVerbose(String input) {
    int onesCount = 0;
    int flipCount = 0;

    System.out.println("Processing string: " + input);
    System.out.println("Position | Char | OnesCount | FlipCount | Decision");
    System.out.println("---------|------|-----------|-----------|----------");

    for (int i = 0; i < input.length(); i++) {
      char ch = input.charAt(i);

      if (ch == '1') {
        onesCount++;
        System.out.printf("   %2d    |  %c   |    %2d     |    %2d     | Increment ones\n",
            i, ch, onesCount, flipCount);
      } else {
        int flipThisZero = flipCount + 1;
        int flipAllOnes = onesCount;

        System.out.printf("   %2d    |  %c   |    %2d     |    %2d     | ",
            i, ch, onesCount, flipCount);

        if (flipThisZero <= flipAllOnes) {
          flipCount = flipThisZero;
          System.out.println("Flip this 0→1 (cost: " + flipThisZero + ")");
        } else {
          flipCount = flipAllOnes;
          System.out.println("Flip all 1's→0 (cost: " + flipAllOnes + ")");
        }
      }
    }

    System.out.println("\nMinimum flips needed: " + flipCount);
    return flipCount;
  }
}
