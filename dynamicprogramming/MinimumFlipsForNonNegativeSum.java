package dynamicprogramming;

import java.util.Arrays;


/**
 * Problem: Minimum Flips to Make Sum Non-negative and Closest to Zero
 *
 * Given an array of positive integers, you need to flip the sign of some elements such that
 * the resultant sum of the array is minimum non-negative (as close to zero as possible).
 * Return the minimum number of elements that need to be flipped.
 *
 * Example:
 * Input: arr = [8, 4, 5, 7, 6, 2]
 * Output: 3
 * Explanation: We can flip [8, 4, 5] to get [-8, -4, -5, 7, 6, 2] = -2
 *              But we want non-negative, so we flip [4, 5, 7] to get [8, -4, -5, -7, 6, 2] = 0
 *              Minimum flips needed = 3
 *
 * GeeksforGeeks Problem Link: https://www.geeksforgeeks.org/minimum-flips-required-such-that-sum-of-array-is-non-negative/
 * InterviewBit Problem Link: https://www.interviewbit.com/problems/flip-array/
 *
 * Follow-up Questions:
 * 1. Q: What if we want exactly zero sum instead of closest to zero?
 *    A: Check if totalSum is even and if we can achieve exactly totalSum/2 using subset sum
 * 2. Q: What if we can add elements instead of just flipping signs?
 *    A: This becomes unbounded knapsack where we can add any positive number
 * 3. Q: Find all possible ways to achieve minimum flips?
 *    A: Use backtracking with DP to enumerate all valid combinations
 * 4. Q: What if elements can be negative initially?
 *    A: Separate positive and negative elements, handle them differently in DP transitions
 * 5. Q: Maximize the sum while using minimum flips?
 *    A: Two-phase DP: first minimize flips, then among minimum flips find maximum sum
 */
public class MinimumFlipsForNonNegativeSum {

  public static void main(String[] args) {
    MinimumFlipsForNonNegativeSum solver = new MinimumFlipsForNonNegativeSum();

    int[] testArray = {8, 4, 5, 7, 6, 2};
    System.out.println("Minimum flips needed: " + solver.minFlipsToAchieveNonNegativeSum(testArray));

    int[] anotherTest = {1, 2, 3, 4};
    System.out.println("Minimum flips needed: " + solver.minFlipsToAchieveNonNegativeSumOptimized(anotherTest));
  }

  /**
   * Finds minimum number of elements to flip to achieve closest non-negative sum using subset sum DP.
   *
   * Algorithm Steps:
   * 1. Calculate total sum and target (closest to totalSum/2)
   * 2. Use DP to find minimum flips needed to achieve each possible sum from 0 to target
   * 3. dp[j] represents minimum flips needed to achieve sum j by flipping elements
   * 4. For each element, update DP table in reverse order to avoid using same element twice
   * 5. Find largest achievable sum <= target and return corresponding flip count
   *
   * Time Complexity: O(n * sum/2) where n is array length and sum is total sum
   * Space Complexity: O(sum/2) for DP array
   *
   * @param positiveArray Array of positive integers
   * @return Minimum number of elements to flip for closest non-negative sum
   */
  public int minFlipsToAchieveNonNegativeSum(int[] positiveArray) {
    if (positiveArray == null || positiveArray.length == 0) {
      return 0;
    }

    int totalSum = Arrays.stream(positiveArray).sum();
    int targetSum = totalSum / 2; // We want to flip elements to get as close to this as possible

    // DP array where dp[i] represents minimum flips to achieve sum i
    int[] minFlipsForSum = new int[targetSum + 1];
    Arrays.fill(minFlipsForSum, Integer.MAX_VALUE);
    minFlipsForSum[0] = 0; // Zero flips needed to achieve sum 0

    // Process each element
    for (int currentElement : positiveArray) {
      // Traverse backward to avoid using same element multiple times
      for (int currentSum = targetSum; currentSum >= currentElement; currentSum--) {
        if (minFlipsForSum[currentSum - currentElement] != Integer.MAX_VALUE) {
          minFlipsForSum[currentSum] =
              Math.min(minFlipsForSum[currentSum], minFlipsForSum[currentSum - currentElement] + 1);
        }
      }
    }

    // Find the largest achievable sum closest to target
    for (int achievableSum = targetSum; achievableSum >= 0; achievableSum--) {
      if (minFlipsForSum[achievableSum] != Integer.MAX_VALUE) {
        return minFlipsForSum[achievableSum];
      }
    }

    return 0; // Should never reach here for valid input
  }

  /**
   * Space and time optimized version using boolean DP with count tracking.
   *
   * Algorithm Steps:
   * 1. Use boolean DP to track achievable sums
   * 2. For each achievable sum, track minimum flips needed separately
   * 3. Process elements one by one, updating both achievability and flip counts
   * 4. Find optimal result by scanning from target down to 0
   *
   * Time Complexity: O(n * sum/2) where n is array length
   * Space Complexity: O(sum/2) for DP arrays
   *
   * @param positiveArray Array of positive integers
   * @return Minimum number of elements to flip for closest non-negative sum
   */
  public int minFlipsToAchieveNonNegativeSumOptimized(int[] positiveArray) {
    if (positiveArray == null || positiveArray.length == 0) {
      return 0;
    }

    int totalSum = Arrays.stream(positiveArray).sum();
    int targetSum = totalSum / 2;

    // Track which sums are achievable and minimum flips for each
    boolean[] canAchieveSum = new boolean[targetSum + 1];
    int[] minFlipsToAchieve = new int[targetSum + 1];

    canAchieveSum[0] = true;
    minFlipsToAchieve[0] = 0;

    // Process each element to update achievable sums
    for (int elementValue : positiveArray) {
      // Process in reverse order to avoid using same element twice
      for (int currentSum = targetSum; currentSum >= elementValue; currentSum--) {
        if (canAchieveSum[currentSum - elementValue]) {
          int newFlipCount = minFlipsToAchieve[currentSum - elementValue] + 1;

          if (!canAchieveSum[currentSum] || newFlipCount < minFlipsToAchieve[currentSum]) {
            canAchieveSum[currentSum] = true;
            minFlipsToAchieve[currentSum] = newFlipCount;
          }
        }
      }
    }

    // Find the best achievable sum (closest to target)
    for (int sum = targetSum; sum >= 0; sum--) {
      if (canAchieveSum[sum]) {
        return minFlipsToAchieve[sum];
      }
    }

    return 0; // Fallback
  }

  /**
   * Alternative approach that also returns the actual elements to flip.
   *
   * Algorithm Steps:
   * 1. Use DP with parent tracking to reconstruct solution
   * 2. Store which element was used to achieve each sum
   * 3. Backtrack from optimal sum to find actual elements to flip
   * 4. Return both minimum flip count and the elements
   *
   * Time Complexity: O(n * sum/2) for DP + O(n) for backtracking
   * Space Complexity: O(sum/2) for DP and parent arrays
   *
   * @param positiveArray Array of positive integers
   * @return FlipResult containing minimum flips and elements to flip
   */
  public FlipResult minFlipsWithElements(int[] positiveArray) {
    if (positiveArray == null || positiveArray.length == 0) {
      return new FlipResult(0, new int[0]);
    }

    int totalSum = Arrays.stream(positiveArray).sum();
    int targetSum = totalSum / 2;

    int[] minFlipsForSum = new int[targetSum + 1];
    int[] parentElement = new int[targetSum + 1]; // Track which element achieved this sum

    Arrays.fill(minFlipsForSum, Integer.MAX_VALUE);
    Arrays.fill(parentElement, -1);
    minFlipsForSum[0] = 0;

    // Build DP table with parent tracking
    for (int elementIndex = 0; elementIndex < positiveArray.length; elementIndex++) {
      int elementValue = positiveArray[elementIndex];

      for (int currentSum = targetSum; currentSum >= elementValue; currentSum--) {
        if (minFlipsForSum[currentSum - elementValue] != Integer.MAX_VALUE) {
          int newFlipCount = minFlipsForSum[currentSum - elementValue] + 1;

          if (newFlipCount < minFlipsForSum[currentSum]) {
            minFlipsForSum[currentSum] = newFlipCount;
            parentElement[currentSum] = elementIndex;
          }
        }
      }
    }

    // Find optimal sum and reconstruct solution
    int optimalSum = -1;
    for (int sum = targetSum; sum >= 0; sum--) {
      if (minFlipsForSum[sum] != Integer.MAX_VALUE) {
        optimalSum = sum;
        break;
      }
    }

    // Backtrack to find elements to flip
    int[] elementsToFlip = new int[minFlipsForSum[optimalSum]];
    int flipIndex = elementsToFlip.length - 1;
    int currentSum = optimalSum;

    while (currentSum > 0 && parentElement[currentSum] != -1) {
      int elementIndex = parentElement[currentSum];
      elementsToFlip[flipIndex--] = positiveArray[elementIndex];
      currentSum -= positiveArray[elementIndex];
    }

    return new FlipResult(minFlipsForSum[optimalSum], elementsToFlip);
  }

  /**
   * Result class to hold both flip count and elements to flip.
   */
  public static class FlipResult {
    public final int minFlips;
    public final int[] elementsToFlip;

    public FlipResult(int minFlips, int[] elementsToFlip) {
      this.minFlips = minFlips;
      this.elementsToFlip = elementsToFlip;
    }

    @Override
    public String toString() {
      return String.format("MinFlips: %d, Elements: %s", minFlips, Arrays.toString(elementsToFlip));
    }
  }

  /**
   * Validation method to verify if the solution produces correct sum.
   *
   * @param originalArray Original array
   * @param elementsToFlip Elements that should be flipped
   * @return Resulting sum after flipping specified elements
   */
  public int calculateResultingSum(int[] originalArray, int[] elementsToFlip) {
    int[] arrayCopy = originalArray.clone();
    boolean[] shouldFlip = new boolean[originalArray.length];

    // Mark elements to flip
    for (int flipValue : elementsToFlip) {
      for (int i = 0; i < originalArray.length; i++) {
        if (originalArray[i] == flipValue && !shouldFlip[i]) {
          shouldFlip[i] = true;
          break;
        }
      }
    }

    // Calculate resulting sum
    int resultSum = 0;
    for (int i = 0; i < arrayCopy.length; i++) {
      resultSum += shouldFlip[i] ? -arrayCopy[i] : arrayCopy[i];
    }

    return resultSum;
  }
}
