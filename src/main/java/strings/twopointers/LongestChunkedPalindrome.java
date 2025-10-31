package strings.twopointers;

import java.util.ArrayList;
import java.util.List;


/**
 * GeeksforGeeks Problem: https://www.geeksforgeeks.org/longest-possible-chunked-palindrome/
 *
 * Problem Statement:
 * Given a string, split it into the **maximum number of non-empty palindromic chunks** such that:
 * - Concatenating the chunks gives the original string.
 * - Each chunk forms a prefix-suffix mirror (the chunk itself does not need to be an internal palindrome).
 *
 * Example:
 * Input: "ghiabcdefhelloadamhelloabcdefghi"
 * Output: 7, Chunks = ["ghi", "abcdef", "hello", "adam", "hello", "abcdef", "ghi"]
 *
 * Input: "merchant"
 * Output: 1, Chunks = ["merchant"]
 *
 * Input: "antaprezatepzapreanta"
 * Output: 11, Chunks = ["a","nt","a","pre","za","tpe","za","pre","a","nt","a"]
 *
 * Follow-up Questions (FAANG-style):
 * 1. Can this be solved iteratively?
 *    - Yes, use two pointers and compare sliding window substrings from both ends.
 * 2. What if we want the actual chunks rather than just the count?
 *    - Store the chunks whenever a matching prefix-suffix is found.
 * 3. Does this relate to classic string matching or palindrome finding algorithms?
 *    - Yes, prefix-suffix matching is the key aspect as in KMP/Z-algorithm.
 */
public class LongestChunkedPalindrome {

  public static void main(String[] args) {
    Result result1 = getMaxChunks("ghiabcdefhelloadamhelloabcdefghi");
    System.out.println("Max chunks: " + result1.count + ", Chunks: " + result1.chunks);

    Result result2 = getMaxChunks("merchant");
    System.out.println("Max chunks: " + result2.count + ", Chunks: " + result2.chunks);

    Result result3 = getMaxChunks("antaprezatepzapreanta");
    System.out.println("Max chunks: " + result3.count + ", Chunks: " + result3.chunks);

    Result result4 = getMaxChunks("volvo");
    System.out.println("Max chunks: " + result4.count + ", Chunks: " + result4.chunks);
  }

  /**
   * Returns both the maximum number of palindromic chunks and the list of chunks (recursive approach).
   *
   * Steps of Solution:
   * - At each recursive step, try to match a prefix-suffix of length from 1 up to half the current substring.
   * - If a match is found, add matched prefix and suffix chunks to the result and recurse into the substring between these chunks.
   * - If no match is found, the entire substring is one chunk.
   *
   * Time Complexity: O(N^2) worst-case (due to substring equality checks at each recursive step)
   * Space Complexity: O(N) for recursion stack and storing chunks
   *
   * @param str Input string to be split
   * @return Result object containing count and list of chunked palindrome segments
   */
  public static Result getMaxChunks(String str) {
    if (str == null || str.isEmpty()) {
      return new Result(0, new ArrayList<>());
    }
    return countChunksAndStore(str, 0, str.length());
  }

  /**
   * Recursive helper for chunk counting and retrieval.
   *
   * @param str   Original string
   * @param left  Start index (inclusive) of current substring
   * @param right End index (exclusive) of current substring
   * @return Result object with chunk count and actual chunk strings
   */
  private static Result countChunksAndStore(String str, int left, int right) {
    int length = right - left;
    Result result = new Result();

    // Base cases
    if (length == 0) {
      result.count = 0;
      return result;
    }
    if (length == 1) {
      result.count = 1;
      result.chunks.add(str.substring(left, right));
      return result;
    }

    for (int chunkLen = 1; chunkLen <= length / 2; chunkLen++) {
      String prefix = str.substring(left, left + chunkLen);
      String suffix = str.substring(right - chunkLen, right);

      if (prefix.equals(suffix)) {
        Result midResult = countChunksAndStore(str, left + chunkLen, right - chunkLen);
        // Place prefix at start, suffix at end, and all middle chunks in between
        result.count = 2 + midResult.count;
        result.chunks.add(prefix);
        result.chunks.addAll(midResult.chunks);
        result.chunks.add(suffix);
        return result;
      }
    }

    // No matching prefix-suffix found; treat whole substring as one chunk
    result.count = 1;
    result.chunks.add(str.substring(left, right));
    return result;
  }

  /**
   * Helper class to store both the chunk count and actual chunks.
   */
  private static class Result {
    int count;
    List<String> chunks;

    public Result() {
      this.count = 0;
      this.chunks = new ArrayList<>();
    }

    public Result(int count, List<String> chunks) {
      this.count = count;
      this.chunks = chunks;
    }
  }
}
