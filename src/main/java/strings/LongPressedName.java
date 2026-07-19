package strings;

/**
 * Problem: Long Pressed Name
 *
 * Decide whether typed could be produced from name when any key may be long
 * pressed, causing extra copies of the same character.
 *
 * Leetcode: https://leetcode.com/problems/long-pressed-name/ (Easy)
 * Rating:   zerotrac 1271 (Q1, weekly-107)
 * Pattern:  String | Two pointers | Run validation
 *
 * Example:
 *   Input:  name = "alex", typed = "aaleex"
 *   Output: true
 *   Why:    the extra 'a' and 'e' can be explained as long presses.
 *
 * Follow-ups:
 *   1. Run-length approach? Compare character runs and require typed counts >= name counts.
 *   2. Unicode support? Iterate over code points and compare runs.
 *   3. Failure location? Return the first typed index that cannot be matched.
 */
public class LongPressedName {

    public static void main(String[] args) {
        LongPressedName solver = new LongPressedName();
        String[] names = {"alex", "saeed", "leelee"};
        String[] typed = {"aaleex", "ssaaedd", "lleeelee"};
        boolean[] expected = {true, false, true};
        for (int i = 0; i < names.length; i++) {
            boolean got = solver.isLongPressedName(names[i], typed[i]);
            System.out.printf("name=%s typed=%s -> %s  expected=%s%n", names[i], typed[i], got, expected[i]);
        }
    }


        /**
     * Intuition: typed can either match the next needed character in name or be
     * an extra copy of the previously matched name character. Two pointers track
     * those two possibilities directly.
     *
     * Algorithm:
     *   1. Walk name and typed with separate indices.
     *   2. Advance both indices on an exact match.
     *   3. Otherwise require typed to repeat the previous name character.
     *   4. Verify any leftover typed characters repeat the final name character.
     *
     * Time:  O(n + m) - both strings are scanned once.
     * Space: O(1) - only indices and characters are stored.
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
