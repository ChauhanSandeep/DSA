package Array;

import java.util.*;

/**
 * Height Checker
 * 
 * Problem: Count students not in correct positions compared to non-decreasing height order.
 * 
 * Example: heights = [1,1,4,2,1,3] -> Output: 3
 * Expected order: [1,1,1,2,3,4]. Indices 2,4,5 are incorrect.
 * 
 * LeetCode: https://leetcode.com/problems/height-checker
 * 
 * Follow-up Questions:
 * - How to get indices of misplaced students? (Store indices instead of counting)
 * - What if we want students in decreasing order? (Sort in reverse order)
 * - Can we solve without sorting? (Use counting sort since heights are bounded)
 */
public class HeightChecker {

    /**
     * Counts students not in correct height order position.
     * 
     * Algorithm:
     * 1. Create expected array by sorting the heights
     * 2. Compare original with expected position by position
     * 3. Count positions where heights don't match
     * 
     * Time Complexity: O(n log n) due to sorting
     * Space Complexity: O(n) for the expected array
     * 
     * @param heights array of student heights
     * @return count of students in wrong positions
     */
    public int heightChecker(int[] heights) {
        int[] expected = heights.clone();
        Arrays.sort(expected);

        int count = 0;
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] != expected[i]) {
                count++;
            }
        }

        return count;
    }

    /**
     * Optimized counting sort approach (heights bounded by 100)
     * Time Complexity: O(n), Space Complexity: O(1) - fixed size array
     */
    public int heightCheckerCountingSort(int[] heights) {
        // Count frequency of each height (1 to 100)
        int[] freq = new int[101];
        for (int height : heights) {
            freq[height]++;
        }

        int count = 0;
        int currentHeight = 1;

        for (int i = 0; i < heights.length; i++) {
            // Find next height that should be at position i
            while (freq[currentHeight] == 0) {
                currentHeight++;
            }

            // Compare with actual height at position i
            if (heights[i] != currentHeight) {
                count++;
            }

            // Decrease frequency of current expected height
            freq[currentHeight]--;
        }

        return count;
    }

    /**
     * Stream-based approach for functional style
     * Time Complexity: O(n log n), Space Complexity: O(n)
     */
    public int heightCheckerStream(int[] heights) {
        int[] expected = Arrays.stream(heights).sorted().toArray();

        return (int) IntStream.range(0, heights.length)
                .filter(i -> heights[i] != expected[i])
                .count();
    }

    /**
     * Helper method to get actual misplaced indices (for debugging)
     */
    public List<Integer> getMisplacedIndices(int[] heights) {
        int[] expected = heights.clone();
        Arrays.sort(expected);

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] != expected[i]) {
                indices.add(i);
            }
        }

        return indices;
    }
}
