package String;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * https://leetcode.com/problems/text-justification/
 *
 * ### Problem Statement:
 * Given an array of words and a max width, format the text such that:
 * - Each line is fully justified (both left and right).
 * - Words are packed greedily into lines.
 * - Spaces are distributed as evenly as possible.
 * - Last line should be left-justified and no extra space inserted between words.
 *
 * ### Example:
 * Input:
 * words = ["What", "must", "be", "acknowledgment", "shall", "be"], maxWidth = 16
 *
 * Output:
 * [
 * "What   must   be",
 * "acknowledgment  ",
 * "shall be        "
 * ]
 *
 * ### Follow-up:
 * - Can you modify it to support center alignment or right justification?
 * - How would you format the text in streaming fashion (line-by-line)?
 *
 * ### Time Complexity: O(N) – N = total number of words processed
 * ### Space Complexity: O(N) – for storing the output lines
 */
public class TextJustification {

  public static void main(String[] args) {
    String[] words = {"What", "must", "be", "acknowledgment", "shall", "be"};
    int maxWidth = 16;

    TextJustification tj = new TextJustification();
    List<String> justified = tj.fullJustify(words, maxWidth);

    for (String line : justified) {
      System.out.println("\"" + line + "\"");
    }
  }

  /**
   * Justifies a list of words into lines of maxWidth characters.
   *
   * Steps:
   * - Use greedy packing to fill lines with as many words as possible.
   * - Once a line is full, call helper to add spacing appropriately.
   * - For the last line, use left-justified formatting.
   *
   * @param words    Input words
   * @param maxWidth Maximum width of each line
   * @return List of fully justified lines
   */
  public List<String> fullJustify(String[] words, int maxWidth) {
    List<String> result = new ArrayList<>();

    int startLineWordIndex = 0; // Index of the first word in the current line
    int currentLength = 0; // Current length of the line without spaces

    for (int lastLineWordIndex = 0; lastLineWordIndex < words.length; lastLineWordIndex++) {
      int wordLengthWithSpace = currentLength + words[lastLineWordIndex].length() + (lastLineWordIndex - startLineWordIndex);

      if (wordLengthWithSpace > maxWidth) {
        result.add(formatLine(words, startLineWordIndex, lastLineWordIndex - 1, maxWidth, false));
        startLineWordIndex = lastLineWordIndex;
        currentLength = 0;
      }
      currentLength += words[lastLineWordIndex].length();
    }

    result.add(formatLine(words, startLineWordIndex, words.length - 1, maxWidth, true));
    return result;
  }

  /**
   * Formats a single line of words with proper spacing.
   *
   * @param words      Full input word list
   * @param startIndex      Start index for the line
   * @param endIndex        End index for the line
   * @param maxWidth   Max allowed width
   * @param isLastLine Whether this is the last line
   * @return Justified line string
   */
  private String formatLine(String[] words, int startIndex, int endIndex, int maxWidth, boolean isLastLine) {
    if (isLastLine || startIndex == endIndex) {
      String line = String.join(" ", Arrays.copyOfRange(words, startIndex, endIndex + 1));
      return padRight(line, maxWidth);
    }

    int totalWords = endIndex - startIndex;
    int totalWordLength = getWordLength(words, startIndex, endIndex);
    int totalSpaces = maxWidth - totalWordLength;

    int minSpacesBetween = totalSpaces / totalWords; // Minimum spaces between words
    int remainingSpaces = totalSpaces % totalWords; // Extra spaces to distribute

    StringBuilder lineBuilder = new StringBuilder();
    for (int i = startIndex; i <= endIndex; i++) {
      lineBuilder.append(words[i]);

      if (i < endIndex) {
        for (int s = 0; s < minSpacesBetween; s++) {
          lineBuilder.append(" ");
        }
        if (remainingSpaces > 0) {
          lineBuilder.append(" ");
          remainingSpaces--;
        }
      }
    }

    return lineBuilder.toString();
  }

  /**
   * Returns the combined length of all words in a range.
   */
  private int getWordLength(String[] words, int start, int end) {
    int len = 0;
    for (int i = start; i <= end; i++) {
      len += words[i].length();
    }
    return len;
  }

  /**
   * Pads a line on the right with spaces until it matches the maxWidth.
   */
  private String padRight(String line, int width) {
    StringBuilder sb = new StringBuilder(line);
    while (sb.length() < width) {
      sb.append(" ");
    }
    return sb.toString();
  }
}