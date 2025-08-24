package strings;

import java.util.HashMap;
import java.util.Map;


/**
 * LeetCode Problem: https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/
 *
 * Problem Statement:
 * Given a string and an integer k, return the length of the longest substring where
 * every character appears at least k times.
 *
 * Example:
 * Input: s = "aaabb", k = 3  → Output: 3 ("aaa")
 * Input: s = "ababbc", k = 2 → Output: 5 ("ababb")
 *
 * Follow-up Questions (FAANG-style):
 * 1. How can you do this in O(n) time and O(1) space for lowercase alphabets?
 *    - Use sliding window with a unique character count limit and window frequency array.
 * 2. What if you need to return the actual substring?
 *    - Track window positions and return substring corresponding to longest found window.
 * 3. How would you handle Unicode or non-lowercase alphabets?
 *    - Use HashMap for flexible character set, adapt window logic accordingly.
 * 4. What if k can vary for each character?
 *    - Change validation logic in window to handle per-character k constraints.
 * 5. How does top-down divide-and-conquer compare to sliding window for very large strings?
 *    - Sliding window is optimal for constant set sizes, divide & conquer suits variable alphabets.
 */
public class LongestSubstringKRepeating {

  public static void main(String[] args) {
    LongestSubstringKRepeating solution = new LongestSubstringKRepeating();
    String input = "aaabb";
    int k = 3;

    System.out.println("Longest Substring (Divide & Conquer): " + solution.longestSubstringDivideConquer(input, k));
    System.out.println("Longest Substring (Sliding Window): " + solution.longestSubstringSlidingWindow(input, k));
  }

  /**
   * Approach 1: Divide and Conquer (Recursive Splitting)
   *
   * Steps of Solution:
   * - Count frequency of each character in the string.
   * - Find the first index where character frequency < k; this is a split point.
   * - Recursively solve for left and right substrings around each split.
   * - If all characters are at least k, return substring length.
   *
   * Algorithm: Top-down recursive splitting
   * Time Complexity: O(n^2) worst case (multiple splits, substring creation)
   * Space Complexity: O(n) recursion depth for splits
   *
   * @param str Input string
   * @param k Minimum repeats required for each character
   * @return Length of the longest valid substring
   */
  public int longestSubstringDivideConquer(String str, int k) {
    int len = str.length();
    if (len == 0 || k > len) {
      return 0;
    }
    if (k == 1) {
      return len;
    }

    // Frequency map for each character
    Map<Character, Integer> freqMap = new HashMap<>();
    for (char c : str.toCharArray()) {
      freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
    }

    // Find split point where character frequency < k
    int splitIdx = 0;
    while (splitIdx < len && freqMap.get(str.charAt(splitIdx)) >= k) {
      splitIdx++;
    }

    if (splitIdx == len) {
      return len; // All characters valid
    }

    // Recursively process left and right substring parts
    int left = longestSubstringDivideConquer(str.substring(0, splitIdx), k);

    // Skip all invalid chars after splitIdx
    while (splitIdx < len && freqMap.get(str.charAt(splitIdx)) < k) {
      splitIdx++;
    }
    int right = splitIdx < len ? longestSubstringDivideConquer(str.substring(splitIdx), k) : 0;

    return Math.max(left, right);
  }

  /**
   * Approach 2: Sliding Window (Optimized & Interview Preferred)
   *
   * Steps of Solution:
   * - For unique counts from 1 to 26 (lowercase letters): try maintaining a window
   *   with exactly uniqueTarget characters, all appearing at least k times.
   * - Expand and contract window as necessary to maintain the invariant.
   * - Update max length if current window is valid.
   *
   * Time Complexity: O(26 * n) = O(n) for lowercase letters
   * Space Complexity: O(1) (fixed window of 26 chars)
   *
   * @param str Input string
   * @param k Minimum repeats required for each character
   * @return Length of longest valid substring
   */
  public int longestSubstringSlidingWindow(String str, int k) {
    int maxLen = 0;
    int length = str.length();

    // Possible unique char counts for lowercase English only
    for (int targetUniqueChars = 1; targetUniqueChars <= 26; targetUniqueChars++) {
      int[] freqArr = new int[26];
      int left = 0, right = 0;
      int currentUniqueChars = 0, charsWithAtleastKFreq = 0;

      while (right < length) {
        if (currentUniqueChars <= targetUniqueChars) {
          int idx = str.charAt(right) - 'a';
          if (freqArr[idx] == 0) {
            currentUniqueChars++;
          }
          freqArr[idx]++;
          if (freqArr[idx] == k) {
            charsWithAtleastKFreq++;
          }
          right++;
        } else {
          int idx = str.charAt(left) - 'a';
          if (freqArr[idx] == k) {
            charsWithAtleastKFreq--;
          }
          freqArr[idx]--;
          if (freqArr[idx] == 0) {
            currentUniqueChars--;
          }
          left++;
        }
        // Check if current window is valid
        if (currentUniqueChars == targetUniqueChars && currentUniqueChars == charsWithAtleastKFreq) {
          maxLen = Math.max(maxLen, right - left);
        }
      }
    }
    return maxLen;
  }
}
