package Heap;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * LeetCode Problems:
 * - Kth Largest Element in an Array: https://leetcode.com/problems/kth-largest-element-in-an-array/
 * - Kth Largest Element in a Stream: https://leetcode.com/problems/kth-largest-element-in-a-stream/
 *
 * This class demonstrates three approaches to find the kth largest element:
 * 1. QuickSelect (findKthLargest): Uses partitioning (similar to QuickSort) to find the kth largest element.
 *    - Average Time Complexity: O(n)
 *    - Worst-case Time Complexity: O(n^2)
 *    - Space Complexity: O(1) (in-place)
 *
 * 2. Min-Heap (findKthLargestUsingHeap): Maintains a min-heap of size k to determine the kth largest element.
 *    - Time Complexity: O(n log k)
 *    - Space Complexity: O(k)
 *
 * 3. Streaming Approach (kthLargestInStream): Processes elements as they arrive, returning an array where each index i
 *    contains the kth largest element among the first (i+1) elements.
 *    - Time Complexity per element: O(log k)
 *    - Space Complexity: O(k)
 */
public class KthLargest {

    public static void main(String[] args) {
        int k = 3;
        int[] inputArray = {3, 2, 3, 1, 2, 4, 5, 5, 6};

        // Demonstrate streaming approach: kth largest element after processing each element in the stream.
        System.out.println(Arrays.toString(kthLargestInStream(k, inputArray)));

        // Uncomment the following line to test the QuickSelect approach.
        // System.out.println(findKthLargest(inputArray, k));

        // Uncomment the following line to test the Min-Heap approach.
        // System.out.println(findKthLargestUsingHeap(inputArray, k));
    }

    /**
     * QuickSelect approach to find the kth largest element in the array.
     *
     * @param nums The input array.
     * @param k    The kth largest element to find.
     * @return The kth largest element.
     */
    public static int findKthLargest(int[] nums, int k) {
        // Convert kth largest to the corresponding index in sorted order (0-indexed)
        int targetIndex = nums.length - k;
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int pivotIndex = partition(nums, left, right);
            if (pivotIndex == targetIndex) {
                break;
            } else if (pivotIndex < targetIndex) {
                left = pivotIndex + 1;
            } else {
                right = pivotIndex - 1;
            }
        }
        return nums[targetIndex];
    }

    /**
     * Partition the array using the last element as pivot.
     *
     * Elements less than or equal to the pivot are moved to its left.
     *
     * @param nums  The array to partition.
     * @param left  The starting index.
     * @param right The ending index.
     * @return The final index position of the pivot element.
     */
    public static int partition(int[] nums, int left, int right) {
        int pivotElement = nums[right];
        int partitionIndex = left;
        for (int i = left; i < right; i++) {
            // Move elements less than or equal to pivot to the left side.
            if (nums[i] <= pivotElement) {
                swap(nums, partitionIndex, i);
                partitionIndex++;
            }
        }
        // Place pivot in its correct sorted position.
        swap(nums, partitionIndex, right);
        return partitionIndex;
    }

    /**
     * Utility method to swap two elements in an array.
     *
     * @param nums The array.
     * @param i    The first index.
     * @param j    The second index.
     */
    public static void swap(int[] nums, int i, int j) {
        if (i == j) return; // No need to swap identical indices.
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * Min-Heap approach to find the kth largest element in the array.
     *
     * @param nums The input array.
     * @param k    The kth largest element to find.
     * @return The kth largest element, or -1 if input is invalid.
     */
    public static int findKthLargestUsingHeap(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) return -1;

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        // Maintain a min-heap of size k.
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }

    /**
     * Streaming approach to determine the kth largest element as new elements arrive.
     *
     * For each new element in the input stream, the method updates a min-heap to maintain
     * the kth largest element seen so far. It returns an array where each index i contains
     * the kth largest element among the first (i+1) elements.
     *
     * @param k   The kth largest element to track.
     * @param arr The input stream represented as an array.
     * @return An array representing the kth largest element after each new element is processed.
     */
    public static int[] kthLargestInStream(int k, int[] arr) {
        if (arr == null || arr.length == 0) return new int[]{};

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int[] kthLargestResults = new int[arr.length];
        // Initialize results with -1 (indicating insufficient elements to determine kth largest)
        Arrays.fill(kthLargestResults, -1);

        for (int i = 0; i < arr.length; i++) {
            // Add the current element to the heap.
            minHeap.offer(arr[i]);
            // If heap size exceeds k, remove the smallest element.
            if (minHeap.size() > k) {
                minHeap.poll();
            }
            // Once we have at least k elements, record the kth largest (top of min-heap).
            if (minHeap.size() == k) {
                kthLargestResults[i] = minHeap.peek();
            }
        }
        return kthLargestResults;
    }
}