package strings;

/**
 * LeetCode 925. Long Pressed Name
 *
 * Your friend is typing his name into a keyboard. Sometimes, when typing a character c,
 * the key might get long pressed, and the character will be typed 1 or more times.
 * Find out if the typed string could be a result of the name being typed with some
 *
 * Example:
 * Input: name = "alex", typed = "aaleex"
 * Output: true
 * Explanation: 'a' and 'e' in 'alex' were long pressed.
 *
 * LeetCode Link: https://leetcode.com/problems/long-pressed-name/
 * LeetCode Contest Rating: 1271
 */
public class LongPressedName {

    /**
     * Checks if typed string could be result of long pressing name.
     *
     * Algorithm:
     * 1. Use two pointers to traverse name and typed.
     * 2. If characters match, move both pointers.
     * 3. If they don't match, check if typed char matches previous name char (
     * long press). If not, return false.
     * 4. After traversing name, ensure any remaining typed chars match last name char
     * (long press).
     *
     * Time Complexity: O(n + m) where n, m are lengths of name, typed
     * Space Complexity: O(1) - only uses constant extra space
     */
    public boolean isLongPressedName(String name, String typed) {
        int nameIndex = 0;
        int typedIndex = 0;

        int nameLength = name.length();
        int typedLength = typed.length();

        while (nameIndex < nameLength && typedIndex < typedLength) {
            char nameChar = name.charAt(nameIndex);
            char typedChar = typed.charAt(typedIndex);

            if (nameChar == typedChar) {
                // Characters match: advance both pointers
                nameIndex++;
                typedIndex++;
            } else {
                // Mismatch: typed char must be a repeat of the previous name char
                if (nameIndex == 0 || typedChar != name.charAt(nameIndex - 1)) {
                    return false;
                }
                // Skip repeated typed character
                typedIndex++;
            }
        }

        // If we didn’t finish consuming name, typed is too short
        if (nameIndex < nameLength) {
            return false;
        }

        // Any remaining characters in typed must equal the last char of name
        char lastNameChar = name.charAt(nameLength - 1);
        while (typedIndex < typedLength) {
            if (typed.charAt(typedIndex) != lastNameChar) {
                return false;
            }
            typedIndex++;
        }

        return true;
    }
}
