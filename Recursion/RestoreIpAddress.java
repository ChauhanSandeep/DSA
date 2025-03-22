package recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Given a string of digits, return all possible valid IP addresses.
 * - An IP address consists of **4 octets** (0-255) separated by dots.
 * - Each octet **cannot** have leading zeros (e.g., "01" is invalid).
 * - The input string should be used **entirely**.
 *
 * Approach:
 * - **Backtracking**: Try placing a dot at valid positions and recurse.
 * - **Pruning**: If a segment is invalid, stop early to improve efficiency.
 *
 * Time Complexity: **O(3^4) ≈ O(81)** (Since each segment has at most 3 choices)
 * Space Complexity: **O(4) → O(1)** (Recursion depth is at most 4)
 *
 * LeetCode Link: https://leetcode.com/problems/restore-ip-addresses/
 */
public class RestoreIpAddress {
    public static void main(String[] args) {
        RestoreIpAddress obj = new RestoreIpAddress();
        System.out.println("Valid IPs for '25525511135': " + obj.restoreIpAddresses("25525511135"));
        System.out.println("Valid IPs for '0000': " + obj.restoreIpAddresses("0000"));
    }

    /**
     * Returns all valid IP addresses from the given numeric string.
     *
     * @param s Input string consisting of digits.
     * @return List of valid IP addresses.
     */
    public List<String> restoreIpAddresses(String s) {
        List<String> result = new ArrayList<>();
        if (s.length() < 4 || s.length() > 12) return result;  // Early exit for impossible cases
        backtrack(s, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * Backtracking helper function to generate valid IP addresses.
     *
     * @param s      Input string of digits.
     * @param index  Current index in the string.
     * @param parts  Current segments of the IP address.
     * @param result List to store valid IPs.
     */
    private void backtrack(String s, int index, List<String> parts, List<String> result) {
        if (parts.size() == 4) {
            if (index == s.length()) {
                result.add(String.join(".", parts));
            }
            return;
        }

        for (int len = 1; len <= 3; len++) {
            if (index + len > s.length()) break;

            String segment = s.substring(index, index + len);
            if (isValidSegment(segment)) {
                parts.add(segment);
                backtrack(s, index + len, parts, result);
                parts.remove(parts.size() - 1); // Backtrack
            }
        }
    }

    /**
     * Checks if a string is a valid IP address segment.
     *
     * @param segment String segment.
     * @return True if valid, otherwise false.
     */
    private boolean isValidSegment(String segment) {
        if (segment.length() > 3 || segment.isEmpty()) return false;
        if (segment.startsWith("0") && segment.length() > 1) return false; // "01", "001" are invalid
        int num = Integer.parseInt(segment);
        return num >= 0 && num <= 255;
    }
}
