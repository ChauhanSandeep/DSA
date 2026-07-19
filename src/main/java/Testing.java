/**
 * Small utility driver for checking string insertion behavior.
 *
 * The reusable method inserts one character at a requested index, while the
 * main method prints a few examples in a quick revision-friendly format.
 */
public class Testing {
    
    private static final int DEFAULT_INSERT_POSITION = 2;
    private static final String DEFAULT_DECIMAL_NUMBER = "0.123456";

    public static void main(String[] args) {
        String[] inputs = { DEFAULT_DECIMAL_NUMBER, "", "abc" };
        int[] indices = { DEFAULT_INSERT_POSITION, 0, 3 };
        char[] characters = { '(', '#', '!' };
        String[] expected = { "0.(123456", "#", "abc!" };

        for (int i = 0; i < inputs.length; i++) {
            String output = insertCharacterAt(inputs[i], indices[i], characters[i]);
            System.out.printf("str=%s index=%d char=%c -> %s  expected=%s%n",
                inputs[i], indices[i], characters[i], output, expected[i]);
        }
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
     * Intuition: a String cannot be opened up and changed in place. Inserting one
     * character means choosing a valid split point, keeping the prefix before the
     * split, placing the new character, then keeping the suffix after the split.
     * The validation protects the two substring calls from invalid boundaries.
     *
     * Algorithm:
     * 1. Reject a null str because there is no valid insertion point.
     * 2. Reject any index outside the inclusive range [0, str.length()].
     * 3. Return prefix + character + suffix using substring around index.
     *
     * Time:  O(n) - substring and concatenation copy the input characters into the result.
     * Space: O(n) - the returned string stores the original characters plus one new character.
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
