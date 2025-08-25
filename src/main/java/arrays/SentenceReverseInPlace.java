package arrays;

import java.util.Arrays;


/**
 * Problem: Reverse the words in a character array (in-place).
 *
 * ✅ Given a character array representing a sentence, reverse the order of the words in-place.
 *    A word is defined as a sequence of non-space characters.
 *
 * 🔗 LeetCode Link (similar problem): https://leetcode.com/problems/reverse-words-in-a-string-ii/
 *
 * Example:
 * Input:  ['h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd']
 * Output: ['w', 'o', 'r', 'l', 'd', ' ', 'h', 'e', 'l', 'l', 'o']
 *
 * Follow-up Questions:
 * - Q: What if multiple spaces between words?
 *   A: You'll need to handle extra spacing as a separate concern.
 * - Q: Can the array contain punctuation?
 *   A: Yes, punctuation is considered part of a word unless told otherwise.
 * - Q: Can you solve this in O(1) extra space?
 *   A: Yes, current solution does it in-place.
 */
public class SentenceReverseInPlace {

  public static void main(String[] args) {
    char[] sentence = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
    reverseWordsInPlace(sentence);
    System.out.println(Arrays.toString(sentence)); // Expected: ['w', 'o', 'r', 'l', 'd', ' ', 'h', 'e', 'l', 'l', 'o']
  }

  /**
   * Reverses the words in a character array, in-place.
   *
   * ✅ Algorithm:
   *    1. Reverse the entire character array.
   *    2. Then reverse each individual word back to restore correct word order.
   *
   * ✅ Time Complexity: O(N)
   * ✅ Space Complexity: O(1)
   *
   * @param chars character array representing the sentence
   * @return same character array with words reversed in-place
   */
  public static char[] reverseWordsInPlace(char[] chars) {
    if (chars == null || chars.length == 0) {
      return chars;
    }

    // Step 1: Reverse the entire sentence
    reverse(chars, 0, chars.length - 1);

    // Step 2: Reverse each word to restore its character order
    int wordStart = 0;

    for (int i = 0; i <= chars.length; i++) {
      if (i == chars.length || chars[i] == ' ') {
        reverse(chars, wordStart, i - 1);
        wordStart = i + 1;
      }
    }

    return chars;
  }

  /**
   * Reverses characters between start and end indices in the array.
   *
   * @param arr   character array
   * @param start starting index
   * @param end   ending index
   */
  private static void reverse(char[] arr, int start, int end) {
    while (start < end) {
      char temp = arr[start];
      arr[start] = arr[end];
      arr[end] = temp;
      start++;
      end--;
    }
  }
}