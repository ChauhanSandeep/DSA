package arrays.prefixsum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Problem: All Divisions With the Highest Score of a Binary Array
 *
 * You are given a 0-indexed binary array nums of length n. nums can be divided at index i
 * (where 0 <= i <= n) into two arrays numsleft and numsright.
 *
 * The score of a partition is the sum of:
 * - The number of 0's in numsleft
 * - The number of 1's in numsright
 *
 * Return all distinct indices that have the highest possible division score. You may return
 * the answer in any order.
 *
 * Example:
 * Input: nums = [0,0,1,0]
 * Output: [2,4]
 * Explanation: Division at index 2: numsleft = [0,0], numsright = [1,0]. Score = 2 + 1 = 3.
 * Division at index 4: numsleft = [0,0,1,0], numsright = []. Score = 3 + 0 = 3.
 * Both indices 2 and 4 have the highest score of 3.
 *
 * Constraints:
 * - n == nums.length
 * - 1 <= n <= 10^5
 * - nums[i] is either 0 or 1
 *
 * LeetCode Problem: https://leetcode.com/problems/all-divisions-with-the-highest-score-of-a-binary-array
 *
 * Follow-up Questions:
 *
 * 1. What if the scoring function changes to weighted sum instead of count?
 *    Answer: Modify the prefix sum calculation to track weighted sums instead of counts.
 *    The algorithm structure remains the same, just update the accumulation logic.
 *
 * 2. How would you handle multiple score queries with different scoring functions?
 *    Answer: Precompute prefix arrays for both zeros and ones. For each query with a
 *    different scoring function, calculate scores using the precomputed arrays in O(n) time.
 *
 * 3. What if you need to find k divisions with highest scores instead of all maximum?
 *    Answer: Use a min-heap of size k to track top k scores along with their indices.
 *    After processing all divisions, extract indices from the heap.
 *
 * 4. Can you solve this in O(1) space excluding the output array?
 *    Answer: Yes, the single-pass approach already achieves this by maintaining only
 *    running counters without storing intermediate scores.
 *
 * 5. How would you extend this to handle array elements with values other than 0 and 1?
 *    Answer: Maintain separate prefix sums for each distinct value in the array. The score
 *    calculation would need to be generalized based on the specific scoring rules for each value.
 */
public class AllDivisionsWithTheHighestScoreOfABinaryArray {

    /**
     * Finds all division indices with maximum score using single-pass approach.
     *
     * Algorithm:
     * 1. Count total ones in array initially (right part score at index 0)
     * 2. Iterate through array, maintaining running counts:
     *    - leftZeros: number of zeros seen so far
     *    - rightOnes: number of ones remaining in right part
     * 3. At each position, calculate score = leftZeros + rightOnes
     * 4. Track maximum score and collect all indices achieving it
     * 5. Update counts: increment leftZeros if current is 0, decrement rightOnes if current is 1
     *
     * Key insight: The score at each division point can be computed incrementally
     * by maintaining counts of zeros on the left and ones on the right as we traverse.
     *
     * Time Complexity: O(N) where N is the length of the array. Single pass to compute
     * initial ones count, then one pass to calculate all division scores.
     *
     * Space Complexity: O(1) excluding the output array. Only uses constant extra space
     * for counters and tracking maximum score.
     *
     * @param nums binary array of 0s and 1s
     * @return list of indices with highest division score
     */
    public List<Integer> maxScoreIndices(int[] nums) {
        int length = nums.length;
        int leftZeros = 0;
        int rightOnes = IntStream.of(nums).sum();

        int maxScore = rightOnes;
        List<Integer> result = new ArrayList<>();
        result.add(0);

        for (int i = 0; i < length; i++) {
            if (nums[i] == 0) {
                leftZeros++;
            } else {
                rightOnes--;
            }

            int currentScore = leftZeros + rightOnes;

            if (currentScore > maxScore) {
                maxScore = currentScore;
                result.clear();
                result.add(i + 1);
            } else if (currentScore == maxScore) {
                result.add(i + 1);
            }
        }

        return result;
    }

    /**
     * Alternative approach using prefix and suffix arrays for clearer separation.
     * Precomputes left zeros and right ones for all positions.
     *
     * Algorithm:
     * 1. Build prefix array counting zeros from left up to each index
     * 2. Build suffix array counting ones from each index to right
     * 3. Calculate score at each division as prefix[i] + suffix[i]
     * 4. Find maximum score and collect all indices with that score
     *
     * Time Complexity: O(N) for three passes: prefix, suffix, and score calculation.
     *
     * Space Complexity: O(N) for prefix and suffix arrays.
     *
     * @param nums binary array of 0s and 1s
     * @return list of indices with highest division score
     */
    public List<Integer> maxScoreIndicesPrefixSuffix(int[] nums) {
        int length = nums.length;
        int[] leftZeros = new int[length + 1];
        int[] rightOnes = new int[length + 1];

        for (int i = 0; i < length; i++) {
            leftZeros[i + 1] = leftZeros[i] + (nums[i] == 0 ? 1 : 0);
        }

        for (int i = length - 1; i >= 0; i--) {
            rightOnes[i] = rightOnes[i + 1] + (nums[i] == 1 ? 1 : 0);
        }

        int maxScore = 0;
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i <= length; i++) {
            int score = leftZeros[i] + rightOnes[i];

            if (score > maxScore) {
                maxScore = score;
                result.clear();
                result.add(i);
            } else if (score == maxScore) {
                result.add(i);
            }
        }

        return result;
    }
}
