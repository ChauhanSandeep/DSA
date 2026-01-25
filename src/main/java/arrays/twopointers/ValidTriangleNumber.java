package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Valid Triangle Number
 *
 * Given an integer array nums, return the number of triplets chosen from the array
 * that can make triangles if we take them as side lengths of a triangle.
 *
 * For three sides to form a valid triangle, they must satisfy the triangle inequality:
 * the sum of any two sides must be greater than the third side.
 *
 * Example:
 * Input: nums = [2,2,3,4]
 * Output: 3
 * Explanation: Valid combinations are: (2,3,4) using first 2, (2,3,4) using second 2, and (2,2,3).
 *
 * Constraints:
 * - 1 <= nums.length <= 1000
 * - 0 <= nums[i] <= 1000
 *
 * LeetCode Problem: https://leetcode.com/problems/valid-triangle-number
 *
 * Follow-up Questions:
 *
 * 1. What if you need to return the actual triplets instead of just the count?
 *    Answer: Store each valid triplet (i, j, k) in a list when found. Modify the count
 *    increment logic to add all triplets between left and right pointers individually.
 *
 * 2. How would you handle duplicate triplets if the same values appear at different indices?
 *    Answer: Use a Set with sorted triplet values as keys, or skip duplicates by checking
 *    if current value equals previous value and skipping accordingly during iteration.
 *
 * 3. Can you solve this for quadrilaterals (4 sides) instead of triangles?
 *    Answer: For a quadrilateral, the sum of any three sides must be greater than the fourth.
 *    Fix the largest side and use three nested pointers or extend the current approach with
 *    an additional dimension. Complexity would become O(n^3).
 *
 * 4. What if we need to find the longest valid triangle perimeter?
 *    Answer: Sort the array and iterate from the end. For each triplet that forms a valid
 *    triangle, calculate and track the maximum perimeter. Use early exit once found since
 *    larger values come first.
 *    Related problem: https://leetcode.com/problems/largest-perimeter-triangle/
 *
 * 5. How would you optimize for very large arrays with many zeros?
 *    Answer: Filter out zeros during preprocessing since they cannot form valid triangles
 *    with positive sides. This reduces the effective array size before applying the main algorithm.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ValidTriangleNumber {

    /**
     * Counts valid triangle triplets using two-pointer technique after sorting.
     *
     * Algorithm:
     * 1. Sort the array to enable efficient triangle inequality checking
     * 2. Fix the largest side at position i (iterate from end to start)
     * 3. Use two pointers: left at start, right at i-1
     * 4. If nums[left] + nums[right] > nums[i], all elements between left and right
     *    form valid triangles with right and i, so add (right - left) to count
     * 5. Move right pointer left if valid, or left pointer right if not valid
     * 6. Continue until all combinations are checked
     *
     * Key insight: After sorting, [a, b, c, d] : if a + b > c where c is the largest side, then
     * a + c > b and b + c > a are automatically satisfied due to ordering. We only
     * need to check one inequality. 
     * Additionally, if nums[left] + nums[right] > nums[i],
     * then all values between left and right also satisfy the condition with right.
     *
     * Time Complexity: O(N^2) where N is the array length. Sorting takes O(N log N),
     * then we have outer loop O(N) with inner two-pointer traversal O(N), giving O(N^2).
     *
     * Space Complexity: O(1) if we exclude the space used by sorting algorithm.
     * Java's Arrays.sort() uses O(log N) space for quicksort recursion.
     *
     * @param nums array of non-negative integers representing potential triangle sides
     * @return count of valid triangle triplets
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
