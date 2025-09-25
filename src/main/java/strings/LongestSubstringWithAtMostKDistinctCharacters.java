package strings;

import java.util.HashMap;
import java.util.Map;

/**
 * Longest Substring with At Most K Distinct Characters
 *
 * Given a string s and an integer k, find the length of the longest substring
 * that contains at most k distinct characters. A substring is a contiguous
 * sequence of characters within a string.
 *
 * The key challenge is maintaining a window with exactly the constraint while
 * maximizing length. Unlike "exactly k distinct", this allows fewer than k
 * distinct characters, making it slightly more permissive.
 *
 * Example:
 * Input: s = "eceba", k = 2
 * Output: 3
 * Explanation: The longest substring with at most 2 distinct characters is "ece"
 * with length 3. Other valid substrings include "ec", "ce", "eb", but "ece" is longest.
 *
 * Example:
 * Input: s = "aa", k = 1
 * Output: 2
 * Explanation: The entire string "aa" has only 1 distinct character, satisfying k=1.
 *
 * LeetCode: https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if we need exactly k distinct characters instead of at most k?
 *    Answer: Modify condition to check len(charMap) == k when updating maxLength.
 * 2. How would you handle streaming data where string length is unknown?
 *    Answer: Use same sliding window but process character by character with buffering.
 * 3. What if k is very large compared to alphabet size?
 *    Answer: Optimization becomes less relevant, but algorithm remains O(n) efficient.
 * 4. How to return the actual substring instead of just length?
 *    Answer: Track bestStart and bestLength variables, update when finding longer valid window.
 *
 * Related Problems:
 * - LeetCode 159: Longest Substring with At Most Two Distinct Characters
 * - LeetCode 3: Longest Substring Without Repeating Characters
 * - LeetCode 424: Longest Repeating Character Replacement
 */
public class LongestSubstringWithAtMostKDistinctCharacters {

    /**
     * Finds longest substring with at most maxDistinctAllowed distinct characters using sliding window.
     *
     * Algorithm:
     * 1. Use sliding window technique with left and right pointers
     * 2. Expand right pointer and add characters to frequency map
     * 3. When distinct characters exceed maxDistinctAllowed, shrink window from left
     * 4. Track maximum window size encountered
     * 5. Return maximum length found
     *
     * Time Complexity: O(n) where n is length of string str
     * Space Complexity: O(min(maxDistinctAllowed, alphabet_size)) for the character frequency map
     *
     * @param str Input string to search within
     * @param maxDistinctAllowed Maximum number of distinct characters allowed
     * @return Length of longest valid substring
     */
    public int lengthOfLongestSubstringKDistinct(String str, int maxDistinctAllowed) {
        if (maxDistinctAllowed == 0 || str == null || str.isEmpty()) {
            return 0;
        }

        Map<Character, Integer> charFrequency = new HashMap<>();
        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < str.length(); right++) {
            char rightChar = str.charAt(right);

            // Add current character to window
            charFrequency.put(rightChar, charFrequency.getOrDefault(rightChar, 0) + 1);

            // Shrink window until we have at most maxDistinctAllowed distinct characters
            while (charFrequency.size() > maxDistinctAllowed) {
                char leftChar = str.charAt(left);
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
     *
     * The change is that in this version we use a fixed-size (256) array for character counts because we know that
     * the input consists of ASCII characters. This reduces the overhead of using a HashMap.
     */
    public int lengthOfLongestSubstringKDistinctOptimized(String str, int maxDistinctAllowed) {
        if (maxDistinctAllowed == 0 || str == null || str.length() == 0) {
            return 0;
        }

        int[] charCount = new int[256]; // ASCII characters
        int left = 0;
        int maxLength = 0;
        int distinctCount = 0;

        for (int right = 0; right < str.length(); right++) {
            char rightChar = str.charAt(right);

            // Add character to window
            if (charCount[rightChar] == 0) {
                distinctCount++;
            }
            charCount[rightChar]++;

            // Shrink window if necessary
            while (distinctCount > maxDistinctAllowed) {
                char leftChar = str.charAt(left);
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
