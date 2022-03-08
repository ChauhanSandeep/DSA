package Heap;

import java.util.Arrays;
import java.util.PriorityQueue;

public class KthLargest {

    public static void main(String[] args) {
        int k = 3;
        int[] arr = {3,2,3,1,2,4,5,5,6};


        System.out.println(Arrays.toString(kthLargestInStream(k, arr)));
//        System.out.println(findKthLargest(arr, k));
    }

    public static int findKthLargest(int[] nums, int k) {
        k = nums.length - k;
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            final int pivotIndex = partition(nums, left, right);
            if(pivotIndex == k) break;
            else if(pivotIndex < k) left = pivotIndex + 1;
            else right = pivotIndex - 1;
        }
        return nums[k];
    }

    public static int partition(int[] nums, int left, int right) {
        int pivotElement = nums[right];
        int pointer = left;
        for (int i = left; i < right; i++) {
            if (nums[i] <= pivotElement) {
                swap(nums, pointer, i);
                pointer++;
            }
        }
        swap(nums, pointer, right);
        return pointer;
    }

    public static void swap(int[] nums, int i, int j) {
        if(i == j) return;
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }



    /**
     * Find the kth largest element in array
     */
    public static int kthLargest(int k, int[] arr) {
        if(arr == null || arr.length == 0 || k < 0) return -1;

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for(int i=0; i<arr.length; i++) {
            minHeap.offer(arr[i]);
            while(minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }

    /**
     * In a stream find the kth the largest element for when each element of stream arrives
     */
    public static int[] kthLargestInStream(int k, int[] arr) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        if (arr == null || arr.length == 0) return new int[]{};

        int[] result = new int[arr.length];
        Arrays.fill(result, -1);

        for (int i = 0; i < arr.length; i++) {
            minHeap.offer(arr[i]);
            while (minHeap.size() > k) {
                minHeap.poll();
            }
            if (minHeap.size() == k) {
                result[i] = minHeap.peek();
            }
        }
        return result;
    }
}
