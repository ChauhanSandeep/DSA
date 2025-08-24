package dynamicprogramming;

/**
 * Problem: Shortest Common Superstring
 *
 * Given an array of strings, find the shortest string that contains all given strings as subsequences
 * while preserving their order.
 *
 * Example:
 * Input: ["abcd", "cdef", "fgh", "de"]
 * Output: 8 ("abcdefgh" is one of the possible shortest superstrings)
 *
 * Leetcode Link: https://leetcode.com/problems/find-the-shortest-superstring/
 *
 * Follow-up Questions (FAANG-style):
 * 1. Can you return the actual shortest superstring instead of its length?
 *    - Yes. Instead of returning the merged string's length, track and return the final string.
 * 2. Can this problem be solved using DP + Bitmask for optimal result?
 *    - Yes, use DP[i][mask] = shortest string ending with i and mask indicating included words.
 *    - Leetcode link for optimal DP + Bitmask approach: https://leetcode.com/problems/find-the-shortest-superstring/
 */
public class ShortestCommonSuperstring {

  public static void main(String[] args) {
    String[] words = {"abcd", "cdef", "fgh", "de"};
    ShortestCommonSuperstring solver = new ShortestCommonSuperstring();
    int length = solver.findShortestSuperstringLength(words);
    System.out.println("Shortest Common Superstring Length: " + length);
  }

  /**
   * Finds the length of the shortest superstring that contains all strings in the array
   * as subsequences while preserving order.
   *
   * Steps:
   * 1. Repeatedly identify the pair of strings with maximum suffix-prefix overlap.
   * 2. Merge them and reduce the list by 1.
   * 3. Continue until only one string remains.
   *
   * Time Complexity: O(n^3 * k), where n = number of words, k = average word length.
   * Space Complexity: O(n * k), for intermediate merged strings.
   *
   * @param words Input array of strings.
   * @return Length of the shortest superstring.
   */
  public int findShortestSuperstringLength(String[] words) {
      if (words.length == 0) {
          return 0;
      }
      if (words.length == 1) {
          return words[0].length();
      }

    // Repeat until all strings are merged into one
    while (words.length > 1) {
      int toBeMergedWordIdx1 = -1;
      int toBeMergedWordIdx2 = -1;
      int maxOverlap = -1;    // Track maximum overlap found
      String mergedWord = ""; // Track the best merged string

      // Step1 : Find the 2 best words to merge based on maximum overlap
      for (int i = 0; i < words.length; i++) {
        for (int j = 0; j < words.length; j++) {
            if (i == j) {
                continue;
            }
          int overlapLength = calculateOverlap(words[i], words[j]);
          int mergedLength = words[i].length() + words[j].length() - overlapLength;

          // If this overlap is better than the previous best, update indices and best merged string
          if (overlapLength > maxOverlap || (overlapLength == maxOverlap && mergedLength < mergedWord.length())) {
            maxOverlap = overlapLength;
            toBeMergedWordIdx1 = i;
            toBeMergedWordIdx2 = j;
            mergedWord = mergeWithOverlap(words[i], words[j], overlapLength);
          }
        }
      }

      // Step2 : If no overlap is found, that means that all words are unique and cannot be merged further. Sum up all
      if (maxOverlap == -1) {
        int total = 0;
        for (String word : words) {
          total += word.length();
        }
        return total;
      }

      // Step3: Create a new array with the merged string and remove the two merged words
      String[] updatedWords = new String[words.length - 1];
      int index = 0;
      for (int i = 0; i < words.length; i++) {
        if (i != toBeMergedWordIdx1 && i != toBeMergedWordIdx2) {
          updatedWords[index++] = words[i];
        }
      }
      updatedWords[index] = mergedWord;

      words = updatedWords; // Update for next iteration
    }

    return words[0].length();
  }

  /**
   * Calculates the maximum suffix-prefix overlap between two strings.
   *
   * For example, overlap("abcde", "cdefg") = 3 ("cde" overlaps)
   *
   * @param first First string
   * @param second Second string
   * @return Length of the overlap
   */
  private int calculateOverlap(String first, String second) {
    for (int i = 0; i < first.length(); i++) {
      if (second.startsWith(first.substring(i))) {
        return first.length() - i;
      }
    }
    return 0;
  }

  /**
   * Merges two strings based on given overlap.
   *
   * Example:
   * str1 = "abcde", str2 = "cdefg", overlap = 3 → "abcdefg"
   *
   * @param str1 First string
   * @param str2 Second string
   * @param overlap Number of overlapping characters
   * @return Merged string
   */
  private String mergeWithOverlap(String str1, String str2, int overlap) {
    return str1 + str2.substring(overlap);
  }
}