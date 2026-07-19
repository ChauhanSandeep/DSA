package arrays;

import java.util.Arrays;


/**
 * Problem: Reverse Words in a String II
 *
 * Given a character array that represents a sentence, reverse the order of the
 * words in place while keeping the characters inside each word in their normal
 * order. Words are separated by single spaces for the standard version.
 *
 * Leetcode: https://leetcode.com/problems/reverse-words-in-a-string-ii/ (Medium)
 * Rating:   acceptance 56.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Reverse whole then reverse words
 *
 * Example:
 *   Input:  ['h','e','l','l','o',' ','w','o','r','l','d']
 *   Output: ['w','o','r','l','d',' ','h','e','l','l','o']
 *   Why:    reversing the full array flips word order but also flips each word,
 *           so reversing each word again restores the letters.
 *
 * Follow-ups:
 *   1. Handle multiple spaces between words?
 *      Preserve them as separators or normalize them first, depending on the required output.
 *   2. Do this for a mutable UTF-8 byte buffer?
 *      Reverse by code point boundaries, not raw bytes, so multibyte characters stay valid.
 *   3. Return a new string instead of editing in place?
 *      Split on spaces and append words in reverse order, which uses O(n) extra space.
 *
 * Related: Reverse Words in a String (151), Reverse String (344).
 */
public class SentenceReverseInPlace {

    public static void main(String[] args) {
        char[][] inputs = { "hello world".toCharArray(), "a".toCharArray(), "blue is sky".toCharArray() };
        String[] expected = { "world hello", "a", "sky is blue" };

        for (int i = 0; i < inputs.length; i++) {
            char[] chars = inputs[i].clone();
            reverseWordsInPlace(chars);
            System.out.printf("chars=%s  ->  %s  expected=%s%n",
                Arrays.toString(inputs[i]), new String(chars), expected[i]);
        }
    }



  /**
   * Intuition: reversing the whole sentence puts the words in the desired order but
   * also reverses the letters inside each word. A second pass over word boundaries
   * reverses each word back, so the final array has words reordered while each word's
   * characters read normally.
   *
   * Algorithm:
   *   1. Return the input unchanged for null or empty arrays.
   *   2. Reverse the entire character array.
   *   3. Scan for spaces and array end to find each word.
   *   4. Reverse each word range in place and return chars.
   *
   * Time:  O(n) - each character participates in a constant number of swaps.
   * Space: O(1) - all reversals happen in the input array.
   *
   * @param chars sentence characters with words separated by spaces
   * @return the same array with word order reversed
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