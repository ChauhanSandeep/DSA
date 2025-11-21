package arrays.xor;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 136. Single Number
 *
 * Problem Statement:
 * Given a non-empty array of integers nums, every element appears exactly twice
 * except for one element that appears only once. Find that single element.
 * You must implement a solution with a linear runtime complexity and use only
 * constant extra space.
 *
 * Example:
 * Input: nums = [2,2,1]
 * Output: 1
 * Explanation: All numbers except 1 appear exactly twice.
 *
 * Input: nums = [4,1,2,1,2]
 * Output: 4
 * Explanation: All numbers except 4 appear exactly twice.
 *
 * Input: nums = [1]
 * Output: 1
 * Explanation: Single element array returns that element.
 *
 * LeetCode Link: https://leetcode.com/problems/single-number/
 *
 * Follow-up Questions:
 * 1. What if every element appears three times except one that appears once?
 *    Answer: Use bit manipulation with counters for each bit position or state machines.
 *    Related: https://leetcode.com/problems/single-number-ii/
 * 2. What if there are exactly two elements that appear once and others appear twice?
 *    Answer: Use XOR to get combined result, then use bit manipulation to separate the two numbers.
 *    Related: https://leetcode.com/problems/single-number-iii/
 * 3. How would you solve this if extra space was allowed?
 *    Answer: Use HashSet or HashMap to track element frequencies.
 *
 * Related Problems:
 * - 137. Single Number II: https://leetcode.com/problems/single-number-ii/
 * - 260. Single Number III: https://leetcode.com/problems/single-number-iii/
 * - 540. Single Element in a Sorted Array: https://leetcode.com/problems/single-element-in-a-sorted-array/
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
     * Logic is that if we sum unique elements and multiply by 2,
     * then subtract the sum of all elements, we get the single element.
     * This works because all other elements appear twice.
     *
     * Time Complexity: O(n),
     * Space Complexity: O(n) for set
     */
    public int singleNumberMath(int[] nums) {
        Set<Integer> uniqueElements = Arrays.stream(nums).boxed().collect(Collectors.toSet());
        int arraySum = Arrays.stream(nums).sum();
        int uniqueSum = uniqueElements.stream().mapToInt(Integer::intValue).sum();

        // If each unique element appeared twice, sum would be 2*uniqueSum
        // Difference gives us the single element
        return 2 * uniqueSum - arraySum;
    }

    /**
     * Naive Approach : Hash map approach for counting frequencies
     * Algorithm:
     * 1. Use a hash map to count occurrences of each element
     * 2. Iterate through the map to find the element with frequency 1
     * 3. Return that element
     *
     * Time Complexity: O(n),
     * Space Complexity: O(n)
     */
    public int singleNumberHashMap(int[] nums) {
        Map<Integer, Integer> frequency = Arrays.stream(nums)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1)));

        // Find element with frequency 1
        return frequency.entrySet().stream()
            .filter(entry -> entry.getValue() == 1)
            .findFirst()
            .map(Entry::getKey)
            .orElse(-1);
    }
}
