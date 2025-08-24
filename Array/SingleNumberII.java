package Array;

import java.util.*;

/**
 * Single Number II
 *
 * Problem: Find element that appears once in array where every other element appears three times.
 *
 * Example: nums = [2,2,3,2] -> Output: 3
 * Element 3 appears once, element 2 appears three times.
 *
 * LeetCode: https://leetcode.com/problems/single-number-ii
 *
 * Follow-up Questions:
 * - What if elements appear k times except one? (Use bit manipulation with mod k)
 * - How to handle negative numbers? (Same bit manipulation works)
 * - Can we solve with O(1) space and O(n) time? (Current optimal solution achieves this)
 */
public class SingleNumberII {

    /**
     * Finds single number using bit manipulation with modular arithmetic.
     *
     * Algorithm:
     * 1. For each bit position, count how many numbers have that bit set
     * 2. If count % 3 != 0, the single number has that bit set
     * 3. Reconstruct the single number from these bits
     * 4. Works because numbers appearing 3 times contribute 0 or 3 to each bit count
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param nums array where every element appears 3 times except one
     * @return the element that appears exactly once
     */
    public int singleNumber(int[] nums) {
        int result = 0;

        // Check each bit position (32 bits for int)
        for (int bit = 0; bit < 32; bit++) {
            int count = 0;

            // Count how many numbers have this bit set
            for (int num : nums) {
                if (((num >> bit) & 1) == 1) {
                    count++;
                }
            }

            // If count is not divisible by 3, single number has this bit
            if (count % 3 != 0) {
                result |= (1 << bit);
            }
        }

        return result;
    }

    /**
     * Digital circuit simulation approach using two variables
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int singleNumberDigital(int[] nums) {
        int ones = 0; // Numbers appearing 1 or 4 or 7... times
        int twos = 0; // Numbers appearing 2 or 5 or 8... times

        for (int num : nums) {
            // Update twos first (before ones changes)
            twos |= ones & num;

            // Update ones
            ones ^= num;

            // Remove numbers that appear 3 times
            int threes = ones & twos;
            ones &= ~threes;
            twos &= ~threes;
        }

        return ones;
    }

    /**
     * Hash map approach for clarity (uses extra space)
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

        return -1; // Should never reach here
    }

    /**
     * Set-based approach with mathematical formula
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int singleNumberMath(int[] nums) {
        Set<Long> uniqueElements = new HashSet<>();
        long arraySum = 0;

        for (int num : nums) {
            uniqueElements.add((long) num);
            arraySum += num;
        }

        long uniqueSum = uniqueElements.stream().mapToLong(Long::longValue).sum();

        // 3 * sum(unique) - sum(array) = 2 * (single element)
        return (int) ((3 * uniqueSum - arraySum) / 2);
    }

    /**
     * Sorting approach
     * Time Complexity: O(n log n), Space Complexity: O(1)
     */
    public int singleNumberSorting(int[] nums) {
        Arrays.sort(nums);

        // Check triplets
        for (int i = 0; i < nums.length - 2; i += 3) {
            if (nums[i] != nums[i + 1] || nums[i] != nums[i + 2]) {
                return nums[i]; // Found single element
            }
        }

        // Single element is the last one
        return nums[nums.length - 1];
    }

    /**
     * Generalized approach for any k repetitions
     * Time Complexity: O(n * log k), Space Complexity: O(1)
     */
    public int singleNumberGeneralized(int[] nums, int k) {
        int result = 0;

        for (int bit = 0; bit < 32; bit++) {
            int count = 0;

            for (int num : nums) {
                if (((num >> bit) & 1) == 1) {
                    count++;
                }
            }

            if (count % k != 0) {
                result |= (1 << bit);
            }
        }

        return result;
    }

    /**
     * State machine approach (finite state automaton)
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int singleNumberStateMachine(int[] nums) {
        int first = 0, second = 0;

        for (int num : nums) {
            // Update second before first
            second = second ^ (first & num);
            first = first ^ num;

            // Reset when both bits are 1 (state 11 -> 00)
            int mask = ~(first & second);
            first &= mask;
            second &= mask;
        }

        return first;
    }

    /**
     * Using three variables for clear state tracking
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int singleNumberThreeState(int[] nums) {
        int a = 0, b = 0;

        for (int c : nums) {
            int ta = (~a & b & c) | (a & ~b & ~c);
            b = (~a & ~b & c) | (~a & b & ~c);
            a = ta;
        }

        return a | b;
    }

    /**
     * Helper method to demonstrate bit counting approach
     */
    public void demonstrateBitCounting(int[] nums) {
        System.out.println("Bit counting demonstration:");

        for (int bit = 0; bit < 8; bit++) { // Show first 8 bits
            int count = 0;
            System.out.printf("Bit %d: ", bit);

            for (int num : nums) {
                int bitValue = (num >> bit) & 1;
                System.out.printf("%d ", bitValue);
                count += bitValue;
            }

            System.out.printf("(count=%d, count%%3=%d)\n", count, count % 3);
        }
    }
}
