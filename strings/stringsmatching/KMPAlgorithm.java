package strings.stringsmatching;

/**
 * KMPAlgorithm implements the Knuth-Morris-Pratt string matching algorithm.
 *
 * Problem Statement:
 * Given a string `text` and a string `pattern`, return the index of the first occurrence
 * of `pattern` in `text`. If the pattern is not found, return -1.
 *
 * This is more efficient than the naive O(m*n) pattern matching algorithm by pre-processing
 * the pattern to avoid redundant comparisons.
 *
 * Example:
 * Input: text = "ababcabcabababd", pattern = "ababd"
 * Output: 10
 *
 * Explanation: The pattern "ababd" is found at index 10 in the text.
 *
 * LeetCode Link: https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
 */

public class KMPAlgorithm {

    /**
     * Finds the first index of the given pattern in the given text using the KMP algorithm.
     *
     * Logical Intuition:
     * - We avoid unnecessary comparisons by pre-processing the pattern to create a "Longest Prefix Suffix" (LPS) array.
     * - If a mismatch happens, instead of restarting from the next character in the text,
     *   we use the LPS array to skip ahead in the pattern intelligently.
     *
     * Steps:
     * 1. Build the LPS array from the pattern.
     * 2. Use the LPS array while scanning the text to avoid redundant comparisons.
     *
     * Time Complexity: O(n + m), where n = length of text, m = length of pattern
     * Space Complexity: O(m), for storing the LPS array
     *
     * @param text    The main string in which we want to search for the pattern.
     * @param pattern The pattern to search for.
     * @return The starting index of the pattern in text if found; otherwise, -1.
     */
    public static int kmpSearch(String text, String pattern) {
      if (pattern.isEmpty()) {
        return 0;
      }

        int[] lps = buildLPS(pattern); // Step 1: Preprocess the pattern
        int textIndex = 0; // Pointer for text
        int patternIndex = 0; // Pointer for pattern

        while (textIndex < text.length()) {
            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                // Characters match, move both pointers
                textIndex++;
                patternIndex++;

                // Entire pattern matched
                if (patternIndex == pattern.length()) {
                    return textIndex - pattern.length(); // Return starting index
                }
            } else {
                // Mismatch after some matches
                if (patternIndex != 0) {
                    // Use LPS to avoid rechecking
                    patternIndex = lps[patternIndex - 1];
                } else {
                    // No prefix matched, move text pointer
                    textIndex++;
                }
            }
        }

        return -1; // Pattern not found
    }

    /**
     * Builds the Longest Prefix Suffix (LPS) array for the given pattern.
     * LPS[i] = length of the longest proper prefix of pattern[0..i] which is also a suffix.
     *
     * Logical Intuition:
     * - At each position, LPS tells us how many characters we can reuse after a mismatch.
     *
     * Time Complexity: O(m), where m = pattern length
     * Space Complexity: O(m)
     *
     * @param pattern The pattern string.
     * @return The LPS array.
     */
    private static int[] buildLPS(String pattern) {
        int[] lps = new int[pattern.length()];
        int length = 0; // length of the previous longest prefix suffix
        int i = 1;

        // Loop through pattern to build LPS array
        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                // Characters match, extend current LPS
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    // Try with the previous longest prefix
                    length = lps[length - 1];
                } else {
                    // No match found, set LPS to 0
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    // Example usage
    public static void main(String[] args) {
        String text = "ababcabcabababd";
        String pattern = "ababd";

        int index = kmpSearch(text, pattern);
        System.out.println("Pattern found at index: " + index);
    }
}