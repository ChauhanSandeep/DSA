package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Valid Triangle Number
 *
 * Given side lengths, count index triplets that can form a valid triangle. After
 * sorting, only the sum of the two smaller sides must be checked against the
 * largest side.
 *
 * Leetcode: https://leetcode.com/problems/valid-triangle-number/ (Medium)
 * Rating:   acceptance 57.0% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Sorting | Two pointers counting window
 *
 * Example:
 *   Input:  nums = [2,2,3,4]
 *   Output: 3
 *   Why:    the valid index triplets are the two (2,3,4) choices and (2,2,3).
 *
 * Follow-ups:
 *   1. Return the actual triangles?
 *      Emit each value or index triplet instead of adding the whole window count at once.
 *   2. Count unique value triplets only?
 *      Skip duplicate values or store normalized triplets in a set.
 *   3. Find the maximum valid perimeter?
 *      Sort and scan largest sides from the end, returning the first valid high-perimeter triplet.
 *
 * Related: Largest Perimeter Triangle (976), 3Sum Smaller (259).
 */
public class ValidTriangleNumber {

public static void main(String[] args) {
    ValidTriangleNumber solver = new ValidTriangleNumber();
    int[][] inputs = { {2, 2, 3, 4}, {4, 2, 3, 4} };
    int[] expected = { 3, 4 };

    for (int i = 0; i < inputs.length; i++) {
        int got = solver.triangleNumber(inputs[i].clone());
        System.out.printf("nums=%s -> %d  expected=%d%n",
            Arrays.toString(inputs[i]), got, expected[i]);
    }
}

    /**
 * Intuition: once side lengths are sorted, fix the largest side at rightIndex.
 * If nums[leftIndex] + nums[middleIndex] is greater than that largest side,
 * then every value between leftIndex and middleIndex also works with
 * middleIndex and rightIndex, so count that whole window at once.
 *
 * Algorithm:
 *   1. Return 0 for null or fewer than three values.
 *   2. Sort nums.
 *   3. Fix rightIndex as the largest side from right to left.
 *   4. Move leftIndex or middleIndex based on the triangle inequality, counting valid windows.
 *
 * Time:  O(n^2) - each fixed largest side runs one two-pointer scan.
 * Space: O(1) - excluding sorting implementation storage.
 *
 * @param nums non-negative side lengths
 * @return count of valid triangle index triplets
 */
    public int triangleNumber(int[] nums) {
        if (nums == null || nums.length < 3) {
            return 0;
        }

        Arrays.sort(nums);
        int count = 0;

        for (int rightIndex = nums.length - 1; rightIndex >= 2; rightIndex--) {
            int leftIndex = 0;
            int middleIndex = rightIndex - 1;

            while (leftIndex < middleIndex) {
                if (nums[leftIndex] + nums[middleIndex] > nums[rightIndex]) {
                    // All elements combinations like (leftIndex, middleIndex), (leftIndex+1, middleIndex), ..., (middleIndex-1, middleIndex)
                    // form valid triangles with nums[rightIndex] because nums[left] + nums[middleIndex] > nums[rightIndex]
                    // and due to sorting, all values between leftIndex and middleIndex also satisfy this condition
                    count += middleIndex - leftIndex;
                    middleIndex--;
                } else {
                    leftIndex++;
                }
            }
        }

        return count;
    }

    /**
     * Alternative approach using binary search for each pair of smaller sides.
     * Fixes two smaller sides and binary searches for valid largest sides.
     *
     * Algorithm:
     * 1. Sort the array to enable binary search
     * 2. Fix first two sides at positions i and j
     * 3. Binary search for the rightmost position k where nums[i] + nums[j] > nums[k]
     * 4. Count valid triangles as (k - j) for each valid pair
     *
     * Time Complexity: O(N^2 log N) where N is array length. Two nested loops O(N^2)
     * with binary search O(log N) in the innermost operation.
     *
     * Space Complexity: O(1) excluding sorting space.
     *
     * Note: This approach is less efficient than two-pointers for this problem but
     * demonstrates an alternative technique that could be useful in modified versions.
     *
     * @param nums array of non-negative integers representing potential triangle sides
     * @return count of valid triangle triplets
     */
    public int triangleNumberBinarySearch(int[] nums) {
        if (nums == null || nums.length < 3) {
            return 0;
        }

        Arrays.sort(nums);
        int count = 0;

        for (int leftIndex = 0; leftIndex < nums.length - 2; leftIndex++) {
            for (int middleIndex = leftIndex + 1; middleIndex < nums.length - 1; middleIndex++) {

                int sum = nums[leftIndex] + nums[middleIndex];
                int rightIndex = binarySearch(nums, middleIndex + 1, nums.length - 1, sum);
                count += rightIndex - middleIndex; // All indices between middleIndex and rightIndex are valid because of sorting
            }
        }

        return count;
    }

    // Helper method to find rightmost index where nums[index] < target
    private int binarySearch(int[] nums, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] >= target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return right;
    }
}
