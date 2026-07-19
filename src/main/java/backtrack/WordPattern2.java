package backtrack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Problem: Word Pattern II
 *
 * Given a pattern and a target string, decide whether each pattern character can
 * map to a non-empty substring so expanding the pattern equals the target. The
 * mapping must be bijective: no two pattern characters may use the same substring.
 *
 * Leetcode: https://leetcode.com/problems/word-pattern-ii/
 * Rating:   acceptance 49.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Backtracking | Bijection | Try substring assignments
 *
 * Example:
 *   Input:  pattern = "abab", s = "redblueredblue"
 *   Output: true
 *   Why:    mapping a to "red" and b to "blue" expands "abab" into
 *           "redblueredblue" with a different substring for each pattern char.
 *
 * Follow-ups:
 *   1. Return the actual mapping instead of boolean?
 *      Return/copy the map when both indexes reach the end.
 *   2. Return all valid mappings?
 *      Do not stop at the first success; collect every complete bijection.
 *   3. Minimize total mapped substring length under wildcard patterns?
 *      This becomes an optimization DFS with branch-and-bound over assignments.
 *   4. Avoid substring allocation for large strings?
 *      Store start/end ranges and compare with regionMatches before materializing.
 *
 * Related: Word Pattern (290), Regular Expression Matching (10).
 */
public class WordPattern2 {

    /**
     * Intuition: each pattern character must consistently stand for one non-empty
     * substring, and two different characters cannot share a substring. When a
     * character already has a mapping, there is no choice: the target must start
     * with that mapped word at the current position. When it is new, we try every
     * possible next substring and backtrack if that assignment prevents the rest
     * of the pattern from matching. The map plus used-substring set enforce the
     * bijection in both directions.
     *
     * Algorithm:
     *   1. Return false for null inputs and start DFS at the beginning of both strings.
     *   2. If the current pattern character is already mapped, require the target
     *      to have that mapped substring at the current target index.
     *   3. If the character is unmapped, try every non-empty candidate substring
     *      that leaves enough characters for the remaining pattern.
     *   4. Assign an unused candidate to the character, recurse after it, and undo
     *      the assignment if the rest of the pattern cannot match.
     *   5. Succeed only when both the pattern and target are consumed at the same time.
     *
     * Time:  O(n^m) - each of m pattern characters may try many substrings from the length-n target.
     * Space: O(m + n) for mappings, used substrings, and recursion depth.
     *
     * @param pattern pattern characters to map
     * @param str target string to match
     * @return true if a bijective mapping exists
     */
    public static boolean wordPatternMatch(String pattern, String str) {
        if (pattern == null || str == null) return false;

        return backtrack(pattern, 0, str, 0, new HashMap<>(), new HashSet<>());
    }

    /** Tries substring assignments for pattern characters while preserving a bijection. */
    private static boolean backtrack(String pattern, int patternIndex,
                                     String target, int targetIndex,
                                     Map<Character, String> charToWord,
                                     Set<String> usedWords) {
        if (patternIndex == pattern.length() && targetIndex == target.length()) return true;
        if (patternIndex == pattern.length() || targetIndex == target.length()) return false;

        char patternChar = pattern.charAt(patternIndex);
        String mappedWord = charToWord.get(patternChar);
        if (mappedWord != null) {
            return target.startsWith(mappedWord, targetIndex)
                && backtrack(pattern, patternIndex + 1, target, targetIndex + mappedWord.length(),
                    charToWord, usedWords);
        }

        int remainingPatternChars = pattern.length() - patternIndex - 1;
        int lastCandidateEnd = target.length() - remainingPatternChars;
        for (int end = targetIndex + 1; end <= lastCandidateEnd; end++) {
            String candidate = target.substring(targetIndex, end);
            if (usedWords.contains(candidate)) continue;

            charToWord.put(patternChar, candidate);
            usedWords.add(candidate);
            if (backtrack(pattern, patternIndex + 1, target, end, charToWord, usedWords)) return true;
            usedWords.remove(candidate);
            charToWord.remove(patternChar);
        }
        return false;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        String[] patterns = {"abab", "aaaa", "aabb", ""};
        String[] strings = {"redblueredblue", "asdasdasdasd", "xyzabcxzyabc", ""};
        boolean[] expected = {true, true, false, true};

        for (int i = 0; i < patterns.length; i++) {
            boolean got = wordPatternMatch(patterns[i], strings[i]);
            System.out.printf("pattern=%s str=%s  ->  %s  expected=%s%n",
                patterns[i], strings[i], got, expected[i]);
        }
    }
}
