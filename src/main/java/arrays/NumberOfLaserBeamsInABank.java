package arrays;

import java.util.*;

/**
 * Problem: Number of Laser Beams in a Bank
 *
 * Each bank row is a binary string where 1 marks a security device. Beams are
 * formed only between devices in two different non-empty rows when every row
 * between them has no devices. Return the total number of beams.
 *
 * Leetcode: https://leetcode.com/problems/number-of-laser-beams-in-a-bank/ (Medium)
 * Rating:   1280 (zerotrac Elo)
 * Pattern:  Array | String scan | Consecutive non-empty rows
 *
 * Example:
 *   Input:  bank = ["011001","000000","010100","001000"]
 *   Output: 8
 *   Why:    row 0 has 3 devices and row 2 has 2, making 6 beams; row 2 and row 3
 *           add 2 more beams, and the empty row is skipped.
 *
 * Follow-ups:
 *   1. Count beams within the same row too?
 *      Add count * (count - 1) / 2 for each row before handling cross-row beams.
 *   2. Return the row pairs that contribute beams?
 *      Store the previous non-empty row index along with its device count.
 *   3. Optimize for very sparse, very long rows?
 *      Store device counts per row while parsing sparse positions instead of scanning full strings repeatedly.
 *
 * Related: Battleships in a Board (419), Number of Islands (200).
 */
public class NumberOfLaserBeamsInABank {

    public static void main(String[] args) {
        NumberOfLaserBeamsInABank solver = new NumberOfLaserBeamsInABank();

        String[][] inputs = {
            {"011001", "000000", "010100", "001000"},
            {"000", "111", "000"},
            {"0"}
        };
        int[] expected = { 8, 0, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.numberOfBeams(inputs[i]);
            System.out.printf("bank=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: empty rows do not create beams and do not block beams between the
     * nearest non-empty rows. Therefore only consecutive non-empty rows matter: if the
     * previous non-empty row has a devices and the current row has b devices, they add
     * a * b beams.
     *
     * Algorithm:
     *   1. Return 0 for null or empty input.
     *   2. Scan each row and count its devices.
     *   3. For every non-empty row, add previousRowDeviceCount * currentRowDeviceCount.
     *   4. Update previousRowDeviceCount to the current non-empty row count.
     *
     * Time:  O(r * c) - every character in the bank strings is inspected once.
     * Space: O(1) - only the previous row count and total are stored.
     *
     * @param bank binary strings representing bank rows
     * @return total number of valid laser beams
     */
    public int numberOfBeams(String[] bank) {
        if (bank == null || bank.length == 0) {
            return 0;
        }

        int totalBeams = 0;
        int previousRowDeviceCount = 0;

        // Process each row to count devices and calculate beams
        for (String currentRow : bank) {
            int currentRowDeviceCount = countDevicesInRow(currentRow);

            // Only process rows that have security devices
            if (currentRowDeviceCount > 0) {
                // Each device in current row connects to each device in previous row
                totalBeams += previousRowDeviceCount * currentRowDeviceCount;

                // Update previous row count for next iteration
                previousRowDeviceCount = currentRowDeviceCount;
            }
            // Skip empty rows - they don't block beams between non-empty rows
        }

        return totalBeams;
    }

    // Helper method to count number of '1's (devices) in a row
    private int countDevicesInRow(String row) {
        int count = 0;
        for (char cell : row.toCharArray()) {
            if (cell == '1') {
                count++;
            }
        }
        return count;
    }
}

