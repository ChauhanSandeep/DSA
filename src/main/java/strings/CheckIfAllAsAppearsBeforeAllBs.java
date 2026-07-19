package strings;

/**
 * Problem: Check if All A's Appears Before All B's
 *
 * Given a string of only 'a' and 'b', return true if every 'a' appears before
 * every 'b'. Any transition from 'b' back to 'a' violates the rule.
 *
 * Leetcode: https://leetcode.com/problems/check-if-all-as-appears-before-all-bs/ (Easy)
 * Rating:   zerotrac 1202 (Q1, weekly-274)
 * Pattern:  String | Ordering invariant | Forbidden substring
 *
 * Example:
 *   Input:  s = "abab"
 *   Output: false
 *   Why:    "ba" appears, so an 'a' occurs after a 'b'.
 *
 * Follow-ups:
 *   1. More than two ordered characters? Track the maximum character rank seen so far.
 *   2. First bad index? Return the first 'a' found after a 'b'.
 *   3. Stream input? Keep one boolean saying whether a 'b' has appeared.
 */
public class CheckIfAllAsAppearsBeforeAllBs {

    public static void main(String[] args) {
        CheckIfAllAsAppearsBeforeAllBs solver = new CheckIfAllAsAppearsBeforeAllBs();
        String[] inputs = {"aaabbb", "abab", "bbb"};
        boolean[] expected = {true, false, true};
        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.checkString(inputs[i]);
            System.out.printf("s=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }


        /**
     * Intuition: if a 'b' ever appears before a later 'a', the adjacent boundary
     * between those regions contains the forbidden substring "ba". Therefore no
     * "ba" means all a-block characters precede all b-block characters.
     *
     * Algorithm:
     *   1. Search for the substring "ba".
     *   2. Return true when it is absent.
     *
     * Time:  O(n) - substring search scans the string.
     * Space: O(1) - no input-sized structure is created.
     *
     * @param s string containing only 'a' and 'b'
     * @return true if all 'a' characters appear before all 'b' characters
     */
    public boolean checkString(String s) {
        return !s.contains("ba");
    }

    /**
     * Alternative single-pass approach without substring search.
     */
    public boolean checkStringAlternative(String s) {
        boolean seenB = false;

        for (char c : s.toCharArray()) {
            if (c == 'b') {
                seenB = true;
            } else if (c == 'a' && seenB) {
                return false;
            }
        }

        return true;
    }
}
