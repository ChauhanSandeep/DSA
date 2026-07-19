package strings.twopointers;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: Longest Chunked Palindrome Decomposition
 *
 * Split a string into the maximum number of non-empty chunks such that the chunk
 * sequence reads the same from the front and back. Individual chunks do not need
 * to be palindromes; matching prefix and suffix chunks are enough.
 *
 * Leetcode: https://leetcode.com/problems/longest-chunked-palindrome-decomposition/ (Hard)
 * Rating:   no contest Elo listed
 * Pattern:  Strings | Two-sided prefix-suffix matching | Recursion
 *
 * Example:
 *   Input:  text = "ghiabcdefhelloadamhelloabcdefghi"
 *   Output: 7
 *   Why:    ["ghi","abcdef","hello","adam","hello","abcdef","ghi"] mirrors by chunks.
 *
 * Follow-ups:
 *   1. How would you make the comparison iterative?
 *      Grow left and right buffers with two pointers until they match.
 *   2. How can substring comparisons be accelerated?
 *      Use rolling hashes or KMP-style prefix-suffix information.
 *   3. How would you return only the count with less memory?
 *      Count matches directly and avoid storing the chunk list.
 */
public class LongestChunkedPalindrome {

  public static void main(String[] args) {
    String[] inputs = {"ghiabcdefhelloadamhelloabcdefghi", "merchant", ""};
    int[] expected = {7, 1, 0};

    for (int i = 0; i < inputs.length; i++) {
      Result got = getMaxChunks(inputs[i]);
      System.out.printf("text=%s -> %d  expected=%d chunks=%s%n",
          inputs[i], got.count, expected[i], got.chunks);
    }
  }


    /**
   * Intuition: to maximize chunks, take the smallest matching prefix and suffix
   * whenever possible, then solve the middle independently. If no matching pair
   * exists, the whole remaining substring must be one chunk.
   *
   * Algorithm:
   *   1. Return an empty result for null or empty input.
   *   2. Recursively search for the shortest equal prefix and suffix in the range.
   *   3. Add that pair around the recursively solved middle.
   *   4. Use the full range as one chunk when no pair matches.
   *
   * Time:  O(n^2) - substring comparisons can rescan characters across recursion levels.
   * Space: O(n) - recursion and the returned chunk list can grow with the string.
   *
   * @param str Input string to decompose.
   * @return Result containing the maximum chunk count and chunk list.
   */
  public static Result getMaxChunks(String str) {
    if (str == null || str.isEmpty()) {
      return new Result(0, new ArrayList<>());
    }
    return countChunksAndStore(str, 0, str.length());
  }

    /** Recursively decomposes str[left, right) into mirrored chunks. */
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
