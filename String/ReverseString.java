package String;

/**
 * Reverse the words in a given sentence while preserving the order of characters within each word.
 * 
 * Approach:
 * - Split the string into words using space as a delimiter.
 * - Reverse the order of words and reassemble the sentence.
 * - Use StringBuilder for efficiency.
 * 
 * Time Complexity: O(N) - Each word is processed once.
 * Space Complexity: O(N) - Stores words in an array.
 * 
 * LeetCode Equivalent: https://leetcode.com/problems/reverse-words-in-a-string/
 */
public class ReverseString {
    public static void main(String[] args) {
        String sentence = "I like this code very much";
        String reversedSentence = reverseWords(sentence);
        System.out.println(reversedSentence);
    }

    /**
     * Reverses the words in a sentence.
     *
     * @param sentence The input string containing words separated by spaces.
     * @return A string with words in reverse order.
     */
    private static String reverseWords(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return sentence; // Handle edge case
        }

        String[] words = sentence.trim().split("\\s+"); // Splitting by one or more spaces
        StringBuilder reversed = new StringBuilder();

        // Iterate backwards and append words
        for (int i = words.length - 1; i >= 0; i--) {
            reversed.append(words[i]).append(" ");
        }

        return reversed.toString().trim(); // Remove trailing space
    }
}
