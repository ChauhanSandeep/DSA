package arrays.twopointers;

import java.util.HashMap;
import java.util.Map;


/**
 * Problem Statement:
 * Given two strings s and t of lengths m and n respectively, return the minimum window substring of s such that every character in t (including duplicates) is included in the window.
 * If there is no such substring, return the empty string "". The testcases will be generated such that the answer is unique.
 *
 * Example:
 * Input: s = "ADOBECODEBANC", t = "ABC"
 * Output: "BANC"
 * Explanation: The minimum window substring "BANC" includes 'A', 'B', and 'C' from string t.
 *
 * LeetCode Link: https://leetcode.com/problems/minimum-window-substring/
 *
 * Follow-up Questions:
 * 1. Could you find an algorithm that runs in O(m + n) time?
 *    - Yes, the sliding window approach below achieves O(m + n) by using frequency maps and two pointers, visiting each character at most twice.
 * 2. What if we need to find all minimum windows instead of just one?
 *    - Modify the algorithm to collect all windows of the minimum length found, instead of just the first one.
 * 3. How would you handle if the window must contain exactly the characters in t, no extras?
 *    - That's a different problem (permutation in string); use similar sliding window but ensure window size == t.length().
 *      Relevant problem: https://leetcode.com/problems/permutation-in-string/
 */
public class MinWindowSubstring {

  public static void main(String[] args) {
    String source = "ADOBECODEBANC";
    String target = "ABC";
    String result = new MinWindowSubstring().minWindow(source, target);
    System.out.println("Min Window Substring: " + result);  // Expected: "BANC"
  }

  /**
   * Finds the minimum window substring using sliding window with frequency counts.
   * This is the optimal O(m + n) approach.
   *
   * Step-by-step explanation:
   * 1. If target is empty, return "" (edge case).
   * 2. Create frequency maps: targetFreq for target'source characters, windowFreq for current window.
   * 3. Initialize two pointers left and right, and counters for matchedChars (matched required counts) and requiredChars (unique in target).
   * 4. Expand right: Add source[right] to windowFreq; if it matches targetFreq and windowFreq count == targetFreq count, increment matchedChars.
   * 5. When matchedChars == requiredChars (valid window), try to minimize by moving left:
   *    - Update result if current window is smaller.
   *    - Remove source[left] from windowFreq; if it drops below targetFreq, decrement matchedChars.
   *    - Increment left.
   * 6. Continue until right reaches end.
   * 7. Return the substring if found, else "".
   *
   * Algorithm: Sliding Window
   * Time Complexity: O(m + n) - Each character visited at most twice (by left and right).
   * Space Complexity: O(1) - Maps size up to 52 (assuming English letters), constant.
   *
   * @param source the source string
   * @param target the target string
   * @return the minimum window substring or "" if none
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