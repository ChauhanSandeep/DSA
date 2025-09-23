package strings;

import java.util.HashMap;
import java.util.Map;

/**
 * Longest Substring Without Repeating Characters
 *
 * Given a string s, find the length of the longest substring without repeating characters.
 * A substring is a contiguous sequence of characters within a string.
 *
 * The solution must find the maximum length of any contiguous subsequence where each
 * character appears at most once. This is different from subsequences which don't need
 * to be contiguous.
 *
 * Example:
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: The longest substring without repeating characters is "abc", with length 3.
 *
 * Example:
 * Input: s = "pwwkew"
 * Output: 3
 * Explanation: The longest substring is "wke" with length 3. Note that "pwke" is a
 * subsequence (not substring) and therefore doesn't count.
 *
 * LeetCode: https://leetcode.com/problems/longest-substring-without-repeating-characters/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if we need to return the actual substring instead of just the length?
 *    Answer: Track starting index and length of best window, then return s.substring(start, start + length).
 * 2. How would you handle Unicode characters or very large character sets?
 *    Answer: Use HashMap instead of fixed array, or implement with character encoding considerations.
 * 3. What if the string is extremely long (billions of characters) and doesn't fit in memory?
 *    Answer: Use streaming approach with buffered reading and sliding window over chunks.
 * 4. How to modify this for "at most k distinct characters" or "exactly k distinct characters"?
 *    Answer: Modify condition to check distinct character count instead of duplicates.
 *
 * Related Problems:
 * - LeetCode 159: Longest Substring with At Most Two Distinct Characters
 * - LeetCode 340: Longest Substring with At Most K Distinct Characters
 * - LeetCode 424: Longest Repeating Character Replacement
 */
public class LongestDistinctCharacter {

  public static void main(String[] args) {
    String input = "abababcdefababcdab";
    int length = lengthOfLongestUniqueSubstring(input);
    System.out.println("Length of longest substring with unique characters: " + length);
  }

  /**
   * Finds the length of the longest substring with all unique characters.
   * Steps:
   * 1. Use a sliding window defined by two pointers (start, end).
   * 2. Expand the end pointer to include new characters.
   * 3. If a duplicate character is found, move the start pointer to one position
   *   after the last occurrence of that character to maintain uniqueness.
   *   4. Track the maximum length of the window during the process.
   * Key insights:
   *  - Sliding window efficiently finds longest unique substring in O(n) time.
   *  - HashMap tracks last seen indices of characters for quick duplicate checks.
   *  - Each character is processed at most twice (once by end, once by start), ensuring linear complexity.
   *
   * Time Complexity: O(n) — Each character is processed at most twice.
   * Space Complexity: O(min(m, n)) — HashMap stores characters in the current window,
   *   where m is the character set size and n is the string length.
   *
   * @param input Input string
   * @return Length of longest substring with all distinct characters
   */
  public static int lengthOfLongestUniqueSubstring(String input) {
    if (input == null || input.isEmpty()) return 0;

    Map<Character, Integer> lastSeenIndex = new HashMap<>();
    int start = 0;     // Start of sliding window
    int maxLength = 0; // Maximum length found

    for (int end = 0; end < input.length(); end++) {
      char c = input.charAt(end);

      // If character was seen and is inside current window, move start
      if (lastSeenIndex.containsKey(c) && lastSeenIndex.get(c) >= start) {
        start = lastSeenIndex.get(c) + 1;
      }

      lastSeenIndex.put(c, end); // Update last seen index
      maxLength = Math.max(maxLength, end - start + 1); // Update max window size
    }

    return maxLength;
  }
}