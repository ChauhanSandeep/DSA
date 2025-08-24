package strings;

/**
 * LeetCode 925. Long Pressed Name
 *
 * Your friend is typing his name into a keyboard. Sometimes, when typing a character c,
 * the key might get long pressed, and the character will be typed 1 or more times.
 *
 * Example:
 * Input: name = "alex", typed = "aaleex"
 * Output: true
 * Explanation: 'a' and 'e' in 'alex' were long pressed.
 *
 * LeetCode Link: https://leetcode.com/problems/long-pressed-name/
 */
public class LongPressedName {

    /**
     * Checks if typed string could be result of long pressing name.
     *
     * Algorithm:
     * 1. Use two pointers to compare characters in both strings
     * 2. For each character, count occurrences in both strings
     * 3. typed must have at least as many occurrences as name
     * 4. Both pointers must reach end simultaneously
     *
     * Time Complexity: O(n + m) where n, m are lengths of name, typed
     * Space Complexity: O(1) - only uses constant extra space
     */
    public boolean isLongPressedName(String name, String typed) {
        int i = 0, j = 0;

        while (i < name.length() && j < typed.length()) {
            if (name.charAt(i) != typed.charAt(j)) {
                return false;
            }

            char currentChar = name.charAt(i);
            int nameCount = 0, typedCount = 0;

            // Count consecutive characters in name
            while (i < name.length() && name.charAt(i) == currentChar) {
                nameCount++;
                i++;
            }

            // Count consecutive characters in typed
            while (j < typed.length() && typed.charAt(j) == currentChar) {
                typedCount++;
                j++;
            }

            // typed must have at least as many as name
            if (typedCount < nameCount) {
                return false;
            }
        }

        return i == name.length() && j == typed.length();
    }
}
