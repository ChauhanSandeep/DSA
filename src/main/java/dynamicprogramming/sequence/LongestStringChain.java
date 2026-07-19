package dynamicprogramming.sequence;

import java.util.*;

/**
 * Problem: Longest String Chain
 *
 * A word can follow another when it is formed by inserting exactly one character while preserving order. Return the longest chain length.
 *
 * Leetcode: https://leetcode.com/problems/longest-string-chain/ (Medium)
 * Rating:   contest Elo 1599
 * Pattern:  Dynamic programming | Hash map | DAG by word length
 *
 * Example:
 *   Input:  words = ["a", "b", "ba", "bca", "bda", "bdca"]
 *   Output: 4
 *   Why:    a -> ba -> bda -> bdca is a valid chain.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Longest Increasing Subsequence (300), Word Ladder (127).
 */
public class LongestStringChain {

    public static void main(String[] args) {
        LongestStringChain solution = new LongestStringChain();
        String[][] inputs = { {"a", "b", "ba", "bca", "bda", "bdca"}, {"xbc", "pcxbcf", "xb", "cxbc", "pcxbc"}, {} };
        int[] expected = {4, 5, 0};
        for (int i = 0; i < inputs.length; i++) {
            String[] words = inputs[i].clone();
            int got = solution.longestStrChain(words);
            System.out.printf("words=%s -> %d  expected=%d%n", Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


        /**
     * Intuition: after sorting by length, every possible predecessor of a word has already been processed. Removing each character generates all predecessors, and the best predecessor chain plus one is the chain ending at the word.
     *
     * Algorithm:
     *   1. Sort words by length.
     *   2. Keep dp from word to best chain length.
     *   3. For each word, remove each character to form predecessors.
     *   4. Use any predecessor value plus one.
     *   5. Store the word value and track the maximum.
     *
     * Time:  O(n * L^2) - each deletion builds a length-L string.
     * Space: O(n) - one map entry per word.
     *
     * @param words input words
     * @return longest chain length
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

        /** Checks whether word1 is a predecessor of word2. */
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
