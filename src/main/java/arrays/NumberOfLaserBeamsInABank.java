package arrays;

import java.util.*;

/**
 * Problem: Number of Laser Beams in a Bank
 *
 * Anti-theft security devices are activated inside a bank. You are given a 0-indexed
 * binary string array bank representing the floor plan of the bank, which is an m x n
 * 2D matrix. bank[i] represents the ith row, consisting of '0's and '1's. '0' means
 * the cell is empty, while '1' means the cell has a security device.
 *
 * There is one laser beam between any two security devices if both of the following
 * conditions are met:
 * - The two devices are located on two different rows: r1 and r2 where r1 < r2
 * - For each row i where r1 < i < r2, there are no security devices in the ith row
 *
 * Laser beams are independent, meaning one beam does not interfere with another.
 * Return the total number of laser beams in the bank.
 *
 * Example:
 * Input: bank = ["011001","000000","010100","001000"]
 * Output: 8
 * Explanation:
 * - Row 0: 3 devices, Row 2: 2 devices → 3×2 = 6 beams
 * - Row 2: 2 devices, Row 3: 1 device → 2×1 = 2 beams
 * - Total: 6 + 2 = 8 beams
 *
 * Input: bank = ["000","111","000"]
 * Output: 0
 * Explanation: No two rows with devices have an empty row between them.
 *
 * LeetCode: https://leetcode.com/problems/number-of-laser-beams-in-a-bank/
 *
 * Follow-up Questions:
 * 1. Q: What if laser beams could form within the same row?
 *    A: Would need to add C(n,2) combinations for each row with n devices.
 *
 * 2. Q: How would you handle 3D laser beams between floors?
 *    A: Similar approach but tracking connections between 2D layers instead of 1D rows.
 *
 * 3. Q: What if some devices could block laser beams?
 *    A: Would need more complex path-finding to check if devices can "see" each other.
 *
 * 4. Q: How would you optimize for very sparse matrices?
 *    A: Could precompute row indices with devices and only process those rows.
 *
 * Related Problems:
 * - Number of Islands: https://leetcode.com/problems/number-of-islands/
 * - Pacific Atlantic Water Flow: https://leetcode.com/problems/pacific-atlantic-water-flow/
 * - Battleships in a Board: https://leetcode.com/problems/battleships-in-a-board/
 * LeetCode Contest Rating: 1280
 **/
public class NumberOfLaserBeamsInABank {

    /**
     * Counts laser beams using sequential row processing approach.
     *
     * Algorithm:
     * 1. For each row, count the number of security devices ('1's)
     * 2. Only consider rows with at least one device (skip empty rows)
     * 3. Each device in current row forms beam with each device in previous non-empty row
     * 4. Multiply device counts between consecutive non-empty rows
     * 5. Sum all beam counts across the entire bank
     *
     * Time Complexity: O(m*n) where m is number of rows and n is row length
     * Space Complexity: O(1) using constant extra space
     *
     * @param bank array of binary strings representing bank floor plan
     * @return total number of laser beams between security devices
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

