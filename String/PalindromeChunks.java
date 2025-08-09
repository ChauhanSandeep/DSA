package String;

/**
 * GeeksforGeeks Problem: https://www.geeksforgeeks.org/longest-possible-chunked-palindrome/
 *
 * Problem Statement:
 * Given a string, split it into the **maximum number of non-empty palindromic chunks** such that:
 * - Concatenating the chunks gives the original string.
 * - Each chunk is a palindrome when compared as a prefix-suffix mirror (not internal palindrome).
 *
 * Example:
 * Input: "apple bus machine bus apple"
 * Output: 5
 * Explanation: Chunks = ["apple", "bus", "machine", "bus", "apple"]
 *
 * Input: "ghiabcbaighiabcbaighi"
 * Output: 7
 * Explanation: Chunks = ["ghi", "abcba", "ghi", "abcba", "ghi"]
 *
 * Follow-up Questions:
 * 1. Can this be solved iteratively?
 *    - Yes, by maintaining two pointers and sliding window for prefix-suffix comparison.
 * 2. What if we want actual chunks instead of just count?
 *    - Store substrings when prefix == suffix before recursing.
 * 3. Does it have any connection with KMP or Z-algorithm?
 *    - Yes, prefix-suffix match patterns are fundamental in both.
 */

public class PalindromeChunks {

  public static void main(String[] args) {
    System.out.println("Max chunks (Recursive): " + maxChunksRecursive("apple bus machine bus apple")); // 5
    System.out.println("Max chunks (Recursive): " + maxChunksRecursive("volvo"));                         // 1
    System.out.println("Max chunks (Recursive): " + maxChunksRecursive("ghiabcbaighiabcbaighi"));         // 7
  }

  /**
   * Returns the maximum number of palindromic chunks from the input string.
   *
   * Steps:
   * - At each recursive step, try to match prefix and suffix of length `len` from 1 to n/2.
   * - If match found, consider them chunks and recursively check for remaining substring.
   * - If no match found, treat the entire string as one chunk.
   *
   * Time Complexity: O(n²) worst-case due to substring comparisons
   * Space Complexity: O(n) recursion depth
   */
  public static int maxChunksRecursive(String inputString) {
    if (inputString == null || inputString.isEmpty()) {
      return 0;
    }
    return countChunks(inputString, 0, inputString.length());
  }

  /**
   * Helper function that recursively counts palindromic chunks.
   *
   * @param str   The original string.
   * @param left  Start index of current substring.
   * @param right End index (exclusive) of current substring.
   * @return Maximum number of palindromic chunks in str[left:right]
   */
  private static int countChunks(String str, int left, int right) {
    int length = right - left;

    // Base cases
      if (length == 0) {
          return 0;
      }
      if (length == 1) {
          return 1;
      }

    // Try matching prefix and suffix chunks
    for (int len = 1; len <= length / 2; len++) {
      String prefix = str.substring(left, left + len);
      String suffix = str.substring(right - len, right);

      if (prefix.equals(suffix)) {
        // If match found, count them and recurse on middle part
        return 2 + countChunks(str, left + len, right - len);
      }
    }

    // No matching prefix-suffix found; whole string is one chunk
    return 1;
  }
}
