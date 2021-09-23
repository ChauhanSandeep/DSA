package String;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Justify the text
 * https://leetcode.com/problems/text-justification/
 */
public class TextJustification {
    public static void main(String[] args) {
        String[] words = {"What", "must", "be", "acknowledgment", "shall", "be"};
        List<String> result = new TextJustification().fullJustify(words, 16);
        System.out.println(result);
    }

    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> result = new ArrayList<>();
        if (words == null || words.length == 0) return result;

        int lastIndex = 0;
        int strLen = 0;
        for (int i = 0; i < words.length; i++) {
            strLen += words[i].length() + 1;
            if (strLen - 1 > maxWidth) {
                String temp = createStr(words, lastIndex, i - 1, maxWidth);
                result.add(temp);
                lastIndex = i;
                strLen = 0;
                i--;
            }
        }
        // System.out.println("Finished for loop. lastIndex " + lastIndex);
        if (lastIndex <= words.length - 1) {
            String temp = createStr(words, lastIndex, words.length - 1, maxWidth);
            result.add(temp);
        }

        return result;
    }

    private String createStr(String[] words, int start, int end, int maxWidth) {
        if (end == words.length - 1) {
            String result = String.join(" ", Arrays.copyOfRange(words, start, words.length));
            while (result.length() < maxWidth) {
                result += " ";
            }
            return result;
        }
        int currLen = 0;
        for (int i = start; i <= end; i++) {
            currLen += words[i].length();
        }
        // System.out.println("start: " + start + " end: " + end + " currLen: " + currLen + "maxWidth: " + maxWidth);
        if (start == end) {
            StringBuilder result = new StringBuilder(words[start]);
            while (result.length() < maxWidth) {
                result.append(" ");
            }
            return result.toString();
        }
        int totalSpaces = maxWidth - currLen;
        int spaces1 = totalSpaces / (end - start);
        int extraSpaces = totalSpaces - (spaces1 * (end - start));
        int div = end - start;
        while (extraSpaces % div != 0) {
            div--;
        }
        int step = extraSpaces / div;
        StringBuilder builder = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (i != start) {
                int currSpaces = spaces1;
                if (extraSpaces > 0) {
                    currSpaces += step;
                    extraSpaces -= step;
                }
                for (int x = 0; x < currSpaces; x++) {
                    builder.append(" ");
                }
            }
            builder.append(words[i]);
        }
        return builder.toString();
    }
}
