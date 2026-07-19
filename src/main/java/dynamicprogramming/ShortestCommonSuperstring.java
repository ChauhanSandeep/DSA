package dynamicprogramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Problem: Find the Shortest Superstring
 *
 * Given an array of strings, return a shortest string that contains every word
 * as a substring. The order is not fixed, so the problem is to choose an order
 * that maximizes saved overlap between consecutive words.
 *
 * Leetcode: https://leetcode.com/problems/find-the-shortest-superstring/ (Hard)
 * Rating:   zerotrac 2186 (Q4, weekly-contest-111)
 * Pattern:  Dynamic Programming | Bitmask DP | Traveling-salesman style overlap
 *
 * Example:
 *   Input:  words = ["abc", "bcd", "cde"]
 *   Output: "abcde"
 *   Why:    "abc" overlaps "bcd" by two characters and "bcd" overlaps "cde"
 *           by two characters, so only five total characters are needed.
 *
 * Follow-ups:
 *   1. How do you remove words contained inside other words first?
 *      Sort or compare all pairs and delete any word that is a substring of another.
 *   2. How would you count all optimal superstrings?
 *      Track all parents that tie for maximum overlap and backtrack every optimal path.
 *   3. Can this scale to hundreds of words exactly?
 *      Not with bitmask DP; use greedy overlap heuristics or approximation algorithms.
 *
 * Related: Longest String Chain (1048), Shortest Common Supersequence (1092).
 */
public class ShortestCommonSuperstring {

    public static void main(String[] args) {
        ShortestCommonSuperstring solver = new ShortestCommonSuperstring();
        String[][] cases = { {"abc", "bcd", "cde"}, {"alone"} };
        String[] expected = { "abcde", "alone" };

        for (int i = 0; i < cases.length; i++) {
            String got = solver.shortestSuperstring(cases[i]);
            System.out.printf("words=%s -> %s  expected=%s%n",
                Arrays.toString(cases[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: once the last word is known, the only history that matters is
     * which words have already been used and how much overlap has been saved.
     * maxOverlapDP[mask][lastWord] stores the best total saved characters for
     * exactly that state. To append lastWord, the previous state is mask without
     * lastWord, and every possible previousWord offers one candidate transition
     * by adding overlap[previousWord][lastWord].
     *
     * Algorithm:
     *   1. Precompute overlap[i][j], the characters saved by placing words[j] after words[i].
     *   2. Fill maxOverlapDP by mask and lastWord, recording parentWord for reconstruction.
     *   3. Choose the best final word, reconstruct the path, and merge words using the overlaps.
     *
     * Time:  O(n^2 * 2^n + n^2 * m) - DP checks word pairs for every mask after overlap preprocessing.
     * Space: O(n * 2^n + n^2) - parent and overlap tables dominate memory.
     *
     * @param words strings that must all appear in the result
     * @return a shortest superstring containing every word
     */
    public String shortestSuperstring(String[] words) {
        int numWords = words.length;
        
        // Step 1: Precompute overlap between every pair of words
        // overlap[i][j] = how many characters we save if word[i] is followed by word[j]
        int[][] overlap = computeOverlapMatrix(words);
        
        // Step 2: Initialize DP table and parent tracking
        // dp[mask][lastWord] = maximum overlap when using words in mask, ending with lastWord
        int[][] maxOverlapDP = new int[1 << numWords][numWords];
        
        // parent[mask][lastWord] = which word came before lastWord in the optimal path
        int[][] parentWord = new int[1 << numWords][numWords];
        
        // Initialize parent array with -1 (no parent)
        for (int mask = 0; mask < (1 << numWords); mask++) {
            Arrays.fill(parentWord[mask], -1);
        }
        
        // Step 3: Fill DP table by trying all possible masks and last words
        for (int currentMask = 0; currentMask < (1 << numWords); currentMask++) {
            for (int lastWord = 0; lastWord < numWords; lastWord++) {
                // Skip if lastWord is not in the current mask
                if ((currentMask & (1 << lastWord)) == 0) {
                    continue;
                }
                
                // Previous mask before adding lastWord
                int previousMask = currentMask ^ (1 << lastWord);
                
                // If previousMask is empty, this is the first word (base case)
                if (previousMask == 0) {
                    continue;
                }
                
                // Try all possible words that could have come before lastWord
                for (int previousWord = 0; previousWord < numWords; previousWord++) {
                    // Skip if previousWord is not in the previous mask
                    if ((previousMask & (1 << previousWord)) == 0) {
                        continue;
                    }
                    
                    // Calculate total overlap if we go from previousWord to lastWord
                    int totalOverlap = maxOverlapDP[previousMask][previousWord] + overlap[previousWord][lastWord];
                    
                    // Update DP table if this path gives better overlap
                    if (totalOverlap > maxOverlapDP[currentMask][lastWord]) {
                        maxOverlapDP[currentMask][lastWord] = totalOverlap;
                        parentWord[currentMask][lastWord] = previousWord;
                    }
                }
            }
        }
        
        // Step 4: Find the best ending word when all words are used
        int bestLastWord = 0;
        int fullMask = (1 << numWords) - 1; // All bits set (all words used)
        
        for (int word = 0; word < numWords; word++) {
            if (maxOverlapDP[fullMask][word] > maxOverlapDP[fullMask][bestLastWord]) {
                bestLastWord = word;
            }
        }
        
        // Step 5: Reconstruct the optimal path from parent pointers
        List<Integer> optimalPath = reconstructPath(parentWord, bestLastWord, numWords);
        
        // Step 6: Build the final superstring using the optimal path
        return buildSuperstring(words, overlap, optimalPath);
    }

    /**
     * Computes overlap matrix for all pairs of words.
     * overlap[i][j] represents how many characters from end of words[i] match beginning of words[j].
     */
    private int[][] computeOverlapMatrix(String[] words) {
        int numWords = words.length;
        int[][] overlap = new int[numWords][numWords];
        
        for (int i = 0; i < numWords; i++) {
            for (int j = 0; j < numWords; j++) {
                if (i != j) {
                    overlap[i][j] = calculateOverlap(words[i], words[j]);
                }
            }
        }
        
        return overlap;
    }

    /**
     * Calculates maximum overlap between end of first string and beginning of second string.
     * Example: calculateOverlap("abc", "bcd") returns 2 because "bc" overlaps.
     */
    private int calculateOverlap(String firstWord, String secondWord) {
        int maxPossibleOverlap = Math.min(firstWord.length(), secondWord.length());
        
        // Try all possible overlap lengths from largest to smallest
        for (int overlapLen = maxPossibleOverlap; overlapLen > 0; overlapLen--) {
            // Check if last overlapLen characters of firstWord match first overlapLen of secondWord
            if (firstWord.substring(firstWord.length() - overlapLen).equals(secondWord.substring(0, overlapLen))) {
                return overlapLen;
            }
        }
        
        return 0; // No overlap found
    }

    /**
     * Reconstructs the optimal path by backtracking through parent pointers.
     * Returns a list of word indices in the order they should appear in the superstring.
     */
    private List<Integer> reconstructPath(int[][] parentWord, int lastWord, int numWords) {
        List<Integer> path = new ArrayList<>();
        int currentMask = (1 << numWords) - 1; // Start with all words used
        int currentWord = lastWord;
        
        // Backtrack through parent pointers to build path in reverse
        while (currentWord != -1) {
            path.add(currentWord);
            int previousWord = parentWord[currentMask][currentWord];
            currentMask ^= (1 << currentWord); // Remove current word from mask
            currentWord = previousWord;
        }
        
        // Reverse to get correct order
        Collections.reverse(path);
        
        // Handle edge case: if some words were never added (shouldn't happen with correct DP)
        // This ensures all words appear in the path
        Set<Integer> usedWords = new HashSet<>(path);
        for (int word = 0; word < numWords; word++) {
            if (!usedWords.contains(word)) {
                path.add(word);
            }
        }
        
        return path;
    }

    /**
     * Builds the superstring by merging words according to the optimal path.
     * Uses overlap information to avoid duplicating overlapping characters.
     */
    private String buildSuperstring(String[] words, int[][] overlap, List<Integer> path) {
        StringBuilder superstring = new StringBuilder(words[path.get(0)]);
        
        // For each subsequent word, append only the non-overlapping part
        for (int i = 1; i < path.size(); i++) {
            int previousWordIndex = path.get(i - 1);
            int currentWordIndex = path.get(i);
            int overlapLength = overlap[previousWordIndex][currentWordIndex];
            
            // Append only the part of current word that doesn't overlap with previous
            // Example: if previous = "abc", current = "bcd", overlap = 2
            // We append "d" only (substring from index 2)
            superstring.append(words[currentWordIndex].substring(overlapLength));
        }
        
        return superstring.toString();
    }

    /**
     * Alternative approach using DFS with memoization.
     * This approach explores different orderings of words recursively.
     *
     * Algorithm:
     * 1. Try all possible orderings using DFS with bitmask to track used words
     * 2. For each state (current mask, last word), memoize the best superstring suffix
     * 3. Build the complete superstring by choosing the first word and appending the best suffix
     * 4. Return the shortest superstring found among all possibilities
     *
     * Key Insight: Unlike the bottom-up DP approach, this top-down approach builds the
     * actual strings during recursion, which is more intuitive but uses more space.
     *
     * Time Complexity: O(n * 2^n * m) where n is number of words and m is average word length.
     * We have 2^n * n states (mask, last word combinations), and for each we try n next words.
     * String operations add O(m) factor.
     *
     * Space Complexity: O(2^n * n * m) for memoization table storing strings.
     * This is higher than the first approach because we store actual strings.
     *
     * @param words array of strings to find superstring for
     * @return the shortest superstring containing all words
     */
    public String shortestSuperstringDFS(String[] words) {
        int numWords = words.length;
        int[][] overlap = computeOverlapMatrix(words);
        
        // Memoization: key = "mask_lastWord", value = best superstring suffix for remaining words
        Map<String, String> memo = new HashMap<>();
        
        return dfs(words, overlap, 0, -1, memo);
    }

    /**
     * Recursive DFS that explores all possible word orderings.
     * Returns the shortest superstring for the remaining words not in the mask.
     */
    private String dfs(String[] words, int[][] overlap, int usedWordsMask, int lastWordIndex, 
                      Map<String, String> memo) {
        int numWords = words.length;
        int allWordsUsedMask = (1 << numWords) - 1;
        
        // Base case: all words have been used, return empty string
        if (usedWordsMask == allWordsUsedMask) {
            return "";
        }
        
        // Check memoization
        String memoKey = usedWordsMask + "_" + lastWordIndex;
        if (memo.containsKey(memoKey)) {
            return memo.get(memoKey);
        }
        
        String shortestSuffix = null;
        
        // Try adding each unused word next
        for (int nextWord = 0; nextWord < numWords; nextWord++) {
            // Skip if word is already used
            if ((usedWordsMask & (1 << nextWord)) != 0) {
                continue;
            }
            
            // Add nextWord to the mask
            int newMask = usedWordsMask | (1 << nextWord);
            
            // Recursively find best suffix after adding nextWord
            String suffixAfterNextWord = dfs(words, overlap, newMask, nextWord, memo);
            
            // Build the complete string with nextWord
            String currentCandidate;
            if (lastWordIndex == -1) {
                // This is the first word, use it completely
                currentCandidate = words[nextWord] + suffixAfterNextWord;
            } else {
                // Merge with overlap
                int overlapLength = overlap[lastWordIndex][nextWord];
                currentCandidate = words[nextWord].substring(overlapLength) + suffixAfterNextWord;
            }
            
            // Update shortest if this candidate is better
            if (shortestSuffix == null || currentCandidate.length() < shortestSuffix.length()) {
                shortestSuffix = currentCandidate;
            }
        }
        
        // Memoize and return
        memo.put(memoKey, shortestSuffix);
        return shortestSuffix;
    }
}