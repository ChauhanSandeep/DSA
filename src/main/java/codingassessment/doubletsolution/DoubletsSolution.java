package codingassessment.doubletsolution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Problem: Word Ladder (Doublets)
 *
 * Given a begin word, an end word, and a dictionary, find the shortest chain
 * that changes one letter at a time until it reaches the end word. Every
 * intermediate word must appear in the dictionary. This assessment variant
 * returns -1 when the end word cannot be reached.
 *
 * Leetcode: https://leetcode.com/problems/word-ladder/ (Hard)
 * Rating:   acceptance 46.1% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Breadth-first search | Implicit word graph
 *
 * Example:
 *   Input:  beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
 *   Output: 5
 *   Why:    hit, hot, dot, dog, cog is the shortest chain; BFS counts words in
 *           the chain, not just the four letter changes.
 *
 * Follow-ups:
 *   1. Speed up lookups for a very large dictionary?
 *      Precompute wildcard buckets such as h*t and look up neighbors by pattern.
 *   2. Return one actual shortest sequence?
 *      Keep a parent pointer for each newly visited word and rebuild the path at the end.
 *   3. Return all shortest sequences?
 *      Run level-order BFS with parent lists, then backtrack only along shortest-level edges.
 *   4. Reduce the search radius on hard cases?
 *      Use bidirectional BFS from beginWord and endWord and stop when the frontiers meet.
 *
 * Related: Word Ladder II (126), Minimum Genetic Mutation (433), Open the Lock (752).
 */
class DoubletsSolution {
    
    /**
     * Intuition: the naive way compares every word with every other word to build
     * the graph first, but that spends most of its time proving non-neighbors.
     * Instead, treat the graph as implicit: a word's neighbors are exactly the
     * dictionary words reachable by changing one position to one lowercase letter.
     * This is then plain BFS on an unweighted graph, the same building block used
     * for shortest paths by number of edges. Processing the queue level by level
     * means all chains of length k are exhausted before any chain of length k + 1.
     *
     * Algorithm:
     *   1. Put the wordList into a HashSet for O(1) dictionary checks.
     *   2. BFS from beginWord, keeping visitedWords so each word is queued once.
     *   3. For every queued word, try all 26 replacements at every position.
     *   4. Return transformationCount when endWord is dequeued, or -1 if BFS ends.
     *
     * Time:  O(n*m^2) - up to n words are visited; each tries 26*m mutations and builds m-char strings.
     * Space: O(n*m) - the dictionary, visited set, and queue can store n words of length m.
     *
     * @param beginWord word where the chain starts
     * @param endWord word the chain must reach
     * @param wordList dictionary of allowed intermediate and final words
     * @return shortest chain length including both endpoints, or -1 if unreachable
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        // Build dictionary set for O(1) lookup
        Set<String> dictionarySet = new HashSet<>(wordList);
        
        // Early exit if endWord not in dictionary - impossible to reach
        if (!dictionarySet.contains(endWord)) {
            return -1;
        }

        // BFS initialization
        Queue<String> queue = new LinkedList<>();
        Set<String> visitedWords = new HashSet<>();
        queue.add(beginWord);
        visitedWords.add(beginWord);

        int transformationCount = 1;

        // BFS: explore all words reachable at current distance
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            
            // Process all words at current level
            for (int i = 0; i < levelSize; i++) {
                String currentWord = queue.poll();
                
                // Check if we reached target
                if (currentWord.equals(endWord)) {
                    return transformationCount;
                }

                // Try all possible single-letter transformations
                for (int position = 0; position < currentWord.length(); position++) {
                    for (char newLetter = 'a'; newLetter <= 'z'; newLetter++) {
                        char[] wordChars = currentWord.toCharArray();
                        wordChars[position] = newLetter;
                        String transformedWord = new String(wordChars);

                        // Add to queue if valid and unvisited
                        if (dictionarySet.contains(transformedWord) && !visitedWords.contains(transformedWord)) {
                            queue.add(transformedWord);
                            visitedWords.add(transformedWord);
                        }
                    }
                }
            }
            
            // Increment transformation count after processing entire level
            transformationCount++;
        }
        
        // No valid transformation sequence found
        return -1;
    }

    public static void main(String[] args) {
        DoubletsSolution solver = new DoubletsSolution();

        List<List<String>> dictionaries = Arrays.asList(
            Arrays.asList("hot", "dot", "dog", "lot", "log", "cog"),
            Arrays.asList("hot", "dot", "dog", "lot", "log"),
            Arrays.asList("apt", "opt", "oat", "mat", "man")
        );
        String[][] pairs = {
            {"hit", "cog"},
            {"hit", "cog"},
            {"ape", "man"}
        };
        int[] expected = {5, -1, 6};

        for (int i = 0; i < pairs.length; i++) {
            int output = solver.ladderLength(pairs[i][0], pairs[i][1], dictionaries.get(i));
            System.out.printf("begin=%s end=%s words=%s -> %d  expected=%d%n",
                pairs[i][0], pairs[i][1], dictionaries.get(i), output, expected[i]);
        }
    }
}