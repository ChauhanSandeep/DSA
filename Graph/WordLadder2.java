package Graph;

import java.util.*;

/**
 * LeetCode Problem: Word Ladder II (https://leetcode.com/problems/word-ladder-ii/)
 *
 * Problem Summary:
 * - Given `beginWord`, `endWord`, and a `wordList`, find all shortest transformation sequences
 *   from `beginWord` to `endWord`, where:
 *     1. Only one letter can be changed at a time.
 *     2. Each transformed word must exist in `wordList`.
 * - Return all such shortest transformation sequences.
 *
 * Intuition & Approach:
 * - **Step 1 (BFS)**: Construct a shortest-path graph using a level-based BFS.
 * - **Step 2 (Backtracking)**: Use DFS to explore all valid paths from `beginWord` to `endWord`.
 *
 * Time Complexity:
 * - BFS Graph Construction: O(N * M * 26) ~ O(NM), where N = word count, M = word length.
 * - DFS Backtracking: O(NM), worst case.
 * - Overall: O(NM) (since each word has at most M * 26 neighbors)
 *
 * Space Complexity: O(NM) (for graph storage, recursion stack, and auxiliary sets).
 */

public class WordLadder2 {

    private final Map<String, List<String>> adjacencyList = new HashMap<>(); // Graph of word transitions
    private final List<List<String>> resultPaths = new ArrayList<>();
    private final List<String> currentPath = new ArrayList<>();

    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList = List.of("hot", "dot", "dog", "lot", "log", "cog");

        WordLadder2 solver = new WordLadder2();
        List<List<String>> shortestLadders = solver.findLadders(beginWord, endWord, wordList);
        System.out.println(shortestLadders);
    }

    /**
     * Finds all shortest transformation sequences from beginWord to endWord.
     */
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return resultPaths; // Early exit if no possible transformation

        // Step 1: Build the graph using BFS
        if (!constructGraphBFS(beginWord, endWord, wordSet)) return resultPaths;

        // Step 2: Find all paths using DFS backtracking
        currentPath.add(beginWord);
        explorePathsDFS(beginWord, endWord);
        return resultPaths;
    }

    /**
     * Uses BFS to build the shortest-path adjacency list for word transformations.
     */
    private boolean constructGraphBFS(String beginWord, String endWord, Set<String> wordSet) {
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);

        Map<String, Integer> wordLevel = new HashMap<>(); // Stores shortest distance from beginWord
        wordLevel.put(beginWord, 0);

        boolean reachedEndWord = false;
        int currentDepth = 0;

        while (!queue.isEmpty() && !reachedEndWord) {
            currentDepth++;
            int levelSize = queue.size();
            Set<String> wordsToRemove = new HashSet<>();

            for (int i = 0; i < levelSize; i++) {
                String word = queue.poll();
                List<String> neighbors = generateNeighbors(word, wordSet);

                for (String neighbor : neighbors) {
                    if (!wordLevel.containsKey(neighbor)) {
                        wordLevel.put(neighbor, currentDepth);
                        queue.add(neighbor);
                    }
                    if (wordLevel.get(neighbor) == currentDepth) { // Ensure shortest paths only
                        adjacencyList.computeIfAbsent(word, k -> new ArrayList<>()).add(neighbor);
                    }
                    if (neighbor.equals(endWord)) reachedEndWord = true;
                }
                wordsToRemove.addAll(neighbors);
            }

            wordSet.removeAll(wordsToRemove); // Remove words that were visited in this level
        }
        return reachedEndWord;
    }

    /**
     * Generates all valid one-letter transformations of a word that exist in the word list.
     */
    private List<String> generateNeighbors(String word, Set<String> wordSet) {
        List<String> neighbors = new ArrayList<>();
        char[] charArray = word.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            char originalChar = charArray[i];

            for (char c = 'a'; c <= 'z'; c++) {
                if (c == originalChar) continue; // Skip replacing with the same character
                charArray[i] = c;
                String newWord = new String(charArray);
                if (wordSet.contains(newWord)) {
                    neighbors.add(newWord);
                }
            }
            charArray[i] = originalChar; // Restore original character
        }
        return neighbors;
    }

    /**
     * Uses DFS to find all shortest paths from beginWord to endWord.
     */
    private void explorePathsDFS(String source, String destination) {
        if (source.equals(destination)) {
            resultPaths.add(new ArrayList<>(currentPath));
            return;
        }

        if (!adjacencyList.containsKey(source)) return;

        for (String neighbor : adjacencyList.get(source)) {
            currentPath.add(neighbor);
            explorePathsDFS(neighbor, destination);
            currentPath.remove(currentPath.size() - 1); // Backtrack
        }
    }
}