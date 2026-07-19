package arrays.twopointers;

import java.util.HashMap;
import java.util.Map;


/**
 * Problem: Minimum Window Substring
 *
 * Given strings source and target, return the smallest substring of source that
 * contains every character of target with the required duplicate counts. Return
 * the empty string when no valid window exists.
 *
 * Leetcode: https://leetcode.com/problems/minimum-window-substring/ (Hard)
 * Rating:   acceptance 48.0% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  String | Sliding window | Frequency debt
 *
 * Example:
 *   Input:  source = "ADOBECODEBANC", target = "ABC"
 *   Output: "BANC"
 *   Why:    BANC contains A, B, and C, and every shorter substring misses one of them.
 *
 * Follow-ups:
 *   1. Return all minimum windows?
 *      Keep collecting windows whose length equals the best length after each shrink.
 *   2. Require exactly the characters of target and no extras?
 *      Use a fixed-size permutation window instead of a variable-size cover window.
 *   3. Support full Unicode efficiently?
 *      Use a map keyed by code point rather than fixed ASCII arrays.
 *
 * Related: Permutation in String (567), Minimum Size Subarray Sum (209).
 */
public class MinWindowSubstring {

public static void main(String[] args) {
  MinWindowSubstring solver = new MinWindowSubstring();
  String[][] cases = { {"ADOBECODEBANC", "ABC"}, {"a", "aa"} };
  String[] expected = { "BANC", "" };

  for (int i = 0; i < cases.length; i++) {
    String got = solver.minWindow(cases[i][0], cases[i][1]);
    System.out.printf("source=%s target=%s -> %s  expected=%s%n",
        cases[i][0], cases[i][1], got, expected[i]);
  }
}

  /**
 * Intuition: targetFreq describes the debt the window must pay. Expanding right
 * adds characters until every required character is paid in full. Once valid,
 * moving left tries to remove unnecessary characters while preserving the debt
 * condition, recording the smallest valid range seen.
 *
 * Algorithm:
 *   1. Build targetFreq and count requiredChars.
 *   2. Expand right, updating windowFreq and matchedChars when a debt is paid.
 *   3. While all debts are paid, update the best window and remove source[left].
 *   4. Return the best substring, or "" if no valid range was recorded.
 *
 * Time:  O(m + n) - each source character enters and leaves the window at most once.
 * Space: O(1) - the maps are bounded by the character alphabet used by the strings.
 *
 * @param source string to search inside
 * @param target characters and counts that must be covered
 * @return shortest covering substring, or the empty string if none exists
 */
  public String minWindow(String source, String target) {
    if (target.isEmpty()) {
      return "";
    }

    Map<Character, Integer> targetFreq = new HashMap<>(); // Frequency of chars in target
    Map<Character, Integer> windowFreq = new HashMap<>(); // Frequency of chars in current window of source

    // Fill targetFreq map
    for (char c : target.toCharArray()) {
      targetFreq.put(c, targetFreq.getOrDefault(c, 0) + 1);
    }

    int requiredChars = targetFreq.size(); // Unique chars in target
    int matchedChars = 0; // How many unique chars have met required frequency

    int[] minWindowIndices = {-1, -1}; // [start, end] of min window
    int minWindowLength = Integer.MAX_VALUE;

    int left = 0;
    for (int right = 0; right < source.length(); right++) {
      char currentChar = source.charAt(right);
      windowFreq.put(currentChar, windowFreq.getOrDefault(currentChar, 0) + 1);

      // If this char is needed and we have enough
      if (targetFreq.containsKey(currentChar) && windowFreq.get(currentChar).equals(targetFreq.get(currentChar))) {
        matchedChars++;
      }

      // Valid window: try to minimize
      while (matchedChars == requiredChars && left <= right) {
        // Update min window
        if (right - left + 1 < minWindowLength) {
          minWindowIndices[0] = left;
          minWindowIndices[1] = right;
          minWindowLength = right - left + 1;
        }

        // Remove left char
        char leftChar = source.charAt(left);
        windowFreq.put(leftChar, windowFreq.get(leftChar) - 1);

        // If we dropped below required
        if (targetFreq.containsKey(leftChar) && windowFreq.get(leftChar) < targetFreq.get(leftChar)) {
          matchedChars--;
        }

        left++;
      }
    }

    return (minWindowIndices[0] != -1) ? source.substring(minWindowIndices[0], minWindowIndices[1] + 1) : "";
  }

  /**
   * Alternative approach using arrays for frequency (optimized for ASCII chars).
   * This is another efficient method, O(m + n), better constant factors for large alphabets.
   *
   * Step-by-step explanation:
   * 1. Use int[128] for targetFreq and windowFreq (assuming ASCII).
   * 2. Filter unique chars in target to compute requiredChars accurately.
   * 3. Proceed similarly with two pointers.
   * 4. This avoids HashMap overhead.
   *
   * Algorithm: Sliding Window with Arrays
   * Time Complexity: O(m + n)
   * Space Complexity: O(1) - Fixed size arrays.
   *
   * @param source the source string
   * @param target the target string
   * @return the minimum window substring or "" if none
   */
  public String minWindowAlt(String source, String target) {
    if (target.isEmpty()) {
      return "";
    }

    int[] targetFreq = new int[128];
    int[] windowFreq = new int[128];
    int requiredChars = 0;

    // Fill targetFreq and count unique
    for (char c : target.toCharArray()) {
      if (targetFreq[c] == 0) {
        requiredChars++;
      }
      targetFreq[c]++;
    }

    int matchedChars = 0;
    int left = 0;
    int[] minWindowIndices = {-1, -1};
    int minWindowLength = Integer.MAX_VALUE;

    for (int right = 0; right < source.length(); right++) {
      char currentChar = source.charAt(right);
      windowFreq[currentChar]++;

      if (targetFreq[currentChar] > 0 && windowFreq[currentChar] == targetFreq[currentChar]) {
        matchedChars++;
      }

      while (matchedChars == requiredChars && left <= right) {
        if (right - left + 1 < minWindowLength) {
          minWindowIndices[0] = left;
          minWindowIndices[1] = right;
          minWindowLength = right - left + 1;
        }

        char leftChar = source.charAt(left);
        windowFreq[leftChar]--;

        if (targetFreq[leftChar] > 0 && windowFreq[leftChar] < targetFreq[leftChar]) {
          matchedChars--;
        }

        left++;
      }
    }

    return (minWindowIndices[0] != -1) ? source.substring(minWindowIndices[0], minWindowIndices[1] + 1) : "";
  }

}