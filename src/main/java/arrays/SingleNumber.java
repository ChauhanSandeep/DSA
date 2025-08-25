package arrays;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Single Number
 *
 * Problem: Find element that appears once in array where every other element appears twice.
 *
 * Example: nums = [2,2,1] -> Output: 1
 * Element 1 appears once, elements 2 appears twice.
 *
 * LeetCode: https://leetcode.com/problems/single-number
 *
 * Follow-up Questions:
 * - What if every element appears 3 times except one? (Use bit manipulation with mod 3)
 * - How to find two numbers that appear once? (XOR all, then separate using differing bit)
 * - Can we solve with different space/time constraints? (Sorting: O(n log n) time, O(1) space)
 */
public class SingleNumber {

    /**
     * Finds single number using XOR bit manipulation.
     *
     * Algorithm:
     * 1. XOR all elements together
     * 2. Identical numbers cancel out (a ^ a = 0)
     * 3. Zero doesn't affect other numbers (0 ^ a = a)
     * 4. Result is the single number that doesn't have a pair
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param nums array where every element appears twice except one
     * @return the element that appears exactly once
     */
    public int singleNumber(int[] nums) {
        int result = 0;

        for (int num : nums) {
            result ^= num; // XOR operation
        }

        return result;
    }

    /**
     * Mathematical approach using sum formula
     * Time Complexity: O(n), Space Complexity: O(n) for set
     */
    public int singleNumberMath(int[] nums) {
        Set<Integer> uniqueElements = new HashSet<>();
        int arraySum = 0;

        for (int num : nums) {
            uniqueElements.add(num);
            arraySum += num;
        }

        int uniqueSum = uniqueElements.stream().mapToInt(Integer::intValue).sum();

        // If each unique element appeared twice, sum would be 2*uniqueSum
        // Difference gives us the single element
        return 2 * uniqueSum - arraySum;
    }

    /**
     * Hash map approach for counting frequencies
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int singleNumberHashMap(int[] nums) {
        Map<Integer, Integer> frequency = new HashMap<>();

        // Count frequencies
        for (int num : nums) {
            frequency.put(num, frequency.getOrDefault(num, 0) + 1);
        }

        // Find element with frequency 1
        for (Map.Entry<Integer, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }

        return -1; // Should never reach here given problem constraints
    }

    /**
     * Hash set approach (add/remove pattern)
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int singleNumberHashSet(int[] nums) {
        Set<Integer> seen = new HashSet<>();

        for (int num : nums) {
            if (seen.contains(num)) {
                seen.remove(num); // Remove duplicate
            } else {
                seen.add(num); // Add first occurrence
            }
        }

        // Only element left is the single one
        return seen.iterator().next();
    }

    /**
     * Sorting approach
     * Time Complexity: O(n log n), Space Complexity: O(1)
     */
    public int singleNumberSorting(int[] nums) {
        Arrays.sort(nums);

        // Check pairs starting from index 0
        for (int i = 0; i < nums.length - 1; i += 2) {
            if (nums[i] != nums[i + 1]) {
                return nums[i]; // Found single element
            }
        }

        // Single element is the last one
        return nums[nums.length - 1];
    }

    /**
     * Stream-based XOR approach
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int singleNumberStream(int[] nums) {
        return Arrays.stream(nums).reduce(0, (a, b) -> a ^ b);
    }

    /**
     * Recursive XOR approach
     * Time Complexity: O(n), Space Complexity: O(n) due to recursion stack
     */
    public int singleNumberRecursive(int[] nums) {
        return singleNumberHelper(nums, 0);
    }

    private int singleNumberHelper(int[] nums, int index) {
        if (index == nums.length) {
            return 0;
        }

        return nums[index] ^ singleNumberHelper(nums, index + 1);
    }

    /**
     * Bit manipulation with explicit loop
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int singleNumberBitManipulation(int[] nums) {
        int single = 0;

        // XOR all numbers
        for (int i = 0; i < nums.length; i++) {
            single ^= nums[i];
        }

        return single;
    }

    /**
     * Generic solution using frequency analysis
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int singleNumberGeneric(int[] nums) {
        return Arrays.stream(nums)
                .boxed()
                .collect(Collectors.groupingBy(
                    Function.identity(),
                    Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Helper method to demonstrate XOR properties
     */
    public void demonstrateXORProperties() {
        System.out.println("XOR Properties:");
        System.out.println("5 ^ 5 = " + (5 ^ 5)); // 0
        System.out.println("0 ^ 7 = " + (0 ^ 7)); // 7
        System.out.println("3 ^ 5 ^ 3 = " + (3 ^ 5 ^ 3)); // 5
        System.out.println("Commutative: 3 ^ 5 = " + (3 ^ 5) + ", 5 ^ 3 = " + (5 ^ 3));
    }
}
