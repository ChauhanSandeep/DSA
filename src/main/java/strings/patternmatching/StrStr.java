package strings.patternmatching;

/**
 * Problem: Implement strStr()
 *
 * Given text and pattern, return the first index where pattern occurs in text,
 * or -1 if it is absent. This file keeps both a direct comparison approach and
 * a KMP approach.
 *
 * Leetcode: https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/ (Easy)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Strings | Brute force matching | KMP
 *
 * Example:
 *   Input:  text = "GeeksForGeeks", pattern = "For"
 *   Output: 5
 *   Why:    the substring starting at index 5 is exactly "For".
 *
 * Follow-ups:
 *   1. How do you handle many patterns at once?
 *      Use Aho-Corasick to share prefix work across patterns.
 *   2. How can matching be made case-insensitive?
 *      Normalize both strings or compare normalized characters while scanning.
 *   3. How would you search a stream?
 *      Keep KMP state between chunks and continue from the saved pattern index.
 */
public class StrStr {
  public static void main(String[] args) {
    String[] texts = {"GeeksForGeeks", "mississippi", "abc"};
    String[] patterns = {"For", "issip", ""};
    int[] expected = {5, 4, 0};

    for (int i = 0; i < texts.length; i++) {
      int got = findSubstringKMP(texts[i], patterns[i]);
      System.out.printf("text=%s pattern=%s -> %d  expected=%d%n",
          texts[i], patterns[i], got, expected[i]);
    }
  }


    /**
   * Intuition: the direct baseline tries every possible start in text. A start can
   * only match when its first character equals pattern's first character, and then
   * isMatch verifies the remaining characters.
   *
   * Algorithm:
   *   1. Reject null inputs and the empty-pattern case used by this brute method.
   *   2. Try each start index where pattern can still fit.
   *   3. Return the first start whose substring matches pattern.
   *   4. Return -1 if no start matches.
   *
   * Time:  O(n * m) - up to m characters are compared at each start.
   * Space: O(1) - only indices are stored.
   *
   * @param text Text to search inside.
   * @param pattern Pattern to find.
   * @return First starting index, or -1 when absent.
   */
  public static int findSubstringBruteForce(String text, String pattern) {
      if (text == null || pattern == null || pattern.isEmpty()) {
          return -1;
      }

    int textLength = text.length();
    int patternLength = pattern.length();

    for (int i = 0; i <= textLength - patternLength; i++) {
      if (text.charAt(i) == pattern.charAt(0) && isMatch(text, pattern, i)) {
        return i;
      }
    }
    return -1;
  }

    /** Checks whether pattern matches text starting exactly at index. */
  private static boolean isMatch(String text, String pattern, int index) {
    int patternLength = pattern.length();
      if (index + patternLength > text.length()) {
          return false;
      }

    for (int i = 0; i < patternLength; i++) {
        if (text.charAt(index + i) != pattern.charAt(i)) {
            return false;
        }
    }
    return true;
  }

    /**
   * Intuition: KMP remembers how much of pattern is still reusable after a
   * mismatch. The LPS array lets j jump to the next best prefix length without
   * moving i backward in text.
   *
   * Algorithm:
   *   1. Return 0 for an empty pattern.
   *   2. Build the LPS array for pattern.
   *   3. Scan text with i and pattern with j, advancing both on matches.
   *   4. On mismatch, jump j by LPS or advance i when j is zero.
   *
   * Time:  O(n + m) - each pointer advances or jumps through its string linearly.
   * Space: O(m) - the LPS array stores one entry per pattern character.
   *
   * @param text Text to search inside.
   * @param pattern Pattern to find.
   * @return First starting index, or -1 when absent.
   */
  public static int findSubstringKMP(String text, String pattern) {
      if (pattern.isEmpty()) {
          return 0;
      }

    int textLength = text.length();
    int patternLength = pattern.length();

    // Compute LPS (Longest Prefix Suffix) array
    int[] lps = computeLPSArray(pattern);

    int i = 0, j = 0; // Pointers for text and pattern

    while (i < textLength) {
      if (text.charAt(i) == pattern.charAt(j)) {
        i++;
        j++;

        // If we found a match
        if (j == patternLength) {
          return i - patternLength; // Found the pattern, return start index
        }
      } else {
        if (j != 0) {
          j = lps[j - 1]; // Move to the last known prefix match
        } else {
          i++; // Move forward in the text
        }
      }
    }
    return -1; // No match found
  }

    /** Builds the longest-prefix-suffix array for KMP matching. */
  private static int[] computeLPSArray(String pattern) {
    int length = pattern.length();
    int[] lps = new int[length];
    int j = 0; // Length of the previous longest prefix suffix
    int i = 1; // Start from the second character

    while (i < length) {
      if (pattern.charAt(i) == pattern.charAt(j)) {
        lps[i] = ++j;
        i++;
      } else {
        if (j != 0) {
          j = lps[j - 1]; // Try the last known prefix match
        } else {
          lps[i] = 0;
          i++;
        }
      }
    }
    return lps;
  }
}