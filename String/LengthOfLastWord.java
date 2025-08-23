package String;

/**
 * LeetCode 58. Length of Last Word
 *
 * Given a string s consisting of words and spaces, return the length of the last word in the string.
 * A word is a maximal substring consisting of non-space characters only.
 *
 * Example 1:
 * Input: s = "Hello World"
 * Output: 5
 * Explanation: The last word is "World" with length 5.
 *
 * LeetCode Link: https://leetcode.com/problems/length-of-last-word/
 */
public class LengthOfLastWord {

    /**
     * Finds length of last word by iterating from end of string.
     *
     * Algorithm:
     * 1. Start from end of string and skip trailing spaces
     * 2. Count characters until we hit a space or reach beginning
     * 3. Return the count of non-space characters found
     *
     * Time Complexity: O(n) in worst case, O(k) average where k is last word length
     * Space Complexity: O(1) - only uses constant extra space
     */
    public int lengthOfLastWord(String s) {
        int end = s.length() - 1;

        // Skip trailing spaces
        while (end >= 0 && s.charAt(end) == ' ') {
            end--;
        }

        // Count characters of last word
        int length = 0;
        while (end >= 0 && s.charAt(end) != ' ') {
            length++;
            end--;
        }

        return length;
    }

    /**
     * Alternative approach using string trim and lastIndexOf.
     */
    public int lengthOfLastWordAlternative(String s) {
        s = s.trim();
        return s.length() - s.lastIndexOf(' ') - 1;
    }
}
