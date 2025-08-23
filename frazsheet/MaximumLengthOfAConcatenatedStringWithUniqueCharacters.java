package frazsheet;

import java.util.*;

/**
 * 1239. Maximum Length of a Concatenated String with Unique Characters
 * 
 * Problem: Given an array of strings arr, return the maximum possible length of a
 * string formed by concatenating a subsequence of arr that has unique characters.
 * 
 * Example:
 * Input: arr = ["un","iq","ue"]
 * Output: 4
 * Explanation: All possible concatenations are "", "un", "iq", "ue", "uniq", "ique".
 * Maximum length is 4.
 * 
 * LeetCode: https://leetcode.com/problems/maximum-length-of-a-concatenated-string-with-unique-characters
 * 
 * Follow-up questions:
 * Q: What if the array is very large?
 * A: Use pruning techniques and memoization to avoid redundant computations.
 * 
 * Q: Can we optimize space usage?
 * A: Use bit manipulation to represent character sets more compactly.
 * 
 * Q: How to find all maximum-length combinations?
 * A: Modify backtracking to collect all solutions that achieve maximum length.
 */
public class MaximumLengthOfAConcatenatedStringWithUniqueCharacters {
    
    /**
     * Backtracking approach exploring all possible combinations.
     * 
     * Algorithm: Depth-First Search with pruning
     * - For each string, decide whether to include it or not
     * - Use Set to track used characters and ensure uniqueness
     * - Prune branches where current string has duplicate chars with existing set
     * - Return maximum length found across all valid combinations
     * 
     * Time Complexity: O(2^n) where n is number of strings
     * Space Complexity: O(1) excluding recursion stack
     */
    public int maxLength(List<String> arr) {
        // Filter out strings that have duplicate characters within themselves
        List<String> validStrings = new ArrayList<>();
        for (String s : arr) {
            if (isUnique(s)) {
                validStrings.add(s);
            }
        }
        
        return backtrack(validStrings, 0, new HashSet<>());
    }
    
    // Check if string has all unique characters
    private boolean isUnique(String s) {
        Set<Character> chars = new HashSet<>();
        for (char c : s.toCharArray()) {
            if (!chars.add(c)) {
                return false;
            }
        }
        return true;
    }
    
    // Backtracking to find maximum length
    private int backtrack(List<String> arr, int index, Set<Character> usedChars) {
        int maxLen = usedChars.size();
        
        for (int i = index; i < arr.size(); i++) {
            String current = arr.get(i);
            
            // Check if current string can be added (no overlapping characters)
            if (canAdd(current, usedChars)) {
                // Add characters from current string
                Set<Character> newUsedChars = new HashSet<>(usedChars);
                for (char c : current.toCharArray()) {
                    newUsedChars.add(c);
                }
                
                // Recursive call
                int length = backtrack(arr, i + 1, newUsedChars);
                maxLen = Math.max(maxLen, length);
            }
        }
        
        return maxLen;
    }
    
    // Check if string can be added without character conflicts
    private boolean canAdd(String s, Set<Character> usedChars) {
        for (char c : s.toCharArray()) {
            if (usedChars.contains(c)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Bit manipulation approach for efficient character set operations.
     * Uses integers as bitmasks to represent character sets.
     */
    public int maxLengthBitMask(List<String> arr) {
        List<Integer> masks = new ArrayList<>();
        List<Integer> lengths = new ArrayList<>();
        
        // Convert strings to bitmasks
        for (String s : arr) {
            int mask = stringToMask(s);
            if (mask != -1) { // Valid string (no duplicates)
                masks.add(mask);
                lengths.add(s.length());
            }
        }
        
        return backtrackBitMask(masks, lengths, 0, 0);
    }
    
    // Convert string to bitmask (-1 if has duplicates)
    private int stringToMask(String s) {
        int mask = 0;
        for (char c : s.toCharArray()) {
            int bit = 1 << (c - 'a');
            if ((mask & bit) != 0) {
                return -1; // Duplicate character
            }
            mask |= bit;
        }
        return mask;
    }
    
    // Backtracking with bitmasks
    private int backtrackBitMask(List<Integer> masks, List<Integer> lengths, int index, int currentMask) {
        int maxLen = Integer.bitCount(currentMask);
        
        for (int i = index; i < masks.size(); i++) {
            int stringMask = masks.get(i);
            
            // Check if string can be added (no overlapping bits)
            if ((currentMask & stringMask) == 0) {
                int newMask = currentMask | stringMask;
                int length = backtrackBitMask(masks, lengths, i + 1, newMask);
                maxLen = Math.max(maxLen, length);
            }
        }
        
        return maxLen;
    }
    
    /**
     * Dynamic Programming approach using memoization.
     * Caches results for subproblems to avoid recomputation.
     */
    public int maxLengthDP(List<String> arr) {
        List<String> validStrings = new ArrayList<>();
        for (String s : arr) {
            if (isUnique(s)) {
                validStrings.add(s);
            }
        }
        
        Map<String, Integer> memo = new HashMap<>();
        return dpHelper(validStrings, 0, "", memo);
    }
    
    // DP helper with memoization
    private int dpHelper(List<String> arr, int index, String current, Map<String, Integer> memo) {
        String key = index + ":" + current;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        int maxLen = current.length();
        
        for (int i = index; i < arr.size(); i++) {
            String candidate = arr.get(i);
            String newString = current + candidate;
            
            if (isUnique(newString)) {
                int length = dpHelper(arr, i + 1, newString, memo);
                maxLen = Math.max(maxLen, length);
            }
        }
        
        memo.put(key, maxLen);
        return maxLen;
    }
    
    /**
     * Iterative approach using breadth-first search.
     * Builds all possible valid combinations level by level.
     */
    public int maxLengthIterative(List<String> arr) {
        List<Set<Character>> validCombinations = new ArrayList<>();
        validCombinations.add(new HashSet<>()); // Empty combination
        
        for (String s : arr) {
            if (!isUnique(s)) continue;
            
            List<Set<Character>> newCombinations = new ArrayList<>();
            
            for (Set<Character> existing : validCombinations) {
                // Try adding current string to existing combination
                if (canAdd(s, existing)) {
                    Set<Character> newCombination = new HashSet<>(existing);
                    for (char c : s.toCharArray()) {
                        newCombination.add(c);
                    }
                    newCombinations.add(newCombination);
                }
            }
            
            validCombinations.addAll(newCombinations);
        }
        
        int maxLen = 0;
        for (Set<Character> combination : validCombinations) {
            maxLen = Math.max(maxLen, combination.size());
        }
        
        return maxLen;
    }
    
    /**
     * Optimized backtracking with early pruning.
     * Includes additional optimizations to reduce search space.
     */
    public int maxLengthOptimized(List<String> arr) {
        // Sort strings by length descending for better pruning
        List<String> validStrings = new ArrayList<>();
        for (String s : arr) {
            if (isUnique(s)) {
                validStrings.add(s);
            }
        }
        
        validStrings.sort((a, b) -> Integer.compare(b.length(), a.length()));
        
        int[] maxPossible = new int[validStrings.size()];
        int total = 0;
        for (int i = validStrings.size() - 1; i >= 0; i--) {
            total += validStrings.get(i).length();
            maxPossible[i] = total;
        }
        
        return backtrackOptimized(validStrings, 0, new HashSet<>(), maxPossible);
    }
    
    // Optimized backtracking with pruning
    private int backtrackOptimized(List<String> arr, int index, Set<Character> usedChars, int[] maxPossible) {
        int currentLen = usedChars.size();
        
        // Pruning: if current + remaining possible <= best found so far
        if (index < arr.size() && currentLen + maxPossible[index] <= currentLen) {
            return currentLen;
        }
        
        int maxLen = currentLen;
        
        for (int i = index; i < arr.size(); i++) {
            String current = arr.get(i);
            
            if (canAdd(current, usedChars)) {
                Set<Character> newUsedChars = new HashSet<>(usedChars);
                for (char c : current.toCharArray()) {
                    newUsedChars.add(c);
                }
                
                int length = backtrackOptimized(arr, i + 1, newUsedChars, maxPossible);
                maxLen = Math.max(maxLen, length);
            }
        }
        
        return maxLen;
    }
    
    /**
     * Returns all maximum-length combinations instead of just the length.
     * Extension that provides actual string combinations.
     */
    public List<String> maxLengthCombinations(List<String> arr) {
        List<String> validStrings = new ArrayList<>();
        for (String s : arr) {
            if (isUnique(s)) {
                validStrings.add(s);
            }
        }
        
        List<String> allCombinations = new ArrayList<>();
        findAllCombinations(validStrings, 0, new StringBuilder(), new HashSet<>(), allCombinations);
        
        int maxLen = allCombinations.stream().mapToInt(String::length).max().orElse(0);
        
        return allCombinations.stream()
                .filter(s -> s.length() == maxLen)
                .collect(java.util.stream.Collectors.toList());
    }
    
    // Find all valid combinations
    private void findAllCombinations(List<String> arr, int index, StringBuilder current, 
                                   Set<Character> usedChars, List<String> results) {
        results.add(current.toString());
        
        for (int i = index; i < arr.size(); i++) {
            String candidate = arr.get(i);
            
            if (canAdd(candidate, usedChars)) {
                // Add string
                current.append(candidate);
                Set<Character> newUsedChars = new HashSet<>(usedChars);
                for (char c : candidate.toCharArray()) {
                    newUsedChars.add(c);
                }
                
                findAllCombinations(arr, i + 1, current, newUsedChars, results);
                
                // Remove string (backtrack)
                current.setLength(current.length() - candidate.length());
            }
        }
    }
    
    /**
     * Parallel processing approach for large inputs.
     * Uses parallel streams to explore different branches concurrently.
     */
    public int maxLengthParallel(List<String> arr) {
        List<String> validStrings = arr.stream()
                .filter(this::isUnique)
                .collect(java.util.stream.Collectors.toList());
        
        if (validStrings.isEmpty()) return 0;
        
        // Use parallel processing for first-level branches
        return validStrings.parallelStream()
                .mapToInt(s -> {
                    Set<Character> initialChars = new HashSet<>();
                    for (char c : s.toCharArray()) {
                        initialChars.add(c);
                    }
                    
                    List<String> remaining = validStrings.stream()
                            .filter(str -> !str.equals(s))
                            .collect(java.util.stream.Collectors.toList());
                    
                    return Math.max(s.length(), 
                                  s.length() + backtrack(remaining, 0, initialChars));
                })
                .max()
                .orElse(0);
    }
    
    /**
     * Trie-based approach for efficient string matching.
     * Uses trie structure to optimize character conflict detection.
     */
    public int maxLengthTrie(List<String> arr) {
        TrieNode root = new TrieNode();
        
        // Build combinations using trie
        for (String s : arr) {
            if (isUnique(s)) {
                insertCombinations(root, s, 0, new StringBuilder(), new HashSet<>());
            }
        }
        
        return findMaxLength(root);
    }
    
    // Trie node structure
    private static class TrieNode {
        Map<String, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
        int length = 0;
    }
    
    // Insert all valid combinations starting with given string
    private void insertCombinations(TrieNode node, String s, int index, StringBuilder current, Set<Character> used) {
        // Mark current combination
        node.isEnd = true;
        node.length = Math.max(node.length, current.length());
        
        for (int i = index; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!used.contains(c)) {
                current.append(c);
                used.add(c);
                
                String key = current.toString();
                node.children.putIfAbsent(key, new TrieNode());
                
                insertCombinations(node.children.get(key), s, i + 1, current, used);
                
                current.deleteCharAt(current.length() - 1);
                used.remove(c);
            }
        }
    }
    
    // Find maximum length in trie
    private int findMaxLength(TrieNode node) {
        int maxLen = node.length;
        
        for (TrieNode child : node.children.values()) {
            maxLen = Math.max(maxLen, findMaxLength(child));
        }
        
        return maxLen;
    }
}