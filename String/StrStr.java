package String;

/**
 * Implement the "strStr()" function that finds the index of the first occurrence of a substring.
 *
 * Approaches:
 * 1. **Brute Force (O(N * M))**: Checks all possible starting positions in the main string.
 * 2. **KMP Algorithm (O(N + M))**: Uses a prefix table (LPS) to avoid redundant comparisons.
 *
 * Time Complexity:
 * - Brute Force: **O(N * M)**
 * - KMP Algorithm: **O(N + M)**
 *
 * Space Complexity:
 * - Brute Force: **O(1)**
 * - KMP Algorithm: **O(M)** (for LPS array)
 *
 * LeetCode Equivalent: https://leetcode.com/problems/implement-strstr/
 */
public class StrStr {
    public static void main(String[] args) {
        String text = "GeeksForGeeks";
        String pattern = "For";

        System.out.println(findSubstringBruteForce(text, pattern)); // 5
        System.out.println(findSubstringKMP(text, pattern)); // 5
    }

    /**
     * Brute Force Approach: Finds the first occurrence of a substring in a string.
     *
     * @param text The main string.
     * @param pattern The substring to search for.
     * @return The index of the first occurrence, or -1 if not found.
     */
    public static int findSubstringBruteForce(String text, String pattern) {
        if (text == null || pattern == null || pattern.isEmpty()) return -1;

        int textLength = text.length();
        int patternLength = pattern.length();

        for (int i = 0; i <= textLength - patternLength; i++) {
            if (text.charAt(i) == pattern.charAt(0) && isMatch(text, pattern, i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Helper function to check if a substring starting at a given index matches the pattern.
     */
    private static boolean isMatch(String text, String pattern, int index) {
        int patternLength = pattern.length();
        if (index + patternLength > text.length()) return false;

        for (int i = 0; i < patternLength; i++) {
            if (text.charAt(index + i) != pattern.charAt(i)) return false;
        }
        return true;
    }

    /**
     * Optimized Approach: Finds the first occurrence using the KMP (Knuth-Morris-Pratt) Algorithm.
     *
     * @param text The main string.
     * @param pattern The substring to search for.
     * @return The index of the first occurrence, or -1 if not found.
     */
    public static int findSubstringKMP(String text, String pattern) {
        if (pattern.isEmpty()) return 0;

        int textLength = text.length();
        int patternLength = pattern.length();

        // Compute LPS (Longest Prefix Suffix) array
        int[] lps = computeLPSArray(pattern);

        int i = 0, j = 0; // Pointers for text and pattern

        while (i < textLength) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                // If we found a match
                if (
