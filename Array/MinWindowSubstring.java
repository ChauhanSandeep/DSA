package Array;

/**
 * 🔹 Problem: Minimum Window Substring
 * 🔗 LeetCode: https://leetcode.com/problems/minimum-window-substring/
 *
 * Given two strings `source` and `target` of lengths m and n respectively, return the minimum window in `source`
 * which contains all the characters of `target`. If no such window exists, return the empty string "".
 *
 * 📌 Example:
 * Input: source = "ADOBECODEBANC", target = "ABC"
 * Output: "BANC"
 *
 * ✅ Follow-up:
 * - What if the character set is Unicode?
 * *   → Use a HashMap for frequency counting instead of fixed-size arrays.
 * - Can you optimize space for large target sets?
 * *   → Use a sliding window with two pointers and only store necessary characters.
 */
public class MinWindowSubstring {

  public static void main(String[] args) {
    String source = "ADOBECODEBANC";
    String target = "ABC";
    String result = new MinWindowSubstring().minWindow(source, target);
    System.out.println("Min Window Substring: " + result);  // Expected: "BANC"
  }

  /**
   * Finds the smallest window in `source` that contains all characters from `target`.
   *
   * ✅ Algorithm: Sliding Window + Frequency Counting
   * - Build a frequency map for `target`
   * - Expand `right` pointer to include valid chars
   * - Contract `left` pointer to shrink window while still valid
   * - Track and update the minimum window
   *
   * Time Complexity: O(N), where N = length of `source`
   * Space Complexity: O(1), assuming ASCII character set (128-size arrays)
   *
   * @param source The source string to search within
   * @param target The target string to find in the window
   * @return The minimum window substring containing all characters from target
   */
  public String minWindow(String source, String target) {
      if (source.isEmpty() || target.isEmpty()) {
          return "";
      }

    // Step 1: Build frequency map for target characters
    int[] targetFreq = new int[128];
    for (char ch : target.toCharArray()) {
      targetFreq[ch]++;
    }

    // Step 2: Initialize sliding window pointers and helpers
    int left = 0, right = 0;
    int required = target.length();  // Total required chars to match
    int minLen = Integer.MAX_VALUE;
    int minStart = 0;

    int[] windowFreq = new int[128];  // Current sliding window character frequency

    // Step 3: Start sliding the window
    while (right < source.length()) {
      char rightChar = source.charAt(right);

      // If it's a target character, add to windowFreq
      if (targetFreq[rightChar] > 0) {
        windowFreq[rightChar]++;
        if (windowFreq[rightChar] <= targetFreq[rightChar]) {
          required--;  // One less character to satisfy
        }
      }

      // Step 4: Try to shrink from the left while window is still valid
      while (required == 0) {
        int windowSize = right - left + 1;
        if (windowSize < minLen) {
          minLen = windowSize;
          minStart = left;
        }

        char leftChar = source.charAt(left);
        if (targetFreq[leftChar] > 0) {
          windowFreq[leftChar]--;
          if (windowFreq[leftChar] < targetFreq[leftChar]) {
            required++;  // We lost a necessary char
          }
        }

        left++;  // Shrink window
      }

      right++;  // Expand window
    }

    // Step 5: Return the best window found
    return minLen == Integer.MAX_VALUE ? "" : source.substring(minStart, minStart + minLen);
  }
}