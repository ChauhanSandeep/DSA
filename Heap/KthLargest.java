package Heap;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * LeetCode: https://leetcode.com/problems/kth-largest-element-in-an-array/
 *
 * Find the Kth largest element in an array using two approaches:
 * 1. QuickSelect (Partitioning) - O(n) average time complexity
 * 2. Min Heap - O(n log k) time complexity
 */
public class KthLargest {

    public static void main(String[] args) {
        int k = 3;
        int[] arr = {3, 2, 3, 1, 2, 4, 5, 5, 6};

        System.out.println(Arrays.toString(kthLargestInStream(k, arr)));
        // System.out.println(findKthLargest(arr, k));
    }

    /**
     * QuickSelect algorithm to find the Kth largest element.
     * Uses partitioning similar to QuickSort.
     */
    public static int findKthLargest(int[] nums, int k) {
        k = nums.length - k;
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int pivotIndex = partition(nums, left, right);
            if (pivotIndex == k) break;
            else if (pivotIndex < k) left = pivotIndex + 1;
            else right = pivotIndex - 1;
        }
        return nums[k];
    }

    private static int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int pointer = left;
        for (int i = left; i < right; i++) {
            if (nums[i] <= pivot) {
                swap(nums, pointer, i);
                pointer++;
            }
        }
        swap(nums, pointer, right);
        return pointer;
    }

    private static void swap(int[] nums, int i, int j) {
        if (i != j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }

    /**
     * Min Heap approach to find the Kth largest element.
     * Maintains a min-heap of size K.
     */
    public static int kthLargest(int k, int[] arr) {
        if (arr == null || arr.length == 0 || k <= 0) return -1;

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : arr) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }

    /**
     * Finds the Kth largest element for each element in the stream.
     */
    public static int[] kthLargestInStream(int k, int[] arr) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        if (arr == null || arr.length == 0) return new int[]{};

        int[] result = new int[arr.length];
        Arrays.fill(result, -1);

        for (int i = 0; i < arr.length; i++) {