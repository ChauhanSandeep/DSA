package Bitwise;

/**
 * LeetCode Problem: https://leetcode.com/problems/maximum-good-people-based-on-statements/
 *
 * Problem Statement:
 * You are given a 2D array `statements` of size n x n representing statements made by people about others.
 * Each `statements[i][j]` can be:
 * - 0: i says j is bad
 * - 1: i says j is good
 * - 2: i made no statement about j
 *
 * A person can either be good or bad. Good people always tell the truth, but bad people can lie or tell the truth.
 * Return the maximum number of people who can be good based on the given statements.
 *
 * Example:
 * Input: statements = [
 * [2,1,2],
 * [1,2,2],
 * [2,0,2]]
 *
 * Output: 2
 * Explanation:
 * - Person 0 states that person 1 is good.
 * - Person 1 states that person 0 is good.
 * - Person 2 states that person 1 is bad.
 *
 * Follow-up Questions (for interviews):
 * - What if the size of `statements` is very large (say > 25)?
 *   👉 Brute-force is not feasible, need constraint propagation or SAT solvers.
 * - Can we avoid using strings and work directly with bits?
 *   👉 Yes, you can optimize memory and speed using bitwise operations on integers.
 */
public class MaxGoodPeople {

  public static void main(String[] args) {
    int[][] statements = {
        {2, 1, 2},
        {1, 2, 2},
        {2, 0, 2}
    };
    System.out.println(new MaxGoodPeople().maximumGood(statements));  // Output: 2
  }

  /**
   * Finds the maximum number of people that can be good based on statements made.
   *
   * Algorithm:
   * - Iterate over all possible combinations of people (2^n subsets).
   * - Represent each combination as a bitmask, where 1 indicates "good".
   * - For each combination, validate it by ensuring all "good" people only tell the truth.
   * - Keep track of the combination with the highest count of good people.
   *
   * Time Complexity: O(2^n * n^2) — for every subset, we check each person’s statement about others.
   * Space Complexity: O(1) — constant extra space apart from input.
   *
   * @param statements 2D array where statements[i][j] indicates what person i says about person j.
   * @return Maximum number of good people possible.
   */
  public int maximumGood(int[][] statements) {
    if (statements == null || statements.length == 0) return 0;

    int n = statements.length;
    int maxGoodPeople = 0;

    int totalCombinations = 1 << n; // 2^n possible combinations
    for (int mask = 0; mask < totalCombinations; mask++) {
      if (isValidConfiguration(mask, statements, n)) {
        int goodCount = Integer.bitCount(mask); // Count of 1's in the bitmask
        maxGoodPeople = Math.max(maxGoodPeople, goodCount);
      }
    }
    return maxGoodPeople;
  }

  /**
   * Validates whether the current combination (bitmask) is logically consistent.
   * Steps:
   * - For each person i assumed to be good (bit i is set in mask):
   * - Check their statements about others.
   * - If they claim someone is good (1), ensure that person is also marked as good in the mask.
   * - If they claim someone is bad (0), ensure that person is marked as bad (not good) in the mask.
   *    - If any contradiction is found, return false.
   *
   */
  private boolean isValidConfiguration(int mask, int[][] statements, int n) {
    for (int i = 0; i < n; i++) {
      // ((mask >> i) & 1) checks if the i-th bit (from the right, 0-based) in the integer mask is set to 1.
      // This means person i is assumed to be good in this configuration.
      if (((mask >> i) & 1) == 1) {
        for (int j = 0; j < n; j++) {
          int statement = statements[i][j]; // statement of i about j
          if (statement == 2) continue; // No statement made

          int assumedGood = (mask >> j) & 1; // Check if j is assumed good in this configuration
          if (statement != assumedGood) {
            // Contradiction found: a good person lied
            return false;
          }
        }
      }
    }
    return true;
  }
}