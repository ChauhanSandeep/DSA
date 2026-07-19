package strings.patternmatching;

/**
 * Problem: Find the Index of the First Occurrence in a String
 *
 * Given text and pattern, return the first index where pattern appears in text.
 * If pattern is absent, return -1. KMP avoids rechecking characters by reusing
 * prefix-suffix information from the pattern.
 *
 * Leetcode: https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/ (Easy)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Strings | KMP | Prefix function
 *
 * Example:
 *   Input:  text = "ababcabcabababd", pattern = "ababd"
 *   Output: 10
 *   Why:    the substring from index 10 through 14 is exactly "ababd".
 *
 * Follow-ups:
 *   1. How would you search many patterns in one text?
 *      Build an Aho-Corasick automaton instead of one LPS array per pattern.
 *   2. How do you support streaming text?
 *      Keep the current pattern index and feed incoming characters one by one.
 *   3. How can this find repeated prefixes in one string?
 *      Reuse the LPS array to identify borders and periodic structure.
 */

public class KMPAlgorithm {

        /**
     * Intuition: when a mismatch happens after matching part of pattern, the
     * matched prefix may also be a suffix of what we just saw. The LPS array tells
     * us the longest reusable prefix, so textIndex never moves backward.
     *
     * Algorithm:
     *   1. Build the LPS array for pattern.
     *   2. Scan text and pattern together while characters match.
     *   3. On mismatch, jump patternIndex using LPS or advance textIndex if nothing matched.
     *   4. Return the start index once patternIndex reaches pattern.length().
     *
     * Time:  O(n + m) - each text and pattern position is advanced or jumped a bounded number of times.
     * Space: O(m) - the LPS array stores one value per pattern character.
     *
     * @param text Text to search inside.
     * @param pattern Pattern to find.
     * @return First starting index of pattern in text, or -1 when absent.
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

        /** Builds the longest-prefix-suffix table used for KMP fallback jumps. */
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

    public static void main(String[] args) {
        String[] texts = {"ababcabcabababd", "aaaaa", "abc"};
        String[] patterns = {"ababd", "bba", ""};
        int[] expected = {10, -1, 0};

        for (int i = 0; i < texts.length; i++) {
            int got = kmpSearch(texts[i], patterns[i]);
            System.out.printf("text=%s pattern=%s -> %d  expected=%d%n",
                texts[i], patterns[i], got, expected[i]);
        }
    }

}