package arrays.xor;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Problem: Single Number
 *
 * Given a non-empty array where every value appears exactly twice except one
 * value that appears once, return the single value. The intended solution should
 * run in linear time and use constant extra space.
 *
 * Leetcode: https://leetcode.com/problems/single-number/
 * Rating:   acceptance 78.0% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Arrays | XOR | Pair cancellation
 *
 * Example:
 *   Input:  [4,1,2,1,2]
 *   Output: 4
 *   Why:    the two 1s cancel each other, the two 2s cancel each other, and 4 is
 *           the only value without a matching pair.
 *
 * Follow-ups:
 *   1. What if every other value appears three times?
 *      Count bits modulo 3 or use a bit-state machine.
 *   2. What if exactly two values appear once?
 *      XOR all values, split by one set bit, and XOR each group separately.
 *   3. What if extra space is allowed and clarity matters more?
 *      Count frequencies with a HashMap and return the value with count 1.
 *
 * Related: Single Number II (137), Single Number III (260).
 */
public class SingleNumber {

    public static void main(String[] args) {
        SingleNumber solver = new SingleNumber();
        int[][] inputs = {{2, 2, 1}, {4, 1, 2, 1, 2}, {1}};
        int[] expected = {1, 4, 1};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.singleNumber(inputs[i]);
            System.out.printf("nums=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


    /**
     * Intuition (interview default): XOR is perfect for pairs because a value xored
     * with itself becomes zero, and zero xored with a value leaves that value. XOR
     * is also order-independent, so we can fold the whole array into one running
     * value. Every duplicated number cancels out with its twin, leaving only the
     * number that appeared once. That gives the required linear time and constant
     * extra space.
     *
     * Time:  O(n) - each number participates in one XOR operation.
     * Space: O(1) - only the running XOR value is stored.
     *
     * @param nums array where every value appears twice except one
     * @return value that appears exactly once
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
