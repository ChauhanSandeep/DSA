package String;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * LeetCode Problem: https://leetcode.com/problems/find-and-replace-in-string/
 *
 * Problem Statement:
 * Given a string `s`, an array of indices, and corresponding arrays of source and target strings,
 * perform multiple find-and-replace operations on the string.
 *
 * Each operation replaces `source[i]` with `target[i]` at position `indices[i]` *only if*
 * the substring of `s` starting at `indices[i]` matches `source[i]` exactly.
 *
 * The replacements must be done **simultaneously**, meaning no index shifting from earlier replacements
 * should affect later ones. Therefore, the operations should be sorted in descending order of indices.
 *
 * Example:
 * Input: s = "abcd", indices = [0,2], sources = ["a","cd"], targets = ["eee","fff"]
 * Output: "eeebfff"
 * Explanation:
 * - Replace "a" at index 0 with "eee" → "eeebcd"
 * - Replace "cd" at index 2 with "fff" → "eeebfff"
 *
 * LeetCode: https://leetcode.com/problems/find-and-replace-in-string/
 *
 * Follow-up questions:
 * 2. How would you optimize for very large strings?
 * — Consider using a character array or buffer pool.
 * 3. Can this be solved in linear time?
 * — Yes, with a mark-and-swap approach using a mapping array.
 *    Follow-up: https://leetcode.com/problems/find-and-replace-in-string/solutions/205982/java-c-python-o-n/
 */
public class FindReplaceInString {

  public static void main(String[] args) {
    FindReplaceInString solver = new FindReplaceInString();
    System.out.println(solver.findReplaceString("abcd", new int[]{0, 2}, new String[]{"a", "cd"},
        new String[]{"eee", "fff"})); // "eeebfff"
    System.out.println(solver.findReplaceString("abcd", new int[]{0, 2}, new String[]{"ab", "ec"},
        new String[]{"eee", "fff"})); // "abcd"
  }

  /**
   * Performs simultaneous find-and-replace operations on the string.
   *
   * Steps:
   * - Pair each index with its original position to track corresponding source and target.
   * - Sort these pairs in descending order to avoid index shift issues while replacing.
   * - For each valid match, replace the substring using StringBuilder.
   *
   * Algorithm: Greedy Replacement in Descending Index Order
   * Time Complexity: O(N log N + M) where N = length of indices, M = length of input string `str`
   * Space Complexity: O(M) due to use of StringBuilder
   */
  public String findReplaceString(String str, int[] indices, String[] sources, String[] targets) {
    List<ReplacementOp> operations = new ArrayList<>();

    // Pair index with corresponding source and target
    for (int i = 0; i < indices.length; i++) {
      operations.add(new ReplacementOp(indices[i], sources[i], targets[i]));
    }

    // Sort in descending order of index to prevent shifting issues during replacement
    Collections.sort(operations, (a, b) -> Integer.compare(b.index, a.index));

    StringBuilder result = new StringBuilder(str);

    for (ReplacementOp op : operations) {
      int sourceIndex = op.index;
      String source = op.source;
      String target = op.target;

      // Validate match before replacement
      if (sourceIndex + source.length() <= str.length() && str.startsWith(source, sourceIndex)) {
        result.replace(sourceIndex, sourceIndex + source.length(), target);
      }
    }

    return result.toString();
  }

  // Helper class to represent a replacement operation
  private static class ReplacementOp {
    int index;
    String source;
    String target;

    public ReplacementOp(int index, String source, String target) {
      this.index = index;
      this.source = source;
      this.target = target;
    }
  }
}
