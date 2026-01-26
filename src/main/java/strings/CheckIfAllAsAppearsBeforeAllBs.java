package strings;

/**
 * LeetCode 2124. Check if All A's Appears Before All B's
 *
 * Given a string s consisting of only the characters 'a' and 'b', return true if every 'a'
 * appears before every 'b' in the string. Otherwise, return false.
 *
 * Example 1:
 * Input: s = "aaabbb"
 * Output: true
 * Explanation: All 'a's appear before all 'b's
 *
 * LeetCode Link: https://leetcode.com/problems/check-if-all-as-appears-before-all-bs/
 *
 * Follow-up Questions:
 * - How would you solve this for multiple characters? (Extend to check ordering of k different characters)
 * - Can you solve this with a single pass without string search? (Track state transitions)
 * LeetCode Contest Rating: 1202
 */
public class CheckIfAllAsAppearsBeforeAllBs {

    /**
     * Checks if all 'a's appear before all 'b's using substring search.
     *
     * Algorithm:
     * 1. If any 'b' appears before any 'a', then substring "ba" must exist
     * 2. If "ba" doesn't exist, then all 'a's come before all 'b's
     *
     * Time Complexity: O(n) where n is length of string
     * Space Complexity: O(1) - only uses constant extra space
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
