package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * Problem: Count Unique Characters of All Substrings of a Given String
 *
 * For every substring, count how many characters appear exactly once inside that
 * substring, then return the sum across all substrings. The answer fits in a
 * 32-bit integer.
 *
 * Leetcode: https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/
 * Rating:   2034 (zerotrac Elo)
 * Pattern:  Contribution counting | Index DP idea | Previous/next occurrence
 *
 * Example:
 *   Input:  s = "ABA"
 *   Output: 8
 *   Why:    the middle B is unique in six substrings, and the two A occurrences
 *           contribute one substring each, for a total of 8.
 *
 * Follow-ups:
 *   1. What if the alphabet is fixed uppercase English letters?
 *      Use two int arrays per character instead of a map of position lists.
 *   2. Count characters that appear exactly k times in all substrings?
 *      Use neighbouring occurrence windows around each kth occurrence group.
 *   3. Find the substring with the maximum number of unique characters?
 *      Use a sliding window if the goal is distinct characters, but exact-once counts need frequency tracking.
 *
 * Related: Longest Substring Without Repeating Characters (3), Distinct Subsequences II (940).
 */
public class CountUniqueCharactersOfAllSubstrings {

    public static void main(String[] args) {
        CountUniqueCharactersOfAllSubstrings solver = new CountUniqueCharactersOfAllSubstrings();
        String[] inputs = {"", "ABC", "ABA"};
        int[] expected = {0, 10, 8};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.uniqueLetterString(inputs[i]);
            System.out.printf("s=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


/**
     * Intuition: instead of asking every substring what it contains, ask each
     * character occurrence how many substrings count it as unique. An occurrence at
     * position p stays unique exactly when the substring starts after the previous
     * same character and ends before the next same character. That gives
     * (p - previous) choices for the left boundary and (next - p) choices for the
     * right boundary, so their product is this occurrence's contribution.
     *
     * Algorithm:
     *   1. Build a map from each character to all of its positions, seeded with left boundary -1.
     *   2. Append the right boundary inputString.length() to each position list.
     *   3. For every real occurrence, add leftDistance * rightDistance to totalSum.
     *
     * Time:  O(n) - each character position is inserted once and visited once again.
     * Space: O(n) - the position lists store all character indices.
     *
     * @param inputString string whose substring unique-character sum is needed
     * @return total unique-character contribution over all substrings
     */
 public int uniqueLetterString(String inputString) {
    if (inputString == null || inputString.isEmpty()) {
      return 0;
    }

    // Map character to list of its positions
    Map<Character, List<Integer>> characterIndexMap = new HashMap<>();

    // Build position map with boundary markers
    for (int i = 0; i < inputString.length(); i++) {
      char currentChar = inputString.charAt(i);

      characterIndexMap.computeIfAbsent(currentChar, k -> {
        List<Integer> list = new ArrayList<>();
        list.add(-1); // Left boundary
        return list;
      }).add(i);
    }

    int totalSum = 0;

    // Calculate contributions for each character
    for (List<Integer> positions : characterIndexMap.values()) {
      positions.add(inputString.length()); // Right boundary

      // Apply contribution formula for each position
      for (int i = 1; i < positions.size() - 1; i++) {
        int leftDistance = positions.get(i) - positions.get(i - 1);
        int rightDistance = positions.get(i + 1) - positions.get(i);
        totalSum += leftDistance * rightDistance; // Because left * right gives count of substrings where this char is unique
      }
    }

    return totalSum;
  }
}
