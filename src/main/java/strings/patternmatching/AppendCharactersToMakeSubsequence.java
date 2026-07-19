package strings.patternmatching;

/**
 * Problem: Append Characters to String to Make Subsequence
 *
 * Given source and target, append the fewest characters to the end of source so
 * that target becomes a subsequence. Characters already matched in order do not
 * need to be appended; only the unmatched suffix of target is missing.
 *
 * Leetcode: https://leetcode.com/problems/append-characters-to-string-to-make-subsequence/ (Medium)
 * Rating:   contest Elo 1363
 * Pattern:  Strings | Two pointers | Subsequence scan
 *
 * Example:
 *   Input:  source = "coaching", target = "coding"
 *   Output: 4
 *   Why:    "co" is matched inside source, so only "ding" must be appended.
 *
 * Follow-ups:
 *   1. What if characters may be inserted anywhere?
 *      Use longest common subsequence; the answer is target.length() - LCS.
 *   2. What if target contains wildcard characters?
 *      Replace the equality check with wildcard matching state transitions.
 *   3. What if many targets are queried against one source?
 *      Preprocess source positions by character and binary-search each target.
 */

public class AppendCharactersToMakeSubsequence {

    public static void main(String[] args) {
        AppendCharactersToMakeSubsequence solver = new AppendCharactersToMakeSubsequence();

        String[] sources = {"coaching", "abcde", ""};
        String[] targets = {"coding", "a", "abc"};
        int[] expected = {4, 0, 3};

        for (int i = 0; i < sources.length; i++) {
            int got = solver.appendCharacters(sources[i], targets[i]);
            System.out.printf("source=%s target=%s -> %d  expected=%d%n",
                sources[i], targets[i], got, expected[i]);
        }
    }

        /**
     * Intuition: scan source once and greedily match the earliest possible
     * characters of target. Any target character matched now can only help later
     * characters, because subsequences care about order, not contiguity. After
     * source is exhausted, the unmatched tail of target is exactly what must be
     * appended.
     *
     * Algorithm:
     *   1. Keep sourceIndex on source and targetIndex on the next target character to match.
     *   2. Advance through source, moving targetIndex only when the characters match.
     *   3. Return target.length() - targetIndex for the still-unmatched target suffix.
     *
     * Time:  O(n) - source is scanned once, with targetIndex only moving forward.
     * Space: O(1) - only two indices are stored.
     *
     * @param source Source string scanned for a subsequence match.
     * @param target Target string that should become a subsequence.
     * @return Minimum number of target characters to append to source.
     */
    public int appendCharacters(String source, String target) {
        // Handle null inputs explicitly
      if (source == null || target == null) {
        return 0;
      }

        int sourceIndex = 0; // Pointer for source
        int targetIndex = 0; // Pointer for target

        // Traverse both strings to find matching subsequence
        while (sourceIndex < source.length() && targetIndex < target.length()) {
            if (source.charAt(sourceIndex) == target.charAt(targetIndex)) {
                targetIndex++; // move both if characters match
            }
            sourceIndex++; // always move pointer sourceIndex
        }

        // targetIndex is the number of matched characters
        // so (target.length() - targetIndex) characters need to be appended
        return target.length() - targetIndex;
    }
}