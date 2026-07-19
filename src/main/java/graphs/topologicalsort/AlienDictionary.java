package graphs.topologicalsort;

import java.util.*;

  /**
   * Intuition: the first differing character between two adjacent sorted words is
   * the only new ordering fact that pair can prove. Collect those facts as edges,
   * then topologically sort characters with zero remaining prerequisites.
   *
   * Algorithm:
   *   1. Initialize adjacency and indegree entries for every character that appears.
   *   2. Compare adjacent words, rejecting invalid prefix order and adding the first-difference edge.
   *   3. Queue all zero-indegree characters and process them with Kahn's algorithm.
   *   4. Return the built order only if every character was processed; otherwise return empty string.
   *
   * Time:  O(C) - adjacent comparisons and graph traversal touch the input characters and edges.
   * Space: O(1) - lowercase alphabet size is bounded, though maps are used for clarity.
   *
   * @param words dictionary words sorted by the alien alphabet
   * @return one valid character order, or empty string if the dictionary is invalid
   */
public class AlienDictionary {

  public static void main(String[] args) {
    AlienDictionary solver = new AlienDictionary();
    String[] words1 = {"wrt", "wrf", "er", "ett", "rftt"};
    String[] words2 = {"abc", "ab"};

    System.out.printf("words=%s -> %s  expected=wertf%n",
        Arrays.toString(words1), solver.findAlienLanguageOrder(words1));
    System.out.printf("words=%s -> '%s'  expected='' %n",
        Arrays.toString(words2), solver.findAlienLanguageOrder(words2));
  }


  /**
   * Derives character order in alien language using BFS topological sort.
   *
   * Steps:
   * 1. Initialize adjacency list and in-degree map for all unique characters.
   * 2. For each adjacent pair of words, find the first differing character
   *    and build directed edges accordingly.
   * 3. Perform BFS (Kahn’s Algorithm) to get topological ordering.
   * 4. If cycle detected (output length < number of unique characters), return "".
   *
   * Time Complexity: O(C), where C is total number of characters.
   * Space Complexity: O(1) since max unique characters = 26.
   *
   * @param words dictionary sorted in alien language order
   * @return a valid character order, or "" if invalid
   */
  public String findAlienLanguageOrder(String[] words) {
    if (words == null || words.length == 0) return "";

    // Step 1: Initialize Graph Structures
    Map<Character, List<Character>> adjacencyList = new HashMap<>();
    Map<Character, Integer> inDegree = new HashMap<>();

    // Add all unique characters
    for (String word : words) {
      for (char character : word.toCharArray()) {
        adjacencyList.putIfAbsent(character, new ArrayList<>());
        inDegree.putIfAbsent(character, 0);
      }
    }

    // Step 2: Build Graph Edges from Word Ordering
    for (int i = 0; i < words.length - 1; i++) {
      String word1 = words[i];
      String word2 = words[i + 1];

      // Invalid case: word2 is prefix of word1 but shorter
      if (word1.length() > word2.length() && word1.startsWith(word2)) {
        return "";
      }

      // Compare characters to establish precedence
      for (int j = 0; j < Math.min(word1.length(), word2.length()); j++) {
        char c1 = word1.charAt(j);
        char c2 = word2.charAt(j);
        if (c1 != c2) {
          // Add directed edge if not already added
          if (!adjacencyList.get(c1).contains(c2)) {
            adjacencyList.get(c1).add(c2);
            inDegree.put(c2, inDegree.get(c2) + 1);
          }
          break; // stop at first difference
        }
      }
    }

    // Step 3: Topological Sort (Kahn’s Algorithm)
    StringBuilder resultOrder = new StringBuilder();
    Deque<Character> queue = new ArrayDeque<>();

    // Start with characters having in-degree 0
    for (Map.Entry<Character, Integer> entry : inDegree.entrySet()) {
      if (entry.getValue() == 0) {
        queue.offer(entry.getKey());
      }
    }

    while (!queue.isEmpty()) {
      char current = queue.poll();
      resultOrder.append(current);

      // Reduce in-degree for neighbors
      for (char neighbor : adjacencyList.get(current)) {
        inDegree.put(neighbor, inDegree.get(neighbor) - 1);
        if (inDegree.get(neighbor) == 0) {
          queue.offer(neighbor);
        }
      }
    }

    // Step 4: Validate result (check for cycle)
    return resultOrder.length() == inDegree.size() ? resultOrder.toString() : "";
  }

  /**
   * Alternative DFS-based Topological Sort approach with cycle detection.
   *
   * Time Complexity: O(C)
   * Space Complexity: O(C)
   */
  public String findAlienLanguageOrderDFS(String[] words) {
    if (words == null || words.length == 0) return "";

    Map<Character, List<Character>> adjacencyList = new HashMap<>();
    for (String word : words) {
      for (char c : word.toCharArray()) {
        adjacencyList.putIfAbsent(c, new ArrayList<>());
      }
    }

    for (int i = 0; i < words.length - 1; i++) {
      String word1 = words[i];
      String word2 = words[i + 1];
      if (word1.length() > word2.length() && word1.startsWith(word2)) return "";
      for (int j = 0; j < Math.min(word1.length(), word2.length()); j++) {
        char c1 = word1.charAt(j), c2 = word2.charAt(j);
        if (c1 != c2) {
          adjacencyList.get(c1).add(c2);
          break;
        }
      }
    }

    Map<Character, Integer> state = new HashMap<>(); // 0=unvisited,1=visiting,2=visited
    StringBuilder order = new StringBuilder();
    for (char c : adjacencyList.keySet()) {
      if (dfs(c, adjacencyList, state, order)) {
        return ""; // cycle detected
      }
    }
    return order.reverse().toString();
  }

  private boolean dfs(char current, Map<Character, List<Character>> graph,
      Map<Character, Integer> state, StringBuilder order) {
    if (state.getOrDefault(current, 0) == 1) return true;  // cycle
    if (state.getOrDefault(current, 0) == 2) return false; // already processed

    state.put(current, 1);
    for (char neighbor : graph.get(current)) {
      if (dfs(neighbor, graph, state, order)) return true;
    }
    state.put(current, 2);
    order.append(current);
    return false;
  }
}
