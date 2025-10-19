package strings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * 205. Isomorphic Strings
 *
 * Problem Statement:
 * Given two strings s and t, determine if they are isomorphic.
 * Two strings s and t are isomorphic if the characters in s can be replaced to get t.
 * All occurrences of a character must be replaced with another character while
 * preserving the order of characters. No two characters may map to the same character,
 * but a character may map to itself.
 *
 * Example:
 * Input: s = "egg", t = "add"
 * Output: true
 * Explanation: 'e' maps to 'a' and 'g' maps to 'd'. The mapping is consistent.
 *
 * Input: s = "foo", t = "bar"
 * Output: false
 * Explanation: 'o' would need to map to both 'a' and 'r', which violates the one-to-one rule.
 *
 * Input: s = "paper", t = "title"
 * Output: true
 * Explanation: 'p'→'t', 'a'→'i', 'e'→'l', 'r'→'e'. Each character maps consistently.
 *
 * LeetCode Link: https://leetcode.com/problems/isomorphic-strings/
 *
 * Follow-up Questions:
 * 1. What if we need to find the actual character mapping?
 *    Answer: Return the HashMap instead of boolean, containing the mapping from s to t.
 * 2. How would you handle Unicode characters or larger character sets?
 *    Answer: HashMap approach scales naturally; array approach needs larger arrays.
 * 3. What if we need to check isomorphism for multiple string pairs efficiently?
 *    Answer: Precompute canonical forms or use efficient hashing techniques.
 * 4. How to extend this to check isomorphism among multiple strings simultaneously?
 *    Answer: Build equivalence classes and ensure all strings have same pattern structure.
 *
 * Related Problems:
 * - 290. Word Pattern: https://leetcode.com/problems/word-pattern/
 * - 242. Valid Anagram: https://leetcode.com/problems/valid-anagram/
 * - 49. Group Anagrams: https://leetcode.com/problems/group-anagrams/
 */
public class IsomorphicStrings {

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
    public boolean isIsomorphicSingleMap(String s, String t) {
        if (s == null || t == null || s.length() != t.length()) {
            return s == t;
        }

        Map<Character, Character> mapping = new HashMap<>();
        Set<Character> usedChars = new HashSet<>();

        for (int i = 0; i < s.length(); i++) {
            char charS = s.charAt(i);
            char charT = t.charAt(i);

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
     * Optimized approach using arrays for ASCII characters.
     * This version uses fixed-size arrays for better performance with ASCII characters.
     *
     * Intuition to solve:
     * 1. Each character in s must map to one character in t.
     * 2. Each character in t must map to one character in s.
     * 3. If we see a character pair (s[i], t[i]) again, the mapping must be consistent.
     *    Means that if s[i] was seen before, it must map to the same t[i] as before.
     * 4. If a character in s maps to multiple characters in t or vice versa, return false.
     * 5. Use two arrays to track last seen positions of characters in s and t.
     * 6. If the last seen positions don't match for a character pair, return false.
     * 7. If we finish checking all character pairs without conflicts, return true.
     * 8. This approach is efficient for ASCII characters due to fixed-size arrays.
     *
     * Time Complexity: O(n) - single pass through strings
     * Space Complexity: O(1) - fixed-size arrays (constant space)
     *
     * This approach is most efficient for ASCII characters.
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
