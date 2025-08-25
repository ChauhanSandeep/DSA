package dailybytes.string;

/**
 * ✅ Problem: Valid Palindrome II
 *
 * Given a string `s`, return true if it can be a palindrome after deleting **at most one character**.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/valid-palindrome-ii/
 *
 * 🧠 Example:
 * Input:  "foobof" → Output: true   (delete 'b')
 * Input:  "abcba"  → Output: true   (already a palindrome)
 * Input:  "abccab" → Output: false  (cannot be made palindrome with 1 deletion)
 *
 * 🔍 Follow-up Questions:
 * 1. What if you’re allowed to delete up to k characters? ➤ Extend with a recursive or DP approach
 * 2. What if you need to return **which character** to delete? ➤ Store the index where mismatch occurs
 * 3. Can you do this in O(1) space? ➤ Yes, with index-based checks (no new strings)
 */
public class ValidPalindrome2 {

    public static void main(String[] args) {
        assert canFormPalindromeByDeletingAtMostOneChar("foobof") : "Test case 1 failed";
        assert canFormPalindromeByDeletingAtMostOneChar("abcba") : "Test case 2 failed";
        assert !canFormPalindromeByDeletingAtMostOneChar("abccab") : "Test case 3 failed";
        assert canFormPalindromeByDeletingAtMostOneChar("abca") : "Test case 4 failed"; // delete 'b' or 'c'
        assert canFormPalindromeByDeletingAtMostOneChar("a") : "Test case 5 failed";
    }

    /**
     * ✅ Checks if a string can become a palindrome by deleting at most one character.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1) — No substring creation
     *
     * @param str Input string
     * @return true if valid with ≤ 1 deletion, false otherwise
     */
    public static boolean canFormPalindromeByDeletingAtMostOneChar(String str) {
        if (str == null || str.length() < 2) return true;

        int left = 0, right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                // Try skipping either the left or the right character
                return isPalindrome(str, left + 1, right) || isPalindrome(str, left, right - 1);
            }
            left++;
            right--;
        }

        return true; // No mismatches
    }

    /**
     * ✅ Helper to check if a substring of str from [left, right] is a palindrome.
     *
     * @param str   The original string
     * @param left  Start index
     * @param right End index
     * @return true if str[left...right] is a palindrome
     */
    private static boolean isPalindrome(String str, int left, int right) {
        while (left < right) {
            if (str.charAt(left++) != str.charAt(right--)) return false;
        }
        return true;
    }
}