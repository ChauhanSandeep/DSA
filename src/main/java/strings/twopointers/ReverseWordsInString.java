package strings.twopointers;

import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * ✅ LeetCode Problem: Reverse Words in a String
 * 🔗 https://leetcode.com/problems/reverse-words-in-a-string/
 *
 * 🔹 Problem Statement:
 * Given a string `sentence`, reverse the order of words. A word is defined as a sequence of non-space characters.
 * The final string should have exactly one space between words and no leading/trailing spaces.
 *
 * 🔸 Example:
 * Input:  "I like this code very much"
 * Output: "much very code this like I"
 *
 * 🔍 Follow-up Questions:
 * - Can you reverse the words **in-place** if given as a character array?
 *   🔗 https://leetcode.com/problems/reverse-words-in-a-string-ii/
 * - What if multiple spaces exist between words? (Already handled via `split("\\s+")`)
 * - How would you implement this with O(1) extra space if allowed to modify the original string?
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ReverseWordsInString {

    public static void main(String[] args) {
        String sentence = "I like this code very much";
        String reversedSentence = reverseWords(sentence);
        System.out.println(reversedSentence);  // Output: "much very code this like I"
    }

    /**
     * Reverses the words in a sentence while preserving the character order within each word.
     *
     * @param sentence The original input string containing words separated by spaces.
     * @return A new string with words in reversed order and trimmed spaces.
     *
     * 🔹 Steps:
     * - Trim leading/trailing spaces.
     * - Split the string by one or more spaces using regex `\\s+`.
     * - Append words in reverse order using a StringBuilder.
     *
     * ⏱ Time Complexity: O(N), where N = total number of characters in the sentence.
     * 🧠 Space Complexity: O(N), to store the split words and result string.
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
