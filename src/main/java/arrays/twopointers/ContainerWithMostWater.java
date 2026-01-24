package arrays.twopointers;

/**
 * Problem: Container With Most Water
 *
 * You are given an integer array height of length n. There are n vertical lines
 * drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).
 *
 * Find two lines that together with the x-axis form a container, such that the
 * container can hold the most water. Return the maximum area of water you can contain.
 *
 * You may not slant the container.
 *
 * Example:
 * Input: height = [1,8,6,2,5,4,8,3,7]
 * Output: 49
 * Explanation: The vertical lines are at indices 1 and 8.
 * Area = min(8, 7) * (8 - 1) = 7 * 7 = 49
 * Container between index 1 and 8: height = 7, width = 7, area = 49
 *
 * Constraints:
 * - 2 <= height.length <= 10^5
 * - 0 <= height[i] <= 10^4
 *
 * LeetCode Problem: https://leetcode.com/problems/container-with-most-water
 *
 * Follow-up Questions:
 *
 * 1. What if you need to find the indices of the two lines, not just the area?
 *    Answer: Track the pointers when maximum area is found. Store (left, right)
 *    indices along with the maximum area for returning both.
 *
 * 2. How would you find all containers with area > threshold?
 *    Answer: Collect all container areas during the two-pointer traversal. Filter
 *    those greater than threshold with their corresponding indices.
 *
 * 3. What if we want to maximize perimeter instead of area?
 *    Answer: Perimeter = 2 * (width + height). Similar two-pointer approach but
 *    update maximum based on perimeter formula instead of area.
 *
 * 4. Can you handle if heights can be negative?
 *    Answer: For negative heights, area calculation changes. If both negative,
 *    area is still positive. Add abs() or handle separately based on requirements.
 *
 * 5. How would you find k containers with largest areas?
 *    Answer: Use min-heap of size k. Store all possible (area, left, right) tuples.
 *    Extract k largest. Time: O(n log k), Space: O(k).
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ContainerWithMostWater {

    public static void main(String[] args) {
        int[] heights = {1, 8, 6, 2, 5, 4, 8, 3, 7};

        int result = maxArea(heights);
        System.out.println("Max water that can be stored: " + result);
    }
    /**
     * Finds maximum water area using two-pointer approach.
     *
     * Algorithm:
     * 1. Initialize left pointer at start, right pointer at end
     * 2. Calculate area with current pointers: width * min(height[left], height[right])
     * 3. Track maximum area found
     * 4. Move the pointer with smaller height inward (towards each other)
     * 5. Repeat until pointers meet
     *
     * Key insight: Area is limited by the shorter line (bottleneck). When width
     * decreases (by moving pointers inward), only way to increase area is to find
     * a taller line. Therefore, always move the shorter line's pointer because:
     * - If we move the taller line, width decreases but height can only stay same or decrease
     * - If we move the shorter line, we have chance to find a taller line to increase height
     *
     * Proof: We can never improve the container by keeping shorter line and moving
     * taller line. The area is limited by shorter line, and moving taller line only
     * reduces width. So any container formed would be strictly worse.
     *
     * Time Complexity: O(N) where N is array length. Each element visited at most once.
     *
     * Space Complexity: O(1) using only constant extra variables.
     *
     * @param height array of integers representing line heights
     * @return maximum area of water that can be contained
     */
    public static int maxArea(int[] height) {
        int leftIndex = 0;
        int rightIndex = height.length - 1;
        int maxArea = 0;

        while (leftIndex < rightIndex) {
            // Calculate current container area
            int width = rightIndex - leftIndex;
            int currentHeight = Math.min(height[leftIndex], height[rightIndex]);
            int currentArea = width * currentHeight;

            // Update maximum area
            maxArea = Math.max(maxArea, currentArea);

            // Move the pointer with shorter height
            if (height[leftIndex] < height[rightIndex]) {
                leftIndex++;
            } else {
                rightIndex--;
            }
        }

        return maxArea;
    }
}