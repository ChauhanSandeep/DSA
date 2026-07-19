package arrays.prefixsum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Arrays;
/**
 * Problem: All Divisions With the Highest Score of a Binary Array
 *
 * Given a binary array, choose a division index i between 0 and n. The score is
 * the number of zeros on the left plus the number of ones on the right. Return
 * every division index that reaches the maximum score.
 *
 * Leetcode: https://leetcode.com/problems/all-divisions-with-the-highest-score-of-a-binary-array/ (Medium)
 * Rating:   1391 (Weekly Contest 278)
 * Pattern:  Prefix counts | Running score | Binary array
 *
 * Example:
 *   Input:  nums = [0,0,1,0]
 *   Output: [2,4]
 *   Why:    divisions 2 and 4 both score 3, which is the largest possible score.
 *
 * Follow-ups:
 *   1. Weighted zeros and ones?
 *      Track weighted running gains instead of plain counts.
 *   2. Return only the earliest best division?
 *      Keep the same scan but clear ties instead of storing all of them.
 *   3. Support point updates?
 *      Use a segment tree over the score delta array.
 *
 * Related: Maximum Score After Splitting a String (1422).
 */
public class AllDivisionsWithTheHighestScoreOfABinaryArray {

    public static void main(String[] args) {
        AllDivisionsWithTheHighestScoreOfABinaryArray solver =
            new AllDivisionsWithTheHighestScoreOfABinaryArray();

        int[][] inputs = { {0, 0, 1, 0}, {1, 1}, {0, 0} };
        String[] expected = { "[2, 4]", "[0]", "[2]" };

        for (int i = 0; i < inputs.length; i++) {
            List<Integer> got = solver.maxScoreIndices(inputs[i]);
            System.out.printf("nums=%s -> output=%s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

/**
 * Intuition: start with the division before index 0, where every one is on the
 * right. Moving the divider past a zero increases the score by one; moving it
 * past a one decreases the right-side ones by one.
 *
 * Algorithm:
 *   1. Count all ones as rightOnes and score division 0.
 *   2. Move the divider across nums from left to right.
 *   3. Update leftZeros or rightOnes based on the crossed value.
 *   4. Compare the new score against maxScore and maintain all best indices.
 *
 * Time:  O(n) - one sum pass and one divider pass.
 * Space: O(1) - excluding the returned list of best indices.
 *
 * @param nums binary array
 * @return all division indices with maximum score
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
