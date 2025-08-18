package Graph;

import java.util.*;

/**
 * Problem: Word Ladder II
 * LeetCode: https://leetcode.com/problems/word-ladder-ii/
 *
 * Problem Statement:
 * Given two words, beginWord and endWord, and a dictionary's word list, find all shortest transformation
 * sequences from beginWord to endWord, such that:
 * 1. Only one letter can be changed at a time.
 * 2. Each transformed word must exist in the word list.
 *
 * Example:
 * Input:
 *   beginWord = "hit"
 *   endWord = "cog"
 *   wordList = ["hot","dot","dog","lot","log","cog"]
 * Output:
 * [
 *   ["hit","hot","dot","dog","cog"],
 *   ["hit","hot","lot","log","cog"]
 * ]
 *
 * Follow-up Questions:
 * 1. How to solve it using single-direction BFS instead of bidirectional BFS? (Less optimal, O(N*M*26) per level)
 * 2. How to count only the shortest transformation length instead of generating paths? (Use BFS with level tracking)
 * 3. How to handle extremely large dictionaries? (Use on-the-fly neighbor generation with hashing)
 *
 * Approach:
 * - Use bidirectional BFS to construct an adjacency map that only stores shortest transformations.
 * - Use DFS to backtrack and generate all shortest paths.
 */
public class WordLadder2 {

  public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
    Set<String> dictionary = new HashSet<>(wordList);
    if (!dictionary.contains(endWord)) {
      return Collections.emptyList();
    }

    // Perform bidirectional BFS to create adjacency map
    Map<String, List<String>> adjacencyMap = new HashMap<>();
    boolean pathFound = bidirectionBfsToCreateAdjacencyMap(beginWord, endWord, dictionary, adjacencyMap);

    // Perform DFS to backtrack and find all shortest paths
    List<List<String>> allPaths = new ArrayList<>();
    if (pathFound) {
      depthFirstSearch(beginWord, endWord, adjacencyMap, new ArrayList<>(), allPaths);
    }
    return allPaths;
  }

  /**
   * This method uses bidirectional BFS to construct an adjacency map that only stores shortest transformations.
   * 1. It creates two frontiers, one for each direction (from beginWord to endWord and vice versa).
   * 2. It expands the frontier with words that differ by one letter.
   * 3. It uses a dictionary to filter out words that are not in the dictionary or already in the opposite frontier.
   * 4. It adds transformed words to the adjacency map if they are not already present in the map.
   * 5. It returns true if a path has been found, false otherwise.
   */
  private boolean bidirectionBfsToCreateAdjacencyMap(String beginWord, String endWord, Set<String> dictionary,
      Map<String, List<String>> adjacencyMap) {
    Set<String> frontSet = new HashSet<>(Collections.singleton(beginWord));
    Set<String> backSet = new HashSet<>(Collections.singleton(endWord));
    boolean pathFound = false; // Track if a path has been found
    boolean reverse = false; // Track which direction to expand in bidirectional BFS

    while (!frontSet.isEmpty() && !backSet.isEmpty() && !pathFound) {
      // Expand smaller frontier for efficiency. We do expansion of frontSet always so keep it smaller
      if (frontSet.size() > backSet.size()) {
        Set<String> temp = frontSet;
        frontSet = backSet;
        backSet = temp;
        reverse = !reverse;
      }

      dictionary.removeAll(frontSet);
      Set<String> neighborSet = new HashSet<>(); // Store words to be expanded in next level

      for (String currentWord : frontSet) {
        for (String transformedWord : generateTransformations(currentWord)) {
          if (!dictionary.contains(transformedWord) && !backSet.contains(transformedWord)) {
            // only proceed if a word is in the dictionary or already in the opposite frontier
            continue;
          }

          // reverse check is done because we are doing bidirectional BFS and we are expanding the smaller frontier
          // so the word that we are expanding is the one that is in the opposite frontier
          String from = reverse ? transformedWord : currentWord; // from contains currentWord
          String to = reverse ? currentWord : transformedWord;  // to contains transformedWord
          adjacencyMap.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

          if (backSet.contains(transformedWord)) {
            pathFound = true; // Path found
          } else {
            neighborSet.add(transformedWord); // Add word to be expanded in next level
          }
        }
      }

      frontSet = neighborSet;
    }
    return pathFound;
  }

  /**
   * This method uses DFS to backtrack and generate all shortest paths from beginWord to endWord.
   */
  private void depthFirstSearch(String currentWord, String endWord, Map<String, List<String>> adjacencyMap,
      List<String> currentPath, List<List<String>> allPaths) {

    // Base case
    currentPath.add(currentWord);

    // Recursive case
    if (currentWord.equals(endWord)) {
      allPaths.add(new ArrayList<>(currentPath));
    } else if (adjacencyMap.containsKey(currentWord)) {
      for (String nextWord : adjacencyMap.get(currentWord)) {
        depthFirstSearch(nextWord, endWord, adjacencyMap, currentPath, allPaths);
      }
    }
    // Backtrack
    currentPath.remove(currentPath.size() - 1);
  }

  /**
   * Helper method to generate all possible transformations by swapping one letter
   */
  private List<String> generateTransformations(String word) {
    List<String> transformations = new ArrayList<>();
    char[] wordChars = word.toCharArray();
    for (int i = 0; i < wordChars.length; i++) {
      char originalChar = wordChars[i];
      for (char c = 'a'; c <= 'z'; c++) {
        if (c == originalChar) continue;
        wordChars[i] = c;
        transformations.add(new String(wordChars));
      }
      wordChars[i] = originalChar;
    }
    return transformations;
  }
}
