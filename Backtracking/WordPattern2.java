package Backtracking;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * LeetCode: https://leetcode.com/problems/word-pattern-ii/
 *
 * Problem:
 * Given a pattern string and a target string, determine if the pattern can be mapped uniquely to substrings.
 *
 * Approach:
 * 1️⃣ **Backtracking with HashMap & HashSet**
 *    - `map<Character, String>` → Tracks pattern-to-substring mappings.
 *    - `visited<String>` → Prevents multiple pattern characters mapping to the same substring.
 *    - If a mapping exists, check if the substring at `sIndex` matches.
 *    - If no mapping, try all possible substrings and backtrack.
 *
 * Time Complexity: **O(N^M)** (where N = string length, M = pattern length)
 * Space Complexity: **O(M)** (storing mappings & recursion depth)
 */
public class WordPattern2 {
    public static void main(String[] args) {
        String pattern = "abab";
        String str = "redblueredblue";
        System.out.println("Word matches pattern? " + wordPatternMatch(pattern, str));
    }

    /**
     * Determines if the string follows the given pattern mapping.
     *
     * @param pattern The pattern string (e.g., "abab").
     * @param str The target string (e.g., "redblueredblue").
     * @return True if a valid mapping exists, false otherwise.
     */
    public static boolean wordPatternMatch(String pattern, String str) {
        return backtrack(pattern, 0, str, 0, new HashMap<>(), new HashSet<>());
    }

    /**
     * Recursive backtracking function.
     *
     * @param pattern Pattern string.
     * @param pIndex Current index in the pattern.
     * @param str Target string.
     * @param sIndex Current index in the string.
     * @param map Mapping of pattern characters to substrings.
     * @param visited Set to track used substrings.
     * @return True if a valid mapping is found, otherwise false.
     */
    private static boolean backtrack(String pattern, int pIndex, String str, int sIndex,
                                     Map<Character, String> map, Set<String> visited) {
        if (pIndex == pattern.length() && sIndex == str.length()) return true;
        if (pIndex >= pattern.length() || sIndex >= str.length()) return false;

        char currentPatternChar = pattern.charAt(pIndex);

        // If the character is already mapped, check consistency
        if (map.containsKey(currentPatternChar)) {
            String mappedSubstring = map.get(currentPatternChar);

            // Direct substring check instead of startsWith (slightly faster)
            if (!str.startsWith(mappedSubstring, sIndex)) return false;

            // Continue recursion after the matched substring
            return backtrack(pattern, pIndex + 1, str, sIndex + mappedSubstring.length(), map, visited);
        }

        // Try all possible substrings for the current pattern character
        for (int i = sIndex + 1; i <= str.length(); i++) {
            String mappedSubstring = str.substring(sIndex, i);

            // Skip if this substring is already mapped to another pattern character
            if (visited.contains(mappedSubstring)) continue;

            // Assign mapping and mark as visited
            map.put(currentPatternChar, mappedSubstring);
            visited.add(mappedSubstring);

            // Recur to check the next part of the pattern
            if (backtrack(pattern, pIndex + 1, str, i, map, visited)) return true;

            // Backtrack: Undo mapping
            map.remove(currentPatternChar);
            visited.remove(mappedSubstring);
        }

        return false;
    }
}
