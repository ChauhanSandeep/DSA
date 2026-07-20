package dailybytes.array;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Problem: First Unique Character in a String
 *
 * Given a lowercase string, return the index of the first character that appears
 * exactly once. If every character repeats, return -1.
 *
 * Leetcode: https://leetcode.com/problems/first-unique-character-in-a-string/ (Easy)
 * Rating:   acceptance 65.9% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Frequency counting | First valid position
 *
 * Example:
 *   Input:  str = "loveleetcode"
 *   Output: 2
 *   Why:    'l' and 'o' repeat, while 'v' appears once and is the earliest
 *           character with frequency one.
 *
 * Follow-ups:
 *   1. Return the first unique character instead of its index?
 *      Return str.charAt(i) from the second pass when freq is one.
 *   2. Process a live stream and query the first unique character anytime?
 *      Maintain counts plus an ordered queue or linked map of candidates.
 *   3. Support arbitrary Unicode characters?
 *      Replace the fixed 26-slot array with a map keyed by code point.
 *   4. Answer many substring first-unique queries?
 *      Precompute per-character prefix counts and scan candidate positions per query.
 *
 * Related: First Unique Number, Find First Non-Repeating Character in a Stream.
 */
public class FirstUniqueChar {

    public static void main(String[] args) {
        String[] inputs = { "aaa", "loveleetcode", "aabb", "aabca", "leetcode", "thedailybyte" };
        int[] expected = { -1, 2, -1, 2, 0, 1 };

        for (int i = 0; i < inputs.length; i++) {
            int output = findFirstUniqueCharIndexWithMap(inputs[i]);
            int primaryOutput = findFirstUniqueCharIndex(inputs[i]);
            System.out.printf("str=%s -> %d  expected=%d  primary=%d%n",
                inputs[i], output, expected[i], primaryOutput);
        }
    }

    /**
     * Intuition: a character can be the answer only if its final frequency is
     * one, and "first" depends on the original order. Count every lowercase
     * letter once, then make a second pass through str so the first frequency-one
     * position is returned immediately.
     *
     * Algorithm:
     *   1. Return -1 for a null or empty string.
     *   2. Count each character of str in the freq array.
     *   3. Scan str again and return the first index whose count is one.
     *   4. Return -1 if every character repeats.
     *
     * Time:  O(n) - two linear passes over the string.
     * Space: O(1) - the 26-slot lowercase frequency array is fixed size.
     *
     * @param str lowercase input string
     * @return index of the first unique character, or -1 when none exists
     */
    public static int findFirstUniqueCharIndex(String str) {
        if (str == null || str.isEmpty()) return -1;

        int[] freq = new int[26];
        int length = str.length();

        for (int i = 0; i < length; i++) {
            freq[str.charAt(i) - 'a']++;
        }

        for (int i = 0; i < length; i++) {
            if (freq[str.charAt(i) - 'a'] == 1) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Intuition: LinkedHashMap preserves first-seen order, so store each
     * character's first index and permanently mark repeats with a sentinel.
     * Once a character repeats, later copies can never make it unique again.
     *
     * Algorithm:
     *   1. Return -1 for a null or empty string.
     *   2. Scan str from left to right.
     *   3. Store a new character's index, or mark an existing character as repeated.
     *   4. Return the first non-sentinel index left in insertion order.
     *   5. Return -1 if every character repeats.
     *
     * Time:  O(n) - one scan plus one pass over distinct characters.
     * Space: O(n) - the linked map can hold each distinct character.
     *
     * @param str lowercase input string
     * @return index of the first unique character, or -1 when none exists
     */
    public static int findFirstUniqueCharIndexWithMap(String str) {
        if (str == null || str.isEmpty()) return -1;

        Map<Character, Integer> charIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (charIndexMap.containsKey(ch)) {
                charIndexMap.put(ch, -1);
            } else {
                charIndexMap.put(ch, i);
            }
        }

        return charIndexMap.values().stream()
            .filter(index -> index != -1)
            .findFirst()
            .orElse(-1);
    }
}
