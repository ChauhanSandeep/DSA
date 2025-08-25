package arrays;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Number Of Laser Beams In A Bank
 *
 * Problem: Given bank layout with security devices ('1') and empty space ('0'),
 * calculate total laser beams. Each device shoots lasers to all devices in next row with devices.
 *
 * Example: bank = ["011001","000000","010100","001000"] -> Output: 8
 * Row 0 has 3 devices, row 2 has 2 devices, row 3 has 1 device.
 * Beams: 3*2 (row 0 to 2) + 2*1 (row 2 to 3) = 6 + 2 = 8.
 *
 * LeetCode: https://leetcode.com/problems/number-of-laser-beams-in-a-bank
 *
 * Follow-up Questions:
 * - What if devices can shoot in all directions? (Consider all pairs across all rows)
 * - How to handle 3D bank layout? (Extend logic to 3D grid)
 * - What if different devices have different ranges? (Add range constraints)
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
