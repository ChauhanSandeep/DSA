package strings.patternmatching;

import java.util.HashMap;
import java.util.Map;


/**
 * Problem: Longest Substring with At Least K Repeating Characters
 *
 * Given a string and k, return the length of the longest substring in which
 * every distinct character appears at least k times. Any character whose total
 * frequency is below k cannot be part of a valid substring crossing it.
 *
 * Leetcode: https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Strings | Divide and conquer | Sliding window by unique count
 *
 * Example:
 *   Input:  s = "ababbc", k = 2
 *   Output: 5
 *   Why:    "ababb" has a twice and b three times; adding c would violate k.
 *
 * Follow-ups:
 *   1. How do you return the substring itself?
 *      Store the best start and length instead of only the length.
 *   2. How does the strategy change for Unicode?
 *      Use maps for counts and iterate over observed unique-count targets.
 *   3. What if each character has a different k requirement?
 *      Validate each frequency against its own threshold during window checks.
 */
public class LongestSubstringKRepeating {

  public static void main(String[] args) {
    LongestSubstringKRepeating solver = new LongestSubstringKRepeating();

    String[] inputs = {"aaabb", "ababbc", ""};
    int[] ks = {3, 2, 2};
    int[] expected = {3, 5, 0};

    for (int i = 0; i < inputs.length; i++) {
      int got = solver.longestSubstringSlidingWindow(inputs[i], ks[i]);
      System.out.printf("s=%s k=%d -> %d  expected=%d%n",
          inputs[i], ks[i], got, expected[i]);
    }
  }


    /**
   * Intuition: a character that appears fewer than k times in the current string
   * can never be part of a valid substring spanning that character. Split around
   * such blockers and solve the remaining pieces recursively.
   *
   * Algorithm:
   *   1. Count frequencies in the current string.
   *   2. Find the first splitIdx whose character frequency is below k.
   *   3. If no split exists, the whole string is valid.
   *   4. Recurse on the left part and the next right part after all invalid split characters.
   *
   * Time:  O(n^2) worst case - repeated substring creation can rescan many characters.
   * Space: O(n) - recursion depth and substring copies can grow with the input.
   *
   * @param str Input string.
   * @param k Minimum required frequency for every character in the substring.
   * @return Length of the longest valid substring.
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
   * Intuition: for lowercase letters, the number of distinct characters in a
   * valid answer is between 1 and 26. If we fix that target count, a sliding
   * window can track both how many distinct characters it has and how many have
   * already reached k.
   *
   * Algorithm:
   *   1. Try each targetUniqueChars value from 1 to 26.
   *   2. Expand or shrink the window to keep at most that many unique characters.
   *   3. When every unique character has frequency at least k, update maxLen.
   *
   * Time:  O(26 * n) - one linear scan for each possible lowercase unique count.
   * Space: O(1) - the frequency array has 26 slots.
   *
   * @param str Input lowercase string.
   * @param k Minimum required frequency for every character in the substring.
   * @return Length of the longest valid substring.
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
