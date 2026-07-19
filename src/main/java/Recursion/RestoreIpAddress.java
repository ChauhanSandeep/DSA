package Recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Restore IP Addresses
 *
 * Given a string containing only digits, return all valid IP addresses that can
 * be formed by inserting three dots. A valid address has exactly four parts;
 * each part is between 0 and 255 and has no leading zero unless the part is "0".
 *
 * Leetcode: https://leetcode.com/problems/restore-ip-addresses/ (Medium)
 * Rating:   acceptance 56.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Recursion | Backtracking | Fixed-depth segmentation
 *
 * Example:
 *   Input:  s = "25525511135"
 *   Output: [255.255.11.135, 255.255.111.35]
 *   Why:    both outputs split all digits into four valid parts; alternatives
 *           either leave digits unused, create too many parts, or exceed 255.
 *
 * Follow-ups:
 *   1. How would you return only the first valid IP address?
 *      Stop recursion as soon as one result is found and bubble that success upward.
 *   2. What if the input may contain non-digits?
 *      Validate before recursing and return an empty list or throw an error by contract.
 *   3. How would you count valid restorations without storing strings?
 *      Keep the same recursion but increment a counter when four valid segments consume the input.
 *   4. How would this generalize to IPv6-style groups?
 *      Change the segment count, length bounds, and validation rule, while keeping fixed-depth DFS.
 */
public class RestoreIpAddress {

    public static void main(String[] args) {
        RestoreIpAddress solver = new RestoreIpAddress();

        String[] inputs = { "1111", "0000", "25525511135" };
        String[] expected = {
            "[1.1.1.1]",
            "[0.0.0.0]",
            "[255.255.11.135, 255.255.111.35]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<String> got = solver.restoreIpAddresses(inputs[i]);
            System.out.printf("s=\"%s\" -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

    /**
     * Intuition: restoring dots is a fixed-depth decision tree. At each level,
     * the choice is how long the next segment should be: 1, 2, or 3 digits. The
     * base case is four chosen segments, and it only counts if those four
     * segments have consumed the entire string. Segment validation prunes illegal
     * branches early because leading zeros and values above 255 can never become
     * valid by adding later dots.
     *
     * Algorithm:
     *   1. Return empty for null input or lengths outside the 4 to 12 range.
     *   2. Backtrack from index 0 with an empty segments list.
     *   3. Stop when four segments are chosen, recording only if all characters are consumed.
     *   4. Try segment lengths 1 to 3; choose valid segments, recurse, then un-choose them.
     *
     * Time:  O(3^4) - there are four segment positions and each tries at most three lengths.
     * Space: O(1) - recursion depth is capped at four segments, excluding the output strings.
     *
     * @param s digit string to split into an IP address
     * @return all valid IP addresses that can be formed from s
     */
    public List<String> restoreIpAddresses(String s) {
        List<String> validIPs = new ArrayList<>();
        if (s == null || s.length() < 4 || s.length() > 12) return validIPs;

        backtrack(s, 0, new ArrayList<>(), validIPs);
        return validIPs;
    }

    /** Chooses up to four valid IP segments and records addresses that consume the whole input. */
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

    /** Checks the numeric range and leading-zero rule for one IP segment. */
    private boolean isValidSegment(String segment) {
        if (segment == null || segment.isEmpty() || segment.length() > 3) return false;
        if (segment.startsWith("0") && segment.length() > 1) return false;

        int value = Integer.parseInt(segment);
        return value >= 0 && value <= 255;
    }
}