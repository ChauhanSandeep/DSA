package greedy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Majority Element
 *
 * Given an integer array, return the value that appears more than n / 2 times.
 * This file also returns -1 when the input does not actually contain such a
 * value, even though the original Leetcode problem guarantees one exists.
 *
 * Leetcode: https://leetcode.com/problems/majority-element/ (Easy)
 * Rating:   acceptance 66.4% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Boyer-Moore voting | Pair cancellation
 *
 * Example:
 *   Input:  nums = [2,2,1,1,1,2,2]
 *   Output: 2
 *   Why:    every non-2 can be paired with a 2 and one 2 still remains, so 2 is
 *           the only value that can cross the n / 2 threshold.
 *
 * Follow-ups:
 *   1. Return all values that appear more than n / 3 times?
 *      Keep two Boyer-Moore candidates, then verify both counts at the end.
 *   2. Answer many majority queries on subarrays?
 *      Use a segment tree or randomized checks with prefix counts for validation.
 *   3. What if no majority is guaranteed?
 *      Keep the same candidate pass, but the verification pass is mandatory.
 *   4. Find the majority in a stream with O(1) memory?
 *      Boyer-Moore still gives the candidate, but exact verification needs a second pass or extra storage.
 *
 * Related: Majority Element II (229), Online Majority Element In Subarray (1157).
 */
public class MajorityElement {

    public static void main(String[] args) {
        MajorityElement solver = new MajorityElement();
        int[][] inputs = { {2, 2, 1, 1, 1, 2, 2}, {1, 2, 3, 4}, {7} };
        int[] expected = { 2, -1, 7 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.findMajorityElement(inputs[i]);
            System.out.printf("nums=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition (interview default): imagine repeatedly deleting one occurrence
     * of value A and one occurrence of a different value B. A true majority
     * survives every such cancellation because it has more copies than all other
     * values combined. Boyer-Moore performs that deletion without a stack: the
     * candidate is the value currently ahead, and count is how many unmatched
     * copies of it remain. When count drops to zero the prefix has canceled
     * itself out, so the next value starts a fresh candidate. Because this file
     * allows "no majority", a final pass counts the survivor to confirm it.
     *
     * Algorithm:
     *   1. Initialize the candidate to the first value with count 1.
     *   2. Scan the rest: increment count on a match, decrement otherwise; when
     *      count reaches zero, adopt the current value as the new candidate.
     *   3. Count the candidate's real occurrences and confirm it passes n / 2.
     *
     * Time:  O(n) - two linear scans, each touching every number once.
     * Space: O(1) - only the candidate and a couple of counters.
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
     * Intuition: the direct baseline is to count real frequencies. A majority is
     * stricter than "most common": it must pass n / 2, so two different values
     * can never both qualify. The moment one count crosses the threshold the
     * answer is settled and we can return early. This costs more memory than
     * Boyer-Moore but is the simplest way to handle the "no majority" variant.
     *
     * Algorithm:
     *   1. Keep a value -> frequency map.
     *   2. For each value, bump its frequency and return it as soon as the
     *      frequency exceeds n / 2.
     *   3. Return -1 if no value ever crosses the threshold.
     *
     * Time:  O(n) - each number updates one map entry once.
     * Space: O(n) - worst case every number is distinct and gets its own entry.
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
