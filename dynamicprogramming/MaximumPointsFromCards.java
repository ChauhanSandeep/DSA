package dynamicprogramming;

/**
 * Problem: Maximum Points You Can Obtain From Cards (LeetCode #1423)
 *
 * Problem Statement:
 * There are several cards arranged in a row, and each card has an associated number of points.
 * The points are given in the integer array cardPoints. In one step, you can take one card from the
 * beginning or from the end of the row. You have to take exactly k cards. Your score is the sum of
 * the points of the cards you have taken. Given the integer array cardPoints and the integer k,
 * return the maximum score you can obtain.
 *
 * Example 1:
 * Input: cardPoints = [1,2,3,4,5,6,1], k = 3
 * Output: 12
 * Explanation: After the first step, your score will always be 1. However, choosing the rightmost card first
 * will maximize your total score. The optimal strategy is to take the three cards on the right, giving a
 * final score of 1 + 6 + 5 = 12.
 *
 * Example 2:
 * Input: cardPoints = [2,2,2], k = 2
 * Output: 4
 * Explanation: Regardless of which two cards you take, your score will always be 4.
 *
 * Approaches:
 * 1. Sliding Window (Optimal): O(k) time, O(1) space
 *    - The problem can be transformed into finding the minimum subarray of size (n - k)
 *    - The maximum points will be totalSum - minSubarraySum
 *
 * 2. Prefix Sum with Sliding Window: O(n) time, O(n) space
 *    - Calculate prefix sums and slide a window of size (n - k) to find minimum subarray sum
 *
 * 3. Two Pointers (Greedy): O(k) time, O(1) space
 *    - Try all possible combinations of taking i cards from left and (k-i) from right
 *
 * Time Complexity: O(k) for optimal solution
 * Space Complexity: O(1) for optimal solution
 *
 * Follow-up Questions:
 * 1. What if we need to handle very large k values (k > cardPoints.length)?
 *    Answer: We can take k % cardPoints.length and calculate accordingly, as the pattern would repeat.
 *
 * 2. Can we extend this to take from both ends in any order?
 *    Answer: The problem already allows taking from either end in any order, but if you mean taking
 *    multiple cards at once, that would be a different problem requiring a different approach.
 *
 * 3. How would you modify the solution if each card has a weight and we need to maximize weighted sum?
 *    Answer: The sliding window approach would still work, but we'd need to adjust the sum calculation
 *    to account for weights, making it more complex.
 *
 * LeetCode: https://leetcode.com/problems/maximum-points-you-can-obtain-from-cards/
 */
public class MaximumPointsFromCards {

    /**
     * Optimal Sliding Window Solution
     *
     * Approach:
     * 1. Calculate the total sum of all cards
     * 2. Calculate the sum of the first (n - k) cards (minimum subarray)
     * 3. Slide the window of size (n - k) and keep track of the minimum sum
     * 4. The answer is totalSum - minSubarraySum
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
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
        int n = cardPoints.length;
        int[] leftSum = new int[k + 1];
        int[] rightSum = new int[k + 1];

        // Calculate prefix sums from left and right
        for (int i = 0; i < k; i++) {
            leftSum[i + 1] = leftSum[i] + cardPoints[i];
            rightSum[i + 1] = rightSum[i] + cardPoints[n - 1 - i];
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
        MaximumPointsFromCards solution = new MaximumPointsFromCards();

        // Test cases
        int[] cards1 = {1, 2, 3, 4, 5, 6, 1};
        System.out.println("Test 1: " + solution.maxScore(cards1, 3)); // Expected: 12

        int[] cards2 = {2, 2, 2};
        System.out.println("Test 2: " + solution.maxScore(cards2, 2)); // Expected: 4

        int[] cards3 = {9, 7, 7, 9, 7, 7, 9};
        System.out.println("Test 3: " + solution.maxScore(cards3, 7)); // Expected: 55

        // Test with k = 0 (edge case)
        System.out.println("Test 4: " + solution.maxScore(cards1, 0)); // Expected: 0
    }
}
