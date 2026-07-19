package strings.patternmatching;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Longest Substring with At Most K Distinct Characters
 *
 * Given a string and k, return the maximum length of a contiguous substring that
 * uses no more than k distinct characters. The window may contain fewer than k
 * distinct characters; it only becomes invalid after crossing that limit.
 *
 * Leetcode: https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/ (Medium)
 * Rating:   no contest Elo (premium problem)
 * Pattern:  Strings | Sliding window | Frequency map
 *
 * Example:
 *   Input:  s = "eceba", k = 2
 *   Output: 3
 *   Why:    "ece" has only e and c, while any longer prefix would add b.
 *
 * Follow-ups:
 *   1. What if the requirement is exactly k distinct characters?
 *      Update the answer only when the frequency map size is exactly k.
 *   2. How would this work on a stream?
 *      Keep the same window state and evict from the left as characters arrive.
 *   3. How do you return the substring instead of its length?
 *      Track the best start and best length whenever maxLength improves.
 *
 * Related: Longest Substring Without Repeating Characters (3), Fruit Into Baskets (904).
 */
public class LongestSubstringWithAtMostKDistinctCharacters {

    public static void main(String[] args) {
        LongestSubstringWithAtMostKDistinctCharacters solver = new LongestSubstringWithAtMostKDistinctCharacters();

        String[] inputs = {"eceba", "aa", "abc"};
        int[] ks = {2, 1, 0};
        int[] expected = {3, 2, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.lengthOfLongestSubstringKDistinct(inputs[i], ks[i]);
            System.out.printf("s=%s k=%d -> %d  expected=%d%n",
                inputs[i], ks[i], got, expected[i]);
        }
    }

        /**
     * Intuition: a window remains valid while its frequency map contains at most
     * maxDistinctAllowed keys. Expand to explore longer substrings, and only when
     * the distinct count is too high, remove characters from the left until the
     * invariant is restored.
     *
     * Algorithm:
     *   1. Expand right and add the new character to charFrequency.
     *   2. While there are too many distinct characters, decrement and remove from the left.
     *   3. Update maxLength with the current valid window size.
     *
     * Time:  O(n) - each character enters and leaves the window at most once.
     * Space: O(k) - the map stores at most maxDistinctAllowed active characters after shrinking.
     *
     * @param str Input string.
     * @param maxDistinctAllowed Maximum number of distinct characters allowed in the window.
     * @return Length of the longest valid substring.
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
