package arrays;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 2125. Number of Laser Beams in a Bank
 *
 * Problem Statement:
 * Anti-theft security devices are activated inside a bank. You are given a 0-indexed
 * binary string array bank representing the floor plan of the bank, which is an m x n
 * 2D matrix. bank[i] represents the ith row, consisting of '0's and '1's.
 * '0' means the cell is empty, while '1' means the cell has a security device.
 *
 * There is one laser beam between any two security devices if both conditions are met:
 * 1. The two devices are located on two different rows: r1 and r2, where r1 < r2.
 * 2. For each row i where r1 < i < r2, there are no security devices in the ith row.
 *
 * Laser beams are independent. Return the total number of laser beams in the bank.
 *
 * Example:
 * Input: bank = [
 *  "011001",
 *  "000000",
 *  "010100",
 *  "001000"
 *  ]
 * Output: 8
 * Explanation: Row 0 has 3 devices, row 2 has 2 devices, row 3 has 1 device.
 * Beams between row 0 and row 2: 3 × 2 = 6 beams
 * Beams between row 2 and row 3: 2 × 1 = 2 beams
 * Total: 6 + 2 = 8 beams
 *
 * LeetCode Link: https://leetcode.com/problems/number-of-laser-beams-in-a-bank/
 *
 * Follow-up Questions:
 * 1. What if laser beams can pass through other devices but are blocked by walls?
 *    Answer: Modify condition to check for wall characters instead of device characters between rows.
 * 2. How would you handle 3D laser beams between floors?
 *    Answer: Extend to 3D array and check all three dimensions for clear paths between devices.
 * 3. What if we need to find the maximum beams between any two consecutive rows?
 *    Answer: Track maximum product of consecutive non-empty row device counts during iteration.
 * 4. How to optimize for very sparse matrices with few devices?
 *    Answer: Precompute only rows with devices and their counts, then process pairs directly.
 *
 * Related Problems:
 * - 1854. Maximum Population Year: https://leetcode.com/problems/maximum-population-year/
 * - 849. Maximize Distance to Closest Person: https://leetcode.com/problems/maximize-distance-to-closest-person/
 * - 598. Range Addition II: https://leetcode.com/problems/range-addition-ii/
 */
public class NumberOfLaserBeamsInABank {

    /**
     * Counts total laser beams between security devices.
     *
     * Algorithm:
     * 1. For each row, count number of security devices ('1's)
     * 2. Skip rows with no devices (they don't participate in laser beams)
     * 3. For consecutive rows with devices, multiply device counts
     * 4. Sum all products to get total laser beams
     *
     * Time Complexity: O(m*n) where m is rows, n is columns
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param bank array of strings representing bank layout
     * @return total number of laser beams
     */
    public int numberOfBeams(String[] bank) {
        int totalBeams = 0;
        int prevRowDevices = 0;

        for (String row : bank) {
            int currentRowDevices = countDevices(row);

            if (currentRowDevices > 0) {
                // Calculate beams between previous row with devices and current row
                totalBeams += prevRowDevices * currentRowDevices;
                prevRowDevices = currentRowDevices;
            }
            // If current row has no devices, it's skipped (no update to prevRowDevices)
        }

        return totalBeams;
    }

    // Helper method to count security devices in a row
    private int countDevices(String row) {
        int count = 0;
        for (char c : row.toCharArray()) {
            if (c == '1') count++;
        }
        return count;
    }

    /**
     * Alternative using Java 8 streams
     * Time Complexity: O(m*n), Space Complexity: O(m) for intermediate stream operations
     */
    public int numberOfBeamsStream(String[] bank) {
        List<Integer> deviceCounts = Arrays.stream(bank)
                .mapToInt(row -> (int) row.chars().filter(c -> c == '1').count())
                .filter(count -> count > 0)
                .boxed()
                .collect(Collectors.toList());

        int totalBeams = 0;
        for (int i = 0; i < deviceCounts.size() - 1; i++) {
            totalBeams += deviceCounts.get(i) * deviceCounts.get(i + 1);
        }

        return totalBeams;
    }

    /**
     * Optimized single-pass approach
     * Time Complexity: O(m*n), Space Complexity: O(1)
     */
    public int numberOfBeamsOptimized(String[] bank) {
        int totalBeams = 0;
        int lastDeviceCount = 0;

        for (String row : bank) {
            int deviceCount = 0;

            // Count devices in current row
            for (int i = 0; i < row.length(); i++) {
                if (row.charAt(i) == '1') {
                    deviceCount++;
                }
            }

            // If current row has devices
            if (deviceCount > 0) {
                totalBeams += lastDeviceCount * deviceCount;
                lastDeviceCount = deviceCount;
            }
        }

        return totalBeams;
    }

    /**
     * Explicit approach showing the logic clearly
     * Time Complexity: O(m*n), Space Complexity: O(m)
     */
    public int numberOfBeamsExplicit(String[] bank) {
        List<Integer> rowsWithDevices = new ArrayList<>();

        // Collect rows that have devices
        for (String row : bank) {
            int devices = 0;
            for (char c : row.toCharArray()) {
                if (c == '1') devices++;
            }

            if (devices > 0) {
                rowsWithDevices.add(devices);
            }
        }

        // Calculate beams between consecutive rows with devices
        int totalBeams = 0;
        for (int i = 0; i < rowsWithDevices.size() - 1; i++) {
            totalBeams += rowsWithDevices.get(i) * rowsWithDevices.get(i + 1);
        }

        return totalBeams;
    }

    /**
     * Debug version that shows which rows contribute to beam count
     */
    public int numberOfBeamsDebug(String[] bank) {
        int totalBeams = 0;
        int prevRowDevices = 0;
        int prevRowIndex = -1;

        for (int i = 0; i < bank.length; i++) {
            int currentRowDevices = countDevices(bank[i]);

            if (currentRowDevices > 0) {
                if (prevRowDevices > 0) {
                    int beams = prevRowDevices * currentRowDevices;
                    System.out.println(String.format("Row %d (%d devices) -> Row %d (%d devices): %d beams",
                            prevRowIndex, prevRowDevices, i, currentRowDevices, beams));
                    totalBeams += beams;
                }
                prevRowDevices = currentRowDevices;
                prevRowIndex = i;
            }
        }

        return totalBeams;
    }
}
