package Array;

/**
 * ✅ Problem: Container With Most Water
 *
 * You are given an integer array `height` where each element represents a vertical line on the x-axis.
 * Find two lines that, together with the x-axis, form a container that holds the **maximum amount of water**.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/container-with-most-water/
 *
 * 🧠 Example:
 * Input:  [1, 8, 6, 2, 5, 4, 8, 3, 7]
 * Output: 49
 * Explanation: Lines at index 1 and 8 with height 8 and 7 give the max area: 7 × (8 - 1) = 49.
 *
 * 🔍 Follow-up Discussions:
 * 1. Why does the greedy two-pointer strategy work?
 *    ➤ Because the area depends on the shorter line, and moving the taller one won’t improve area.
 * 2. Can you optimize it using prefix/suffix arrays?
 *    ➤ No. Brute force would be O(n²), but two-pointer gives optimal O(n).
 * 3. Real-world extension?
 *    ➤ Rainwater harvesting, reservoir design simulations.
 */
public class ContainerWithMostWater {

    public static void main(String[] args) {
        int[] heights = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int result = getMaxWaterStored(heights);
        System.out.println("Max water that can be stored: " + result);
    }

    /**
     * Uses a greedy two-pointer strategy to find the max area.
     *
     * Key Insight:
     * - The water stored is determined by the shorter line among the two.
     * - To maximize area, move the pointer pointing to the shorter height inward.
     *
     * Time Complexity: O(n), where n = number of lines
     * Space Complexity: O(1), no extra space used
     *
     * @param heights Array of vertical line heights
     * @return Maximum water that can be stored between two lines
     */
    public static int getMaxWaterStored(int[] heights) {
        if (heights == null || heights.length < 2) return 0;

        int left = 0;
        int right = heights.length - 1;
        int maxWater = 0;

        while (left < right) {
            // Calculate current container area
            int minHeight = Math.min(heights[left], heights[right]);
            int width = right - left;
            int currentArea = minHeight * width;

            // Update max if current area is greater
            maxWater = Math.max(maxWater, currentArea);

            // Move pointer with smaller height to try to improve minHeight
            if (heights[left] < heights[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxWater;
    }
}