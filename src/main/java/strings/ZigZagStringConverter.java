package strings;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: ZigZag Conversion
 *
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this:
 * (you may want to display this pattern in a fixed font for better legibility)
 *
 * P   A   H   N
 * A P L S I I G
 * Y   I   R
 *
 * And then read line by line: "PAHNAPLSIIGYIR"
 *
 * Write the code that will take a string and make this conversion given a number of rows.
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
 * Example 3:
 * Input: s = "A", numRows = 1
 * Output: "A"
 *
 * LeetCode: https://leetcode.com/problems/zigzag-conversion/
 *
 * Follow-up Questions (FAANG-style):
 * 1. What if we need to handle very large strings that don't fit in memory?
 *    - Use streaming approach, process characters on-the-fly without storing intermediate rows.
 * 2. How would you optimize for space if numRows is very large?
 *    - Use mathematical formula to directly calculate character positions instead of row arrays.
 * 3. What if we need to convert back from zigzag to original string?
 *    - Store original indices during zigzag creation, then use inverse mapping.
 * 4. How to handle Unicode characters or multi-byte characters?
 *    - Process as char array or use proper Unicode-aware string handling.
 * 5. What if we need to support different zigzag patterns (diagonal, spiral)?
 *    - Abstract the direction pattern into configurable movement vectors.
 * 6. How to optimize for multiple conversions with same numRows?
 *    - Precompute the cyclic pattern and reuse for different strings.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ZigZagStringConverter {

  public static void main(String[] args) {
    ZigZagStringConverter converter = new ZigZagStringConverter();

    String testString = "PAYPALISHIRING";

    // Test basic cases
    System.out.println("ZigZag (3 rows): " + converter.convertToZigZag(testString, 3)); // Expected: PAHNAPLSIIGYIR
    System.out.println("ZigZag (4 rows): " + converter.convertToZigZag(testString, 4)); // Expected: PINALSIGYAHRPI
    System.out.println("ZigZag (1 row): " + converter.convertToZigZag(testString, 1));  // Expected: PAYPALISHIRING

    // Test edge cases
    System.out.println("ZigZag (single char): " + converter.convertToZigZag("A", 3));    // Expected: A
    System.out.println("ZigZag (empty): " + converter.convertToZigZag("", 3));           // Expected: ""

    // Test optimized version
    System.out.println("Optimized ZigZag: " + converter.convertToZigZagOptimized(testString, 3));
  }

  /**
   * Converts string into zigzag pattern and reads row-wise.
   *
   * Algorithm: Row-by-Row Construction with Direction Tracking
   * Steps:
   * 1. Create array of StringBuilder for each row
   * 2. Traverse string character by character
   * 3. Place each character in current row
   * 4. Update row index based on zigzag direction (down/up)
   * 5. Change direction at boundaries (first/last row)
   * 6. Concatenate all rows to form result
   *
   * Pattern Analysis:
   * - Characters move vertically down until last row
   * - Then move diagonally up until first row
   * - This creates a cyclic pattern of length 2*numRows - 2
   *
   * Time Complexity: O(n) where n is string length
   * Space Complexity: O(n) for storing characters in row arrays
   *
   * @param inputString the string to convert
   * @param numRows number of rows in zigzag pattern
   * @return converted string read row-wise
   */
  public String convertToZigZag(String inputString, int numRows) {
    if (inputString == null || inputString.isEmpty() || numRows == 1) {
      return inputString;
    }

    List<StringBuilder> builderList = new ArrayList<>();
    for (int i = 0; i < Math.min(numRows, inputString.length()); i++) {
      builderList.add(new StringBuilder());
    }

    int currentRow = 0;
    int direction = 1;

    for (char character : inputString.toCharArray()) {
      builderList.get(currentRow).append(character);

      if (currentRow == 0 || currentRow == numRows - 1) {
        direction = -direction;
      }

      currentRow += direction;
    }

    StringBuilder result = new StringBuilder();
    for (StringBuilder row : builderList) {
      result.append(row);
    }

    return result.toString();
  }

  /**
   * Space-optimized approach using mathematical formula (for very large strings).
   *
   * Algorithm: Direct Position Calculation
   * Steps:
   * 1. For each row, calculate which original string positions belong to it
   * 2. Use mathematical formula to find positions without storing intermediate data
   * 3. Extract characters directly from original string
   *
   * Mathematical Pattern:
   * - Row 0: positions 0, 2n-2, 4n-4, 6n-6, ...
   * - Row i (middle): positions i, 2n-2-i, 2n-2+i, 4n-4-i, 4n-4+i, ...
   * - Row n-1: positions n-1, 3n-3, 5n-5, 7n-7, ...
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1) auxiliary space (excluding result)
   *
   * @param inputString the string to convert
   * @param numRows number of rows in zigzag pattern
   * @return converted string using mathematical approach
   */
  public String convertToZigZagOptimized(String inputString, int numRows) {
    if (inputString == null || inputString.isEmpty()) {
      return "";
    }
    if (numRows == 1) {
      return inputString;
    }

    StringBuilder result = new StringBuilder();
    int stringLength = inputString.length();
    int cycleLength = 2 * numRows - 2; // Complete zigzag cycle length

    for (int row = 0; row < numRows; row++) {
      for (int startPosition = row; startPosition < stringLength; startPosition += cycleLength) {
        // Add character from current cycle position
        result.append(inputString.charAt(startPosition));

        // For middle rows, add the diagonal character
        if (isMiddleRow(row, numRows)) {
          int diagonalPosition = startPosition + cycleLength - 2 * row;
          if (diagonalPosition < stringLength) {
            result.append(inputString.charAt(diagonalPosition));
          }
        }
      }
    }

    return result.toString();
  }

  /**
   * Checks if the row is a middle row (not first or last).
   *
   * @param row current row index
   * @param numRows total number of rows
   * @return true if middle row, false otherwise
   */
  private boolean isMiddleRow(int row, int numRows) {
    return row != 0 && row != numRows - 1;
  }
}
