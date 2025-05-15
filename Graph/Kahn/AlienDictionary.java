package Graph.Kahn;

import java.util.*;

public class AlienDictionary {

  public static void main(String[] args) {
    String[] words = {"wrt", "wrf", "er", "ett", "rftt"};
    AlienDictionary solver = new AlienDictionary();
    String order = solver.findAlienLanguageOrder(words);
    System.out.println(order); // Expected Output: "wertf"
  }

  /**
   * LeetCode Problem: Alien Dictionary (Hard)
   * https://leetcode.com/problems/alien-dictionary/
   *
   * Determines the character order in an alien language based on a sorted dictionary.
   *
   * **Approach:**
   * - Construct a directed graph where an edge `u -> v` means `u` comes before `v`.
   * - Use Kahn’s Algorithm (BFS Topological Sort) to determine the valid order.
   *
   * **Time Complexity:** O(C), where C is the total number of characters across all words.
   * **Space Complexity:** O(1) (since only 26 lowercase English letters exist).
   *
   * @param words Array of words sorted lexicographically in the alien language.
   * @return A string representing the character order, or an empty string if no valid order exists.
   */
  public String findAlienLanguageOrder(String[] words) {
    if (words == null || words.length == 0) return "";

    // Step 1: Initialize Graph Data Structures
    Map<Character, List<Character>> adjacencyList = new HashMap<>();
    Map<Character, Integer> inDegree = new HashMap<>();

    // Populate the graph with unique characters
    for (String word : words) {
      for (char character : word.toCharArray()) {
        adjacencyList.putIfAbsent(character, new ArrayList<>());
        inDegree.putIfAbsent(character, 0);
      }
    }

    // Step 2: Build Graph Relationships Based on Word Order
    for (int i = 0; i < words.length - 1; i++) {
      String firstWord = words[i];
      String secondWord = words[i + 1];

      // Edge case: If secondWord is a prefix of firstWord but shorter, the order is invalid
      if (firstWord.length() > secondWord.length() && firstWord.startsWith(secondWord)) {
        return "";
      }

      // Find the first differing character to establish precedence
      for (int j = 0; j < Math.min(firstWord.length(), secondWord.length()); j++) {
        char precedingChar = firstWord.charAt(j);
        char followingChar = secondWord.charAt(j);

        if (precedingChar != followingChar) {
          // Add edge if not already present
          if (!adjacencyList.get(precedingChar).contains(followingChar)) {
            adjacencyList.get(precedingChar).add(followingChar);
            inDegree.put(followingChar, inDegree.get(followingChar) + 1);
          }
          break; // Stop at the first differing character
        }
      }
    }

    // Step 3: Perform Topological Sort using Kahn’s Algorithm (BFS)
    StringBuilder characterOrder = new StringBuilder();
    Deque<Character> queue = new ArrayDeque<>();

    // Start with characters that have no dependencies (in-degree = 0)
    for (Map.Entry<Character, Integer> entry : inDegree.entrySet()) {
      if (entry.getValue() == 0) {
        queue.offer(entry.getKey());
      }
    }

    while (!queue.isEmpty()) {
      char currentCharacter = queue.poll();
      characterOrder.append(currentCharacter);

      // Reduce in-degree for all dependent characters
      for (char neighbor : adjacencyList.get(currentCharacter)) {
        inDegree.put(neighbor, inDegree.get(neighbor) - 1);
        if (inDegree.get(neighbor) == 0) {
          queue.offer(neighbor);
        }
      }
    }

    // If the order does not include all characters, a cycle exists (invalid order)
    return characterOrder.length() == inDegree.size() ? characterOrder.toString() : "";
  }
}
