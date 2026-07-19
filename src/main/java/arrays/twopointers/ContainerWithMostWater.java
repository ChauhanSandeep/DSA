package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Container With Most Water
 *
 * Given vertical line heights, choose two lines that together with the x-axis
 * hold the maximum possible water. The container cannot be slanted, so area is
 * width times the shorter selected height.
 *
 * Leetcode: https://leetcode.com/problems/container-with-most-water/ (Medium)
 * Rating:   acceptance 60.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Greedy shrink from shorter wall
 *
 * Example:
 *   Input:  height = [1,8,6,2,5,4,8,3,7]
 *   Output: 49
 *   Why:    indices 1 and 8 give width 7 and limiting height 7, so area is 49.
 *
 * Follow-ups:
 *   1. Return the pair of indices as well?
 *      Store leftIndex and rightIndex whenever maxArea improves.
 *   2. Find the top k containers?
 *      Use a heap over candidate pairs, but expect higher complexity than this pass.
 *   3. What if line widths are not uniform?
 *      Replace index difference with prefix-distance between the two chosen lines.
 *
 * Related: Trapping Rain Water (42), Valid Triangle Number (611).
 */
public class ContainerWithMostWater {

public static void main(String[] args) {
    int[][] inputs = { {1, 8, 6, 2, 5, 4, 8, 3, 7}, {1, 1} };
    int[] expected = { 49, 1 };

    for (int i = 0; i < inputs.length; i++) {
        int got = maxArea(inputs[i]);
        System.out.printf("height=%s -> %d  expected=%d%n",
            Arrays.toString(inputs[i]), got, expected[i]);
    }
}
    /**
 * Intuition: area loses width every time pointers move inward, so the only
 * possible improvement is finding a taller limiting wall. If the left wall is
 * shorter, keeping it and moving the right wall cannot help because the same
 * short wall still caps the height; therefore move the shorter side.
 *
 * Algorithm:
 *   1. Start leftIndex at the first line and rightIndex at the last line.
 *   2. Compute the area from the current width and shorter height.
 *   3. Update maxArea with the best area seen so far.
 *   4. Move the pointer whose line is shorter, then repeat until pointers meet.
 *
 * Time:  O(n) - each pointer moves inward at most n times total.
 * Space: O(1) - only pointer and area variables are kept.
 *
 * @param height array of line heights
 * @return maximum water area that any two lines can contain
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