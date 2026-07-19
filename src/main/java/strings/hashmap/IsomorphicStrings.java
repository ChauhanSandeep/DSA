package strings.hashmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * Problem: Isomorphic Strings
 *
 * Two strings are isomorphic when every character in the first string can be
 * replaced by exactly one character in the second string, and no two source
 * characters map to the same target character.
 *
 * Leetcode: https://leetcode.com/problems/isomorphic-strings/ (Easy)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Hash map | Bijection | Last-seen positions
 *
 * Example:
 *   Input:  s = "egg", t = "add"
 *   Output: true
 *   Why:    e maps to a and g maps to d consistently, with no target reused by another source.
 *
 * Follow-ups:
 *   1. Return the actual mapping?
 *      Build and return the source-to-target map after validation.
 *   2. Support Unicode?
 *      Use HashMaps instead of fixed ASCII arrays.
 *   3. Compare many strings by pattern?
 *      Convert each string to a canonical first-seen-index signature.
 *
 * Related: Word Pattern (290), Valid Anagram (242), Group Anagrams (49).
 */
public class IsomorphicStrings {

    public static void main(String[] args) {
        IsomorphicStrings solver = new IsomorphicStrings();
        String[] sources = {"egg", "foo", "paper", "badc"};
        String[] targets = {"add", "bar", "title", "baba"};
        boolean[] expected = {true, false, true, false};

        for (int i = 0; i < sources.length; i++) {
            boolean got = solver.isIsomorphicArray(sources[i], targets[i]);
            System.out.printf("s=%s t=%s -> %s  expected=%s%n", sources[i], targets[i], got, expected[i]);
        }
    }


    /**
     * Approach using single HashMap with value validation.
     * This version uses one map but checks for duplicate values explicitly.
     *
     * Algorithm: Single map with value uniqueness check
     * 1. Use one HashMap to map s[i] -> t[i]
     * 2. Use HashSet to track used target characters
     * 3. For each character pair, ensure bijective property
     *
     * Time Complexity: O(n) - single pass through strings
     * Space Complexity: O(k) - HashMap and HashSet storage
     *
     */
    public boolean isIsomorphicSingleMap(String source, String target) {
        if (source == null || target == null || source.length() != target.length()) {
            return source == target;
        }

        Map<Character, Character> mapping = new HashMap<>();
        Set<Character> usedChars = new HashSet<>();

        for (int i = 0; i < source.length(); i++) {
            char charS = source.charAt(i);
            char charT = target.charAt(i);

            if (mapping.containsKey(charS)) {
                // Check if existing mapping is consistent
                if (mapping.get(charS) != charT) {
                    return false;
                }
            } else {
                // Check if charT is already used for different mapping
                if (usedChars.contains(charT)) {
                    return false;
                }
                mapping.put(charS, charT);
                usedChars.add(charT);
            }
        }

        return true;
    }

    /**
     * Intuition: two strings are isomorphic if every aligned character pair has the
     * same history. Recording the last position where each source and target
     * character appeared turns the bijection check into one equality comparison per
     * index.
     *
     * Algorithm:
     *   1. Return true only for equal null references when either string is null or lengths differ.
     *   2. Keep two arrays of last-seen positions for source and target characters.
     *   3. For each index, the two current characters must have matching last-seen positions.
     *   4. Store i + 1 for both characters and continue until all pairs are checked.
     *
     * Time:  O(n) - one pass over both strings.
     * Space: O(1) - fixed arrays of size 256 are used for ASCII characters.
     *
     * @param s source string
     * @param t target string
     * @return true if s and t are isomorphic
     */
    public boolean isIsomorphicArray(String s, String t) {
        if (s == null || t == null || s.length() != t.length()) {
            return Objects.equals(s, t);
        }

        // Arrays to track last seen position for each character
        int[] sLastSeen = new int[256]; // For characters in s
        int[] tLastSeen = new int[256]; // For characters in t

        for (int i = 0; i < s.length(); i++) {
            char charS = s.charAt(i);
            char charT = t.charAt(i);

            // Check if both characters were last seen at same relative position
            if (sLastSeen[charS] != tLastSeen[charT]) {
                return false;
            }

            // Update last seen position (use i+1 to distinguish from initial 0)
            sLastSeen[charS] = i + 1;
            tLastSeen[charT] = i + 1;
        }

        return true;
    }
}
