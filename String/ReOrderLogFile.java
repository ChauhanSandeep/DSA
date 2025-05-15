package string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Reorder Log Files
 *
 * Intuition:
 * - Letter-logs come before digit-logs.
 * - Letter-logs are sorted lexicographically by content (ignoring identifier).
 * - If contents are identical, sort by identifier.
 * - Digit-logs retain their original order.
 *
 * Algorithm:
 * - Separate letter-logs and digit-logs.
 * - Sort letter-logs using a custom comparator.
 * - Concatenate sorted letter-logs and original digit-logs.
 *
 * Time Complexity: O(M log M) - Sorting letter-logs (where M is the number of letter-logs).
 * Space Complexity: O(M) - To store letter-logs separately.
 */
public class ReorderLogFiles {

    public static void main(String[] args) {
        String[] logs = {
            "a1 9 2 3 1", "g1 act car", "zo4 4 7",
            "ab1 off key dog", "a8 act zoo", "a2 act car"
        };

        String[] result = reorderLogFiles(logs);
        System.out.println(Arrays.toString(result));
    }

    /**
     * Reorders log files so that:
     * - Letter-logs appear before digit-logs.
     * - Letter-logs are sorted lexicographically by content, and then by identifier.
     * - Digit-logs maintain their relative order.
     *
     * @param logs Array of log entries.
     * @return Reordered logs.
     */
    public static String[] reorderLogFiles(String[] logs) {
        if (logs == null || logs.length == 0) return logs;

        List<String> letterLogs = new ArrayList<>();
        List<String> digitLogs = new ArrayList<>();

        // Separate letter and digit logs
        for (String log : logs) {
            if (isDigitLog(log)) {
                digitLogs.add(log);
            } else {
                letterLogs.add(log);
            }
        }

        // Sort letter logs: First by content, then by identifier
        letterLogs.sort((log1, log2) -> {
            String[] split1 = log1.split(" ", 2);
            String[] split2 = log2.split(" ", 2);

            int contentComparison = split1[1].compareTo(split2[1]);
            return contentComparison != 0 ? contentComparison : split1[0].compareTo(split2[0]);
        });

        // Combine sorted letter logs and original order digit logs
        letterLogs.addAll(digitLogs);
        return letterLogs.toArray(new String[0]);
    }

    /**
     * Checks if a log is a digit-log (i.e., content after the identifier consists of numbers).
     *
     * @param log Log entry.
     * @return true if it's a digit-log, false otherwise.
     */
    private static boolean isDigitLog(String log) {
        String[] parts = log.split(" ", 2);
        return Character.isDigit(parts[1].charAt(0));
    }
}
