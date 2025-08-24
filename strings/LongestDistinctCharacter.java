package string;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.com/problems/longest-substring-without-repeating-characters/
 *
 * ### Problem Statement:
 * Given a string, find the length of the longest substring that contains only unique characters.
 *
 * ### Example:
 * Input: "abababcdefababcdab"
 * Output: 7
 * Explanation: The longest substring is "abcdef"
 *
 * ### Approach: Sliding Window + HashMap
 * - Maintain a sliding window of unique characters.
 * - Use a HashMap to store the last seen index of each character.
 * - If a duplicate is encountered, move the start pointer right after the last seen index.
 * - At each step, compute the window size and update the max.
 *
 * ### Time Complexity: O(n), where n = input length
 * Each character is visited at most twice.
 *
 * ### Space Complexity: O(min(n, a)), where a = alphabet size
 * Map stores most `a` characters (typically 26 for lowercase).
 *
 * ### Follow-up:
 * - What if you need the **substring itself**, not just the length? Store start & end indices.
 * - Can you solve it with constant space assuming only ASCII? Yes, use an int[128] array.
 */
public class LongestDistinctCharacter {

  public static void main(String[] args) {
    String input = "abababcdefababcdab";
    int length = lengthOfLongestUniqueSubstring(input);
    System.out.println("Length of longest substring with unique characters: " + length);
  }

  /**
   * Finds the length of the longest substring with all unique characters.
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