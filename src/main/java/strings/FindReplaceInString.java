package strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;


/**
 * Problem: Find And Replace in String
 *
 * Apply multiple replacements to the original string. A replacement applies only
 * when its source matches at its index, and all replacements are simultaneous.
 *
 * Leetcode: https://leetcode.com/problems/find-and-replace-in-string/ (Medium)
 * Rating:   zerotrac 1461 (Q2, weekly-84)
 * Pattern:  String | Sort operations | Simultaneous replacement
 *
 * Example:
 *   Input:  s = "abcd", indices = [0,2], sources = ["a","cd"], targets = ["eee","fff"]
 *   Output: "eeebfff"
 *   Why:    both sources match the original string at their requested indices.
 *
 * Follow-ups:
 *   1. Linear time? Mark valid operations by start index and build once left to right.
 *   2. Overlaps? Define priority or reject overlapping operations.
 *   3. Huge strings? Append unchanged ranges and replacements into one builder.
 */
public class FindReplaceInString {

  public static void main(String[] args) {
    FindReplaceInString solver = new FindReplaceInString();
    String[] inputs = {"abcd", "abcd"};
    int[][] indices = {{0, 2}, {0, 2}};
    String[][] sources = {{"a", "cd"}, {"ab", "ec"}};
    String[][] targets = {{"eee", "fff"}, {"eee", "fff"}};
    String[] expected = {"eeebfff", "eeecd"};
    for (int i = 0; i < inputs.length; i++) {
      String got = solver.findReplaceString(inputs[i], indices[i], sources[i], targets[i]);
      System.out.printf("s=%s indices=%s -> %s  expected=%s%n", inputs[i], Arrays.toString(indices[i]), got, expected[i]);
    }
  }


    /**
   * Intuition: applying replacements from left to right shifts later indices.
   * Sorting by descending index makes every edit occur to the right of all edits
   * still waiting, so original indices remain usable.
   *
   * Algorithm:
   *   1. Package each index, source, and target into an operation.
   *   2. Sort operations by descending index.
   *   3. Validate each source against the original string.
   *   4. Replace matching ranges in the builder.
   *
   * Time:  O(k log k + n) - operations are sorted and applied to the builder.
   * Space: O(k + n) - stores operations and the mutable result.
   *
   * @param str original string
   * @param indices replacement start indices
   * @param sources source strings to match
   * @param targets replacement strings
   * @return string after all valid simultaneous replacements
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
