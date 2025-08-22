package Maths;

import java.util.Arrays;
import java.util.Random;


/**
 * Problem: Shuffle an Array
 *
 * Given an integer array nums, design an algorithm to randomly shuffle the array.
 * All permutations of the array should be equally likely as a result of the shuffling.
 *
 * Implement the Solution class:
 * - Solution(int[] nums): Initializes the object with the integer array nums.
 * - int[] reset(): Resets the array to its original configuration and returns it.
 * - int[] shuffle(): Returns a random shuffling of the array.
 *
 * Example:
 * Input:
 * ["Solution", "shuffle", "reset", "shuffle"]
 * [[[1, 2, 3]], [], [], []]
 * Output:
 * [null, [3, 1, 2], [1, 2, 3], [1, 3, 2]]
 *
 * LeetCode: https://leetcode.com/problems/shuffle-an-array/
 *
 * Follow-up Questions (FAANG-style):
 * 1. How do you ensure all permutations are equally likely?
 *    - Use Fisher-Yates shuffle which guarantees uniform distribution of all n! permutations.
 * 2. What if you need to shuffle a stream of elements without knowing the size?
 *    - Use Reservoir Sampling algorithm for streaming shuffle.
 *    - Related: https://leetcode.com/problems/random-pick-index/
 * 3. How would you shuffle a linked list?
 *    - Convert to array, shuffle, then rebuild list, or use recursive approach.
 * 4. What if you need to shuffle very large arrays that don't fit in memory?
 *    - Use external sorting techniques with random pivot selection.
 * 5. How do you shuffle with weighted probabilities for each element?
 *    - Use weighted reservoir sampling or cumulative distribution function.
 * 6. What if multiple threads need to shuffle simultaneously?
 *    - Use ThreadLocalRandom or synchronize access to shared Random instance.
 */
public class RandomizedArrayShuffler {

  public static void main(String[] args) {
    int[] testArray = {1, 2, 3, 4, 5};
    ShuffleArraySolution solution = new ShuffleArraySolution(testArray);

    System.out.println("Original: " + Arrays.toString(testArray));
    System.out.println("Shuffled: " + Arrays.toString(solution.shuffle()));
    System.out.println("Reset: " + Arrays.toString(solution.reset()));
    System.out.println("Shuffled Again: " + Arrays.toString(solution.shuffle()));

    // Test edge cases
    int[] emptyArray = {};
    ShuffleArraySolution emptySolution = new ShuffleArraySolution(emptyArray);
    System.out.println("Empty Array Shuffle: " + Arrays.toString(emptySolution.shuffle()));

    int[] singleElement = {42};
    ShuffleArraySolution single = new ShuffleArraySolution(singleElement);
    System.out.println("Single Element Shuffle: " + Arrays.toString(single.shuffle()));
  }
}

class ShuffleArraySolution {

  private final int[] originalConfiguration; // Immutable reference to original array
  private int[] currentConfiguration; // Mutable working copy for shuffling
  private final Random randomGenerator; // Random number generator for shuffling

  /**
   * Initializes the solution with the given array.
   *
   * Algorithm: Array Cloning and Random Generator Setup
   * Steps:
   * 1. Store immutable copy of original array for reset functionality
   * 2. Create mutable working copy for shuffle operations
   * 3. Initialize random generator for unbiased shuffling
   *
   * Time Complexity: O(n) for array cloning
   * Space Complexity: O(n) for storing original and working copies
   *
   * @param nums the input array to be shuffled
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
   * Resets the array to its original configuration and returns it.
   *
   * Algorithm: Array Restoration
   * Steps:
   * 1. Create fresh copy of original array
   * 2. Replace current working array with restored version
   * 3. Return the reset array
   *
   * Time Complexity: O(n) for array cloning
   * Space Complexity: O(1) auxiliary space
   *
   * @return array in original configuration
   */
  public int[] reset() {
    currentConfiguration = originalConfiguration.clone();
    return currentConfiguration;
  }

  /**
   * Implementation using Knuth shuffle (original Fisher-Yates).
   *
   * Algorithm: Knuth Shuffle (Forward Direction)
   * Steps:
   * 1. Iterate from first to second-last element
   * 2. For each position i, select random element from remaining unshuffled elements
   * 3. Swap current element with randomly selected element
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   *
   * @return randomly shuffled array using forward iteration
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

  /**
   * Swaps two elements in the array at specified indices.
   *
   * Performs in-place swap using temporary variable to avoid
   * potential issues with XOR swap when indices are equal.
   *
   * Time Complexity: O(1)
   * Space Complexity: O(1)
   *
   * @param array the array containing elements to swap
   * @param firstIndex index of first element
   * @param secondIndex index of second element
   */
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
