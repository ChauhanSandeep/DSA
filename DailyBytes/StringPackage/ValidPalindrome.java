package DailyBytes.StringPackage;

/**
 * ✅ Problem: Valid Palindrome
 *
 * Given a string, determine if it is a palindrome,
 * considering only alphanumeric characters and ignoring cases.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/valid-palindrome/
 *
 * 🧠 Examples:
 * 1. "level" → true
 * 2. "algorithm" → false
 * 3. "A man, a plan, a canal: Panama" → true
 *
 * 🧠 Follow-up:
 * 1. Can we handle Unicode? ➤ Use Character classes instead of ASCII
 * 2. Can we do this in O(1) space? ➤ Yes, using in-place two pointers
 * 3. What if it's a stream of characters? ➤ Use a queue and deque
 */
public class ValidPalindrome {

    public static void main(String[] args) {
        assert isValidPalindrome("level") : "Test case 1 failed";
        assert !isValidPalindrome("algorithm") : "Test case 2 failed";
        assert isValidPalindrome("A man, a plan, a canal: Panama.") : "Test case 3 failed";
        assert isValidPalindrome(" ") : "Test case 4 failed";
        assert isValidPalindrome(".,") : "Test case 5 failed";
    }

    /**
     * ✅ Checks if the input string is a valid palindrome, ignoring non-alphanumeric characters.
     *
     * Time Complexity: O(n) — scans each character once
     * Space Complexity: O(1) — constant space, no extra buffer
     *
     * @param str Input string
     * @return true if the cleaned version of the string is a palindrome
     */
    public static boolean isValidPalindrome(String str) {
        if (str == null) return false;

        int left = 0, right = str.length() - 1;

        while (left < right) {
            // Skip non-alphanumeric characters
            while (left < right && !Character.isLetterOrDigit(str.charAt(left))) left++;
            while (left < right && !Character.isLetterOrDigit(str.charAt(right))) right--;

            if (Character.toLowerCase(str.charAt(left)) != Character.toLowerCase(str.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }
}