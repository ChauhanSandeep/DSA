package Maths;

import java.util.Arrays;
import java.util.Random;

/**
 * Problem: Shuffle an array randomly while allowing a reset to the original order.
 * 
 * Given an integer array, implement two functions:
 * - `shuffle()`: Returns a randomly shuffled version of the array.
 * - `reset()`: Restores the original array configuration.
 * 
 * Approach:
 * - Store the original array as a reference for the reset operation.
 * - Use the Fisher-Yates Shuffle Algorithm for efficient in-place shuffling.
 * - Generate random indices carefully to avoid biased results.
 * 
 * Time Complexity:
 * - `reset()`: O(N) - Cloning the original array.
 * - `shuffle()`: O(N) - Iterating and swapping elements.
 * 
 * Space Complexity: O(N) - Storing a clone of the original array.
 * 
 * LeetCode Link: https://leetcode.com/problems/shuffle-an-array/
 */
public class ArrayPermutationDriver {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        Solution solution = new Solution(arr);

        System.out.println("Shuffled: " + Arrays.toString(solution.shuffle()));
        System.out.println("Reset: " + Arrays.toString(solution.reset()));
        System.out.println("Shuffled Again: " + Arrays.toString(solution.shuffle()));
    }
}

class Solution {
    private final int[] originalArray; // Stores the original array
    private int[] shuffledArray; // Working copy of the array
    private final Random random; // Random generator

    public Solution(int[] nums) {
        this.originalArray = nums.clone(); // Store original array for reset
        this.shuffledArray = nums.clone(); // Clone array for mutation
        this.random = new Random();
    }

    /** Resets the array to its original configuration and returns it. */
    public int[] reset() {
        shuffledArray = originalArray.clone(); // Restore original order
        return shuffledArray;
    }

    /** Returns a randomly shuffled version of the array using Fisher-Yates Algorithm. */
    public int[] shuffle() {
        for (int i = shuffledArray.length - 1; i > 0; i--) {
            int randomIndex = getRandomIndex(i);
            swap(shuffledArray, i, randomIndex);
        }
        return shuffledArray;
    }

    /**
     * Generates a random index between 0 and `end` (inclusive).
     * Ensures fair shuffling by selecting from the remaining elements.
     */
    private int getRandomIndex(int end) {
        return random.nextInt(end + 1); // Generates random number in range [0, end]
    }

    /** Swaps two elements in the array. */
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
