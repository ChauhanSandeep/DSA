package String;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Text Justification
 * https://leetcode.com/problems/text-justification/
 *
 * Given an array of words and a maximum width, format the text so that each line is fully justified.
 *
 * **Approach & Explanation:**
 * - Use a greedy approach to group words into lines without exceeding `maxWidth`.
 * - Distribute spaces evenly between words for full justification.
 * - Ensure the last line is left-justified.
 *
 * **Time Complexity:** O(N) - Each word is processed once.
 * **Space Complexity:** O(N) - Output list stores justified lines.
 */
public class TextJustification {
    public static void main(String[] args) {
        String[] words = {"What", "must", "be", "acknowledgment", "shall", "be"};
        int maxWidth = 16;

        TextJustification tj = new TextJustification();
        List<String> result = tj.fullJustify(words, maxWidth);

        // Print justified text
        for (String line : result) {
            System.out.println("\"" + line + "\"");
        }
    }

    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> justifiedText = new ArrayList<>();
        int start = 0, currentLength = 0;

        for (int i = 0; i < words.length; i++) {
            // Calculate total length including space between words
            int potentialLength = currentLength + words[i].length() + (i - start);

            // If adding words[i] exceeds maxWidth, justify the current line
            if (potentialLength > maxWidth) {
                justifiedText.add(formatLine(words, start, i - 1, maxWidth, false));
                start = i;
                currentLength = 0;
            }
            currentLength += words[i].length();
        }

        // Justify the last line (left-justified)
        justifiedText.add(formatLine(words, start, words.length - 1, maxWidth, true));

        return justifiedText;
    }

    /**
     * Formats a line by adding appropriate spaces.
     *
     * @param words     Array of words.
     * @param start     Start index of words for this line.
     * @param end       End index of words for this line.
     * @param maxWidth  The max width of each line.
     * @param isLastLine Whether this is the last line (left-justified).
     * @return A justified line as a string.
     */
    private String formatLine(String[] words, int start, int end, int maxWidth, boolean isLastLine) {
        // Join words with single spaces for the last line or if there's only one word
        if (isLastLine || start == end) {
            String line = String.join(" ", Arrays.copyOfRange(words, start, end + 1));
            return padRight(line, maxWidth);
        }

        // Calculate total spaces needed
        int totalWords = end - start;
        int totalSpaces = maxWidth - getWordLength(words, start, end);
        int minSpace = totalSpaces / totalWords;
        int extraSpace = totalSpaces % totalWords;

        // Build justified line
        StringBuilder justifiedLine = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (i > start) {
                justifiedLine.append(" ".repeat(minSpace));
                if (extraSpace > 0) {
                    justifiedLine.append(" ");
                    extraSpace--;
                }
            }
            justifiedLine.append(words[i]);
        }

        return justifiedLine.toString();
    }

    /**
     * Calculates the total length of words in a given range (excluding spaces).
     */
    private int getWordLength(String[] words, int start, int end) {
        int length = 0;
        for (int i = start; i <= end; i++) {
            length += words[i].length();
        }
        return length;
    }

    /**
     * Pads the given string with spaces on the right to match the desired width.
     */
    private String padRight(String text, int width) {
        return text + " ".repeat(width - text.length());
    }
}
