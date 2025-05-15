package String;

/**
 * Checks if one string can be obtained by rotating another string exactly two places.
 * 
 * Problem Link: 
 * https://practice.geeksforgeeks.org/problems/check-if-string-is-rotated-by-two-places-1587115620/1
 * 
 * Approach:
 * - Rotate the original string by 2 places in both directions (clockwise & anticlockwise).
 * - Check if either of the rotated strings matches the target string.
 * - Edge cases:
 *   - If strings are null or their lengths are less than 2, return false.
 *   - If strings are of different lengths, return false immediately.
 * 
 * Time Complexity: O(N) - String comparison and substring operations.
 * Space Complexity: O(N) - Due to string concatenation.
 */
public class RotatedString {
    public static void main(String[] args) {
        System.out.println(isRotationByTwo("amazon", "azonam"));  // true
        System.out.println(isRotationByTwo("geeksforgeeks", "geeksgeeksfor")); // false
    }

    /**
     * Checks if str2 is a rotation of str1 by exactly two places.
     * 
     * @param str1 The original string.
     * @param str2 The string to be checked.
     * @return true if str2 is a rotation of str1 by 2 places, otherwise false.
     */
    private static boolean isRotationByTwo(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() != str2.length()) {
            return false;
        }

        // If the string length is less than 2, rotation doesn't make sense
        if (str1.length() < 2) {
            return str1.equals(str2);
        }

        // Generate clockwise and anticlockwise rotated versions
        String clockwiseRotation = str1.substring(2) + str1.substring(0, 2);
        String anticlockwiseRotation = str1.substring(str1.length() - 2) + str1.substring(0, str1.length() - 2);

        return str2.equals(clockwiseRotation) || str2.equals(anticlockwiseRotation);
    }
}
