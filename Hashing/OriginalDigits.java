package Hashing;

/**
 * https://leetcode.com/problems/reconstruct-original-digits-from-english/
 *
 * Given a string s containing an out-of-order English representation of digits 0-9, return the digits in ascending order.
 * complete string should be exhausted while creating the numbers
 */
public class OriginalDigits {

    public static void main(String[] args) {
        String result = new OriginalDigits().originalDigits("owoztneoer");
        System.out.println(result);
    }

    public String originalDigits(String str) {
        char[] charFreq = new char[26 + (int)'a'];
        for(char letter: str.toCharArray()) {
            charFreq[letter]++;
        }

        // building hashmap digit -> its frequency
        int[] numFreq = new int[10];
        // letter "z" is present only in "zero"
        numFreq[0] = charFreq['z'];
        // letter "w" is present only in "two"
        numFreq[2] = charFreq['w'];
        // letter "u" is present only in "four"
        numFreq[4] = charFreq['u'];
        // letter "x" is present only in "six"
        numFreq[6] = charFreq['x'];
        // letter "g" is present only in "eight"
        numFreq[8] = charFreq['g'];
        // letter "h" is present only in "three" and "eight"
        numFreq[3] = charFreq['h'] - numFreq[8];
        // letter "f" is present only in "five" and "four"
        numFreq[5] = charFreq['f'] - numFreq[4];
        // letter "s" is present only in "seven" and "six"
        numFreq[7] = charFreq['s'] - numFreq[6];
        // letter "i" is present in "nine", "five", "six", and "eight"
        numFreq[9] = charFreq['i'] - numFreq[5] - numFreq[6] - numFreq[8];
        // letter "n" is present in "one", "nine", and "seven"
        numFreq[1] = charFreq['n'] - numFreq[7] - 2 * numFreq[9];

        // building output string
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < numFreq[i]; j++) {
                builder.append(i);
            }
        }

        return builder.toString();
    }
}
