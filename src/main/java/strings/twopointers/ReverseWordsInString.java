package strings.twopointers;

import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * Problem: Reverse Words in a String
 *
 * Given a sentence, reverse the order of its words. The output should have a
 * single space between words and no leading or trailing spaces.
 *
 * Leetcode: https://leetcode.com/problems/reverse-words-in-a-string/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Strings | Split and rebuild | Two pointers variant
 *
 * Example:
 *   Input:  sentence = "I like this code very much"
 *   Output: "much very code this like I"
 *   Why:    the words keep their internal spelling, but their order is reversed.
 *
 * Follow-ups:
 *   1. How would you do it in place in a character array?
 *      Reverse the whole array, then reverse each word segment.
 *   2. How should multiple spaces be handled?
 *      Trim the ends and split on one or more whitespace characters.
 *   3. How would you stream words from a file?
 *      Push words onto a stack or write chunks in reverse if random access is available.
 */
public class ReverseWordsInString {

    public static void main(String[] args) {
        String[] inputs = {"I like this code very much", "  hello   world  ", ""};
        String[] expected = {"much very code this like I", "world hello", ""};

        for (int i = 0; i < inputs.length; i++) {
            String got = reverseWords(inputs[i]);
            System.out.printf("s=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }


        /**
     * Intuition: after trimming and splitting, each word is an independent token.
     * The desired output is those tokens read from right to left with one space
     * inserted between adjacent words.
     *
     * Algorithm:
     *   1. Return null or empty input unchanged.
     *   2. Trim the sentence and split it on one or more spaces.
     *   3. Append words from the last index down to the first.
     *   4. Add spaces only between words to avoid a trailing space.
     *
     * Time:  O(n) - splitting and rebuilding touch the sentence characters linearly.
     * Space: O(n) - the words array and output builder store the result.
     *
     * @param sentence Input sentence.
     * @return Sentence with word order reversed and spacing normalized.
     */
    private static String reverseWords(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return sentence; // Edge case: null or empty string
        }

        String[] words = sentence.trim().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = words.length - 1; i >= 0; i--) {
            result.append(words[i]);
            if (i > 0) result.append(" "); // Avoid trailing space
        }

        return result.toString();
    }
}
