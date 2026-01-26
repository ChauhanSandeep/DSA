package arrays.greedy;

/**
 * Problem: Flip String to Monotone Increasing
 *
 * A binary string is monotone increasing if it consists of some number of '0's
 * (possibly zero), followed by some number of '1's (also possibly zero).
 *
 * You are given a binary string s. You can flip s[i] changing it from '0' to '1'
 * or from '1' to '0'. Return the minimum number of flips to make s monotone increasing.
 *
 * Example:
 * Input: s = "00110"
 * Output: 1
 * Explanation: Flip the last digit to get "00111" (1 flip).
 *
 * Example 2:
 * Input: s = "010110"
 * Output: 2
 * Explanation: Flip to get "011111" or alternatively "000111" (2 flips).
 *
 * Constraints:
 * - 1 <= s.length <= 10^5
 * - s[i] is either '0' or '1'
 *
 * LeetCode Problem: https://leetcode.com/problems/flip-string-to-monotone-increasing
 *
 * Follow-up Questions:
 *
 * 1. What if you need to return the actual resulting string, not just flip count?
 *    Answer: Track which choice was made at each position during DP. Backtrack from end
 *    to reconstruct the optimal string. Store parent decisions at each state.
 *
 * 2. How would you handle if flipping '0' to '1' costs differently than '1' to '0'?
 *    Answer: Modify DP transitions to use different costs. Instead of +1, use cost[0to1]
 *    or cost[1to0] based on which flip is performed. Track minimum cost instead of count.
 *
 * 3. What if string must be monotone decreasing instead (1's followed by 0's)?
 *    Answer: Reverse the logic. Track number of 0's seen so far. When encountering '1',
 *    choose min of (flipping all previous 0's, or flipping current 1 to 0).
 *
 * 4. Can you extend to allow k transitions (k blocks of 0's and 1's alternating)?
 *    Answer: Use DP with state (position, transitions_used, last_digit). For k=2 (monotone
 *    increasing), we already handle this. Generalize to track k transitions.
 *
 * 5. How would you handle very long strings (streaming scenario)?
 *    Answer: Process in chunks, maintaining state (ones_count, flip_count) between chunks.
 *    Update state incrementally as new characters arrive. Space remains O(1).
 * LeetCode Contest Rating: 1602
 */
public class MinFlipsToMonotoneIncreasing {

  public static void main(String[] args) {
    String input = "00011000111000";
    int flips = minFlipsMonoIncrVerbose(input);
    System.out.println("Minimum flips required: " + flips); // Output: 2
  }

  /**
   * Approach considering all possible split points.
   * At each position i, consider making everything before i as '0' and after as '1'.
   *
   * Algorithm:
   * 1. For each possible split point (0 to n):
   *    - Count '1's before split (need to flip to '0')
   *    - Count '0's after split (need to flip to '1')
   *    - Total flips = leftOnes + rightZeros
   * 2. Return minimum across all split points
   *
   * Time Complexity: O(N) using prefix sums to avoid recounting.
   * Space Complexity: O(N) for prefix arrays.
   *
   * @param input binary string consisting of '0's and '1's
   * @return minimum number of flips to make string monotone increasing
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