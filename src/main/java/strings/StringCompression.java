package strings;
import java.util.Arrays;

/**
 * Problem: String Compression
 *
 * Compress consecutive character runs in place. Each run writes the character and
 * then its decimal count digits only when the count is greater than one.
 *
 * Leetcode: https://leetcode.com/problems/string-compression/ (Medium)
 * Rating:   acceptance 60.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  String | Two pointers | In-place run-length encoding
 *
 * Example:
 *   Input:  chars = ['a','a','b','b','c','c','c']
 *   Output: 6 with prefix ['a','2','b','2','c','3']
 *   Why:    the runs are a x2, b x2, and c x3.
 *
 * Follow-ups:
 *   1. Decompress? Parse count digits after each character and expand into a new buffer.
 *   2. Different format? Keep read/write pointers and change run emission.
 *   3. Longer compressed output? Caller can compare returned length to original length.
 *
 * Related: String Compression II (1531), Count and Say (38).
 */
public class StringCompression {

  public static void main(String[] args) {
    StringCompression solver = new StringCompression();
    char[][] inputs = {{'a', 'a', 'b', 'b', 'c', 'c', 'c'}, {'a'}, {'a', 'b', 'c'}};
    int[] expectedLengths = {6, 1, 3};
    String[] expectedPrefixes = {"a2b2c3", "a", "abc"};
    for (int i = 0; i < inputs.length; i++) {
      char[] chars = inputs[i].clone();
      int gotLength = solver.compress(chars);
      String gotPrefix = new String(chars, 0, gotLength);
      System.out.printf("chars=%s -> length=%d prefix=%s  expected=%d/%s%n", Arrays.toString(inputs[i]), gotLength, gotPrefix, expectedLengths[i], expectedPrefixes[i]);
    }
  }


    /**
   * Intuition: read one run at a time and write its compressed form into the
   * front of the same array. The write pointer never needs data that the read
   * pointer has not already consumed.
   *
   * Algorithm:
   *   1. Use readPointer to scan runs and writePointer to write output.
   *   2. Count the current run length.
   *   3. Write the character and then count digits when count > 1.
   *   4. Return writePointer as the compressed length.
   *
   * Time:  O(n) - each input character is read once.
   * Space: O(1) - compression is in-place.
   */
  public int compress(char[] chars) {
    int writePointer = 0;
    int readPointer = 0;
    int length = chars.length;

    while (readPointer < length) {
      char currentChar = chars[readPointer];
      int count = 0;

      // Count occurrences of currentChar
      while (readPointer < length && chars[readPointer] == currentChar) {
        readPointer++;
        count++;
      }

      chars[writePointer++] = currentChar;

      // Write count digits only if count > 1
      if (count > 1) {
        String countStr = String.valueOf(count);
        for (char digit : countStr.toCharArray()) {
          chars[writePointer++] = digit;
        }
      }
    }

    return writePointer;
  }
}
