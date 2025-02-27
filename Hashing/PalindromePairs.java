package Hashing;

/**
 * LeetCode: https://leetcode.com/problems/reconstruct-original-digits-from-english/
 *
 * Given a string containing an unordered English representation of digits 0-9,
 * return the digits in ascending order.
 *
 * Approach:
 * - Count the frequency of each character in the input string.
 * - Use unique identifiers for specific digits (e.g., 'z' uniquely identifies "zero").
 * - Deduct known digits to determine remaining ones.
 * - Construct the output by appending digits in order based on frequency.
 *
 * Time Complexity: O(N) - We iterate over the input string and process a fixed set of characters.
 * Space Complexity: O(1) - Uses a constant-size frequency array and output storage.
 */
public class OriginalDigits {

    public static void main(String[] args) {
        String result = new OriginalDigits().originalDigits("owoztneoer");
        System.out.println(result); // Expected output: "012"
    }

    public String originalDigits(String s) {
        int[] charCount = new int[26]; // Frequency array for letters 'a' to 'z'
        for (char ch : s.toCharArray()) {
            charCount[ch - 'a']++;
        }

        int[] digitCount = new int[10]; // Stores frequency of digits 0-9
        
        // Identify unique digits using distinct characters
        digitCount[0] = charCount['z' - 'a']; // 'z' is unique to "zero"
        digitCount[2] = charCount['w' - 'a']; // 'w' is unique to "two"
        digitCount[4] = charCount['u' - 'a']; // 'u' is unique to "four"
        digitCount[6] = charCount['x' - 'a']; // 'x' is unique to "six"
        digitCount[8] = charCount['g' - 'a']; // 'g' is unique to "eight"

        // Identify remaining digits by subtracting known occurrences
        digitCount[3] = charCount['h' - 'a'] - digitCount[8]; // "three" shares 'h' with "eight"
        digitCount[5] = charCount['f' - 'a'] - digitCount[4]; // "five" shares 'f' with "four"
        digitCount[7] = charCount['s' - 'a'] - digitCount[6]; // "seven" shares 's' with "six"
        digitCount[9] = charCount['i' - 'a'] - digitCount[5] - digitCount[6] - digitCount[8]; // "nine" shares 'i'
        digitCount[1] = charCount['n' - 'a'] - digitCount[7] - 2 * digitCount[9]; // "one" shares 'n' with "nine" and "seven"

        // Construct the sorted digit string
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < digitCount[i]; j++) {
                result.append(i);
            }
        }
        return result.toString();
    }
}
