package Graph;

import java.util.*;

public class AlienDictionary {

  public static void main(String[] args) {
    String[] words = {"wrt", "wrf", "er", "ett", "rftt"};
    AlienDictionary solver = new AlienDictionary();
    String order = solver.findAlienLanguageOrder(words);
    System.out.println(order); // Expected Output: "wertf"
  }

  /**
   * Determines the character order in an alien language.
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
        char parent = firstWord.charAt(j);
        char child = secondWord.charAt(j);
        
        if (parent != child) {
          // Only add edge if it doesn't already exist
          if (!adjacencyList.get(parent).contains(child)) {
            adjacencyList.get(parent).add(child);
            inDegree.put(child, inDegree.get(child) + 1);
          }
          break; // Stop at the first differing character
        }
      }
    }

    // Step 3: Perform Topological Sort (Kahn’s Algorithm - BFS)
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

    // If we couldn’t process all characters, it means there was a cycle (invalid order)
    return characterOrder.length() == inDegree.size() ? characterOrder.toString() : "";
  }
}
