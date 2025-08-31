package arrays;

import java.util.*;

/**
 * Add To Array Form Of Integer
 *
 * Problem: Given an array representing the digits of a non-negative integer and an integer k,
 * return the array form of the integer num + k.
 *
 * Example: nums = [1,2,0,0], k = 34 -> Output: [1,2,3,4]
 * The array represents 1200, adding 34 gives 1234.
 *
 * LeetCode: https://leetcode.com/problems/add-to-array-form-of-integer
 *
 * Follow-up Questions:
 * Que - How would you handle very large numbers that exceed integer limits?
 * Ans - (Use BigInteger or string manipulation)
 * Que - Can you solve this without using extra space?
 * Ans - (Modify the input array if possible, otherwise use a linked list)
 * Que - What if k could be negative?
 * Ans - Need to handle borrowing and negative results, more complex logic required.
 */
public class AddToArrayFormOfInteger {

    /**
     * Adds integer k to a number represented as an array of digits.
     *
     * Algorithm:
     * 1. Start from the least significant digit (rightmost)
     * 2. Add carry (initially 0) + current digit + last digit of k
     * 3. Update carry for next iteration
     * 4. Continue until all digits processed and no carry remains
     * 5. Reverse result since we build from right to left
     *
     * Time Complexity: O(max(N, log K)) where N is length of nums array
     * Space Complexity: O(max(N, log K)) for the result array
     *
     * @param nums array representing digits of the number
     * @param k integer to add
     * @return array form of nums + k
     */
    public List<Integer> addToArrayForm(int[] nums, int k) {
        List<Integer> result = new ArrayList<>();
        int carry = 0;
        int index = nums.length - 1;

        // Process until all digits are handled and no carry remains
        while (index >= 0 || k > 0 || carry > 0) {
            // Get current digit from nums array (0 if out of bounds)
            int currentDigit = index >= 0 ? nums[index] : 0;

            // Get last digit of k
            int kDigit = k%10;
            k = k/10;

            // Add current digit + last digit of k + carry
            int sum = currentDigit + kDigit + carry;

            // Add the unit digit to result
            result.add(sum % 10);

            // Update carry and move to next digit of k
            carry = sum / 10;
            index--;
        }

        // Reverse since we built the result backwards
        Collections.reverse(result);
        return result;
    }

    /**
     * Alternative approach using string conversion (less efficient but more intuitive)
     * This approach may lead to overflow for very large numbers and would not be allowed in an interview setting.
     *
     * Steps:
     * 1. Convert array to number, add k
     * 2. Convert result back to array form
     *
     * Time Complexity: O(N + log K),
     * Space Complexity: O(N + log K)
     */
    public List<Integer> addToArrayFormAlternative(int[] nums, int k) {
        // Convert array to number, add k, then convert back
        // Note: This approach has overflow risks for large numbers
        long number = 0;
        for (int digit : nums) {
            number = number * 10 + digit;
        }

        number += k;

        // Convert result back to list
        List<Integer> result = new ArrayList<>();
        if (number == 0) {
            result.add(0);
            return result;
        }

        while (number > 0) {
            result.add(0, (int)(number % 10));
            number /= 10;
        }

        return result;
    }
}
