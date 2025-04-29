package String;

/**
 * Implement the "strStr()" function that finds the index of the first occurrence of a substring.
 * For example : strStr("GeeksForGeeks", "For") will return 5.
 *
 * Approaches:
 * 1. **Brute Force (O(N * M))**: Checks all possible starting positions in the main string.
 * 2. **KMP Algorithm (O(N + M))**: Uses a prefix table (LPS) to avoid redundant comparisons.
 *
 * Time Complexity:
 * - Brute Force: **O(N * M)**
 * - KMP Algorithm: **O(N + M)**
 *
 * Space Complexity:
 * - Brute Force: **O(1)**
 * - KMP Algorithm: **O(M)** (for LPS array)
 *
 * LeetCode Equivalent: https://leetcode.com/problems/implement-strstr/
 */
public class StrStr {
  public static void main(String[] args) {
    String text = "GeeksForGeeks";
    String pattern = "For";

    System.out.println(findSubstringBruteForce(text, pattern)); // 5
    System.out.println(findSubstringKMP(text, pattern)); // 5
  }

  /**
   * Brute Force Approach: Finds the first occurrence of a substring in a string.
   *
   * @param text The main string.
   * @param pattern The substring to search for.
   * @return The index of the first occurrence, or -1 if not found.
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

  /**
   * Helper function to check if a substring starting at a given index matches the pattern.
   */
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
   * Optimized Approach: Finds the first occurrence using the KMP (Knuth-Morris-Pratt) Algorithm.
   *
   * @param text The main string.
   * @param pattern The substring to search for.
   * @return The index of the first occurrence, or -1 if not found.
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

  /**
   * Computes the LPS (Longest Prefix Suffix) array for the KMP algorithm.
   *
   * @param pattern The pattern for which LPS is computed.
   * @return The LPS array.
   */
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