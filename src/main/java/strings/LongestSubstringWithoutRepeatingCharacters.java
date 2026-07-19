package strings;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Longest Substring Without Repeating Characters
 *
 * Return the length of the longest contiguous substring whose characters are all
 * distinct. Substrings are contiguous; subsequences do not count.
 *
 * Leetcode: https://leetcode.com/problems/longest-substring-without-repeating-characters/ (Medium)
 * Rating:   acceptance 39.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  String | Sliding window | Last-seen map
 *
 * Example:
 *   Input:  input = "pwwkew"
 *   Output: 3
 *   Why:    "wke" is the longest valid substring; "pwke" is not contiguous.
 *
 * Follow-ups:
 *   1. Return the substring? Track best start and best length.
 *   2. Fixed alphabet? Replace the map with an int array of last indices.
 *   3. At most k distinct? Track frequencies and shrink while distinct count exceeds k.
 *
 * Related: Longest Repeating Character Replacement (424).
 */
public class LongestSubstringWithoutRepeatingCharacters {

  public static void main(String[] args) {
    String[] inputs = {"abcabcbb", "bbbbb", "pwwkew", ""};
    int[] expected = {3, 1, 3, 0};
    for (int i = 0; i < inputs.length; i++) {
      int got = lengthOfLongestUniqueSubstring(inputs[i]);
      System.out.printf("input=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
    }
  }


    /**
   * Intuition: keep a window with no duplicate characters. When the current
   * character was already seen inside the window, jump the start just past that
   * previous position and continue expanding.
   *
   * Algorithm:
   *   1. Return 0 for null or empty input.
   *   2. Expand the end pointer one character at a time.
   *   3. Move start after a repeated character's last index when needed.
   *   4. Update last-seen index and maximum window length.
   *
   * Time:  O(n) - every character is processed once by the end pointer.
   * Space: O(min(n, m)) - the map stores seen characters.
   */
  public static int lengthOfLongestUniqueSubstring(String input) {
    if (input == null || input.isEmpty())
      return 0;

    Map<Character, Integer> lastSeenIndex = new HashMap<>();
    int start = 0; // Start of sliding window
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