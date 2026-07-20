package strings;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: ZigZag Conversion
 *
 * Convert a string into a zigzag pattern for a row count and read rows from top
 * to bottom. This file includes row-builder and direct-position variants.
 *
 * Leetcode: https://leetcode.com/problems/zigzag-conversion/ (Medium)
 * Rating:   acceptance 54.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  String | Row builders | Mathematical cycle
 *
 * Example:
 *   Input:  inputString = "PAYPALISHIRING", numRows = 4
 *   Output: "PINALSIGYAHRPI"
 *   Why:    the 4-row zigzag is read one complete row at a time.
 *
 * Follow-ups:
 *   1. Very large strings? Use the direct-position formula.
 *   2. Convert back? Split by row lengths, then replay the row path.
 *   3. Custom pattern? Replace boundary flips with configurable row deltas.
 *
 * Related: Text Justification (68).
 */
public class ZigZagStringConverter {

  public static void main(String[] args) {
    ZigZagStringConverter converter = new ZigZagStringConverter();
    String[] inputs = {"PAYPALISHIRING", "PAYPALISHIRING", "PAYPALISHIRING", "A", ""};
    int[] rows = {3, 4, 1, 3, 3};
    String[] expected = {"PAHNAPLSIIGYIR", "PINALSIGYAHRPI", "PAYPALISHIRING", "A", ""};
    for (int i = 0; i < inputs.length; i++) {
      String got = converter.convertToZigZag(inputs[i], rows[i]);
      System.out.printf("input=%s rows=%d -> %s  expected=%s%n", inputs[i], rows[i], got, expected[i]);
    }
  }


    /**
   * Intuition: follow the visual zigzag path through row indices. Append each
   * character to its current row, flip direction at the first and last rows, and
   * read all row builders in order at the end.
   *
   * Algorithm:
   *   1. Return input for null, empty, or one-row cases.
   *   2. Create builders for rows that can receive characters.
   *   3. Append each character and update direction at boundaries.
   *   4. Concatenate the row builders.
   *
   * Time:  O(n) - every character is processed once.
   * Space: O(n) - row builders store the output.
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
    int direction = -1;

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

    /** Returns true for rows that have diagonal characters in each zigzag cycle. */
  private boolean isMiddleRow(int row, int numRows) {
    return row != 0 && row != numRows - 1;
  }
}
