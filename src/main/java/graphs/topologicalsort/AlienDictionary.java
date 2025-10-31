package graphs.topologicalsort;

import java.util.*;

/**
 * Problem: Alien Dictionary
 * LeetCode: https://leetcode.com/problems/alien-dictionary/
 *
 * Statement:
 * You are given a list of words sorted lexicographically in an alien language.
 * Derive the order of characters in this language.
 * If the dictionary order is invalid, return an empty string.
 * If multiple valid orders exist, return any.
 *
 * Example:
 * Input: ["wrt","wrf","er","ett","rftt"]
 * Output: "wertf"
 *
 * Approach:
 * - Build a directed graph where an edge u -> v indicates u must appear before v.
 * - Use Kahn’s Algorithm (BFS Topological Sort) to generate a valid character order.
 *
 * Time Complexity: O(C) where C = total number of characters across all words.
 * Space Complexity: O(1), since max distinct characters = 26 (lowercase English letters).
 *
 * Follow-up Questions (FAANG-style):
 * 1. What if multiple valid orders exist?
 *    - Any valid topological order is acceptable; but to return the lexicographically smallest, use a PriorityQueue instead of Queue.
 * 2. How to detect cycles in this problem?
 *    - If after topological sort the output length < total unique characters, a cycle exists.
 * 3. Can this be solved using DFS instead of BFS?
 *    - Yes, perform a DFS-based topological sort with cycle detection.
 */
public class AlienDictionary {

  public static void main(String[] args) {
    String[] words = {"wrt", "wrf", "er", "ett", "rftt"};
    AlienDictionary solver = new AlienDictionary();
    String order = solver.findAlienLanguageOrder(words);
    System.out.println(order); // Expected Output: "wertf"
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
