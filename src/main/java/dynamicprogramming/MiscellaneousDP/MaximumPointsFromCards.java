package dynamicprogramming.MiscellaneousDP;

import java.util.Arrays;
/**
 * Problem: Maximum Points You Can Obtain From Cards
 *
 * Cards are in a row, and each move takes one card from either the left end or
 * the right end. After exactly k cards, return the maximum score possible.
 *
 * Leetcode: https://leetcode.com/problems/maximum-points-you-can-obtain-from-cards/
 * Rating:   1574 (zerotrac Elo)
 * Pattern:  Sliding window | Prefix choice | Complement subarray
 *
 * Example:
 *   Input:  cardPoints = [1,2,3,4,5,6,1], k = 3
 *   Output: 12
 *   Why:    taking the three rightmost cards gives 1 + 6 + 5 = 12, better than any other end split.
 *
 * Follow-ups:
 *   1. Return which side was chosen at each step?
 *      Track the best split between left-taken and right-taken cards, then output that many L and R moves.
 *   2. What if cards can be taken from either end in variable-size batches?
 *      The simple complement window no longer applies; use interval DP over remaining ranges.
 *   3. What if k changes for many queries?
 *      Precompute prefix sums from both ends and answer each k by checking splits in O(k) or with extra RMQ structure.
 *
 * Related: Minimum Size Subarray Sum (209), Sliding Window Maximum (239).
 */
public class MaximumPointsFromCards {

    /**
     * Intuition: taking k cards from the ends is the same as leaving exactly n - k
     * contiguous cards in the middle. So maximizing the taken score means minimizing
     * the score of the left-behind window. A fixed-size sliding window finds the
     * minimum middle sum, and the answer is total sum minus that minimum.
     *
     * Algorithm:
     *   1. Sum all card points and return it immediately when k takes every card.
     *   2. Let windowSize be the number of cards left behind.
     *   3. Slide that fixed-size window to find the minimum middle sum.
     *   4. Return totalSum - minWindowSum.
     *
     * Time:  O(n) - the total and fixed-size window scans are linear.
     * Space: O(1) - only running sums are stored.
     *
     * @param cardPoints points on each card
     * @param k number of cards to take
     * @return maximum obtainable score
     */
    public int maxScore(int[] cardPoints, int k) {
        int totalCards = cardPoints.length;
        int totalSum = 0;

        // Calculate total sum of all cards
        for (int point : cardPoints) {
            totalSum += point;
        }

        // If we can take all cards, return total sum
        if (k >= totalCards) {
            return totalSum;
        }

        // Calculate sum of first (totalCards - k) cards
        int windowSize = totalCards - k;
        int windowSum = 0;
        for (int i = 0; i < windowSize; i++) {
            windowSum += cardPoints[i];
        }

        // Slide the window and find minimum window sum
        int minWindowSum = windowSum;
        for (int i = windowSize; i < totalCards; i++) {
            windowSum = windowSum + cardPoints[i] - cardPoints[i - windowSize];
            minWindowSum = Math.min(minWindowSum, windowSum);
        }

        // The maximum points is total sum minus minimum window sum
        return totalSum - minWindowSum;
    }

    /**
     * Two Pointers (Greedy) Solution
     *
     * Approach:
     * 1. Calculate the sum of the first k cards (all from left)
     * 2. Try all combinations of taking i cards from left and (k-i) from right
     * 3. Keep track of the maximum sum found
     *
     * Time Complexity: O(k)
     * Space Complexity: O(1)
     */
    public int maxScoreTwoPointers(int[] cardPoints, int k) {
        int leftSum = 0;
        int rightSum = 0;
        int maxSum = 0;

        // Calculate sum of first k cards (all from left)
        for (int i = 0; i < k; i++) {
            leftSum += cardPoints[i];
        }
        maxSum = leftSum;

        // Try all combinations of taking i cards from left and (k-i) from right
        int left = k - 1;
        int right = cardPoints.length - 1;

        for (int i = 0; i < k; i++) {
            leftSum -= cardPoints[left--];
            rightSum += cardPoints[right--];
            maxSum = Math.max(maxSum, leftSum + rightSum);
        }

        return maxSum;
    }

    /**
     * Dynamic Programming Solution
     *
     * Approach:
     * 1. Create two prefix sum arrays: one from left and one from right
     * 2. For each possible split (i cards from left, k-i from right), calculate the sum
     * 3. Return the maximum sum found
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int maxScoreDP(int[] cardPoints, int k) {
        int totalCards = cardPoints.length;
        int[] leftSum = new int[k + 1];
        int[] rightSum = new int[k + 1];

        // Calculate prefix sums from left and right
        for (int i = 0; i < k; i++) {
            leftSum[i + 1] = leftSum[i] + cardPoints[i];
            rightSum[i + 1] = rightSum[i] + cardPoints[totalCards - 1 - i];
        }

        // Try all possible splits
        int maxScore = 0;
        for (int i = 0; i <= k; i++) {
            int currentScore = leftSum[i] + rightSum[k - i];
            maxScore = Math.max(maxScore, currentScore);
        }

        return maxScore;
    }

    public static void main(String[] args) {
        MaximumPointsFromCards solver = new MaximumPointsFromCards();
        int[][] cards = {{1, 2, 3}, {1, 2, 3, 4, 5, 6, 1}, {2, 2, 2}};
        int[] kValues = {0, 3, 2};
        int[] expected = {0, 12, 4};

        for (int i = 0; i < cards.length; i++) {
            int got = solver.maxScore(cards[i], kValues[i]);
            System.out.printf("cardPoints=%s k=%d -> %d  expected=%d%n",
                Arrays.toString(cards[i]), kValues[i], got, expected[i]);
        }
    }

}
