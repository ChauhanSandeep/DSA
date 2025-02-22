package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;


/**
 * This class determines the lexicographical order of characters in an alien language.
 * The input is a list of words sorted lexicographically according to the unknown language.
 * The goal is to derive the correct character precedence rules and return a valid order.
 *
 * Algorithm:
 * - Construct a directed graph where an edge A → B means 'A' appears before 'B' in the language.
 * - Use Kahn's Algorithm (Topological Sorting) via BFS to derive a valid order.
 *
 * Time Complexity: O(C) where C is the total number of characters across all words.
 * Space Complexity: O(1) since the maximum number of unique characters is 26 (A-Z).
 */
public class AlienDictionary {

  public static void main(String[] args) {
    String[] words = {"wrt", "wrf", "er", "ett", "rftt"};
    AlienDictionary solver = new AlienDictionary();
    String order = solver.findAlienLanguageOrder(words);
    System.out.println(order);
  }

  /**
   * Determines the character order in an alien language.
   *
   * @param words Array of words sorted lexicographically in the alien language.
   * @return A string representing the character order, or an empty string if no valid order exists.
   */
  public String findAlienLanguageOrder(String[] words) {
    // Step 1: Initialize Graph Data Structures
    Map<Character, List<Character>> adjacencyList = new HashMap<>();
    Map<Character, Integer> inDegree = new HashMap<>();

    // Populate the graph with unique characters
    for (String word : words) {
      for (char character : word.toCharArray()) {
        inDegree.putIfAbsent(character, 0);
        adjacencyList.putIfAbsent(character, new ArrayList<>());
      }
    }

    // Step 2: Build Graph Relationships Based on Word Order
    for (int i = 0; i < words.length - 1; i++) {
      String firstWord = words[i];
      String secondWord = words[i + 1];

      // If secondWord is a prefix of firstWord but shorter, the order is invalid
      if (firstWord.length() > secondWord.length() && firstWord.startsWith(secondWord)) {
        return "";
      }

      // Find the first differing character and establish precedence
      for (int j = 0; j < Math.min(firstWord.length(), secondWord.length()); j++) {
        char from = firstWord.charAt(j);
        char to = secondWord.charAt(j);
        if (from != to) {
          adjacencyList.get(from).add(to);
          inDegree.put(to, inDegree.get(to) + 1);
          break; // Stop at the first differing character
        }
      }
    }

    // Step 3: Perform Topological Sort (Kahn’s Algorithm - BFS)
    StringBuilder characterOrder = new StringBuilder();
    Deque<Character> queue = new ArrayDeque<>();

    // Start with characters that have no dependencies (in-degree = 0)
    for (char character : inDegree.keySet()) {
      if (inDegree.get(character) == 0) {
        queue.offer(character);
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

    // If we couldn’t process all characters, it means there was a cycle (invalid order)
    return characterOrder.length() == inDegree.size() ? characterOrder.toString() : "";
  }
}
