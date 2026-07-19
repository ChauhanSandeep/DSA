package strings.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Reorder Data in Log Files
 *
 * Reorder logs so all letter-logs come before digit-logs. Letter-logs are sorted
 * by content and then identifier, while digit-logs keep their original relative
 * order.
 *
 * Leetcode: https://leetcode.com/problems/reorder-data-in-log-files/ (Easy)
 * Rating:   acceptance 56.8% (Easy), contest rating 1387
 * Pattern:  Sorting | Stable partition | Custom comparator
 *
 * Example:
 *   Input:  logs = ["a1 9 2 3 1", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo", "a2 act car"]
 *   Output: ["a2 act car", "g1 act car", "a8 act zoo", "ab1 off key dog", "a1 9 2 3 1", "zo4 4 7"]
 *   Why:    letter contents sort first, ties use identifiers, and digit logs remain in input order.
 *
 * Follow-ups:
 *   1. Reorder in place?
 *      Hard with stable digit-log order; usually extra storage or stable partitioning is used.
 *   2. Scale to logs on disk?
 *      External-sort letter logs and append digit logs in streaming order.
 *   3. Support mixed-case letter logs?
 *      Normalize or define a case-aware comparator for contents and identifiers.
 *
 * Related: Custom Sort String (791), Sort Characters By Frequency (451).
 */
public class ReOrderLogFile {

    public static void main(String[] args) {
        String[][] inputs = {
            {"a1 9 2 3 1", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo", "a2 act car"},
            {"dig1 8 1 5 1", "let1 art can", "dig2 3 6", "let2 own kit dig", "let3 art zero"}
        };
        String[][] expected = {
            {"a2 act car", "g1 act car", "a8 act zoo", "ab1 off key dog", "a1 9 2 3 1", "zo4 4 7"},
            {"let1 art can", "let3 art zero", "let2 own kit dig", "dig1 8 1 5 1", "dig2 3 6"}
        };

        for (int i = 0; i < inputs.length; i++) {
            String[] got = reorderLogFiles(inputs[i]);
            System.out.printf("logs=%s -> %s  expected=%s%n",
                java.util.Arrays.toString(inputs[i]), java.util.Arrays.toString(got), java.util.Arrays.toString(expected[i]));
        }
    }


    /**
     * Intuition: digit-logs do not need sorting, but letter-logs do. Separating the
     * two types makes the rules direct: sort only the letter list by content then
     * identifier, then append the untouched digit list.
     *
     * Algorithm:
     *   1. Return logs directly when the input is null or empty.
     *   2. Split logs into letterLogs and digitLogs using the first character after the identifier.
     *   3. Sort letterLogs by content, breaking ties by identifier.
     *   4. Append digitLogs after letterLogs and return the combined array.
     *
     * Time:  O(n log n) - sorting the letter logs dominates.
     * Space: O(n) - separate lists store the reordered logs.
     *
     * @param logs input log strings
     * @return logs reordered by the problem rules
     */
    public static String[] reorderLogFiles(String[] logs) {
        if (logs == null || logs.length == 0) return logs;

        List<String> letterLogs = new ArrayList<>();
        List<String> digitLogs = new ArrayList<>();

        // Step 1: Separate letter-logs and digit-logs
        for (String log : logs) {
            if (isDigitLog(log)) {
                digitLogs.add(log);
            } else {
                letterLogs.add(log);
            }
        }

        // Step 2: Sort letter-logs based on content, then identifier
        letterLogs.sort((log1, log2) -> {
            String[] split1 = log1.split(" ", 2);
            String[] split2 = log2.split(" ", 2);
            String identifier1 = split1[0];
            String identifier2 = split2[0];
            String content1 = split1[1];
            String content2 = split2[1];

            int cmp = content1.compareTo(content2);
            if (cmp != 0) {
                return cmp;
            } else {
                return identifier1.compareTo(identifier2);
            }
        });

        // Step 3: Merge sorted letter-logs and original-order digit-logs
        letterLogs.addAll(digitLogs);
        return letterLogs.toArray(new String[0]); // This converts List to String array
    }

    /** Returns true when the log content starts with a digit. */
    private static boolean isDigitLog(String log) {
        String[] parts = log.split(" ", 2);
        return Character.isDigit(parts[1].charAt(0));
    }
}
