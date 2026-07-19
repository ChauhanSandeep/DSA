package strings;

/**
 * Problem: Time Needed to Rearrange a Binary String
 *
 * Each second, every "01" substring changes to "10" simultaneously. Return the
 * seconds needed until no "01" remains.
 *
 * Leetcode: https://leetcode.com/problems/time-needed-to-rearrange-a-binary-string/ (Medium)
 * Rating:   zerotrac 1481 (Q2, biweekly-85)
 * Pattern:  String | Simulation | One-pass finish time
 *
 * Example:
 *   Input:  s = "0110101"
 *   Output: 4
 *   Why:    after four simultaneous rounds the string has all ones before zeros.
 *
 * Follow-ups:
 *   1. Avoid simulation? Track zeros before each '1' and previous finish time.
 *   2. Reverse operation? Mirror the scan from right to left.
 *   3. Limited swaps per second? Use a capacity-aware queue or counting simulation.
 */
public class TimeNeededToRearrangeBinaryString {

    public static void main(String[] args) {
        TimeNeededToRearrangeBinaryString solver = new TimeNeededToRearrangeBinaryString();
        String[] inputs = {"0110101", "11100", "0001", "01"};
        int[] expected = {4, 0, 3, 1};
        for (int i = 0; i < inputs.length; i++) {
            int got = solver.secondsToRemoveOccurrences(inputs[i]);
            System.out.printf("s=%s -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


        /**
     * Intuition: simulate the rule exactly. One scan performs every non-overlapping
     * "01" to "10" swap for that second; if a scan swaps nothing, the string is done.
     *
     * Algorithm:
     *   1. Return 0 for null or short strings.
     *   2. Convert to a char array.
     *   3. Repeatedly scan, swap each "01", and skip the next index.
     *   4. Count scans that made at least one swap.
     *
     * Time:  O(n^2) - up to O(n) scans, each O(n).
     * Space: O(n) - mutable character buffer.
     */
    public int secondsToRemoveOccurrencesBrute(String str) {
        if (str == null || str.length() < 2) return 0;

        char[] charArray = str.toCharArray();
        int seconds = 0;

        while (true) {
            boolean swapped = false;
            for (int i = 0; i < charArray.length - 1; i++) {
                if (charArray[i] == '0' && charArray[i + 1] == '1') {
                    charArray[i] = '1';
                    charArray[i + 1] = '0';
                    i++;            // non-overlapping: swaps happen simultaneously
                    swapped = true;
                }
            }
            if (!swapped) return seconds;
            seconds++;
        }
    }

        /**
     * Intuition: each '1' must cross all zeros before it, but it may also wait
     * behind an earlier moving '1'. Its finish time is the later of free travel
     * through zeros and one second after the previous moving '1'.
     *
     * Algorithm:
     *   1. Return 0 for null or short strings.
     *   2. Scan left to right while counting zeros.
     *   3. For each '1' after a zero, update max(zeros, lastFinishTime + 1).
     *   4. Return lastFinishTime.
     *
     * Time:  O(n) - one scan of the string.
     * Space: O(1) - only counters are stored.
     */
    public int secondsToRemoveOccurrences(String s) {
        if (s == null || s.length() < 2) return 0;

        int zeros = 0;
        int lastFinishTime = 0;

        for (char c : s.toCharArray()) {
            if (c == '0') {
                zeros++;
            } else if (zeros > 0) {
                // Option 1: Pass through all zeros before this '1'
                int freeTravelFinish = zeros;

                // Option 2: Wait for the previous '1' to finish, then move
                int blockedFinish = lastFinishTime + 1;

                // The '1' finishes at whichever time is later
                lastFinishTime = Math.max(freeTravelFinish, blockedFinish);
            }
        }
        return lastFinishTime;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    }

