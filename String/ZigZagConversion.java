package String;

/**
 * ZigZag Conversion
 * https://leetcode.com/problems/zigzag-conversion/
 * 
 * **Problem Statement:**
 * Given a string and a number of rows, write the characters in a zigzag pattern
 * and then read row-wise to return the final string.
 *
 * **Example:**
 * Input: "PAYPALISHIRING", numRows = 3
 * ZigZag Pattern:
 * P   A   H   N
 * A P L S I I G
 * Y   I   R
 * Output: "PAHNAPLSIIGYIR"
 * 
 * **Time Complexity:** O(N) - Iterates through the string once.
 * **Space Complexity:** O(N) - Stores characters in row buffers.
 */
public class ZigZagConversion {

    public static void main(String[] args) {
        String input = "PAYPALISHIRING";

        // Expected Output: PAHNAPLSIIGYIR
        System.out.println(convertZigZag(input, 3));

        // Expected Output: PINALSIGYAHRPI
        System.out.println(convertZigZag(input, 4));
    }

    /**
     * Converts the given string into a zigzag pattern based on the specified number of rows.
     *
     * @param str     The input string.
     * @param numRows The number of rows for the zigzag pattern.
     * @return The transformed string read row-wise.
     */
    public static String convertZigZag(String str, int numRows) {
        if (numRows == 1 || str.length() <= numRows) return str; // No transformation needed.

        // Create an array of StringBuilder for each row
        StringBuilder[] zigzagRows = new StringBuilder[numRows];
        for (int i = 0; i < numRows; i++) {
            zigzagRows[i] = new StringBuilder();
        }

        int row = 0; // Tracks current row index
        boolean movingDown = true; // Direction flag

        // Iterate through the characters in the input string
        for (char ch : str.toCharArray()) {
            zigzagRows[row].append(ch);

            // Change direction when reaching the top or bottom row
            if (row == 0) {
                movingDown = true;
            } else if (row == numRows - 1) {
                movingDown = false;
            }

            row += movingDown ? 1 : -1;
        }

        // Concatenate all rows to form the final string
        StringBuilder result = new StringBuilder();
        for (StringBuilder rowContent : zigzagRows) {
            result.append(rowContent);
        }

        return result.toString();
    }
}
