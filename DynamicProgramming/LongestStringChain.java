package DynamicProgramming;

import java.util.*;

/**
 * Problem: Longest String Chain (LeetCode #1048)
 * 
 * Problem Statement:
 * Given a list of words, each word consists of English lowercase letters.
 * Let's say word1 is a predecessor of word2 if and only if we can add exactly one letter anywhere in word1 to make it equal to word2.
 * A word chain is a sequence of words [word_1, word_2, ..., word_k] with k >= 1, where word_1 is a predecessor of word_2, word_2 is a predecessor of word_3, and so on.
 * Return the longest possible length of a word chain with words chosen from the given list of words.
 * 
 * Example 1:
 * Input: words = ["a","b","ba","bca","bda","bdca"]
 * Output: 4
 * Explanation: One of the longest word chains is ["a","ba","bda","bdca"].
 * 
 * Example 2:
 * Input: words = ["xbc","pcxbcf","xb","cxbc","pcxbc"]
 * Output: 5
 * Explanation: One of the longest word chains is ["xb", "xbc", "cxbc", "pcxbc", "pcxbcf"].
 * 
 * Approach:
 * We'll use dynamic programming to solve this problem. The key insight is that for each word, we can remove one character at a time and check if the resulting word exists in our set of words.
 * 
 * Steps to solve:
 * 1. Sort the words by their lengths to ensure we process shorter words before longer ones.
 * 2. Use a hash map to store the longest chain length ending with each word.
 * 3. For each word, try removing each character and check if the resulting word exists in our map.
 * 4. Update the maximum chain length for the current word based on its predecessors.
 * 5. Keep track of the overall maximum chain length.
 * 
 * Time Complexity: O(N * L^2) where N is the number of words and L is the maximum length of a word
 * Space Complexity: O(N) for the hash map
 * 
 * Follow-up Questions:
 * 1. What if we need to return the actual longest word chain instead of just its length?
 *    Answer: We can modify the solution to store the actual chain for each word in the map instead of just the length.
 * 
 * 2. What if the words can contain uppercase letters and special characters?
 *    Answer: We would need to handle case sensitivity and potentially filter out invalid words based on the problem constraints.
 * 
 * 3. Can we solve this using a graph-based approach?
 *    Answer: Yes, we can model this as a graph where words are nodes and edges represent the predecessor relationship.
 *    The longest chain would then be the longest path in this DAG, which can be found using topological sorting.
 * 
 * LeetCode: https://leetcode.com/problems/longest-string-chain/
 */
public class LongestStringChain {
    
    /**
     * Calculates the length of the longest possible word chain.
     * 
     * Steps to solve:
     * 1. Sort the words by their lengths to process shorter words first.
     * 2. Use a hash map to store the longest chain length for each word.
     * 3. For each word, generate all possible predecessors by removing one character.
     * 4. For each predecessor, if it exists in the map, update the current word's chain length.
     * 5. Keep track of the maximum chain length found.
     * 
     * @param words The array of words
     * @return The length of the longest word chain
     */
    public int longestStrChain(String[] words) {
        // Sort the words by their lengths
        Arrays.sort(words, (a, b) -> a.length() - b.length());
        
        // Map to store the longest chain length for each word
        Map<String, Integer> dp = new HashMap<>();
        int maxChainLength = 0;
        
        for (String word : words) {
            int currentMax = 1;  // Minimum chain length is 1 (the word itself)
            
            // Generate all possible predecessors by removing one character
            for (int i = 0; i < word.length(); i++) {
                StringBuilder sb = new StringBuilder(word);
                String predecessor = sb.deleteCharAt(i).toString();
                
                // If the predecessor exists in our map, update the current max chain length
                if (dp.containsKey(predecessor)) {
                    currentMax = Math.max(currentMax, dp.get(predecessor) + 1);
                }
            }
            
            // Update the map with the current word's chain length
            dp.put(word, currentMax);
            maxChainLength = Math.max(maxChainLength, currentMax);
        }
        
        return maxChainLength;
    }
    
    /**
     * Alternative solution using BFS (Breadth-First Search)
     * 
     * This approach builds a graph where an edge exists from word A to word B if A is a predecessor of B.
     * We then perform BFS from each word to find the longest chain.
     * 
     * Time Complexity: O(N^2 * L) where N is the number of words and L is the maximum length of a word
     * Space Complexity: O(N^2) for the graph
     */
    public int longestStrChainBFS(String[] words) {
        // Sort words by length to ensure we process shorter words first
        Arrays.sort(words, (a, b) -> a.length() - b.length());
        
        // Build a graph where an edge exists from word A to word B if A is a predecessor of B
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        
        // Initialize the graph and in-degree map
        for (String word : words) {
            graph.put(word, new ArrayList<>());
            inDegree.put(word, 0);
        }
        
        // Build the graph
        for (int i = 0; i < words.length; i++) {
            String word1 = words[i];
            for (int j = i + 1; j < words.length; j++) {
                String word2 = words[j];
                
                // If word2 is one character longer than word1 and word1 is a predecessor of word2
                if (word2.length() == word1.length() + 1 && isPredecessor(word1, word2)) {
                    graph.get(word1).add(word2);
                    inDegree.put(word2, inDegree.get(word2) + 1);
                }
            }
        }
        
        // Perform BFS to find the longest chain
        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> chainLength = new HashMap<>();
        
        // Add all words with in-degree 0 to the queue
        for (String word : words) {
            if (inDegree.get(word) == 0) {
                queue.offer(word);
                chainLength.put(word, 1);
            }
        }
        
        int maxChainLength = 1;
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            for (String neighbor : graph.get(current)) {
                // Update the chain length for the neighbor
                int newLength = chainLength.get(current) + 1;
                if (newLength > chainLength.getOrDefault(neighbor, 0)) {
                    chainLength.put(neighbor, newLength);
                    maxChainLength = Math.max(maxChainLength, newLength);
                }
                
                // Decrement the in-degree of the neighbor
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                
                // If in-degree becomes 0, add to the queue
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        return maxChainLength;
    }
    
    /**
     * Checks if word1 is a predecessor of word2
     * A word1 is a predecessor of word2 if we can add exactly one character to word1 to get word2
     * and the relative order of characters is preserved
     */
    private boolean isPredecessor(String word1, String word2) {
        if (word2.length() != word1.length() + 1) {
            return false;
        }
        
        int i = 0, j = 0;
        boolean foundDifference = false;
        
        while (i < word1.length() && j < word2.length()) {
            if (word1.charAt(i) == word2.charAt(j)) {
                i++;
                j++;
            } else {
                if (foundDifference) {
                    return false;
                }
                foundDifference = true;
                j++;
            }
        }
        
        return true;
    }
    
    /**
     * Space-optimized solution using 1D DP array
     * 
     * This approach sorts the words and uses a 1D array to store the longest chain length
     * for each word, updating it in place as we iterate through the sorted list.
     */
    public int longestStrChainOptimized(String[] words) {
        // Sort the words by their lengths
        Arrays.sort(words, (a, b) -> a.length() - b.length());
        
        // Map to store the index of each word for O(1) lookup
        Map<String, Integer> wordToIndex = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            wordToIndex.put(words[i], i);
        }
        
        // dp[i] represents the longest chain length ending at words[i]
        int[] dp = new int[words.length];
        Arrays.fill(dp, 1);  // Each word is a chain of length 1
        
        int maxChainLength = 1;
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            
            // Try removing each character and check if the resulting word exists
            for (int j = 0; j < word.length(); j++) {
                String predecessor = word.substring(0, j) + word.substring(j + 1);
                
                if (wordToIndex.containsKey(predecessor)) {
                    int predecessorIndex = wordToIndex.get(predecessor);
                    dp[i] = Math.max(dp[i], dp[predecessorIndex] + 1);
                }
            }
            
            maxChainLength = Math.max(maxChainLength, dp[i]);
        }
        
        return maxChainLength;
    }
}
