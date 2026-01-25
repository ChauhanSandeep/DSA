package Recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Leetcode Problem: https://leetcode.com/problems/restore-ip-addresses/
 *
 * Given a string containing only digits, return all possible valid IP address combinations.
 * A valid IP address consists of exactly four integers (each between 0 and 255) separated by dots.
 * Each integer must not have leading zeros unless it is "0".
 *
 * Example:
 * Input: "25525511135"
 * Output: ["255.255.11.135", "255.255.111.35"]
 *
 * Follow-up Questions:
 * 1. What if the string contains non-digit characters?
 *    → Return empty list or throw validation error; current problem restricts to digits only.
 * 2. Can you return only one valid IP?
 *    → Yes. Exit early once the first valid result is found (modify recursion).
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class RestoreIpAddress {

    public static void main(String[] args) {
        RestoreIpAddress resolver = new RestoreIpAddress();
        System.out.println("Valid IPs for '25525511135': " + resolver.restoreIpAddresses("25525511135"));
        System.out.println("Valid IPs for '0000': " + resolver.restoreIpAddresses("0000"));
    }

    /**
     * Returns all valid IP address combinations that can be formed from the input string.
     *
     * Steps:
     * 1. Early return if the input length is invalid for an IP (must be 4 to 12 characters).
     * 2. Use backtracking to explore all segmentations into 4 parts.
     * 3. Each segment must be valid: 0–255 with no leading zero (except "0").
     * 4. Collect combinations that consume all characters and have exactly 4 parts.
     *
     * Time Complexity: O(3^4) → Since each part can be 1 to 3 digits and there are 4 parts.
     * Space Complexity: O(1) auxiliary (ignoring output list)
     *
     * @param s Input string of digits.
     * @return List of all valid IP addresses.
     */
    public List<String> restoreIpAddresses(String s) {
        List<String> validIPs = new ArrayList<>();
        if (s == null || s.length() < 4 || s.length() > 12) return validIPs;

        backtrack(s, 0, new ArrayList<>(), validIPs);
        return validIPs;
    }

    /**
     * Recursive backtracking method to explore valid segmentations.
     *
     * @param input     Original input string.
     * @param index     Current index in the string.
     * @param segments  List of current segments built so far.
     * @param results   List to collect all valid IP addresses.
     */
    private void backtrack(String input, int index, List<String> segments, List<String> results) {
        // Base case: exactly 4 segments and consumed all characters
        if (segments.size() == 4) {
            if (index == input.length()) {
                results.add(String.join(".", segments));
            }
            return;
        }

        // Try segment lengths of 1 to 3
        for (int len = 1; len <= 3; len++) {
            if (index + len > input.length()) break;

            String candidate = input.substring(index, index + len);
            if (isValidSegment(candidate)) {
                segments.add(candidate);
                backtrack(input, index + len, segments, results);
                segments.remove(segments.size() - 1); // Backtrack step
            }
        }
    }

    /**
     * Validates a potential IP segment.
     *
     * Rules:
     * - Should be a number between 0 and 255
     * - Cannot have leading zeros unless it's exactly "0"
     *
     * @param segment Substring segment to validate.
     * @return True if valid, else false.
     */
    private boolean isValidSegment(String segment) {
        if (segment == null || segment.isEmpty() || segment.length() > 3) return false;
        if (segment.startsWith("0") && segment.length() > 1) return false;

        int value = Integer.parseInt(segment);
        return value >= 0 && value <= 255;
    }
}