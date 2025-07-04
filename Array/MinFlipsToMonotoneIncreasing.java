package Array;

/**
 * 🔗 LeetCode: https://leetcode.com/problems/flip-string-to-monotone-increasing/
 *
 * Problem: Given a binary string, return the minimum number of flips needed to make it monotone increasing.
 * A string is monotone increasing if it contains only 0s followed by only 1s.
 *
 * You can flip any character (0 ↔ 1).
 *
 * Example:
 * Input:  "00110000111111"
 * Output: 2
 * Explanation: Flip two '1's at index 2 and 3 to '0' or flip two '0's at index 6 and 7 to '1'.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Follow-up Questions:
 * - Q: Can we solve this without DP array?
 *   A: Yes, by tracking running ones and flip counts (greedy-style).
 * - Q: How would this change if we want monotone decreasing?
 *   A: Invert the logic (track zeros before and flip ones after).
 */
public class MinFlipsToMonotoneIncreasing {

  public static void main(String[] args) {
    String input = "00110000111111";
    int flips = minFlipsMonoIncr(input);
    System.out.println("Minimum flips required: " + flips); // Output: 2
  }

  /**
   * Greedy DP Approach (space-optimized)
   *
   * For each character in the string:
   * - If it's '1', we can leave it as is → increase count of ones so far.
   * - If it's '0', we must decide:
   *    1. Flip this '0' to '1' (costs one more flip)
   *    2. Flip all previous '1's to '0' → cost = countOnes
   * - Take the min of the two.
   *
   * Why greedy works:
   * - At each step, we only care about the current character and how many flips are needed
   *  to maintain the monotonicity.
   *
   * Time Complexity: O(n) where n is the length of the string
   * Space Complexity: O(1) since we only use a few counters
   *
   * @param str binary string
   * @return minimum number of flips to make it monotone increasing
   */
  public static int minFlipsMonoIncr(String str) {
      if (str == null || str.isEmpty()) {
          return 0;
      }

    int countOnes = 0;  // # of 1s seen so far
    int minFlips = 0;   // running minimum flips required (either 0 to 1 or 1 to 0)

    for (char ch : str.toCharArray()) {
      if (ch == '1') {
        countOnes++;
      } else { // ch == '0'
        minFlips++; // consider flipping this 0 to 1
      }

      // Either flip all previous 1s to 0s OR current 0s to 1s
      minFlips = Math.min(minFlips, countOnes);
    }

    return minFlips;
  }
}