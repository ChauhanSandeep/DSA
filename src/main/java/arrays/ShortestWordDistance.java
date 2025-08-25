package arrays;

/**
 * Problem: Shortest Word Distance
 * Leetcode Link: https://leetcode.com/problems/shortest-word-distance/
 *
 * Given a list of words and two words word1 and word2, return the shortest distance
 * (index difference) between these two words in the list.
 *
 * Example:
 * Input: wordsDict = ["practice", "makes", "perfect", "coding", "makes"]
 *        word1 = "makes", word2 = "coding"
 * Output: 1
 *
 * Follow-up Questions (FAANG-relevant):
 * 1. What if word1 and word2 are the same? (Leetcode follow-up: https://leetcode.com/problems/shortest-word-distance-iii/)
 *    - You need to ensure the two occurrences are at different indices.
 * 2. What if this function needs to be called many times? (Leetcode follow-up: https://leetcode.com/problems/shortest-word-distance-ii/)
 *    - Preprocess the word positions and optimize with two-pointer technique.
 */
public class ShortestWordDistance {

  public static void main(String[] args) {
    String[] wordsDict = {"practice", "makes", "perfect", "coding", "makes"};
    String word1 = "makes";
    String word2 = "coding";
    int result = new ShortestWordDistance().shortestDistance(wordsDict, word1, word2);
    System.out.println("Shortest distance: " + result);
  }

  /**
   * Calculates the shortest distance between two words in the given list.
   *
   * Steps:
   * - Traverse the list and track the most recent indices of word1 and word2.
   * - Each time both have been seen at least once, compute the distance and update the minimum.
   *
   * Algorithm:
   * - Single pass linear scan using two pointers (indices of word1 and word2).
   *
   * Time Complexity: O(n), where n is the number of words in the array.
   * Space Complexity: O(1), constant space used.
   *
   * @param words List of words (dictionary)
   * @param word1 First target word
   * @param word2 Second target word
   * @return The minimum index distance between word1 and word2 in the list
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
