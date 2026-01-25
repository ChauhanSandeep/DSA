package arrays.twopointers;

/**
 * Problem: Two Sum II - Input Array Is Sorted
 *
 * Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order,
 * find two numbers such that they add up to a specific target number. Let these two numbers be
 * numbers[index1] and numbers[index2] where 1 <= index1 < index2 <= numbers.length.
 *
 * Return the indices of the two numbers, index1 and index2, added by one as an integer array
 * [index1, index2] of length 2. The tests are generated such that there is exactly one solution.
 * You may not use the same element twice. Your solution must use only constant extra space.
 *
 * Example:
 * Input: numbers = [2,7,11,15], target = 9
 * Output: [1,2]
 * Explanation: The sum of 2 and 7 is 9. Since the array is 1-indexed, index1 = 1, index2 = 2.
 *
 * LeetCode Problem: https://leetcode.com/problems/two-sum-ii-input-array-is-sorted
 *
 * Follow-up Questions:
 *
 * 1. What if the array could contain duplicate values and you need all unique pairs?
 *    Answer: Use the same two-pointer approach but skip duplicate values after finding a valid
 *    pair. Move both pointers while they point to the same value to avoid counting duplicates.
 *
 * 2. How would you modify this for finding three numbers that sum to target (3Sum)?
 *    Answer: Fix one element and apply two-pointer technique on the remaining sorted array.
 *    Iterate through each element, treating it as the first number and finding pairs that sum
 *    to target minus that number.
 *    Related problem: https://leetcode.com/problems/3sum/
 *
 * 3. What if you need to find pairs with sum closest to target instead of exact match?
 *    Answer: Track the minimum absolute difference while using two pointers. Update the result
 *    whenever a smaller difference is found. Continue moving pointers to explore all possibilities.
 *    Related problem: https://leetcode.com/problems/3sum-closest/
 *
 * 4. Can you solve this using binary search instead of two pointers?
 *    Answer: Yes, iterate through each element and binary search for target minus current element
 *    in the remaining array. Time complexity becomes O(n log n). Two pointers is more optimal at O(n).
 *
 * 5. What if the array is not sorted?
 *    Answer: Use a HashMap to store complements. For each element, check if target minus current
 *    exists in the map. This gives O(n) time with O(n) space, but violates the constant space requirement.
 *    Related problem: https://leetcode.com/problems/two-sum/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class TwoSumIIInputArrayIsSorted {

    /**
     * Finds two numbers that sum to target using two-pointer technique.
     *
     * Algorithm:
     * 1. Initialize left pointer at start and right pointer at end of array
     * 2. Calculate sum of elements at both pointers
     * 3. If sum equals target, return 1-indexed positions
     * 4. If sum is less than target, move left pointer right to increase sum
     * 5. If sum is greater than target, move right pointer left to decrease sum
     * 6. Repeat until target sum is found
     *
     * Key insight: Since the array is sorted, we can efficiently search by moving pointers
     * based on whether current sum is too small or too large. Moving left pointer increases
     * sum, moving right pointer decreases sum.
     *
     * Time Complexity: O(N) where N is the length of the array. Each pointer moves at most
     * N positions, and they move toward each other, visiting each element at most once.
     *
     * Space Complexity: O(1) as only two pointer variables are used regardless of input size.
     *
     * @param numbers sorted array of integers (1-indexed in problem, 0-indexed in implementation)
     * @param target the target sum to find
     * @return array containing 1-indexed positions of the two numbers
     */
    public int[] twoSum(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];

            if (sum == target) {
                return new int[]{left + 1, right + 1};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return new int[]{-1, -1};
    }

    /**
     * Alternative approach using binary search for each element.
     * For each element, binary search for its complement in remaining array.
     *
     * Algorithm:
     * 1. Iterate through each element at index i
     * 2. Calculate complement = target - numbers[i]
     * 3. Binary search for complement in subarray [i+1, n-1]
     * 4. If found, return 1-indexed positions of both elements
     *
     * Time Complexity: O(N log N) where N is array length. For each of N elements,
     * we perform binary search taking O(log N).
     *
     * Space Complexity: O(1) excluding recursion stack for binary search.
     *
     * Note: While this approach works, the two-pointer method is more efficient with
     * O(N) time complexity. Binary search approach is included to demonstrate alternatives.
     *
     * @param numbers sorted array of integers
     * @param target the target sum to find
     * @return array containing 1-indexed positions of the two numbers
     */
    public int[] twoSumBinarySearch(int[] numbers, int target) {
        for (int i = 0; i < numbers.length - 1; i++) {
            int complement = target - numbers[i];
            int complementIndex = binarySearch(numbers, i + 1, numbers.length - 1, complement);

            if (complementIndex != -1) {
                return new int[]{i + 1, complementIndex + 1};
            }
        }

        return new int[]{-1, -1};
    }

    // Helper method for binary search in a range
    private int binarySearch(int[] arr, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }
}
