package strings;

/**
 * Problem: Length of Last Word
 *
 * Given a string of words and spaces, return the length of the last word. A word
 * is a maximal run of non-space characters.
 *
 * Leetcode: https://leetcode.com/problems/length-of-last-word/ (Easy)
 * Rating:   acceptance 59.3% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  String | Reverse scan | Ignore trailing spaces
 *
 * Example:
 *   Input:  s = " fly me to the moon "
 *   Output: 4
 *   Why:    trailing spaces are skipped and the last word is "moon".
 *
 * Follow-ups:
 *   1. Unicode whitespace? Use Character.isWhitespace.
 *   2. Return the word? Save the end index and return the substring.
 *   3. Stream input? Track current word length and last completed word length.
 */
public class LengthOfLastWord {

    public static void main(String[] args) {
        LengthOfLastWord solver = new LengthOfLastWord();
        String[] inputs = {"Hello World", " fly me to the moon ", "a"};
        int[] expected = {5, 4, 1};
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.lengthOfLastWord(inputs[i]);
            System.out.printf("s=\"%s\" -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


        /**
     * Intuition: only the suffix matters. Walk backward over trailing spaces,
     * then count the final run of non-space characters.
     *
     * Algorithm:
     *   1. Start at the last index.
     *   2. Skip trailing spaces.
     *   3. Count characters until a space or the beginning is reached.
     *   4. Return the count.
     *
     * Time:  O(n) - the scan may cross the string once.
     * Space: O(1) - only counters are used.
     */
    public int lengthOfLastWord(String s) {
        int end = s.length() - 1;

        // Skip trailing spaces to reach last word's end
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
