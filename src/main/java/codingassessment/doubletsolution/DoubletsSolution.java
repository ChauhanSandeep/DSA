package codingassessment.doubletsolution;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Problem: Word Ladder (Doublets)
 *
 * Given a start word, an end word, and a dictionary of valid words, find the minimum number
 * of transformations needed to convert the start word to the end word. Each transformation
 * changes exactly one letter, and intermediate words must be in the dictionary.
 *
 * 🔗 LeetCode: https://leetcode.com/problems/word-ladder/
 *
 * 📝 Example:
 * Input: beginWord = "APE", endWord = "MAN", 
 *        wordList = ["APE", "APT", "OPT", "OAT", "MAT", "MAN"]
 * Output: 6
 * Explanation: APE → APT → OPT → OAT → MAT → MAN (5 transformations)
 *
 * Input: beginWord = "hit", endWord = "cog",
 *        wordList = ["hot","dot","dog","lot","log","cog"]
 * Output: 5
 * Explanation: hit → hot → dot → dog → cog
 *
 * 🎯 Constraints:
 * - 1 <= beginWord.length <= 10
 * - endWord.length == beginWord.length
 * - 1 <= wordList.length <= 5000
 * - All words consist of lowercase English letters
 * - beginWord != endWord
 * - All words in wordList are unique
 *
 * 💡 Follow-up Questions with Answers:
 * 1. Q: How would you optimize for very long word lists?
 *    A: Use bidirectional BFS (search from both ends). Build adjacency list once by grouping
 *       words by pattern (e.g., h*t, *ot). Use trie for efficient pattern matching.
 *
 * 2. Q: What if you need to return the actual transformation sequence, not just length?
 *    A: Maintain parent map during BFS to track path. Backtrack from endWord to beginWord
 *       using parent pointers to reconstruct sequence.
 *
 * 3. Q: How would you handle multiple shortest paths?
 *    A: Use BFS to find distance, then use DFS/backtracking from endWord maintaining same
 *       distance constraint to find all paths. Can also use BFS with parent list per node.
 *
 * 4. Q: What if transformations have different costs (not all cost 1)?
 *    A: Use Dijkstra's algorithm with priority queue instead of BFS. Maintain distance map
 *       with minimum cost to reach each word.
 *
 * 5. Q: How would you handle case-insensitive or words of different lengths?
 *    A: Normalize to lowercase. For different lengths, modify transformation rules to allow
 *       insertion/deletion (edit distance problem). Use dynamic programming or Dijkstra's.
 *
 * Related Problems:
 * - Word Ladder II (LeetCode #126) - Return all shortest transformation sequences
 * - Minimum Genetic Mutation (LeetCode #433)
 */
class DoubletsSolution {
    
    /**
     * Finds minimum transformations using BFS to explore word transformation graph.
     *
     * Algorithm:
     * 1. Build word set from dictionary for O(1) lookup
     * 2. Use BFS queue starting with beginWord
     * 3. For each word, try all 26 letter substitutions at each position
     * 4. If transformed word in dictionary and not visited, add to queue
     * 5. Process level by level, incrementing transformation count
     * 6. Return count when endWord reached, or -1 if unreachable
     *
     * Key insight: This is shortest path problem in unweighted graph where nodes are words
     * and edges exist between words differing by one letter. BFS guarantees shortest path.
     *
     * Time Complexity: O(M² × N) where M is word length, N is dictionary size
     * - For each word, try M positions × 26 letters = O(26M) transformations
     * - Each transformation creates new M-length string: O(M)
     * - Visit at most N words: O(M² × N) total
     *
     * Space Complexity: O(M × N) for queue and visited set storing N words of length M
     *
     * @param beginWord Starting word
     * @param endWord Target word
     * @param wordList Dictionary of valid words
     * @return Minimum number of transformations, or -1 if impossible
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        // Build dictionary set for O(1) lookup
        Set<String> dictionarySet = new HashSet<>(wordList);
        
        // Early exit if endWord not in dictionary
        if (!dictionarySet.contains(endWord)) {
            return 0;
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
}