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
 *
 * 1. What if the compressed string is longer than the original string?
 *    Answer: The algorithm still works correctly. We compress in-place and return the new length.
 *    Callers can compare the returned length with the original to decide whether to use compression.
 *
 * 2. How would you handle counts greater than 9999?
 *    Answer: The current solution handles any count size by converting it to a string and writing
 *    each digit separately. For very large counts, we could optimize by using a more efficient
 *    integer-to-string conversion or buffer management strategy.
 *
 * 3. Can you decompress a compressed string back to original?
 *    Answer: Yes, we'd iterate through the compressed string, read each character, then read
 *    following digits (if any) to get the count, and expand by writing that character count times.
 *    This would require O(N) space for the decompressed result.
 *
 * 4. How would you optimize for strings with very few repeating characters?
 *    Answer: We could add an early exit check by scanning once to estimate compression ratio.
 *    If most characters appear only once, we could skip compression. However, this adds overhead
 *    and the current solution is already optimal for the general case.
 *
 * 5. What if we need to compress using run-length encoding with different rules?
 *    Answer: The two-pointer technique remains applicable. We'd modify the counting logic and
 *    how we write the count (e.g., always include count, use special delimiter, etc).
 *    Related problem: https://leetcode.com/problems/string-compression-ii/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class StringCompression {

  /**
   * Compresses character array in-place using two-pointer technique.
   *
   * Algorithm:
   * 1. Use read pointer to traverse and count consecutive identical characters
   * 2. Use write pointer to write compressed result back to same array
   * 3. For each group: write character, then write count digits if count > 1
   * 4. Convert count to string and write each digit separately
   * 5. Return final write position as new array length
   *
   * Key insight: We write the compressed version to the front of the array while
   * reading from current position. Since compression reduces or maintains size,
   * we never overwrite unprocessed data.
   *
   * Time Complexity: O(N) where N is the length of chars array. We traverse
   * the array once with the read pointer, and each character is processed once.
   * Converting count to string is O(log K) where K is the count, but this is
   * bounded by O(log N) per group.
   *
   * Space Complexity: O(1) as we modify the array in-place and use only a
   * constant amount of extra variables. The count string conversion uses O(log N)
   * space but this is considered constant relative to input size.
   *
   * @param chars array of characters to compress in-place
   * @return new length of the compressed array
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
