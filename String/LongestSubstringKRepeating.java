package String;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Given a string and an integer k, return the length of the longest substring where
 * each character appears at least k times.
 *
 * Approaches:
 * 1. **Divide and Conquer (Recursive Splitting) - O(n²) Worst Case**
 *    - Find the first character that appears less than k times and use it as a split point.
 *    - Recursively apply the same logic to the left and right parts.
 *    - Return the max of the left and right results.
 * 
 * 2. **Sliding Window (Optimized Approach) - O(26*n) = O(n)**
 *    - Iterate with different numbers of unique characters (1 to 26).
 *    - Maintain a window with at most `uniqueCharCount` unique characters.
 *    - If all characters in the window appear at least k times, update the max length.
 * 
 * Time Complexity: **O(n²) worst case (Divide and Conquer), O(n) (Sliding Window optimal)**  
 * Space Complexity: **O(n) for recursion (Divide and Conquer), O(1) for Sliding Window**  
 * 
 * LeetCode Problem: https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/
 */
public class LongestSubstringKRepeating {
    public static void main(String[] args) {
        LongestSubstringKRepeating solution = new LongestSubstringKRepeating();
        
        String input = "aaabb";
        int k = 3;

        System.out.println("Longest Substring (Divide & Conquer): " + solution.longestSubstringDivideConquer(input, k));
        System.out.println("Longest Substring (Sliding Window): " + solution.longestSubstringSlidingWindow(input, k));
    }

    /**
     * Divide and Conquer Approach: O(n²) Worst Case
     */
    public int longestSubstringDivideConquer(String str, int k) {
        int len = str.length();
        if (len == 0 || k > len) return 0; // Base case: If k > string length, no valid substring
        if (k == 1) return len; // If k == 1, entire string is valid
        
        // Frequency map
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : str.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Find first invalid character to use as a split point
        int pivot = 0;
        while (pivot < len && frequencyMap.get(str.charAt(pivot)) >= k) {
            pivot++;
        }
        if (pivot == len) return len; // Entire string is valid
        
        // Recursively check left and right substrings
        int left = longestSubstringDivideConquer(str.substring(0, pivot), k);
        
        // Skip characters that are below k frequency
        while (pivot < len && frequencyMap.get(str.charAt(pivot)) < k) {
            pivot++;
        }
        
        int right = (pivot < len) ? longestSubstringDivideConquer(str.substring(pivot), k) : 0;
        
        return Math.max(left, right);
    }

    /**
     * Sliding Window Approach: O(26 * n) = O(n)
     */
    public int longestSubstringSlidingWindow(String str, int k) {
        int maxLen = 0;
        int n = str.length();

        // Try all possible unique character counts from 1 to 26
        for (int uniqueTarget = 1; uniqueTarget <= 26; uniqueTarget++) {
            int[] freq = new int[26];
            int left = 0, right = 0;
            int uniqueChars = 0, charsAtLeastK = 0;

            while (right < n) {
                if (uniqueChars <= uniqueTarget) {
                    // Expand window: add new character
                    char rightChar = str.charAt(right);
                    int idx = rightChar - 'a';
                    if (freq[idx] == 0) uniqueChars++;
                    freq[idx]++;
                    if (freq[idx] == k) charsAtLeastK++;
                    right++;
                } else {
                    // Shrink window from left
                    char leftChar = str.charAt(left);
                    int idx = leftChar - 'a';
                    if (freq[idx] == k) charsAtLeastK--;
                    freq[idx]--;
                    if (freq[idx] == 0) uniqueChars--;
                    left++;
                }

                // Update max length if valid
                if (uniqueChars == uniqueTarget && uniqueChars == charsAtLeastK) {
                    maxLen = Math.max(maxLen, right - left);
                }
            }
        }
        return maxLen;
    }
}
