/**
 * Problem: String Manipulation - Insert Character at Position
 *
 * Demonstrates basic string manipulation by inserting a character at a specified position.
 * This is a utility class for testing string operations and demonstrating proper Java practices.
 *
 * 📝 Example:
 * Input: str = "0.123456", index = 2, char = '('
 * Output: "0.(123456"
 *
 * 🎯 Constraints:
 * - String is not null
 * - Index is within valid range [0, string.length]
 *
 * 💡 Follow-up Questions with Answers:
 * 1. Q: How would you insert multiple characters at different positions efficiently?
 *    A: Use StringBuilder and insert characters from right to left to avoid index shifting.
 *       Or build from left to right keeping track of cumulative offset.
 *
 * 2. Q: What if you need to insert the same character at multiple positions?
 *    A: Sort positions in descending order and insert from right to left, or use StringBuilder
 *       with a single pass, inserting as you go.
 *
 * 3. Q: How would you optimize for many insert operations?
 *    A: Use StringBuilder which has O(1) amortized insert complexity compared to O(n) for String
 *       concatenation. For bulk operations, collect all changes and apply once.
 *
 * 4. Q: What if the string is very large (millions of characters)?
 *    A: Consider rope data structure or gap buffer for efficient insertions at arbitrary positions.
 *       StringBuilder still works but may require memory reallocation.
 *
 * 5. Q: How would you handle Unicode characters and surrogate pairs?
 *    A: Use codePointAt() and offsetByCodePoints() instead of charAt() and character indices
 *       to properly handle multi-byte Unicode characters.
 */
public class Testing {
    
    private static final int DEFAULT_INSERT_POSITION = 2;
    private static final String DEFAULT_DECIMAL_NUMBER = "0.123456";

    public static void main(String[] args) {
        testStringManipulation();
    }

    /**
     * Demonstrates string manipulation by inserting parenthesis at specified position.
     * This test method shows proper error handling and string formatting.
     */
    public static void testStringManipulation() {
        String formattedNumber = insertCharacterAt(DEFAULT_DECIMAL_NUMBER, DEFAULT_INSERT_POSITION, '(');
        System.out.println("Original: " + DEFAULT_DECIMAL_NUMBER);
        System.out.println("Formatted: " + formattedNumber);
    }

    /**
     * Inserts a character at the specified index in the given string.
     *
     * Algorithm:
     * 1. Validate input parameters
     * 2. Split string at insertion point using substring
     * 3. Concatenate parts with new character
     *
     * Time Complexity: O(n) where n is string length (due to substring operations)
     * Space Complexity: O(n) for creating new string
     *
     * @param str The input string
     * @param index The position where character should be inserted (0-based)
     * @param character The character to insert
     * @return New string with character inserted at specified position
     * @throws IllegalArgumentException if string is null or index is out of bounds
     */
    public static String insertCharacterAt(String str, int index, char character) {
        if (str == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        if (index < 0 || index > str.length()) {
            throw new IllegalArgumentException(
                String.format("Index %d out of bounds for string length %d", index, str.length())
            );
        }
        return str.substring(0, index) + character + str.substring(index);
    }

    /**
     * Legacy method maintained for backward compatibility.
     * 
     * @deprecated Use {@link #insertCharacterAt(String, int, char)} instead.
     *             This method will be removed in a future version.
     * @param numberString The input string
     * @param index Position where parenthesis should be inserted
     * @return Modified string with parenthesis inserted
     */
    @Deprecated
    public static String formatDecimalWithParentheses(String numberString, int index) {
        return insertCharacterAt(numberString, index, '(');
    }
}
