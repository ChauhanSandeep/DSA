package maths;

import java.util.Arrays;
import java.util.Random;


/**
 * Problem: Shuffle an Array
 *
 * Design an object that can reset an array to its original configuration and
 * return a uniformly random shuffle. The shuffle method should make every
 * permutation equally likely.
 *
 * Leetcode: https://leetcode.com/problems/shuffle-an-array/ (Medium)
 * Rating:   acceptance 59.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Randomization | Fisher-Yates shuffle
 *
 * Example:
 *   Input:  nums = [1,2,3], then call shuffle(), reset()
 *   Output: reset returns [1,2,3]
 *   Why:    reset must restore the saved original array regardless of prior shuffles.
 *
 * Follow-ups:
 *   1. How do you prove the shuffle is uniform?
 *      At each index, choose uniformly among the remaining elements, giving probability 1 / n!.
 *   2. How would you shuffle a stream of unknown length?
 *      Use reservoir sampling or assign random priorities as elements arrive.
 *   3. How would you make concurrent shuffles safe?
 *      Avoid shared mutable arrays or synchronize access around state changes.
 *
 * Related: Random Pick Index (398), Random Pick with Weight (528).
 */

public class RandomizedArrayShuffler {

  public static void main(String[] args) {
    int[][] inputs = { {1, 2, 3}, {} };

    for (int[] input : inputs) {
      ShuffleArraySolution solution = new ShuffleArraySolution(input);
      int[] shuffled = solution.shuffle();
      int[] sortedShuffle = shuffled.clone();
      int[] sortedExpected = input.clone();
      Arrays.sort(sortedShuffle);
      Arrays.sort(sortedExpected);
      boolean isPermutation = Arrays.equals(sortedShuffle, sortedExpected);
      System.out.printf("nums=%s -> %s  expected=%s%n",
          Arrays.toString(input), isPermutation, true);

      int[] reset = solution.reset();
      System.out.printf("reset nums=%s -> %s  expected=%s%n",
          Arrays.toString(input), Arrays.toString(reset), Arrays.toString(input));
    }
  }
}

class ShuffleArraySolution {

  private final int[] originalConfiguration; // Immutable reference to original array
  private int[] currentConfiguration; // Mutable working copy for shuffling
  private final Random randomGenerator; // Random number generator for shuffling

    /**
   * Intuition: reset needs a clean snapshot of the original input, while shuffle
   * needs a mutable working copy. Storing both copies lets each operation be
   * independent of previous shuffles.
   *
   * Algorithm:
   *   1. Reject null input.
   *   2. Clone nums into originalConfiguration for future resets.
   *   3. Clone nums into currentConfiguration for shuffle mutations.
   *   4. Create the Random instance used by shuffle.
   *
   * Time:  O(n) - two array clones copy every element.
   * Space: O(n) - original and current configurations are stored.
   *
   * @param nums input array to shuffle and reset
   */

  public ShuffleArraySolution(int[] nums) {
    if (nums == null) {
      throw new IllegalArgumentException("Input array cannot be null");
    }

    this.originalConfiguration = nums.clone(); // Deep copy to preserve original
    this.currentConfiguration = nums.clone(); // Working copy for mutations
    this.randomGenerator = new Random(); // Initialize random generator
  }

    /**
   * Intuition: resetting should forget all random swaps done so far. Cloning the
   * saved original configuration restores the exact starting order and prevents
   * callers from mutating the saved copy through the returned array.
   *
   * Algorithm:
   *   1. Clone originalConfiguration into currentConfiguration.
   *   2. Return the restored currentConfiguration.
   *
   * Time:  O(n) - cloning copies every element.
   * Space: O(n) - the restored working array is a fresh clone.
   *
   * @return array restored to the original configuration
   */

  public int[] reset() {
    currentConfiguration = originalConfiguration.clone();
    return currentConfiguration;
  }

    /**
   * Intuition: Fisher-Yates fixes the array from left to right. At index
   * currentIndex, every element from currentIndex through the end is equally
   * likely to be chosen for that slot, so no permutation is favored.
   *
   * Algorithm:
   *   1. Return immediately for arrays of size 0 or 1.
   *   2. For each currentIndex before the last element, count remainingElements.
   *   3. Pick randomOffset in that remaining range and convert it to randomIndex.
   *   4. Swap currentIndex with randomIndex in currentConfiguration.
   *   5. Return the shuffled working array.
   *
   * Time:  O(n) - each index is processed once.
   * Space: O(1) - shuffling mutates the working array in place.
   *
   * @return uniformly shuffled current array
   */

  public int[] shuffle() {
    if (currentConfiguration.length <= 1) {
      return currentConfiguration;
    }

    for (int currentIndex = 0; currentIndex < currentConfiguration.length - 1; currentIndex++) {
      int remainingElements = currentConfiguration.length - currentIndex;
      int randomOffset = randomGenerator.nextInt(remainingElements);
      int randomIndex = currentIndex + randomOffset;
      swapElements(currentConfiguration, currentIndex, randomIndex);
    }

    return currentConfiguration;
  }

    /** Swaps two positions in the given array when they differ. */

  private void swapElements(int[] array, int firstIndex, int secondIndex) {
    if (firstIndex != secondIndex) { // Optimization: avoid unnecessary work
      int temporaryValue = array[firstIndex];
      array[firstIndex] = array[secondIndex];
      array[secondIndex] = temporaryValue;
    }
  }

  /**
   * Returns the current state of the array without modification.
   * Useful for debugging and testing purposes.
   *
   * @return current array configuration
   */
  public int[] getCurrentConfiguration() {
    return currentConfiguration.clone(); // Return defensive copy
  }

  /**
   * Returns the original array configuration without modification.
   * Useful for verification and testing purposes.
   *
   * @return original array configuration
   */
  public int[] getOriginalConfiguration() {
    return originalConfiguration.clone(); // Return defensive copy
  }
}
