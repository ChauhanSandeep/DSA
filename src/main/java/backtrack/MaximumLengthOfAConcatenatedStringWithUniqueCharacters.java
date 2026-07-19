package backtrack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Problem: Maximum Length of a Concatenated String with Unique Characters
 *
 * Given an array of lowercase strings, choose any subsequence and concatenate it.
 * Return the maximum possible length such that every character in the final
 * string is unique.
 *
 * Leetcode: https://leetcode.com/problems/maximum-length-of-a-concatenated-string-with-unique-characters/
 * Rating:   zerotrac 1719
 * Pattern:  Backtracking | Subsequence search | Bitmask character sets
 *
 * Example:
 *   Input:  ["cha", "r", "act", "ers"]
 *   Output: 6
 *   Why:    "cha" + "ers" forms "chaers" with six distinct characters; adding
 *           "r" or "act" would repeat a character.
 *
 * Follow-ups:
 *   1. Return all maximum-length concatenations?
 *      Track strings beside masks and collect ties when length equals the best.
 *   2. Support uppercase/larger alphabets beyond 26 lowercase letters?
 *      Use BitSet or long masks split across multiple words instead of one int.
 *   3. Count valid subsequences rather than the maximum length?
 *      DFS over masks and increment for every reachable state, or DP by mask.
 *   4. Optimize when arr is long but many strings conflict?
 *      Pre-filter duplicate-character strings and memoize by (index, mask).
 *
 * Related: Subsets (78), Maximum Product of Word Lengths (318).
 *
 *   Approach                 Method             Time    Space (extra)
 *   -----------------------  -----------------  ------  -------------
 *   Set backtracking         maxLengthSetBased  O(2^n)  O(n + alphabet)
 *   Bitmask backtracking     maxLength          O(2^n)  O(n)
 */
public class MaximumLengthOfAConcatenatedStringWithUniqueCharacters {
    private static final int ALPHABET_SIZE = 26;

    /**
     * Intuition: the final string is valid only if every chosen character is new.
     * A set makes that rule very literal: it contains the characters already used
     * by the current concatenation. For each later string, either it conflicts
     * with the set and cannot be chosen, or it adds only new characters and is safe
     * to explore. We filter out strings that already contain duplicates because
     * they can never be part of a valid answer.
     *
     * Algorithm:
     *   1. Return 0 for null input and keep only strings whose own characters are unique.
     *   2. Start DFS at index 0 with an empty set of used characters.
     *   3. At each index, try every later string that has no character already in the set.
     *   4. For a safe string, copy the used-character set, add the string's characters,
     *      recurse after that string, and keep the best length found.
     *
     * Time:  O(2^n * L) - each string is either chosen or skipped, and checking a choice scans its characters.
     * Space: O(n + alphabet) recursion depth and used-character set.
     *
     * @param arr candidate strings
     * @return maximum unique-character concatenation length
     */
    public int maxLengthSetBased(List<String> arr) {
        if (arr == null) return 0;

        List<String> validStrings = new ArrayList<>();
        for (String value : arr) {
            if (isUnique(value)) validStrings.add(value);
        }
        return backtrackWithSet(validStrings, 0, new HashSet<>());
    }

    /** Explores valid concatenations while storing used characters in a set. */
    private int backtrackWithSet(List<String> strings, int index, Set<Character> usedChars) {
        int bestLength = usedChars.size();
        for (int i = index; i < strings.size(); i++) {
            String candidate = strings.get(i);
            if (!canAdd(candidate, usedChars)) continue;

            Set<Character> nextUsedChars = new HashSet<>(usedChars);
            for (char ch : candidate.toCharArray()) nextUsedChars.add(ch);
            bestLength = Math.max(bestLength, backtrackWithSet(strings, i + 1, nextUsedChars));
        }
        return bestLength;
    }

    /** Returns true when a string has no repeated characters. */
    private boolean isUnique(String value) {
        if (value == null) return false;

        Set<Character> seenChars = new HashSet<>();
        for (char ch : value.toCharArray()) {
            if (!seenChars.add(ch)) return false;
        }
        return true;
    }

    /** Checks whether a string has no character already present in the current set. */
    private boolean canAdd(String value, Set<Character> usedChars) {
        for (char ch : value.toCharArray()) {
            if (usedChars.contains(ch)) return false;
        }
        return true;
    }

    /**
     * Intuition (interview default): since the strings are lowercase, each string's
     * character set fits in one integer mask. A 1 bit means that character is
     * present. Two strings are compatible exactly when their masks have no common
     * 1 bits, which is a single AND check. That turns the same choose-or-skip
     * search into a much cheaper bit operation, while still exploring every valid
     * subsequence that could be the maximum.
     *
     * Algorithm:
     *   1. Return 0 for null input and convert each duplicate-free lowercase string into a bitmask.
     *   2. DFS through the mask list, carrying the combined mask of the current concatenation.
     *   3. Treat the bit count of the current mask as the current valid length.
     *   4. For each later mask with no overlapping bits, recurse with it OR'ed into
     *      the current mask and keep the largest length returned.
     *
     * Time:  O(2^n) - after masks are built, each branch checks compatibility with one bit operation.
     * Space: O(n) for masks and recursion depth.
     *
     * @param arr candidate lowercase strings
     * @return maximum unique-character concatenation length
     */
    public int maxLength(List<String> arr) {
        if (arr == null) return 0;

        List<Integer> masks = new ArrayList<>();
        for (String value : arr) {
            int mask = toMask(value);
            if (mask != -1) masks.add(mask);
        }
        return backtrackWithMask(masks, 0, 0);
    }

    /** Explores compatible string masks and returns the best bit count reachable. */
    private int backtrackWithMask(List<Integer> masks, int index, int currentMask) {
        int bestLength = Integer.bitCount(currentMask);
        for (int i = index; i < masks.size(); i++) {
            int nextMask = masks.get(i);
            if ((currentMask & nextMask) != 0) continue;

            bestLength = Math.max(bestLength, backtrackWithMask(masks, i + 1, currentMask | nextMask));
        }
        return bestLength;
    }

    /** Converts a lowercase unique-character string to a bitmask, or -1 when invalid. */
    private int toMask(String value) {
        if (value == null) return -1;

        int mask = 0;
        for (char ch : value.toCharArray()) {
            int offset = ch - 'a';
            if (offset < 0 || offset >= ALPHABET_SIZE) return -1;

            int bit = 1 << offset;
            if ((mask & bit) != 0) return -1;
            mask |= bit;
        }
        return mask;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        MaximumLengthOfAConcatenatedStringWithUniqueCharacters solver =
            new MaximumLengthOfAConcatenatedStringWithUniqueCharacters();

        List<List<String>> inputs = new ArrayList<>();
        inputs.add(listOf("un", "iq", "ue"));
        inputs.add(listOf("cha", "r", "act", "ers"));
        inputs.add(listOf("aa", "bb"));
        int[] expected = {4, 6, 0};

        for (int i = 0; i < inputs.size(); i++) {
            int got = solver.maxLength(inputs.get(i));
            System.out.printf("arr=%s  ->  %d  expected=%d%n", inputs.get(i), got, expected[i]);
        }
    }

    /** Builds a mutable list for compact demo inputs. */
    private static List<String> listOf(String... values) {
        List<String> list = new ArrayList<>();
        for (String value : values) list.add(value);
        return list;
    }
}
