package DailyBytes.StringPackage;

/**
 * This class contains a method to determine if a given string can form a palindrome by deleting at most one character.
 * 
 * Algorithm:
 * - Use two pointers to check for mismatches.
 * - If a mismatch is found, check the substrings formed by skipping either of the mismatched characters.
 * - Time Complexity: O(n)
 * - Space Complexity: O(n) due to the substring operations.
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/valid-palindrome-ii/
 */
public class ValidPalindrome2 {

    public static void main(String[] args) {
        assert isPalindrome2("foobof") : "Incorrect";
        assert isPalindrome2("abcba") : "Incorrect";
        assert !isPalindrome2("abccab") : "Incorrect";
    }

    /**
     * Determines if a given string can form a palindrome by deleting at most one character.
     * @param str The input string.
     * @return True if the string can form a palindrome by deleting at most one character, false otherwise.
     */
    public static boolean isPalindrome2(String str) {
        if (str == null || str.length() < 2) {
            return true;
        }

        int left = 0, right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return isPalindrome(str.substring(left, right)) || isPalindrome(str.substring(left + 1, right + 1));
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * Determines if a given string is a palindrome.
     * @param str The input string.
     * @return True if the string is a palindrome, false otherwise.
     */
    public static boolean isPalindrome(String str) {
        if (str == null || str.length() < 2) {
            return true;
        }

        int left = 0, right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left++) != str.charAt(right--)) {
                return false;
            }
        }
        return true;
    }
}
