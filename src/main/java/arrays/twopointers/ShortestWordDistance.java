package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Shortest Word Distance
 *
 * Given a list of words and two target words, return the minimum difference
 * between any occurrence indices of the two targets. A single scan keeps the
 * latest seen position of each target.
 *
 * Leetcode: https://leetcode.com/problems/shortest-word-distance/ (Easy)
 * Rating:   acceptance 66.4% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Last-seen indices
 *
 * Example:
 *   Input:  words = ["practice","makes","perfect","coding","makes"], word1 = "makes", word2 = "coding"
 *   Output: 1
 *   Why:    "coding" at index 3 is adjacent to "makes" at index 4.
 *
 * Follow-ups:
 *   1. What if word1 equals word2?
 *      Track distinct occurrences and compare each new occurrence with the previous one.
 *   2. What if many queries are asked?
 *      Preprocess word-to-positions lists and answer each query with two pointers.
 *   3. Return the actual closest pair of indices?
 *      Store latestIndexWord1 and latestIndexWord2 whenever minDistance improves.
 *
 * Related: Shortest Word Distance II (244), Shortest Word Distance III (245).
 */
public class ShortestWordDistance {

public static void main(String[] args) {
  ShortestWordDistance solver = new ShortestWordDistance();
  String[] words = {"practice", "makes", "perfect", "coding", "makes"};
  String[][] queries = { {"makes", "coding"}, {"practice", "coding"} };
  int[] expected = { 1, 3 };

  for (int i = 0; i < queries.length; i++) {
    int got = solver.shortestDistance(words, queries[i][0], queries[i][1]);
    System.out.printf("words=%s word1=%s word2=%s -> %d  expected=%d%n",
        Arrays.toString(words), queries[i][0], queries[i][1], got, expected[i]);
  }
}

  /**
 * Intuition: the closest pair using the current word must involve the most
 * recent occurrence of the other target. Keeping only latestIndexWord1 and
 * latestIndexWord2 is enough because older occurrences are farther away in a
 * left-to-right scan.
 *
 * Algorithm:
 *   1. Initialize both latest indices to -1 and minDistance to infinity.
 *   2. Scan words from left to right.
 *   3. Update the latest index for word1 or word2 when seen.
 *   4. Once both were seen, update minDistance with their absolute difference.
 *
 * Time:  O(n) - one scan over the word list.
 * Space: O(1) - only two latest indices and the best distance are stored.
 *
 * @param words dictionary words in order
 * @param word1 first target word
 * @param word2 second target word
 * @return shortest index distance between the two targets
 */
  public int shortestDistance(String[] words, String word1, String word2) {
    int latestIndexWord1 = -1;
    int latestIndexWord2 = -1;
    int minDistance = Integer.MAX_VALUE;

    for (int index = 0; index < words.length; index++) {
      String currentWord = words[index];

      if (currentWord.equals(word1)) {
        latestIndexWord1 = index;
      }

      if (currentWord.equals(word2)) {
        latestIndexWord2 = index;
      }

      // Only calculate distance if both words have been seen
      if (latestIndexWord1 != -1 && latestIndexWord2 != -1) {
        int currentDistance = Math.abs(latestIndexWord1 - latestIndexWord2);
        minDistance = Math.min(minDistance, currentDistance);
      }
    }

    return minDistance;
  }
}
