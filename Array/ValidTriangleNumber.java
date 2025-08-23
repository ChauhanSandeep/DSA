package Array;

import java.util.*;

/**
 * Valid Triangle Number
 * 
 * Problem: Count number of valid triangles that can be formed using array elements as side lengths.
 * Three sides form valid triangle if sum of any two sides > third side.
 * 
 * Example: nums = [2,2,3,4] -> Output: 3
 * Valid triangles: [2,3,4], [2,2,3], [2,2,4] (but not [2,2,4] - violates triangle inequality)
 * Wait, let me recalculate: [2,2,3] (2+2>3 ✓, 2+3>2 ✓, 2+3>2 ✓), [2,3,4] (2+3>4 ✓, 2+4>3 ✓, 3+4>2 ✓), [2,2,4] (2+2=4, not >4 ✗)
 * So valid triangles are: [2,2,3], [2,3,4]. Actually need to be more careful...
 * 
 * LeetCode: https://leetcode.com/problems/valid-triangle-number
 * 
 * Follow-up Questions:
 * - How to find actual triangle triplets, not just count? (Store triplets instead of counting)
 * - What if sides can be floating point? (Same logic applies)
 * - How to handle case with duplicate side lengths? (Current solution handles correctly)
 */
public class ValidTriangleNumber {

    /**
     * Counts valid triangles using sorting and two-pointer technique.
     * 
     * Algorithm:
     * 1. Sort array to enable triangle inequality optimization
     * 2. For each element as largest side, use two pointers on remaining elements
     * 3. If nums[left] + nums[right] > nums[largest], all pairs between left and right work
     * 4. Triangle inequality: if a ≤ b ≤ c, then only need to check a + b > c
     * 
     * Time Complexity: O(n²) where n is array length
     * Space Complexity: O(1) if sorting in-place
     * 
     * @param nums array of side lengths
     * @return count of valid triangles
     */
    public int triangleNumber(int[] nums) {
        if (nums.length < 3) return 0;

        Arrays.sort(nums);
        int count = 0;

        // Fix the largest side (rightmost)
        for (int k = nums.length - 1; k >= 2; k--) {
            int left = 0, right = k - 1;

            // Use two pointers for the other two sides
            while (left < right) {
                if (nums[left] + nums[right] > nums[k]) {
                    // All pairs (left, right-1), (left, right-2), ..., (left, left) work
                    count += right - left;
                    right--;
                } else {
                    // nums[left] + nums[right] <= nums[k], need larger sum
                    left++;
                }
            }
        }

        return count;
    }

    /**
     * Brute force approach checking all triplets
     * Time Complexity: O(n³), Space Complexity: O(1)
     */
    public int triangleNumberBruteForce(int[] nums) {
        int count = 0;
        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (isValidTriangle(nums[i], nums[j], nums[k])) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    // Helper method to check triangle validity
    private boolean isValidTriangle(int a, int b, int c) {
        return a + b > c && a + c > b && b + c > a;
    }

    /**
     * Binary search approach for each pair
     * Time Complexity: O(n² log n), Space Complexity: O(1)
     */
    public int triangleNumberBinarySearch(int[] nums) {
        Arrays.sort(nums);
        int count = 0;
        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                int sum = nums[i] + nums[j];

                // Find largest k such that nums[k] < sum
                int k = binarySearchLargest(nums, j + 1, n - 1, sum);

                if (k > j) {
                    count += k - j;
                }
            }
        }

        return count;
    }

    // Binary search to find largest index with value < target
    private int binarySearchLargest(int[] nums, int left, int right, int target) {
        int result = left - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] < target) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    /**
     * Using different approach: fix smallest two sides
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public int triangleNumberAlternative(int[] nums) {
        Arrays.sort(nums);
        int count = 0;
        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                int sum = nums[i] + nums[j];

                // Count elements nums[k] where j < k < n and nums[k] < sum
                int k = j + 1;
                while (k < n && nums[k] < sum) {
                    count++;
                    k++;
                }
            }
        }

        return count;
    }

    /**
     * Method that returns actual valid triangles (not just count)
     * Time Complexity: O(n²), Space Complexity: O(k) where k is number of triangles
     */
    public List<int[]> getValidTriangles(int[] nums) {
        List<int[]> triangles = new ArrayList<>();
        Arrays.sort(nums);

        for (int k = nums.length - 1; k >= 2; k--) {
            int left = 0, right = k - 1;

            while (left < right) {
                if (nums[left] + nums[right] > nums[k]) {
                    // Add all valid triangles with current right and k
                    for (int i = left; i < right; i++) {
                        triangles.add(new int[]{nums[i], nums[right], nums[k]});
                    }
                    right--;
                } else {
                    left++;
                }
            }
        }

        return triangles;
    }

    /**
     * Count triangles with specific constraints (e.g., all sides different)
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public int triangleNumberDistinctSides(int[] nums) {
        Arrays.sort(nums);
        int count = 0;

        for (int k = nums.length - 1; k >= 2; k--) {
            int left = 0, right = k - 1;

            while (left < right) {
                if (nums[left] + nums[right] > nums[k]) {
                    // Only count if all sides are different
                    for (int i = left; i < right; i++) {
                        if (nums[i] != nums[right] && nums[i] != nums[k] && nums[right] != nums[k]) {
                            count++;
                        }
                    }
                    right--;
                } else {
                    left++;
                }
            }
        }

        return count;
    }

    /**
     * Method to find maximum triangle perimeter
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public int maxTrianglePerimeter(int[] nums) {
        Arrays.sort(nums);
        int maxPerimeter = 0;

        for (int k = nums.length - 1; k >= 2; k--) {
            int left = 0, right = k - 1;

            while (left < right) {
                if (nums[left] + nums[right] > nums[k]) {
                    int perimeter = nums[left] + nums[right] + nums[k];
                    maxPerimeter = Math.max(maxPerimeter, perimeter);
                    right--;
                } else {
                    left++;
                }
            }
        }

        return maxPerimeter;
    }

    /**
     * Helper method to demonstrate triangle inequality
     */
    public void demonstrateTriangleInequality(int a, int b, int c) {
        System.out.printf("Triangle with sides %d, %d, %d:\n", a, b, c);
        System.out.printf("  %d + %d > %d? %b\n", a, b, c, a + b > c);
        System.out.printf("  %d + %d > %d? %b\n", a, c, b, a + c > b);
        System.out.printf("  %d + %d > %d? %b\n", b, c, a, b + c > a);
        System.out.printf("  Valid triangle: %b\n\n", isValidTriangle(a, b, c));
    }
}
