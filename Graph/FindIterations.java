package Graph;

import java.util.*;

/**
 * Problem: Word Ladder (Shortest transformation sequence)
 * LeetCode Link: https://leetcode.com/problems/word-ladder/
 *
 * Given a start word, an end word, and a dictionary of words, find the shortest 
 * transformation sequence from the start word to the end word, where:
 *  - Only one letter can be changed at a time.
 *  - Each transformed word must exist in the given dictionary.
 * 
 * Approach:
 * - **Graph Representation:** Treat words as graph nodes, connecting words that differ by one letter.
 * - **BFS Traversal:** Use Breadth-First Search to find the shortest path from `beginWord` to `endWord`.
 * 
 * Time Complexity: **O(N^2 * M)** (where N = number of words, M = word length)
 * Space Complexity: **O(N^2)**
 */
public class FindIterations {
    public static void main(String[] args) {
        String[] wordList = {"but", "put", "big", "pot", "pog", "pig", "dog", "lot"};
        String beginWord = "bit", endWord = "pog";

        int result = findShortestTransformation(beginWord, endWord, wordList);
        System.out.println("Shortest transformation length: " + result);
    }

    /**
     * Finds the shortest transformation sequence length from `beginWord` to `endWord` using BFS.
     *
     * @param beginWord Start word of the transformation
     * @param endWord   Target word to reach
     * @param wordList  Dictionary of valid words
     * @return Length of the shortest transformation sequence, or -1 if not possible.
     */
    public static int findShortestTransformation(String beginWord, String endWord, String[] wordList) {
        Set<String> wordSet = new HashSet<>(Arrays.asList(wordList));
        if (!wordSet.contains(endWord)) return -1; // If target word isn't in the dictionary, no transformation is possible.

        // Build adjacency list graph representation
        Map<String, List<String>> graph = buildGraph(wordSet, beginWord);

        // Perform BFS to find the shortest transformation sequence
        return bfsShortestPath(graph, beginWord, endWord);
    }

    /**
     * Builds an adjacency list graph where words differing by one letter are connected.
     *
     * @param wordSet   Set of dictionary words
     * @param beginWord Start word, which may not be in the dictionary
     * @return Graph represented as an adjacency list
     */
    private static Map<String, List<String>> buildGraph(Set<String> wordSet, String beginWord) {
        Map<String, List<String>> graph = new HashMap<>();

        // Ensure beginWord is part of the graph
        wordSet.add(beginWord);

        for (String word1 : wordSet) {
            for (String word2 : wordSet) {
                if (isOneLetterDifferent(word1, word2)) {
                    graph.computeIfAbsent(word1, k -> new ArrayList<>()).add(word2);
                    graph.computeIfAbsent(word2, k -> new ArrayList<>()).add(word1);
                }
            }
        }
        return graph;
    }

    /**
     * Performs BFS to find the shortest path in the transformation sequence.
     *
     * @param graph     Adjacency list representation of the word graph
     * @param beginWord Start word of the sequence
     * @param endWord   Target word
     * @return Length of the shortest transformation sequence, or -1 if not found
     */
    private static int bfsShortestPath(Map<String, List<String>> graph, String beginWord, String endWord) {
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);

        Map<String, Integer> distance = new HashMap<>();
        distance.put(beginWord, 1);

        while (!queue.isEmpty()) {
            String currentWord = queue.poll();
            int currentDistance = distance.get(currentWord);

            for (String neighbor : graph.getOrDefault(currentWord, new ArrayList<>())) {
                if (!distance.containsKey(neighbor)) { // If not visited
                    distance.put(neighbor, currentDistance + 1);
                    queue.add(neighbor);
                    if (neighbor.equals(endWord)) {
                        return currentDistance + 1; // Early exit when endWord is reached
                    }
                }
            }
        }
        return -1; // No transformation sequence found
    }

    /**
     * Checks if two words differ by exactly one letter.
     *
     * @param word1 First word
     * @param word2 Second word
     * @return True if they differ by one letter, else false
     */
    private static boolean isOneLetterDifferent(String word1, String word2) {
        if (word1.length() != word2.length()) return false;

        int differenceCount = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                differenceCount++;
                if (differenceCount > 1) return false; // Stop early if more than one difference
            }
        }
        return differenceCount == 1;
    }
}
