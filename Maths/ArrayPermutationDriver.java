package Maths;

import java.util.Arrays;
import java.util.Random;

public class ArrayPermutationDriver {
    public static void main(String[] args) {
        int[] arr = {1,2,3};
        Solution solution = new Solution(arr);
        System.out.println(Arrays.toString(solution.shuffle()));
        System.out.println(Arrays.toString(solution.reset()));
        System.out.println(Arrays.toString(solution.shuffle()));
    }
}

class Solution {

    int[] original;
    int[] clone;

    Random random = new Random();

    public Solution(int[] nums) {
        this.clone = nums;
        original = nums.clone();
    }

    /** Resets the array to its original configuration and return it. */
    public int[] reset() {
        clone = original.clone();
        return clone;
    }

    /** Returns a random shuffling of the array. */
    public int[] shuffle() {
        for(int i=0; i<original.length; i++) {
            swap(clone, i, createRandom(i, original.length));
        }
        return clone;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int createRandom(int start, int end) {
        return random.nextInt(end-start) + start;
    }
}
