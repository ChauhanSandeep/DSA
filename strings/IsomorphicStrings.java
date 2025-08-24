package strings;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 205. Isomorphic Strings
 *
 * Given two strings s and t, determine if they are isomorphic.
 * Two strings s and t are isomorphic if the characters in s can be replaced to get t.
 *
 * Example 1:
 * Input: s = "egg", t = "add"
 * Output: true
 * Explanation: 'e' maps to 'a', 'g' maps to 'd'
 *
 * LeetCode Link: https://leetcode.com/problems/isomorphic-strings/
 */
public class IsomorphicStrings {

    /**
     * Checks if two strings are isomorphic using bidirectional character mapping.
     *
     * Algorithm:
     * 1. Maintain two mappings: s->t and t->s
     * 2. For each character pair, check if mapping is consistent
     * 3. If character already mapped, verify it maps to same target
     * 4. If not mapped, ensure target is not already mapped to different character
     *
     * Time Complexity: O(n) where n is length of strings
     * Space Complexity: O(k) where k is number of unique characters
     */
    public boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        Map<Character, Character> sToT = new HashMap<>();
        Map<Character, Character> tToS = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char sChar = s.charAt(i);
            char tChar = t.charAt(i);

            // Check s -> t mapping
            if (sToT.containsKey(sChar)) {
                if (sToT.get(sChar) != tChar) {
                    return false;
                }
            } else {
                sToT.put(sChar, tChar);
            }

            // Check t -> s mapping
            if (tToS.containsKey(tChar)) {
                if (tToS.get(tChar) != sChar) {
                    return false;
                }
            } else {
                tToS.put(tChar, sChar);
            }
        }

        return true;
    }
}
