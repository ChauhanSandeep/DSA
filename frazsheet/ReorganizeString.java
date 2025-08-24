package frazsheet;

import java.util.PriorityQueue;

/**
 * Given a string s, rearrange the characters of s so that any two adjacent characters are not the same.
 * Return any possible rearrangement of s or return an empty string if not possible.
 * 
 * Example 1:
 * Input: s = "aab"
 * Output: "aba"
 * 
 * Example 2:
 * Input: s = "aaab"
 * Output: ""
 * 
 * LeetCode: https://leetcode.com/problems/reorganize-string/
 * 
 * Follow-up Questions:
 * 1. How would you handle the case where we need to ensure that no two identical characters are at least k distance apart?
 *    - We could modify the solution to maintain a queue of recently used characters and their availability times.
 * 2. What if the string contains Unicode characters?
 *    - The solution would work the same way since we're working with character frequencies.
 * 3. How would you find all possible valid rearrangements instead of just one?
 *    - We would need to use backtracking to explore all possible valid orderings.
 * 
 * Related Problems:
 * - Task Scheduler (https://leetcode.com/problems/task-scheduler/)
 * - Rearrange String k Distance Apart (https://leetcode.com/problems/rearrange-string-k-distance-apart/)
 */
public class ReorganizeString {
    /**
     * Reorganizes the string so that no two adjacent characters are the same.
     * 
     * @param s Input string
     * @return Reorganized string or empty string if not possible
     */
    public String reorganizeString(String s) {
        // Count character frequencies
        int[] charCounts = new int[26];
        for (char c : s.toCharArray()) {
            charCounts[c - 'a']++;
        }
        
        // Max heap based on character frequency
        PriorityQueue<CharFreq> maxHeap = new PriorityQueue<>((a, b) -> b.freq - a.freq);
        
        // Add characters to the heap
        for (int i = 0; i < 26; i++) {
            if (charCounts[i] > 0) {
                // If any character's count exceeds (n+1)/2, reorganization is not possible
                if (charCounts[i] > (s.length() + 1) / 2) {
                    return "";
                }
                maxHeap.offer(new CharFreq((char) ('a' + i), charCounts[i]));
            }
        }
        
        StringBuilder result = new StringBuilder();
        
        while (!maxHeap.isEmpty()) {
            // Get the most frequent character (excluding the one just used)
            CharFreq first = maxHeap.poll();
            
            if (result.length() == 0 || result.charAt(result.length() - 1) != first.c) {
                // If the last character is different, append the current character
                result.append(first.c);
                if (--first.freq > 0) {
                    maxHeap.offer(first);
                }
            } else {
                // Otherwise, get the second most frequent character
                if (maxHeap.isEmpty()) {
                    return ""; // No valid arrangement possible
                }
                
                CharFreq second = maxHeap.poll();
                result.append(second.c);
                if (--second.freq > 0) {
                    maxHeap.offer(second);
                }
                
                // Add the first character back to the heap
                maxHeap.offer(first);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Helper class to store character and its frequency.
     */
    private static class CharFreq {
        char c;
        int freq;
        
        CharFreq(char c, int freq) {
            this.c = c;
            this.freq = freq;
        }
    }
    
    /**
     * Alternative solution using an array for better performance.
     * This approach is more efficient as it avoids the overhead of the PriorityQueue.
     */
    public String reorganizeStringArrayBased(String s) {
        // Count character frequencies
        int[] charCounts = new int[26];
        int maxCount = 0;
        int maxChar = 0;
        
        for (char c : s.toCharArray()) {
            int index = c - 'a';
            charCounts[index]++;
            if (charCounts[index] > maxCount) {
                maxCount = charCounts[index];
                maxChar = index;
            }
        }
        
        // If the most frequent character appears more than (n+1)/2 times, return empty string
        if (maxCount > (s.length() + 1) / 2) {
            return "";
        }
        
        char[] result = new char[s.length()];
        int idx = 0;
        
        // Place the most frequent character at even indices
        while (charCounts[maxChar] > 0) {
            result[idx] = (char) (maxChar + 'a');
            idx += 2;
            charCounts[maxChar]--;
        }
        
        // Place the remaining characters
        for (int i = 0; i < 26; i++) {
            while (charCounts[i] > 0) {
                // If we've reached the end of the array, start from index 1
                if (idx >= s.length()) {
                    idx = 1;
                }
                
                result[idx] = (char) (i + 'a');
                idx += 2;
                charCounts[i]--;
            }
        }
        
        return new String(result);
    }
    
    /**
     * Another approach using interleaving.
     * This solution separates characters into two groups and interleaves them.
     */
    public String reorganizeStringInterleave(String s) {
        // Count character frequencies
        int[] charCounts = new int[26];
        for (char c : s.toCharArray()) {
            charCounts[c - 'a']++;
        }
        
        // Find the character with maximum frequency
        int maxCount = 0;
        int maxChar = 0;
        for (int i = 0; i < 26; i++) {
            if (charCounts[i] > maxCount) {
                maxCount = charCounts[i];
                maxChar = i;
            }
        }
        
        // If the most frequent character appears more than (n+1)/2 times, return empty string
        if (maxCount > (s.length() + 1) / 2) {
            return "";
        }
        
        // Fill all even positions with the most frequent character
        char[] result = new char[s.length()];
        int index = 0;
        
        // Place the most frequent character first
        while (charCounts[maxChar] > 0) {
            result[index] = (char) (maxChar + 'a');
            index += 2;
            charCounts[maxChar]--;
        }
        
        // Place the remaining characters
        for (int i = 0; i < 26; i++) {
            while (charCounts[i] > 0) {
                // If we've reached the end of the array, start from index 1
                if (index >= s.length()) {
                    index = 1;
                }
                
                result[index] = (char) (i + 'a');
                index += 2;
                charCounts[i]--;
            }
        }
        
        return new String(result);
    }
}
