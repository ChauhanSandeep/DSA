public class Testing {
  public static void main(String[] args) {
    testStringManipulation();
  }

  /**
   * Test method for demonstrating decimal formatting.
   */
  public static void testStringManipulation() {
    final int insertAt = 2; // Position where '(' should be inserted
    final String decimalNumber = "0.123456";

    // Format the string and print the result
    String formattedNumber = formatDecimalWithParentheses(decimalNumber, insertAt);
    System.out.println(formattedNumber);
  }

  /**
   * Inserts an opening parenthesis '(' at the specified index in the given string.
   *
   * @param numberString The input decimal number in string format.
   * @param index        The position where the parenthesis should be inserted.
   * @return Modified string with the inserted parenthesis.
   */
  public static String formatDecimalWithParentheses(String numberString, int index) {
    if (numberString == null || index < 0 || index > numberString.length()) {
      throw new IllegalArgumentException("Invalid input: index out of bounds or null string.");
    }
    return numberString.substring(0, index) + "(" + numberString.substring(index);
  }
}
