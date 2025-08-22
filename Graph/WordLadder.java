package Graph;

import java.util.*;


/**
 * LeetCode 127: Word Ladder
 * https://leetcode.com/problems/word-ladder/
 *
 * Problem:
 * Given two words (beginWord and endWord) and a dictionary wordList,
 * find the length of the shortest transformation sequence from beginWord to endWord:
 * - Only one letter can be changed at a time.
 * - Each transformed word must exist in the dictionary.
 *
 * Example:
 * Input: beginWord = "hit", endWord = "cog",
 *        wordList = ["hot","dot","dog","lot","log","cog"]
 * Output: 5
 * Explanation: hit -> hot -> dot -> dog -> cog
 *
 */
public class WordLadder {

  public static void main(String[] args) {
    String[] wordList = {"but", "put", "big", "pot", "pog", "pig", "dog", "lot"};
    String beginWord = "bit", endWord = "pog";

    WordLadder solver = new WordLadder();
    int result = solver.ladderLength(beginWord, endWord, Arrays.asList(wordList));
    System.out.println("Shortest transformation length: " + result);
  }

  /**
   *
   * Approach:
   * 1. Use BFS to explore word transformations.
   * 2. Instead of building full graph O(N^2), precompute "patterns":
   *    e.g., "hot" -> "*ot", "h*t", "ho*".
   * 3. BFS (or Bidirectional BFS for optimization).
   *
   * Complexity:
   * - Preprocessing: O(N * M^2) (N = words, M = word length)
   * - BFS: O(N * M)
   * - Space: O(N * M) for graph + visited set
   *
   * Optimizations:
   * - Bidirectional BFS reduces search space drastically.
   *
   * @param beginWord start word
   * @param endWord   target word
   * @param wordList  dictionary
   * @return length of shortest transformation, or 0 if not possible
   */
  public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    Set<String> wordSet = new HashSet<>(wordList);
      if (!wordSet.contains(endWord)) {
          return 0;
      }

    // Pattern map: e.g., "*ot" -> hot, dot
    Map<String, List<String>> patternToWordsMapping = buildPatternToWordsMapping(wordSet, beginWord.length()); // <Pattern, List<Words>>

    // BFS from both ends
    Set<String> beginSet = new HashSet<>();
    Set<String> endSet = new HashSet<>();
    Set<String> visited = new HashSet<>();
    beginSet.add(beginWord);
    endSet.add(endWord);

    int steps = 1;

    while (!beginSet.isEmpty() && !endSet.isEmpty()) {
      // Always expand the smaller set (optimization)
      if (beginSet.size() > endSet.size()) {
        Set<String> temp = beginSet;
        beginSet = endSet;
        endSet = temp;
      }

      Set<String> neighborWords = new HashSet<>();
      for (String word : beginSet) {
        if (exploreAndCheckEndSet(word, endSet, visited, patternToWordsMapping, neighborWords)) {
          return steps + 1;
        }
      }
      beginSet = neighborWords;
      steps++;
    }
    return 0;
  }

  /**
   * Builds a pattern map: for each word, generate patterns with * substitution.
   * Example:
   * *ot -> hot, dot
   * h*t -> hot, hat
   *
   */
  private Map<String, List<String>> buildPatternToWordsMapping(Set<String> wordSet, int wordLength) {
    Map<String, List<String>> patternMap = new HashMap<>();

    for (String word : wordSet) {
      for (int i = 0; i < wordLength; i++) {
        String pattern = word.substring(0, i) + "*" + word.substring(i + 1);
        patternMap.computeIfAbsent(pattern, k -> new ArrayList<>()).add(word);
      }
    }
    return patternMap;
  }

  /**
   * Explores neighbors for BFS expansion.
   *
   * @return true if endSet is reached
   */
  private boolean exploreAndCheckEndSet(String word, Set<String> endSet, Set<String> visited,
      Map<String, List<String>> patternToWordsMapping, Set<String> nextLevel) {
    for (int i = 0; i < word.length(); i++) {
      String pattern = word.substring(0, i) + "*" + word.substring(i + 1);
      for (String neighbor : patternToWordsMapping.getOrDefault(pattern, Collections.emptyList())) {
        if (endSet.contains(neighbor)) {
          return true; // Found path
        }
        if (!visited.contains(neighbor)) {
          visited.add(neighbor);
          nextLevel.add(neighbor);
        }
      }
    }
    return false;
  }
}
