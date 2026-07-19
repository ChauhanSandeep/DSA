package hashing;

/**
 * Problem: Reconstruct Original Digits from English
 *
 * Given a scrambled string made by concatenating English digit words, rebuild
 * the original digits in ascending numeric order. The input may contain many
 * copies of the same digit word mixed together.
 *
 * Leetcode: https://leetcode.com/problems/reconstruct-original-digits-from-english/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Hashing | Character counts | Unique-letter elimination
 *
 * Example:
 *   Input:  "owoztneoer"
 *   Output: "012"
 *   Why:    the letters can be rearranged into "zero", "one", and "two".
 *
 * Follow-ups:
 *   1. How would you validate malformed inputs?
 *      After deriving digit counts, subtract all chosen words and ensure no letter count is negative or leftover.
 *   2. How would you support another language?
 *      Build an elimination order from letters that uniquely identify remaining words.
 *   3. What if output order should match the original spoken order?
 *      The counts alone are insufficient; positional information would be required.
 *   4. How would you handle uppercase or mixed-case input?
 *      Normalize characters before counting, or keep separate buckets for each case.
 *
 * Related: Find Anagrams (438), Sort Characters By Frequency (451).
 */
public class OriginalDigits {

    public static void main(String[] args) {
        OriginalDigits solver = new OriginalDigits();
        String[] inputs = { "owoztneoer", "fviefuro" };
        String[] expected = { "012", "45" };

        for (int i = 0; i < inputs.length; i++) {
            String got = solver.originalDigits(inputs[i]);
            System.out.printf("s=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: some digit words contain letters no other digit word has, such
     * as z for zero and w for two. Count those unique letters first, then use the
     * known digits to subtract overlaps for the remaining words.
     *
     * Algorithm:
     *   1. Count every lowercase letter in the scrambled string.
     *   2. Determine digits with unique markers, then derive the overlapping digits.
     *   3. Append each digit count times from 0 through 9.
     *
     * Time:  O(n) - counting scans the input once and output construction writes each digit once.
     * Space: O(1) - the letter and digit arrays have fixed sizes.
     *
     * @param str scrambled concatenation of English digit words
     * @return reconstructed digits in ascending order
     */
    public String originalDigits(String str) {
        int[] charCount = new int[26]; // Frequency array for letters 'a' to 'z'

        // Count the occurrences of each letter in the input string
        for (char letter : str.toCharArray()) {
            charCount[letter - 'a']++;
        }

        int[] digitCount = new int[10]; // Stores the count of each digit (0-9)

        // Identifying digits using unique characters
        digitCount[0] = charCount['z' - 'a']; // 'z' appears only in "zero"
        digitCount[2] = charCount['w' - 'a']; // 'w' appears only in "two"
        digitCount[4] = charCount['u' - 'a']; // 'u' appears only in "four"
        digitCount[6] = charCount['x' - 'a']; // 'x' appears only in "six"
        digitCount[8] = charCount['g' - 'a']; // 'g' appears only in "eight"

        // Identifying remaining digits based on dependent characters
        digitCount[3] = charCount['h' - 'a'] - digitCount[8]; // 'h' appears in "three" and "eight"
        digitCount[5] = charCount['f' - 'a'] - digitCount[4]; // 'f' appears in "five" and "four"
        digitCount[7] = charCount['s' - 'a'] - digitCount[6]; // 's' appears in "seven" and "six"
        digitCount[9] = charCount['i' - 'a'] - digitCount[5] - digitCount[6] - digitCount[8]; // 'i' in "nine", "five", "six", "eight"
        digitCount[1] = charCount['n' - 'a'] - digitCount[7] - 2 * digitCount[9]; // 'n' in "one", "nine", "seven"

        // Build the result string in ascending order of digits
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < digitCount[i]; j++) {
                result.append(i);
            }
        }

        return result.toString();
    }
}
