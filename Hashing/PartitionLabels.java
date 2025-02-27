package Hashing;

/**
 * LeetCode: https://leetcode.com/problems/reconstruct-original-digits-from-english/
 *
 * Given a string containing an unordered English representation of digits 0-9,
 * return the digits in ascending order.
 *
 * Approach:
 * - Count the frequency of each character in the input string.
 * - Identify digits using unique letters (e.g., 'z' uniquely identifies "zero").
 * - Deduct known digits to determine the remaining ones.
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
        int[] letterFrequency = new int[26]; // Frequency array for letters 'a' to 'z'
        for (char ch : s.toCharArray()) {
            letterFrequency[ch - 'a']++;
        }

        int[] digitFrequency = new int[10]; // Stores frequency of digits 0-9

        // Identify unique digits using distinct characters
        digitFrequency[0] = letterFrequency['z' - 'a']; // 'z' is unique to "zero"
        digitFrequency[2] = letterFrequency['w' - 'a']; // 'w' is unique to "two"
        digitFrequency[4] = letterFrequency['u' - 'a']; // 'u' is unique to "four"
        digitFrequency[6] = letterFrequency['x' - 'a']; // 'x' is unique to "six"
        digitFrequency[8] = letterFrequency['g' - 'a']; // 'g' is unique to "eight"

        // Identify remaining digits by subtracting known occurrences
        digitFrequency[3] = letterFrequency['h' - 'a'] - digitFrequency[8]; // "three" shares 'h' with "eight"
        digitFrequency[5] = letterFrequency['f' - 'a'] - digitFrequency[4]; // "five" shares 'f' with "four"
        digitFrequency[7] = letterFrequency['s' - 'a'] - digitFrequency[6]; // "seven" shares 's' with "six"
        digitFrequency[9] = letterFrequency['i' - 'a'] - digitFrequency[5] - digitFrequency[6] - digitFrequency[8]; // "nine" shares 'i'
        digitFrequency[1] = letterFrequency['n' - 'a'] - digitFrequency[7] - 2 * digitFrequency[9]; // "one" shares 'n' with "nine" and "seven"

        // Construct the sorted digit string
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            result.append(String.valueOf(i).repeat(digitFrequency[i]));
        }
        return result.toString();
    }
}
