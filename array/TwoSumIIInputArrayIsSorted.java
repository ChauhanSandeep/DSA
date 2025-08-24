package array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * Two Sum II Input Array Is Sorted
 *
 * Problem: Find two numbers in sorted array that add up to target. Return 1-indexed positions.
 *
 * Example: numbers = [2,7,11,15], target = 9 -> Output: [1,2]
 * numbers[0] + numbers[1] = 2 + 7 = 9, so return [1,2] (1-indexed).
 *
 * LeetCode: https://leetcode.com/problems/two-sum-ii-input-array-is-sorted
 *
 * Follow-up Questions:
 * - What if multiple pairs exist? (Problem guarantees exactly one solution)
 * - How to find all pairs that sum to target? (Continue search after finding each pair)
 * - Can we solve with O(1) space? (Current solution already achieves this)
 */
public class TwoSumIIInputArrayIsSorted {

    /**
     * Finds two numbers that sum to target using two-pointer technique.
     *
     * Algorithm:
     * 1. Use two pointers: left at start, right at end
     * 2. If sum equals target, return indices (1-indexed)
     * 3. If sum < target, move left pointer right (increase sum)
     * 4. If sum > target, move right pointer left (decrease sum)
     * 5. Continue until target found
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param numbers sorted array of integers
     * @param target sum to find
     * @return 1-indexed positions of two numbers that sum to target
     */
    public int[] twoSum(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];

            if (sum == target) {
                return new int[]{left + 1, right + 1}; // Convert to 1-indexed
            } else if (sum < target) {
                left++;  // Need larger sum
            } else {
                right--; // Need smaller sum
            }
        }

        // Should never reach here given problem constraints
        return new int[]{-1, -1};
    }

    /**
     * Binary search approach (less efficient but educational)
     * Time Complexity: O(n log n), Space Complexity: O(1)
     */
    public int[] twoSumBinarySearch(int[] numbers, int target) {
        for (int i = 0; i < numbers.length - 1; i++) {
            int complement = target - numbers[i];
            int complementIndex = binarySearch(numbers, complement, i + 1, numbers.length - 1);

            if (complementIndex != -1) {
                return new int[]{i + 1, complementIndex + 1};
            }
        }

        return new int[]{-1, -1};
    }

    // Helper method for binary search
    private int binarySearch(int[] arr, int target, int left, int right) {
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

    /**
     * Hash map approach (works for unsorted arrays too)
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int[] twoSumHashMap(int[] numbers, int target) {
        Map<Integer, Integer> numToIndex = new HashMap<>();

        for (int i = 0; i < numbers.length; i++) {
            int complement = target - numbers[i];

            if (numToIndex.containsKey(complement)) {
                return new int[]{numToIndex.get(complement) + 1, i + 1};
            }

            numToIndex.put(numbers[i], i);
        }

        return new int[]{-1, -1};
    }

    /**
     * Brute force approach for comparison
     * Time Complexity: O(n²), Space Complexity: O(1)
     */
    public int[] twoSumBruteForce(int[] numbers, int target) {
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] + numbers[j] == target) {
                    return new int[]{i + 1, j + 1};
                }
            }
        }

        return new int[]{-1, -1};
    }

    /**
     * Finding all pairs that sum to target (returns list of pairs)
     * Time Complexity: O(n), Space Complexity: O(k) where k is number of pairs
     */
    public List<int[]> twoSumAllPairs(int[] numbers, int target) {
        List<int[]> result = new ArrayList<>();
        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];

            if (sum == target) {
                result.add(new int[]{left + 1, right + 1});

                // Skip duplicates
                int leftVal = numbers[left];
                int rightVal = numbers[right];

                while (left < right && numbers[left] == leftVal) left++;
                while (left < right && numbers[right] == rightVal) right--;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return result;
    }

    /**
     * Using streams for functional programming approach
     * Time Complexity: O(n²), Space Complexity: O(n)
     */
    public int[] twoSumStream(int[] numbers, int target) {
        return IntStream.range(0, numbers.length)
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, numbers.length)
                        .filter(j -> numbers[i] + numbers[j] == target)
                        .mapToObj(j -> new int[]{i + 1, j + 1}))
                .findFirst()
                .orElse(new int[]{-1, -1});
    }

    /**
     * Recursive two-pointer approach
     * Time Complexity: O(n), Space Complexity: O(n) due to recursion
     */
    public int[] twoSumRecursive(int[] numbers, int target) {
        return twoSumHelper(numbers, target, 0, numbers.length - 1);
    }

    private int[] twoSumHelper(int[] numbers, int target, int left, int right) {
        if (left >= right) {
            return new int[]{-1, -1};
        }

        int sum = numbers[left] + numbers[right];

        if (sum == target) {
            return new int[]{left + 1, right + 1};
        } else if (sum < target) {
            return twoSumHelper(numbers, target, left + 1, right);
        } else {
            return twoSumHelper(numbers, target, left, right - 1);
        }
    }

    /**
     * Helper method to validate that solution is correct
     */
    public boolean validateSolution(int[] numbers, int target, int[] result) {
        if (result.length != 2) return false;

        int index1 = result[0] - 1; // Convert to 0-indexed
        int index2 = result[1] - 1;

        // Check bounds
        if (index1 < 0 || index1 >= numbers.length ||
            index2 < 0 || index2 >= numbers.length) {
            return false;
        }

        // Check sum
        return numbers[index1] + numbers[index2] == target;
    }
}
