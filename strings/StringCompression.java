package strings;

/**
 * LeetCode 443. String Compression
 *
 * Given an array of characters chars, compress it using the following algorithm:
 * Begin with an empty string s. For each group of consecutive repeating characters in chars:
 * - If the group's length is 1, append the character to s.
 * - Otherwise, append the character followed by the group's length.
 *
 * Example 1:
 * Input: chars = ['a','a','b','b','c','c','c']
 * Output: Return 6, and the first 6 characters of the input array should be: ['a','2','b','2','c','3']
 *
 * Example 2:
 * Input: chars = ['a']
 * Output: Return 1, and the first 1 character of the input array should be: ['a']
 *
 * LeetCode Link: https://leetcode.com/problems/string-compression/
 *
 * Follow-up Questions:
 * - How would you handle very large counts (>9)? (Current solution handles multi-digit counts)
 * - Can you optimize for arrays with no repeating characters? (Early detection and minimal processing)
 * - How would you extend to different compression algorithms? (Modify grouping and encoding logic)
 * - What if we need to decode the compressed string back? (Implement reverse algorithm)
 */
public class StringCompression {

    /**
     * Compresses character array in-place using run-length encoding.
     *
     * Algorithm:
     * 1. Use two pointers: read pointer to scan input, write pointer to build result
     * 2. For each group of consecutive characters, count the length
     * 3. Write the character to result position
     * 4. If count > 1, write count digits to subsequent positions
     * 5. Continue until all characters are processed
     * 6. Return length of compressed array
     *
     * Time Complexity: O(n) where n is length of chars array
     * Space Complexity: O(1) - compression done in-place
     *
     * @param chars Character array to compress in-place
     * @return Length of compressed array
     */
    public int compress(char[] chars) {
        if (chars == null || chars.length == 0) {
            return 0;
        }

        int writeIndex = 0; // Position to write compressed data
        int readIndex = 0;  // Position to read input data

        while (readIndex < chars.length) {
            char currentChar = chars[readIndex];
            int count = 0;

            // Count consecutive occurrences of current character
            while (readIndex < chars.length && chars[readIndex] == currentChar) {
                count++;
                readIndex++;
            }

            // Write character to compressed array
            chars[writeIndex++] = currentChar;

            // Write count if greater than 1
            if (count > 1) {
                String countStr = String.valueOf(count);
                for (char digit : countStr.toCharArray()) {
                    chars[writeIndex++] = digit;
                }
            }
        }

        return writeIndex;
    }

    /**
     * Alternative approach with explicit count digit handling.
     */
    public int compressAlternative(char[] chars) {
        int writeIndex = 0;
        int i = 0;

        while (i < chars.length) {
            char currentChar = chars[i];
            int count = 1;

            // Count consecutive characters
            while (i + count < chars.length && chars[i + count] == currentChar) {
                count++;
            }

            // Write character
            chars[writeIndex++] = currentChar;

            // Write count digits if count > 1
            if (count > 1) {
                writeIndex = writeCount(chars, writeIndex, count);
            }

            i += count;
        }

        return writeIndex;
    }

    // Helper method to write count digits to array
    private int writeCount(char[] chars, int startIndex, int count) {
        String countStr = String.valueOf(count);

        for (int i = 0; i < countStr.length(); i++) {
            chars[startIndex + i] = countStr.charAt(i);
        }

        return startIndex + countStr.length();
    }

    /**
     * Approach that handles large counts efficiently without string conversion.
     */
    public int compressOptimized(char[] chars) {
        int writeIndex = 0;
        int readIndex = 0;

        while (readIndex < chars.length) {
            char currentChar = chars[readIndex];
            int count = 0;

            // Count occurrences
            while (readIndex < chars.length && chars[readIndex] == currentChar) {
                count++;
                readIndex++;
            }

            // Write character
            chars[writeIndex++] = currentChar;

            // Write count digits for counts > 1
            if (count > 1) {
                int start = writeIndex;

                // Extract digits in reverse order
                while (count > 0) {
                    chars[writeIndex++] = (char) ('0' + count % 10);
                    count /= 10;
                }

                // Reverse the digits to correct order
                reverse(chars, start, writeIndex - 1);
            }
        }

        return writeIndex;
    }

    // Helper method to reverse portion of array
    private void reverse(char[] chars, int start, int end) {
        while (start < end) {
            char temp = chars[start];
            chars[start] = chars[end];
            chars[end] = temp;
            start++;
            end--;
        }
    }
}
