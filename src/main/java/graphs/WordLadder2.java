package graphs;

import java.util.*;

/**
 * Problem: Word Ladder II
 *
 * Find all shortest transformation sequences from beginWord to endWord. Each step
 * may change one letter, and every intermediate word must exist in the dictionary.
 *
 * Leetcode: https://leetcode.com/problems/word-ladder-ii/
 * Rating:   acceptance 27.9% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Bidirectional BFS + DFS | Shortest-path DAG reconstruction
 *
 * Example:
 *   Input:  beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
 *   Output: [[hit,hot,dot,dog,cog],[hit,hot,lot,log,cog]]
 *   Why:    both sequences use five words, and no shorter valid transformation
 *           reaches cog.
 *
 * Follow-ups:
 *   1. Return only the number of shortest ladders?
 *      Count paths in the BFS DAG with dynamic programming instead of materializing lists.
 *   2. Handle a dictionary with millions of words?
 *      Use wildcard buckets or bidirectional BFS with indexed neighbor generation.
 *   3. Weighted transformations?
 *      Replace BFS with Dijkstra and build a shortest-path DAG from equal best distances.
 *
 * Related: Word Ladder (127), Minimum Genetic Mutation (433), Open the Lock (752).
 */
public class WordLadder2 {

  public static void main(String[] args) {
    WordLadder2 solver = new WordLadder2();
    List<String> words = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
    List<List<String>> output = solver.findLadders("hit", "cog", words);
    output.sort(Comparator.comparing(Object::toString));
    List<List<String>> expected = Arrays.asList(Arrays.asList("hit", "hot", "dot", "dog", "cog"), Arrays.asList("hit", "hot", "lot", "log", "cog"));
    expected.sort(Comparator.comparing(Object::toString));
    System.out.printf("begin=%s end=%s words=%s -> %s  expected=%s%n", "hit", "cog", words, output, expected);
    List<List<String>> noPath = solver.findLadders("hit", "cog", Arrays.asList("hot", "dot", "dog"));
    System.out.printf("begin=%s end=%s words=%s -> %s  expected=%s%n", "hit", "cog", Arrays.asList("hot", "dot", "dog"), noPath, Collections.emptyList());
  }

  /**
   * Main method to find all shortest transformation sequences from beginWord to endWord.
   *
   * Steps:
   * 1. Check if endWord is in the dictionary, if not return empty list.
   * 2. Use bidirectional BFS to create an adjacency map of shortest transformations.
   * 3. Use DFS to backtrack and find all paths from beginWord to endWord using the adjacency map.
   *
   * Time Complexity: O(N * M * 26) where N is the number of words in the dictionary and M is the length of each word.
   * Space Complexity: O(N) for storing the adjacency map and visited sets.
   */
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
    // Initialize frontiers for bidirectional BFS. These will store the current level of words to be expanded.
    Set<String> frontSet = new HashSet<>(Collections.singleton(beginWord));
    Set<String> backSet = new HashSet<>(Collections.singleton(endWord));

    boolean isPathFound = false; // Track if a path has been found between the two frontiers
    boolean isReverseTraversal = false; // Track direction to expand in bidirectional BFS. True -> backSet, False -> frontSet

    while (!frontSet.isEmpty() && !backSet.isEmpty() && !isPathFound) {
      // Expand smaller frontier for efficiency. We do expansion of frontSet always so keep it smaller
      if (frontSet.size() > backSet.size()) {
        // Swap frontSet and backSet
        Set<String> temp = frontSet;
        frontSet = backSet;
        backSet = temp;
        isReverseTraversal = !isReverseTraversal;
      }

      dictionary.removeAll(frontSet); // Remove already-visited words from the dictionary (prevents revisiting).
      Set<String> neighborSet = new HashSet<>(); // collects words discovered for the next BFS level.

      for (String currentWord : frontSet) {

        for (String transformedWord : generateTransformations(currentWord)) {
          if (!dictionary.contains(transformedWord) && !backSet.contains(transformedWord)) {
            // Skip invalid words. Must either be in the dictionary (valid unexplored word) or in the opposite frontier (backSet)
            continue;
          }

          // reverse check is done because we are doing bidirectional BFS and we are expanding the smaller frontier
          // so the word that we are expanding is the one that is in the opposite frontier
          String from = isReverseTraversal ? transformedWord : currentWord; // from contains currentWord
          String to = isReverseTraversal ? currentWord : transformedWord;  // to contains transformedWord
          adjacencyMap.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

          if (backSet.contains(transformedWord)) {
            isPathFound = true; // Path found
          } else {
            neighborSet.add(transformedWord); // Add word to be expanded in next level
          }
        }

      }

      frontSet = neighborSet;
    }

    return isPathFound;
  }

  /**
   * This method uses DFS to backtrack and generate all shortest paths from beginWord to endWord.
   *
   * Steps:
   * 1. It adds the current word to the current path.
   * 2. If the current word is the endWord, it adds the current path to the list of all paths.
   * 3. If the current word has neighbors in the adjacency map, it recursively explores each neighbor.
   * 4. It backtracks by removing the last word from the current path
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
