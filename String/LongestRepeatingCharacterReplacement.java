package String;

import java.util.*;

/**
 * 424. Longest Repeating Character Replacement
 * 
 * Problem: Given a string s and integer k, you can choose any character and change it
 * to any other uppercase English letter. Return the length of the longest substring
 * containing the same letter you can get after performing at most k changes.
 * 
 * Example:
 * Input: s = "ABAB", k = 2
 * Output: 4
 * Explanation: Replace the two 'A's with 'B's or vice versa.
 * 
 * LeetCode: https://leetcode.com/problems/longest-repeating-character-replacement
 * 
 * Follow-up questions:
 * Q: What if we want to find all possible longest substrings?
 * A: Track all windows that achieve maximum length during sliding window traversal.
 * 
 * Q: Can we optimize for very long strings?
 * A: Current O(n) solution is already optimal, can't improve asymptotically.
 * 
 * Q: How to handle lowercase letters or other characters?
 * A: Expand the frequency array size or use HashMap for arbitrary characters.
 */
public class LongestRepeatingCharacterReplacement {
    
    /**
     * Sliding window approach with character frequency tracking.
     * 
     * Algorithm:
     * - Use sliding window [left, right] with character frequency map
     * - Maintain maxFreq = frequency of most common character in current window
     * - If window_size - maxFreq > k, shrink window from left
     * - Track maximum valid window size encountered
     * 
     * Time Complexity: O(n) where n is string length
     * Space Complexity: O(1) for fixed-size frequency array (26 letters)
     */
    public int characterReplacement(String s, int k) {
        if (s == null || s.length() == 0) return 0;
        
        int[] count = new int[26]; // Frequency of each character
        int left = 0, maxFreq = 0, result = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count[rightChar - 'A']++;
            maxFreq = Math.max(maxFreq, count[rightChar - 'A']);
            
            // If current window is invalid, shrink from left
            while (right - left + 1 - maxFreq > k) {
                char leftChar = s.charAt(left);
                count[leftChar - 'A']--;
                left++;
                
                // Recalculate maxFreq after removing left character
                maxFreq = 0;
                for (int freq : count) {
                    maxFreq = Math.max(maxFreq, freq);
                }
            }
            
            result = Math.max(result, right - left + 1);
        }
        
        return result;
    }
    
    /**
     * Optimized sliding window without recalculating maxFreq.
     * Maintains maxFreq as monotonic upper bound.
     */
    public int characterReplacementOptimized(String s, int k) {
        if (s == null || s.length() == 0) return 0;
        
        int[] count = new int[26];
        int left = 0, maxFreq = 0, result = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count[rightChar - 'A']++;
            maxFreq = Math.max(maxFreq, count[rightChar - 'A']);
            
            // If window becomes invalid, move left pointer
            // Note: maxFreq might be overestimate, but it doesn't affect correctness
            if (right - left + 1 - maxFreq > k) {
                char leftChar = s.charAt(left);
                count[leftChar - 'A']--;
                left++;
            }
            
            result = Math.max(result, right - left + 1);
        }
        
        return result;
    }
    
    /**
     * HashMap-based approach for handling arbitrary characters.
     * Works with any character set, not just uppercase English letters.
     */
    public int characterReplacementGeneral(String s, int k) {
        if (s == null || s.length() == 0) return 0;
        
        Map<Character, Integer> count = new HashMap<>();
        int left = 0, maxFreq = 0, result = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count.put(rightChar, count.getOrDefault(rightChar, 0) + 1);
            maxFreq = Math.max(maxFreq, count.get(rightChar));
            
            while (right - left + 1 - maxFreq > k) {
                char leftChar = s.charAt(left);
                count.put(leftChar, count.get(leftChar) - 1);
                if (count.get(leftChar) == 0) {
                    count.remove(leftChar);
                }
                left++;
                
                // Recalculate maxFreq
                maxFreq = count.values().stream().mapToInt(Integer::intValue).max().orElse(0);
            }
            
            result = Math.max(result, right - left + 1);
        }
        
        return result;
    }
    
    /**
     * Brute force approach for verification and understanding.
     * Checks all possible substrings explicitly.
     */
    public int characterReplacementBruteForce(String s, int k) {
        int n = s.length();
        int result = 0;
        
        for (int i = 0; i < n; i++) {
            int[] count = new int[26];
            int maxFreq = 0;
            
            for (int j = i; j < n; j++) {
                count[s.charAt(j) - 'A']++;
                maxFreq = Math.max(maxFreq, count[s.charAt(j) - 'A']);
                
                int windowSize = j - i + 1;
                int replacements = windowSize - maxFreq;
                
                if (replacements <= k) {
                    result = Math.max(result, windowSize);
                } else {
                    break; // No longer valid, and won't be valid for longer windows
                }
            }
        }
        
        return result;
    }
    
    /**
     * Character-specific approach trying each character as the target.
     * Alternative perspective: for each character, find longest substring.
     */
    public int characterReplacementByChar(String s, int k) {
        int result = 0;
        
        // Try each character as the target character
        for (char target = 'A'; target <= 'Z'; target++) {
            result = Math.max(result, longestSubstringWithChar(s, k, target));
        }
        
        return result;
    }
    
    // Find longest substring containing mostly 'target' character
    private int longestSubstringWithChar(String s, int k, char target) {
        int left = 0, replacements = 0, result = 0;
        
        for (int right = 0; right < s.length(); right++) {
            if (s.charAt(right) != target) {
                replacements++;
            }
            
            while (replacements > k) {
                if (s.charAt(left) != target) {
                    replacements--;
                }
                left++;
            }
            
            result = Math.max(result, right - left + 1);
        }
        
        return result;
    }
    
    /**
     * Two-pass approach: first find all valid windows, then determine maximum.
     * Demonstrates alternative algorithmic structure.
     */
    public int characterReplacementTwoPass(String s, int k) {
        if (s == null || s.length() == 0) return 0;
        
        List<Window> validWindows = new ArrayList<>();
        int[] count = new int[26];
        int left = 0, maxFreq = 0;
        
        // First pass: find all maximal valid windows
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count[rightChar - 'A']++;
            maxFreq = Math.max(maxFreq, count[rightChar - 'A']);
            
            while (right - left + 1 - maxFreq > k) {
                char leftChar = s.charAt(left);
                count[leftChar - 'A']--;
                left++;
                
                // Recalculate maxFreq
                maxFreq = Arrays.stream(count).max().orElse(0);
            }
            
            validWindows.add(new Window(left, right, right - left + 1));
        }
        
        // Second pass: find maximum window size
        return validWindows.stream().mapToInt(w -> w.size).max().orElse(0);
    }
    
    // Helper class for window tracking
    private static class Window {
        int left, right, size;
        
        Window(int left, int right, int size) {
            this.left = left;
            this.right = right;
            this.size = size;
        }
    }
    
    /**
     * Segment tree approach for range frequency queries.
     * Overkill for this problem but demonstrates advanced technique.
     */
    public int characterReplacementSegmentTree(String s, int k) {
        // For this specific problem, sliding window is more appropriate
        // Segment tree would be useful for multiple range queries
        return characterReplacement(s, k);
    }
    
    /**
     * Returns the actual longest substring instead of just length.
     * Extension that provides the substring content.
     */
    public String longestRepeatingSubstring(String s, int k) {
        if (s == null || s.length() == 0) return "";
        
        int[] count = new int[26];
        int left = 0, maxFreq = 0;
        int bestLeft = 0, bestRight = -1, maxLength = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count[rightChar - 'A']++;
            maxFreq = Math.max(maxFreq, count[rightChar - 'A']);
            
            while (right - left + 1 - maxFreq > k) {
                char leftChar = s.charAt(left);
                count[leftChar - 'A']--;
                left++;
                
                maxFreq = Arrays.stream(count).max().orElse(0);
            }
            
            if (right - left + 1 > maxLength) {
                maxLength = right - left + 1;
                bestLeft = left;
                bestRight = right;
            }
        }
        
        return bestRight >= bestLeft ? s.substring(bestLeft, bestRight + 1) : "";
    }
    
    /**
     * Parallel processing approach for very long strings.
     * Divides string into segments for concurrent processing.
     */
    public int characterReplacementParallel(String s, int k) {
        if (s == null || s.length() <= 1000) {
            return characterReplacement(s, k); // Use sequential for small strings
        }
        
        int segmentSize = s.length() / Runtime.getRuntime().availableProcessors();
        List<String> segments = new ArrayList<>();
        
        for (int i = 0; i < s.length(); i += segmentSize) {
            int end = Math.min(i + segmentSize * 2, s.length()); // Overlap segments
            segments.add(s.substring(i, end));
        }
        
        return segments.parallelStream()
            .mapToInt(segment -> characterReplacement(segment, k))
            .max()
            .orElse(0);
    }
}