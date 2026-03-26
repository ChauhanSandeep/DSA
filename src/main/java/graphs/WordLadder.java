package graphs;

import java.util.*;

/**
 * 127. Word Ladder
 *
 * Problem: Given two words (beginWord and endWord), and a dictionary's word
 * list,
 * find the length of shortest transformation sequence from beginWord to
 * endWord,
 * where only one letter can be changed at a time and each transformed word must
 * exist in the word list.
 *
 * Example:
 * Input: beginWord = "hit", endWord = "cog", wordList =
 * ["hot","dot","dog","lot","log","cog"]
 * Output: 5
 * Explanation: "hit" -> "hot" -> "dot" -> "dog" -> "cog"
 *
 * LeetCode: https://leetcode.com/problems/word-ladder
 *
 * Approaches:
 * 1. Simple BFS - Standard breadth-first search approach
 * 2. Bidirectional BFS - Optimized search from both ends for better performance
 */
public class WordLadder {

    /**
     * BFS approach - optimal for shortest path.
     *
     * Algorithm: Breadth-First Search
     * - Treat each word as a graph node
     * - Two words are connected if they differ by exactly one character
     * - Use BFS to find shortest path from beginWord to endWord
     * - Use visited set to avoid revisiting words
     *
     * Time Complexity: O(M²×N) where M is word length, N is word count
     * Space Complexity: O(M×N) for queue and visited set
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord))
            return 0;

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(beginWord);
        visited.add(beginWord);
        int level = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                String currentWord = queue.poll();

                if (currentWord.equals(endWord)) {
                    return level;
                }

                // Try all possible one-character changes
                for (String neighbor : getNeighbors(currentWord, wordSet)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }

            level++;
        }

        return 0; // No transformation possible
    }

    // Get all valid neighbors (words differing by one character)
    private List<String> getNeighbors(String word, Set<String> wordSet) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];

            // Try all 26 possible characters
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != originalChar) {
                    chars[i] = c;
                    String newWord = new String(chars);

                    if (wordSet.contains(newWord)) {
                        neighbors.add(newWord);
                    }
                }
            }

            chars[i] = originalChar; // Restore original character
        }

        return neighbors;
    }

    /**
     * Bidirectional BFS for better performance.
     * 
     * Algorithm: Bidirectional Breadth-First Search
     * - Start BFS from both beginWord and endWord simultaneously
     * - Maintain two frontiers expanding towards each other
     * - Optimization: Always expand the smaller frontier to minimize work
     * - Stop when the two frontiers meet (intersection found)
     * - This reduces search space from O(b^d) to O(b^(d/2)) where b is branching
     * factor, d is depth
     * 
     * Key advantages over single-directional BFS:
     * 1. Reduces nodes explored (exponential improvement)
     * 2. More efficient for large word lists
     * 3. Better cache locality when alternating small frontiers
     * 
     * Time Complexity: O(M²×N) where M is word length, N is word count (same worst
     * case, better average)
     * Space Complexity: O(M×N) for frontiers and visited set
     */
    public int ladderLengthBidirectional(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord))
            return 0;

        // Two frontiers: one expanding from start, one from end
        Set<String> forwardFrontier = new HashSet<>();
        Set<String> backwardFrontier = new HashSet<>();
        Set<String> visited = new HashSet<>();

        forwardFrontier.add(beginWord);
        backwardFrontier.add(endWord);
        int level = 1;

        while (!forwardFrontier.isEmpty() && !backwardFrontier.isEmpty()) {
            // Optimization: Always expand the smaller frontier to minimize work
            // This keeps the search balanced and reduces total nodes explored
            if (forwardFrontier.size() > backwardFrontier.size()) {
                Set<String> temp = forwardFrontier;
                forwardFrontier = backwardFrontier;
                backwardFrontier = temp;
            }

            // Build next level of the forward frontier
            Set<String> nextForwardFrontier = new HashSet<>();

            for (String word : forwardFrontier) {
                for (String newWord : getNeighbors(word, wordSet)) {
                    // Check if frontiers meet - transformation complete!
                    if (backwardFrontier.contains(newWord)) {
                        return level + 1;
                    }

                    // Add valid unvisited words to next frontier
                    if (!visited.contains(newWord)) {
                        visited.add(newWord);
                        nextForwardFrontier.add(newWord);
                    }
                }
            }

            // Move to next level
            forwardFrontier = nextForwardFrontier;
            level++;
        }

        return 0; // No path found
    }
}