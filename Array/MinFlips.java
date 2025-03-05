package Array;

/**
 * A binary string is monotone increasing if it consists of some number of 0's (possibly none),
 * followed by some number of 1's (also possibly none). You can flip s[i] changing it from 0 to 1 or from 1 to 0.
 *
 * https://leetcode.com/problems/flip-string-to-monotone-increasing/
 */
public class MinFlips {

    public static void main(String[] args) {
        String input = "00110000111111";
        int flips = minFlipsMonoIncr(input);
        System.out.println("Minimum flips required: " + flips);
    }

    /**
     * Dynamic Programming Approach:
     * - If '1' appears, increase `countOnes` (it stays in place).
     * - If '0' appears, we can either flip it to '1' or flip an earlier '1' to '0'.
     * - The minimum flips required at any step is `min(countOnes, minFlips + 1)`.
     * 
     * Time Complexity: O(N), Space Complexity: O(1)
     */
    public static int minFlipsMonoIncr(String str) {
        if (str.isEmpty()) return 0;

        int minFlips = 0;
        int countOnes = 0;

        for (char ch : str.toCharArray()) {
            if (ch == '1') {
                countOnes++;
            } else { // '0' case
                minFlips++;
            }
            minFlips = Math.min(countOnes, minFlips);
        }
        return minFlips;
    }
}
