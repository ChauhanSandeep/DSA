package hashing;

import java.util.ArrayList;
import java.util.List;


/**
 * 🔗 Problem: https://leetcode.com/problems/partition-labels/
 *
 * Given a string `s`, partition it into as many parts as possible such that
 * no letter appears in more than one part. Return the list of partition sizes.
 *
 * Approach:
 * 1. Record the **last occurrence** of each character.
 * 2. Traverse the string while keeping track of the **farthest boundary** (`maxPartitionEnd`) of the current window.
 * 3. When the current index equals `maxPartitionEnd`, we finalize a partition.
 *
 * ⏱ Time Complexity: O(N) — one pass for last indices, one pass to compute partitions.
 * 🧠 Space Complexity: O(1) — only 26-sized array for lowercase letters.
 */
public class PartitionLabels {

  public static void main(String[] args) {
    PartitionLabels solver = new PartitionLabels();
    String input = "ababcbacadefegdehijhklij";

    List<Integer> partitionSizes = solver.getPartitionSizes(input);
    System.out.println("Partition sizes: " + partitionSizes);
  }

  /**
   * Returns a list of partition sizes such that each letter appears in at most one part.
   *
   * @param str The input lowercase string.
   * @return List of sizes of each partition.
   */
  public List<Integer> getPartitionSizes(String str) {
    int[] lastIndex = new int[26];  // Last index for each lowercase char

    // Step 1: Build last occurrence map
    for (int i = 0; i < str.length(); i++) {
      lastIndex[str.charAt(i) - 'a'] = i;
    }

    List<Integer> result = new ArrayList<>();
    int start = 0, end = 0;

    // Step 2: Traverse string and form partitions
    for (int i = 0; i < str.length(); i++) {
      end = Math.max(end, lastIndex[str.charAt(i) - 'a']);

      if (i == end) {
        result.add(end - start + 1);
        start = i + 1;
      }
    }

    return result;
  }
}