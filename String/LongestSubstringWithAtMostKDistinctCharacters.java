package String;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 340. Longest Substring with At Most K Distinct Characters
 *
 * Given a string s and an integer k, return the length of the longest substring
 * that contains at most k distinct characters.
 *
 * Example 1:
 * Input: s = "eceba", k = 2
 * Output: 3
 * Explanation: The substring is "ece" with length 3.
 *
 * Example 2:
 * Input: s = "aa", k = 1
 * Output: 2
 * Explanation: The substring is "aa" with length 2.
 *
 * LeetCode Link: https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/
 *
 * Follow-up Questions:
 * - How would you modify this for exactly k distinct characters? (Add condition to check exactly k characters)
 * - Can you optimize for very large strings with small alphabet? (Use array instead of HashMap for ASCII)
 * - How would you extend to handle Unicode characters efficiently? (Current HashMap approach already handles this)
 * - What if k is very large compared to string length? (Early termination when distinct chars < k)
 */
public class LongestSubstringWithAtMostKDistinctCharacters {

    /**
     * Finds longest substring with at most k distinct characters using sliding window.
     *
     * Algorithm:
     * 1. Use sliding window technique with left and right pointers
     * 2. Expand right pointer and add characters to frequency map
     * 3. When distinct characters exceed k, shrink window from left
     * 4. Track maximum window size encountered
     * 5. Return maximum length found
     *
     * Time Complexity: O(n) where n is length of string s
     * Space Complexity: O(min(k, alphabet_size)) for the character frequency map
     *
     * @param s Input string to search within
     * @param k Maximum number of distinct characters allowed
     * @return Length of longest valid substring
     */
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (k == 0 || s == null || s.length() == 0) {
            return 0;
        }

        Map<Character, Integer> charFrequency = new HashMap<>();
        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);

            // Add current character to window
            charFrequency.put(rightChar, charFrequency.getOrDefault(rightChar, 0) + 1);

            // Shrink window until we have at most k distinct characters
            while (charFrequency.size() > k) {
                char leftChar = s.charAt(left);
                charFrequency.put(leftChar, charFrequency.get(leftChar) - 1);

                if (charFrequency.get(leftChar) == 0) {
                    charFrequency.remove(leftChar);
                }
                left++;
            }

            // Update maximum length
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }

    /**
     * Alternative approach optimized for ASCII characters using array.
     */
    public int lengthOfLongestSubstringKDistinctOptimized(String s, int k) {
        if (k == 0 || s == null || s.length() == 0) {
            return 0;
        }

        int[] charCount = new int[256]; // ASCII characters
        int left = 0;
        int maxLength = 0;
        int distinctCount = 0;

        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);

            // Add character to window
            if (charCount[rightChar] == 0) {
                distinctCount++;
            }
            charCount[rightChar]++;

            // Shrink window if necessary
            while (distinctCount > k) {
                char leftChar = s.charAt(left);
                charCount[leftChar]--;
                if (charCount[leftChar] == 0) {
                    distinctCount--;
                }
                left++;
            }

            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }
}
