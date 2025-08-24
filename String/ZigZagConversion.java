package String;

/**
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this:
 * P   A   H   N
 * A P L S I I G
 * Y   I   R
 * 
 * And then read line by line: "PAHNAPLSIIGYIR"
 * 
 * Example 1:
 * Input: s = "PAYPALISHIRING", numRows = 3
 * Output: "PAHNAPLSIIGYIR"
 * 
 * Example 2:
 * Input: s = "PAYPALISHIRING", numRows = 4
 * Output: "PINALSIGYAHRPI"
 * Explanation:
 * P     I    N
 * A   L S  I G
 * Y A   H R
 * P     I
 * 
 * LeetCode: https://leetcode.com/problems/zigzag-conversion/
 * 
 * Follow-up Questions:
 * 1. How would you modify your solution to print the zigzag pattern?
 *    - We can build a 2D array to represent the pattern and then print it.
 * 2. What if we need to convert from the zigzag format back to the original string?
 *    - We can reverse the process by calculating the original positions.
 * 3. How would you handle very large strings efficiently?
 *    - We can optimize by processing the string in chunks or using mathematical patterns.
 * 
 * Related Problems:
 * - Text Justification (https://leetcode.com/problems/text-justification/)
 * - Encode and Decode Strings (https://leetcode.com/problems/encode-and-decode-strings/)
 */
public class ZigZagConversion {
    
    /**
     * Converts the given string into a zigzag pattern with the specified number of rows.
     * 
     * @param s The input string
     * @param numRows The number of rows in the zigzag pattern
     * @return The converted string read line by line
     */
    public String convert(String s, int numRows) {
        if (numRows == 1 || s.length() <= numRows) {
            return s;
        }
        
        // Create an array of StringBuilders, one for each row
        StringBuilder[] rows = new StringBuilder[numRows];
        for (int i = 0; i < numRows; i++) {
            rows[i] = new StringBuilder();
        }
        
        int currentRow = 0;
        boolean goingDown = false;
        
        // Distribute characters to the appropriate row
        for (char c : s.toCharArray()) {
            rows[currentRow].append(c);
            
            // Change direction if we've reached the top or bottom row
            if (currentRow == 0 || currentRow == numRows - 1) {
                goingDown = !goingDown;
            }
            
            // Move to the next row in the current direction
            currentRow += goingDown ? 1 : -1;
        }
        
        // Combine all rows to get the result
        StringBuilder result = new StringBuilder();
        for (StringBuilder row : rows) {
            result.append(row);
        }
        
        return result.toString();
    }
    
    /**
     * Alternative approach using mathematical patterns.
     * This approach calculates the position of each character in the original string
     * and places it directly in the result string.
     */
    public String convertMath(String s, int numRows) {
        if (numRows == 1 || s.length() <= numRows) {
            return s;
        }
        
        StringBuilder result = new StringBuilder();
        int cycleLen = 2 * numRows - 2;
        int n = s.length();
        
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j + i < n; j += cycleLen) {
                // Add the main column character
                result.append(s.charAt(j + i));
                
                // Add the diagonal character (if not first or last row)
                if (i != 0 && i != numRows - 1 && j + cycleLen - i < n) {
                    result.append(s.charAt(j + cycleLen - i));
                }
            }
        }
        
        return result.toString();
    }
    
    /**
     * Prints the zigzag pattern for visualization.
     * 
     * @param s The input string
     * @param numRows The number of rows in the zigzag pattern
     */
    public void printZigZag(String s, int numRows) {
        if (numRows == 1) {
            System.out.println(s);
            return;
        }
        
        // Initialize a 2D grid to represent the pattern
        char[][] grid = new char[numRows][s.length()];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < s.length(); j++) {
                grid[i][j] = ' ';
            }
        }
        
        int row = 0, col = 0;
        boolean goingDown = true;
        
        // Fill the grid with characters in zigzag pattern
        for (char c : s.toCharArray()) {
            grid[row][col] = c;
            
            if (goingDown) {
                if (row == numRows - 1) {
                    row--;
                    col++;
                    goingDown = false;
                } else {
                    row++;
                }
            } else {
                if (row == 0) {
                    row++;
                    goingDown = true;
                } else {
                    row--;
                    col++;
                }
            }
        }
        
        // Print the grid
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < s.length(); j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
    
    /**
     * Converts a string from zigzag format back to the original string.
     * This is the inverse of the convert method.
     * 
     * @param s The zigzag converted string
     * @param numRows The number of rows used in the original conversion
     * @return The original string
     */
    public String zigzagToOriginal(String s, int numRows) {
        if (numRows == 1) {
            return s;
        }
        
        char[] result = new char[s.length()];
        int index = 0;
        int cycleLen = 2 * numRows - 2;
        
        // Fill the result array by reading in the original order
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j + i < s.length(); j += cycleLen) {
                // Main column character
                result[index++] = s.charAt(j + i);
                
                // Diagonal character (if not first or last row)
                if (i != 0 && i != numRows - 1 && j + cycleLen - i < s.length()) {
                    result[index++] = s.charAt(j + cycleLen - i);
                }
            }
        }
        
        return new String(result);
    }
    
    /**
     * Solution for "Text Justification" (LeetCode #68).
     * This is a related problem that involves formatting text in a specific pattern.
     */
    public java.util.List<String> fullJustify(String[] words, int maxWidth) {
        java.util.List<String> result = new java.util.ArrayList<>();
        int index = 0;
        
        while (index < words.length) {
            int count = words[index].length();
            int last = index + 1;
            
            // Find the last word that can fit in the current line
            while (last < words.length) {
                if (words[last].length() + count + 1 > maxWidth) break;
                count += words[last].length() + 1;
                last++;
            }
            
            StringBuilder builder = new StringBuilder();
            int diff = last - index - 1;
            
            // If last line or only one word in the line, left-justify
            if (last == words.length || diff == 0) {
                for (int i = index; i < last; i++) {
                    builder.append(words[i] + " ");
                }
                builder.deleteCharAt(builder.length() - 1);
                
                // Add spaces to the end if needed
                while (builder.length() < maxWidth) {
                    builder.append(" ");
                }
            } else {
                // Calculate spaces
                int spaces = (maxWidth - count) / diff;
                int extra = (maxWidth - count) % diff;
                
                for (int i = index; i < last; i++) {
                    builder.append(words[i]);
                    
                    if (i < last - 1) {
                        int spacesToAdd = spaces + (i - index < extra ? 1 : 0);
                        for (int j = 0; j <= spacesToAdd; j++) {
                            builder.append(" ");
                        }
                    }
                }
            }
            
            result.add(builder.toString());
            index = last;
        }
        
        return result;
    }
}
