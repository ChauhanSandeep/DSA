package String;

import java.util.*;

/**
 * 68. Text Justification
 *
 * Problem: Given an array of strings words and a width maxWidth, format the text
 * such that each line has exactly maxWidth characters and is fully justified.
 * Pack as many words as possible in each line and pad with spaces.
 *
 * Example:
 * Input: words = ["This","is","an","example","of","text","justification."], maxWidth = 16
 * Output: [
 *   "This    is    an",
 *   "example  of text",
 *   "justification.  "
 * ]
 *
 * LeetCode: https://leetcode.com/problems/text-justification
 *
 * Follow-up questions:
 * Q: How to handle different languages with varying character widths?
 * A: Implement character width calculation and adjust spacing algorithms.
 *
 * Q: Can we support rich text formatting (bold, italic, etc.)?
 * A: Extend to handle markup tags and formatting metadata.
 *
 * Q: How to optimize for very large documents?
 * A: Use streaming approach and parallel processing for independent sections.
 */
public class TextJustification {
    // Helper method to repeat spaces (Java 8 compatible)
    private static String repeatSpace(int count) {
        return new String(new char[count]).replace('\0', ' ');
    }

    /**
     * Standard greedy approach - optimal solution.
     *
     * Algorithm: Line-by-line greedy packing
     * - For each line, pack maximum words that fit within maxWidth
     * - Distribute extra spaces evenly between words (left-biased)
     * - Handle last line separately (left-justified only)
     * - Special case for single word lines
     *
     * Time Complexity: O(n) where n is total characters in all words
     * Space Complexity: O(maxWidth) for line construction
     */
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> result = new ArrayList<>();
        int index = 0;

        while (index < words.length) {
            // Determine words for current line
            LineInfo lineInfo = getWordsForLine(words, index, maxWidth);

            // Format the line
            String line = formatLine(lineInfo, maxWidth, index + lineInfo.wordCount == words.length);
            result.add(line);

            index += lineInfo.wordCount;
        }

        return result;
    }

    // Helper to determine which words fit in current line
    private LineInfo getWordsForLine(String[] words, int start, int maxWidth) {
        int totalLength = words[start].length();
        int wordCount = 1;

        // Add words while they fit (accounting for at least one space between words)
        while (start + wordCount < words.length) {
            int nextWordLength = words[start + wordCount].length();

            // Need at least one space between words
            if (totalLength + 1 + nextWordLength > maxWidth) {
                break;
            }

            totalLength += 1 + nextWordLength;
            wordCount++;
        }

        return new LineInfo(wordCount, totalLength);
    }

    // Format a single line with appropriate spacing
    private String formatLine(LineInfo lineInfo, int maxWidth, boolean isLastLine) {
        if (lineInfo.wordCount == 1 || isLastLine) {
            return formatLastLine(lineInfo, maxWidth);
        } else {
            return formatMiddleLine(lineInfo, maxWidth);
        }
    }

    // Format last line or single word line (left-justified)
    private String formatLastLine(LineInfo lineInfo, int maxWidth) {
        StringBuilder line = new StringBuilder();

        for (int i = 0; i < lineInfo.wordCount; i++) {
            if (i > 0) line.append(" ");
            line.append(lineInfo.words.get(i));
        }

        // Right-pad with spaces
        while (line.length() < maxWidth) {
            line.append(" ");
        }

        return line.toString();
    }

    // Format middle line with even space distribution
    private String formatMiddleLine(LineInfo lineInfo, int maxWidth) {
        StringBuilder line = new StringBuilder();

        // Calculate space distribution
        int totalWordChars = lineInfo.words.stream().mapToInt(String::length).sum();
        int totalSpaces = maxWidth - totalWordChars;
        int gaps = lineInfo.wordCount - 1;

        int spacesPerGap = totalSpaces / gaps;
        int extraSpaces = totalSpaces % gaps;

        // Build line with distributed spaces
        for (int i = 0; i < lineInfo.wordCount; i++) {
            line.append(lineInfo.words.get(i));

            if (i < gaps) { // Not the last word
                // Add base spaces
                for (int j = 0; j < spacesPerGap; j++) {
                    line.append(" ");
                }

                // Add extra space if needed (left-biased)
                if (i < extraSpaces) {
                    line.append(" ");
                }
            }
        }

        return line.toString();
    }

    private static class LineInfo {
        int wordCount;
        int totalLength;
        List<String> words;

        LineInfo(int wordCount, int totalLength) {
            this.wordCount = wordCount;
            this.totalLength = totalLength;
            this.words = new ArrayList<>();
        }
    }

    /**
     * Enhanced version with proper LineInfo construction.
     * More robust implementation with better separation of concerns.
     */
    public List<String> fullJustifyEnhanced(String[] words, int maxWidth) {
        List<String> result = new ArrayList<>();
        int index = 0;

        while (index < words.length) {
            List<String> lineWords = new ArrayList<>();
            int totalLength = 0;

            // Pack words for current line
            while (index < words.length) {
                String word = words[index];
                int requiredLength = totalLength + (lineWords.isEmpty() ? 0 : 1) + word.length();

                if (requiredLength > maxWidth) break;

                lineWords.add(word);
                totalLength = requiredLength;
                index++;
            }

            // Format the line
            boolean isLastLine = (index == words.length);
            String line = justifyLine(lineWords, maxWidth, isLastLine);
            result.add(line);
        }

        return result;
    }

    private String justifyLine(List<String> words, int maxWidth, boolean isLastLine) {
        if (words.size() == 1 || isLastLine) {
            return leftJustify(words, maxWidth);
        } else {
            return fullJustifyLine(words, maxWidth);
        }
    }

    private String leftJustify(List<String> words, int maxWidth) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < words.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(words.get(i));
        }

        // Pad right with spaces
        while (sb.length() < maxWidth) {
            sb.append(" ");
        }

        return sb.toString();
    }

    private String fullJustifyLine(List<String> words, int maxWidth) {
        StringBuilder sb = new StringBuilder();

        int totalWordLength = words.stream().mapToInt(String::length).sum();
        int totalSpaces = maxWidth - totalWordLength;
        int gaps = words.size() - 1;

        int spacesPerGap = totalSpaces / gaps;
        int extraSpaces = totalSpaces % gaps;

        for (int i = 0; i < words.size(); i++) {
            sb.append(words.get(i));

            if (i < gaps) {
                // Add regular spaces
                sb.append(repeatSpace(spacesPerGap));

                // Add extra space if needed
                if (i < extraSpaces) {
                    sb.append(" ");
                }
            }
        }

        return sb.toString();
    }

    /**
     * Advanced formatter supporting different justification modes.
     * Supports left, right, center, and full justification.
     */
    public static class AdvancedTextFormatter {

        public enum JustificationMode {
            LEFT, RIGHT, CENTER, FULL
        }

        public List<String> formatText(String[] words, int maxWidth, JustificationMode mode) {
            List<String> result = new ArrayList<>();
            int index = 0;

            while (index < words.length) {
                List<String> lineWords = new ArrayList<>();
                int totalLength = 0;

                // Pack words for current line
                while (index < words.length) {
                    String word = words[index];
                    int requiredLength = totalLength + (lineWords.isEmpty() ? 0 : 1) + word.length();

                    if (requiredLength > maxWidth) break;

                    lineWords.add(word);
                    totalLength = requiredLength;
                    index++;
                }

                // Format based on mode
                boolean isLastLine = (index == words.length);
                String line = formatLine(lineWords, maxWidth, mode, isLastLine);
                result.add(line);
            }

            return result;
        }

        private String formatLine(List<String> words, int maxWidth, JustificationMode mode, boolean isLastLine) {
            switch (mode) {
                case LEFT:
                    return leftJustifyLine(words, maxWidth);
                case RIGHT:
                    return rightJustifyLine(words, maxWidth);
                case CENTER:
                    return centerJustifyLine(words, maxWidth);
                case FULL:
                    return isLastLine ? leftJustifyLine(words, maxWidth) : fullJustifyLine(words, maxWidth);
                default:
                    return leftJustifyLine(words, maxWidth);
            }
        }

        private String leftJustifyLine(List<String> words, int maxWidth) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < words.size(); i++) {
                if (i > 0) sb.append(" ");
                sb.append(words.get(i));
            }

            while (sb.length() < maxWidth) {
                sb.append(" ");
            }

            return sb.toString();
        }

        private String rightJustifyLine(List<String> words, int maxWidth) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < words.size(); i++) {
                if (i > 0) sb.append(" ");
                sb.append(words.get(i));
            }

            // Pad left with spaces
            int padding = maxWidth - sb.length();
            return repeatSpace(padding) + sb.toString();
        }

        private String centerJustifyLine(List<String> words, int maxWidth) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < words.size(); i++) {
                if (i > 0) sb.append(" ");
                sb.append(words.get(i));
            }

            // Center the line
            int totalPadding = maxWidth - sb.length();
            int leftPadding = totalPadding / 2;
            int rightPadding = totalPadding - leftPadding;

            return repeatSpace(leftPadding) + sb.toString() + repeatSpace(rightPadding);
        }

        private String fullJustifyLine(List<String> words, int maxWidth) {
            if (words.size() == 1) {
                return leftJustifyLine(words, maxWidth);
            }

            StringBuilder sb = new StringBuilder();

            int totalWordLength = words.stream().mapToInt(String::length).sum();
            int totalSpaces = maxWidth - totalWordLength;
            int gaps = words.size() - 1;

            int spacesPerGap = totalSpaces / gaps;
            int extraSpaces = totalSpaces % gaps;

            for (int i = 0; i < words.size(); i++) {
                sb.append(words.get(i));

                if (i < gaps) {
                    sb.append(repeatSpace(spacesPerGap));
                    if (i < extraSpaces) {
                        sb.append(" ");
                    }
                }
            }

            return sb.toString();
        }
    }

    /**
     * Rich text formatter supporting markup.
     * Handles basic formatting like bold, italic, etc.
     */
    public static class RichTextFormatter {

        public List<String> formatRichText(String[] words, int maxWidth) {
            List<FormattedWord> formattedWords = parseWords(words);
            return formatWithMarkup(formattedWords, maxWidth);
        }

        private List<FormattedWord> parseWords(String[] words) {
            List<FormattedWord> result = new ArrayList<>();

            for (String word : words) {
                Set<TextStyle> styles = new HashSet<>();
                String cleanWord = word;

                // Parse basic markup
                if (word.startsWith("**") && word.endsWith("**")) {
                    styles.add(TextStyle.BOLD);
                    cleanWord = word.substring(2, word.length() - 2);
                } else if (word.startsWith("*") && word.endsWith("*")) {
                    styles.add(TextStyle.ITALIC);
                    cleanWord = word.substring(1, word.length() - 1);
                }

                result.add(new FormattedWord(cleanWord, styles));
            }

            return result;
        }

        private List<String> formatWithMarkup(List<FormattedWord> words, int maxWidth) {
            List<String> result = new ArrayList<>();
            int index = 0;

            while (index < words.size()) {
                List<FormattedWord> lineWords = new ArrayList<>();
                int totalLength = 0;

                // Pack words considering display width
                while (index < words.size()) {
                    FormattedWord word = words.get(index);
                    int displayWidth = calculateDisplayWidth(word);
                    int requiredLength = totalLength + (lineWords.isEmpty() ? 0 : 1) + displayWidth;

                    if (requiredLength > maxWidth) break;

                    lineWords.add(word);
                    totalLength = requiredLength;
                    index++;
                }

                String line = formatRichLine(lineWords, maxWidth, index == words.size());
                result.add(line);
            }

            return result;
        }

        private int calculateDisplayWidth(FormattedWord word) {
            // In real implementation, consider actual rendering width
            return word.text.length();
        }

        private String formatRichLine(List<FormattedWord> words, int maxWidth, boolean isLastLine) {
            if (isLastLine || words.size() == 1) {
                return formatLeftAligned(words, maxWidth);
            } else {
                return formatFullJustified(words, maxWidth);
            }
        }

        private String formatLeftAligned(List<FormattedWord> words, int maxWidth) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < words.size(); i++) {
                if (i > 0) sb.append(" ");
                sb.append(renderWord(words.get(i)));
            }

            while (sb.length() < maxWidth) {
                sb.append(" ");
            }

            return sb.toString();
        }

        private String formatFullJustified(List<FormattedWord> words, int maxWidth) {
            StringBuilder sb = new StringBuilder();

            int totalWordWidth = words.stream().mapToInt(this::calculateDisplayWidth).sum();
            int totalSpaces = maxWidth - totalWordWidth;
            int gaps = words.size() - 1;

            if (gaps == 0) {
                return formatLeftAligned(words, maxWidth);
            }

            int spacesPerGap = totalSpaces / gaps;
            int extraSpaces = totalSpaces % gaps;

            for (int i = 0; i < words.size(); i++) {
                sb.append(renderWord(words.get(i)));

                if (i < gaps) {
                    sb.append(repeatSpace(spacesPerGap));
                    if (i < extraSpaces) {
                        sb.append(" ");
                    }
                }
            }

            return sb.toString();
        }

        private String renderWord(FormattedWord word) {
            // In real implementation, apply actual formatting
            String result = word.text;

            if (word.styles.contains(TextStyle.BOLD)) {
                result = "**" + result + "**";
            }
            if (word.styles.contains(TextStyle.ITALIC)) {
                result = "*" + result + "*";
            }

            return result;
        }

        private enum TextStyle {
            BOLD, ITALIC, UNDERLINE
        }

        private static class FormattedWord {
            String text;
            Set<TextStyle> styles;

            FormattedWord(String text, Set<TextStyle> styles) {
                this.text = text;
                this.styles = styles;
            }
        }
    }

    /**
     * Streaming text formatter for large documents.
     * Processes text in chunks to handle memory constraints.
     */
    public static class StreamingTextFormatter {

        public void formatStream(Iterator<String> wordIterator, int maxWidth,
                               StreamingCallback callback) {
            List<String> currentLine = new ArrayList<>();
            int currentLength = 0;

            while (wordIterator.hasNext()) {
                String word = wordIterator.next();
                int requiredLength = currentLength + (currentLine.isEmpty() ? 0 : 1) + word.length();

                if (!currentLine.isEmpty() && requiredLength > maxWidth) {
                    // Output current line
                    String formattedLine = justifyLine(currentLine, maxWidth, false);
                    callback.onLineFormatted(formattedLine);

                    // Start new line
                    currentLine.clear();
                    currentLength = 0;
                }

                currentLine.add(word);
                currentLength = (currentLine.size() == 1) ? word.length() : currentLength + 1 + word.length();
            }

            // Output final line
            if (!currentLine.isEmpty()) {
                String formattedLine = justifyLine(currentLine, maxWidth, true);
                callback.onLineFormatted(formattedLine);
            }
        }

        private String justifyLine(List<String> words, int maxWidth, boolean isLastLine) {
            if (words.size() == 1 || isLastLine) {
                // Left justify
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < words.size(); i++) {
                    if (i > 0) sb.append(" ");
                    sb.append(words.get(i));
                }
                while (sb.length() < maxWidth) {
                    sb.append(" ");
                }
                return sb.toString();
            } else {
                // Full justify
                StringBuilder sb = new StringBuilder();
                int totalWordLength = words.stream().mapToInt(String::length).sum();
                int totalSpaces = maxWidth - totalWordLength;
                int gaps = words.size() - 1;

                int spacesPerGap = totalSpaces / gaps;
                int extraSpaces = totalSpaces % gaps;

                for (int i = 0; i < words.size(); i++) {
                    sb.append(words.get(i));
                    if (i < gaps) {
                        sb.append(repeatSpace(spacesPerGap));
                        if (i < extraSpaces) {
                            sb.append(" ");
                        }
                    }
                }

                return sb.toString();
            }
        }

        public interface StreamingCallback {
            void onLineFormatted(String line);
        }
    }

    /**
     * Performance analysis and benchmarking utilities.
     */
    public static class TextJustificationAnalyzer {

        public static PerformanceMetrics analyze(String[] words, int maxWidth) {
            long startTime = System.nanoTime();

            TextJustification formatter = new TextJustification();
            List<String> result = formatter.fullJustify(words, maxWidth);

            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds

            int totalCharacters = Arrays.stream(words).mapToInt(String::length).sum();
            int totalLines = result.size();
            double avgWordsPerLine = (double) words.length / totalLines;

            return new PerformanceMetrics(
                executionTime,
                totalCharacters,
                totalLines,
                avgWordsPerLine,
                validateJustification(result, maxWidth)
            );
        }

        private static boolean validateJustification(List<String> lines, int maxWidth) {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.length() != maxWidth) {
                    return false;
                }

                // Check if last line is left-justified (except single word)
                if (i == lines.size() - 1) {
                    String trimmed = line.trim();
                    if (!line.startsWith(trimmed)) {
                        return false; // Last line should not have leading spaces
                    }
                }
            }

            return true;
        }

        public static class PerformanceMetrics {
            public final double executionTimeMs;
            public final int totalCharacters;
            public final int totalLines;
            public final double avgWordsPerLine;
            public final boolean isValid;

            PerformanceMetrics(double executionTimeMs, int totalCharacters, int totalLines,
                             double avgWordsPerLine, boolean isValid) {
                this.executionTimeMs = executionTimeMs;
                this.totalCharacters = totalCharacters;
                this.totalLines = totalLines;
                this.avgWordsPerLine = avgWordsPerLine;
                this.isValid = isValid;
            }

            @Override
            public String toString() {
                return String.format(
                    "Execution: %.2fms, Characters: %d, Lines: %d, Avg Words/Line: %.1f, Valid: %s",
                    executionTimeMs, totalCharacters, totalLines, avgWordsPerLine, isValid
                );
            }
        }
    }
}