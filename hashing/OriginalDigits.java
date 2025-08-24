package hashing;

/**
 * LeetCode Problem: https://leetcode.com/problems/reconstruct-original-digits-from-english/
 *
 * Given a string `s` containing an out-of-order English representation of digits 0-9,
 * this function reconstructs the original digits in ascending order.
 *
 * Example Input: "owoztneoer"
 * Output: "012"
 * Explanation: The string contains the words for "zero", "one", and "two" in a jumbled form.
 */
public class OriginalDigits {

    public static void main(String[] args) {
        String result = new OriginalDigits().originalDigits("owoztneoer");
        System.out.println(result); // Expected output: "012"
    }

    /**
     * Reconstructs the original digits from a shuffled English representation.
     *
     * Approach:
     * - Identify unique characters that appear in only one digit.
     * - Use a frequency map to determine counts of each digit.
     * - Construct the final sorted number sequence.
     *
     * Time Complexity: O(N), where N is the length of the input string.
     * Space Complexity: O(1), since we use a fixed array of size 26.
     * @param str Input string containing scrambled digit words.
     * @return A string containing digits in ascending order.
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
