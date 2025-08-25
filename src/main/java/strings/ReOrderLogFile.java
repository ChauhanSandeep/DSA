package string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode Problem: https://leetcode.com/problems/reorder-data-in-log-files/
 *
 * Problem Statement:
 * You are given an array of logs. Each log is a space-delimited string where the first word is an identifier.
 * There are two types of logs:
 *   1. Letter-logs: All words after the identifier consist of lowercase English letters.
 *   2. Digit-logs: All words after the identifier consist of digits.
 *
 * Your task is to reorder these logs as follows:
 *   - All letter-logs come before digit-logs.
 *   - Letter-logs are sorted lexicographically by content. If content is the same, sort by identifier.
 *   - Digit-logs should retain their original relative order.
 *
 * Example:
 * Input:  ["a1 9 2 3 1", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo", "a2 act car"]
 * Output: ["a2 act car", "g1 act car", "a8 act zoo", "ab1 off key dog", "a1 9 2 3 1", "zo4 4 7"]
 *
 * LeetCode: https://leetcode.com/problems/reorder-data-in-log-files/
 *
 * Follow-up Questions:
 * 1. Can you perform this in-place?
 * — Not easily without auxiliary space because sorting of letter-logs is required.
 * 2. How would you scale this for streaming logs?
 * — Use a priority queue or external merge sort with file buffers.
 */
public class ReOrderLogFile {

    public static void main(String[] args) {
        String[] logs = {
            "a1 9 2 3 1", "g1 act car", "zo4 4 7",
            "ab1 off key dog", "a8 act zoo", "a2 act car"
        };

        String[] result = reorderLogFiles(logs);
        System.out.println(Arrays.toString(result));
    }

    /**
     * Reorders the logs with letter-logs first (sorted by content and identifier),
     * followed by digit-logs (in original order).
     *
     * Steps:
     * 1. Separate letter-logs and digit-logs.
     * 2. Sort letter-logs:
     *    - First by log content.
     *    - If contents are equal, by identifier.
     * 3. Append digit-logs at the end.
     *
     * Time Complexity: O(N log N) where N = number of letter-logs (due to sorting)
     * Space Complexity: O(N) to store separate letter and digit logs
     *
     * @param logs Array of log strings
     * @return Reordered log array
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

    /**
     * Checks whether a given log is a digit-log.
     *
     * @param log The log string
     * @return true if it's a digit-log; false if it's a letter-log
     */
    private static boolean isDigitLog(String log) {
        String[] parts = log.split(" ", 2);
        return Character.isDigit(parts[1].charAt(0));
    }
}
