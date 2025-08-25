package greedy;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode: https://leetcode.com/problems/majority-element/
 *
 * Problem: Find the majority element (appears more than ⌊ n/2 ⌋ times) in an array.
 *
 * Intuition:
 * - The Boyer-Moore Voting Algorithm efficiently finds the majority element in O(n) time with O(1) space.
 * - The HashMap approach provides an alternative, using extra space but maintaining simplicity.
 *
 * Algorithm:
 * 1. **Boyer-Moore Voting Algorithm** (O(n) time, O(1) space):
 *    - Select a candidate that potentially occurs more than ⌊ n/2 ⌋ times.
 *    - Validate if the candidate is truly the majority element.
 * 2. **HashMap Approach** (O(n) time, O(n) space):
 *    - Maintain a frequency map and return the first element exceeding ⌊ n/2 ⌋ occurrences.
 */
public class MajorityElement {

    public static void main(String[] args) {
        int[] nums = {1, 2, 2, 1, 1};
        MajorityElement solution = new MajorityElement();

        System.out.println("Majority Element (Boyer-Moore): " + solution.findMajorityElement(nums));
        System.out.println("Majority Element (HashMap): " + solution.findMajorityElementUsingMap(nums));
    }

    /**
     * Boyer-Moore Voting Algorithm
     * Finds the majority element in O(n) time and O(1) space.
     *
     * Approach
     * 1. Initialize a candidate and a count.
     * 2. Traverse the array:
     *   - If the current element matches the candidate, increment the count.
     *   - If it doesn't match, decrement the count.
     *   - If the count reaches zero, update the candidate to the current element.
     * 3. Verify if the candidate is indeed the majority element by counting its occurrences.
     *
     * @param nums Input array
     * @return Majority element or -1 if no majority exists
     */
    public int findMajorityElement(int[] nums) {
        int majorityCandidate = nums[0]; // Initial candidate
        int count = 1;

        // Phase 1: Identify potential majority candidate
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == majorityCandidate) {
                count++;
            } else {
                count--;
                if (count == 0) { // Reset candidate
                    majorityCandidate = nums[i];
                    count = 1;
                }
            }
        }

        // Phase 2: Verify if the candidate is actually the majority
        // we need to verify because the candidate might not be the majority in cases like [1,2,3,4,5]
        int occurrenceCount = 0;
        for (int num : nums) {
            if (num == majorityCandidate) {
                occurrenceCount++;
            }
        }

        return occurrenceCount > nums.length / 2 ? majorityCandidate : -1;
    }

    /**
     * HashMap Approach
     * Uses a HashMap to count occurrences and identify the majority element.
     * Time Complexity: O(n),
     * Space Complexity: O(n)
     *
     * @param nums Input array
     * @return Majority element or -1 if no majority exists
     */
    public int findMajorityElementUsingMap(int[] nums) {
        int majorityThreshold = nums.length / 2;
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int num : nums) {
            frequencyMap.merge(num, 1, Integer::sum); // Efficiently updates frequency count
            if (frequencyMap.get(num) > majorityThreshold) {
                return num; // Early exit if majority is found
            }
        }

        return -1; // Shouldn't occur, assuming the problem guarantees a majority element
    }
}
