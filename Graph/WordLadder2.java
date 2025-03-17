package Graph;

import java.util.*;

/**
 * Word Ladder II - Find all shortest transformation sequences from beginWord to endWord.
 * 
 * Approach:
 * 1. **BFS (Breadth-First Search)** to construct a graph that ensures shortest paths only.
 * 2. **DFS (Backtracking)** to reconstruct all valid paths from beginWord to endWord.
 *
 * Time Complexity: **O(N * 26^L) ≈ O(N * L)** for BFS and **O(P) for backtracking**,  
 * where `N` is wordList size, `L` is word length, and `P` is total paths found.
 * 
 * Space Complexity: **O(N * L) for the graph** and **O(P) for storing paths**.
 */
public class WordLadder2 {

    private Map<String, List<String>> adjacencyList = new HashMap<>();
    private List<List<String>> shortestPaths = new ArrayList<>();

    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");

        List<List<String>> ladders = new WordLadder2().findLadders(beginWord, endWord, wordList);
        System.out.println(ladders);
    }

    /**
     * Finds all shortest transformation sequences from beginWord to endWord.
     */
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return shortestPaths; // No valid transformation exists.

        // Step 1: Build the adjacency list using BFS
        if (!buildGraphUsingBFS(beginWord, endWord, wordSet)) return shortestPaths;

        // Step 2: Find all paths using backtracking
        List<String> currPath = new ArrayList<>();
        currPath.add(beginWord);
        backtrack(beginWord, endWord, currPath);

        return shortestPaths;
    }

    /**
     * Builds the shortest path graph using BFS.
     */
    private boolean buildGraphUsingBFS(String beginWord, String endWord, Set<String> wordSet) {
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);

        Map<String, Integer> wordLevelMap = new HashMap<>();
        wordLevelMap.put(beginWord, 0);

        boolean foundEndWord = false;
        int depth = 0;

        while (!queue.isEmpty() && !foundEndWord) {
            depth++;
            int size = queue.size();
            Set<String> visitedThisLevel = new HashSet<>();

            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                List<String> neighbors = findNeighbors(word, wordSet);

                for (String neighbor : neighbors) {
                    if (!wordLevelMap.containsKey(neighbor)) {
                        wordLevelMap.put(neighbor, depth);
                        queue.add(neighbor);
                    }
                    if (wordLevelMap.get(neighbor) == depth) { // Ensures shortest paths only
                        adjacencyList.computeIfAbsent(word, k -> new ArrayList<>()).add(neighbor);
                    }
                    if (neighbor.equals(endWord)) foundEndWord = true;
                    visitedThisLevel.add(neighbor);
                }
            }
            wordSet.removeAll(visitedThisLevel); // Remove only after processing the entire level
        }
        return foundEndWord;
    }

    /**
     * Finds valid one-letter-different neighbors in the wordSet.
     */
    private List<String> findNeighbors(String word, Set<String> wordSet) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == originalChar) continue;
                chars[i] = c;
                String newWord = new String(chars);
                if (wordSet.contains(newWord)) {
                    neighbors.add(newWord);
                }
            }
            chars[i] = originalChar; // Restore original word
        }
        return neighbors;
    }

    /**
     * Uses DFS to find all valid transformation sequences.
     */
