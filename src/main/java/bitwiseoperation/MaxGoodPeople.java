package bitwiseoperation;

import java.util.Arrays;


/**
 * Problem: Maximum Good People Based on Statements
 *
 * Each person is either good or bad. Good people always tell the truth, while
 * bad people may say anything. Given statements about who is good or bad,
 * return the largest number of people who can consistently be good.
 *
 * Leetcode: https://leetcode.com/problems/maximum-good-people-based-on-statements/ (Hard)
 * Rating:   1980 (zerotrac Elo)
 * Pattern:  Bit manipulation | Subset enumeration | Truth consistency check
 *
 * Example:
 *   Input:  statements = [[2,1,2],[1,2,2],[2,0,2]]
 *   Output: 2
 *   Why:    People 0 and 1 can both be good because they only support each other;
 *           making person 2 good would force person 1 to be bad.
 *
 * Follow-ups:
 *   1. What if n grows beyond the subset-enumeration limit?
 *      Model the statements as SAT constraints or use branch-and-bound with propagation.
 *   2. Can partial assignments be pruned earlier?
 *      While assigning people, reject a branch as soon as a good person's known statement is contradicted.
 *   3. How would you return one optimal group, not just its size?
 *      Store the mask whenever it improves maxGoodPeople, then decode its set bits.
 *   4. What if bad people are required to always lie?
 *      Add the opposite constraints for bad speakers; the problem becomes stricter.
 */
public class MaxGoodPeople {

  public static void main(String[] args) {
    MaxGoodPeople solver = new MaxGoodPeople();

    int[][][] cases = {
        {
            {2, 1, 2},
            {1, 2, 2},
            {2, 0, 2}
        },
        {
            {2}
        },
        {
            {2, 0},
            {0, 2}
        }
    };
    int[] expected = {2, 1, 1};

    for (int i = 0; i < cases.length; i++) {
      int output = solver.maximumGood(cases[i]);
      System.out.printf("statements=%s -> %d  expected=%d%n",
          Arrays.deepToString(cases[i]), output, expected[i]);
    }
  }

  /**
   * Intuition: every possible set of good people can be encoded as a bitmask:
   * bit i is 1 when person i is assumed good. For a chosen mask, only good
   * people must be truthful, so the validation step checks statements made by
   * set bits and ignores statements from unset bits. Enumerating all masks then
   * lets the code keep the valid mask with the most set bits.
   *
   * Algorithm:
   *   1. Return 0 for a null or empty statements matrix.
   *   2. Enumerate all masks from 0 to 2^n - 1.
   *   3. Validate each mask by checking every statement made by assumed-good people.
   *   4. Count set bits in valid masks and keep the maximum count.
   *
   * Time:  O(2^n * n^2) - every mask may check every pair of people.
   * Space: O(1) - aside from the input, only counters and the current mask are stored.
   *
   * @param statements matrix where 0 means bad, 1 means good, and 2 means no statement
   * @return maximum possible number of good people
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

  /** Returns true when every assumed-good person's statements match the mask. */
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

  /**
   * Implementation using backtracking for better understanding.
   *
   * Algorithm: Recursive Backtracking with Pruning
   * Step 1: Try assigning each person as good or bad recursively
   * Step 2: At each level, check if current partial assignment is consistent
   * Step 3: Prune branches early if inconsistency is detected
   * Step 4: Continue recursion only for promising branches
   * Step 5: Track maximum good count found across all valid complete assignments
   *
   * Time Complexity: O(2ⁿ × n²) in worst case, but with pruning can be much better
   * Space Complexity: O(n) for recursion stack depth
   *
   * @param statements the statements matrix
   * @return maximum number of good people using backtracking
   */
  public int maximumGoodBacktracking(int[][] statements) {
    if (statements == null || statements.length == 0) {
      return 0;
    }

    int size = statements.length;
    int[] assignments = new int[size]; // 0 = bad, 1 = good, -1 = unknown
    Arrays.fill(assignments, -1);

    return backtrack(0, assignments, statements, 0);
  }

  // Recursive backtracking helper
  private int backtrack(int personIndex, int[] assignments, int[][] statements, int currentGoodCount) {
    int size = statements.length;

    // Base case: all people assigned
    if (personIndex == size) {
      return isValidAssignment(assignments, statements, size) ? currentGoodCount : 0;
    }

    int maxFound = 0;

    // Step 1: Try assigning current person as bad (0)
    assignments[personIndex] = 0;
    if (isValidAssignment(assignments, statements, personIndex)) {
      maxFound = Math.max(maxFound, backtrack(personIndex + 1, assignments, statements, currentGoodCount));
    }

    // Step 1: Try assigning current person as good (1)
    assignments[personIndex] = 1;
    if (isValidAssignment(assignments, statements, personIndex)) {
      maxFound = Math.max(maxFound, backtrack(personIndex + 1, assignments, statements, currentGoodCount + 1));
    }

    // Backtrack
    assignments[personIndex] = -1;
    return maxFound;
  }

  // Check if the assignment is valid till the last assigned person
  private boolean isValidAssignment(int[] assignment, int[][] statements, int lastAssigned) {
    // Check statements involving assigned people
    for (int i = 0; i <= lastAssigned; i++) {
      if (assignment[i] == 1) { // If person i is good
        for (int j = 0; j <= lastAssigned; j++) {
          if (statements[i][j] != 2) { // If statement was made
            if (assignment[j] != statements[i][j]) {
              return false; // Good person's statement contradicted
            }
          }
        }
      }
    }
    return true;
  }
}