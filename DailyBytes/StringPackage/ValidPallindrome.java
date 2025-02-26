package DailyBytes.StringPackage;

/**
 * This class contains a method to determine if a given string is a valid palindrome, ignoring non-alphabet characters.
 * 
 * Algorithm:
 * - Convert the string to lowercase.
 * - Use a StringBuilder to collect only the alphabetic characters.
 * - Check if the resulting string is a palindrome.
 * - Time Complexity: O(n)
 * - Space Complexity: O(n)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/valid-palindrome/
 */
public class ValidPalindrome {
    public static void main(String[] args) {
        assert isValidPalindrome("level") : "Incorrect";
        assert !isValidPalindrome("algorithm") : "Incorrect";
        assert isValidPalindrome("A man, a plan, a canal: Panama.") : "Incorrect";
    }

    /**
     * Determines if the input string is a valid palindrome, ignoring non-alphabet characters.
     * @param str The input string.
     * @return True if the string is a valid palindrome, false otherwise.
     */
    public static boolean isValidPalindrome(String str) {
        if (str == null || str.length() < 2) {
            return true;
        }

        str = str.toLowerCase();
        StringBuilder filteredStr = new StringBuilder();

        // Collect only alphabetic characters
        for (char c : str.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                filteredStr.append(c);
            }
        }

        str = filteredStr.toString();
        int left = 0;
        int right = str.length() - 1;

        // Check if the filtered string is a palindrome
        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
