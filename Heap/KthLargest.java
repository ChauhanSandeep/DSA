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
        int lo = 0;
        int hi = nums.length - 1;
        while (lo < hi) {
            final int j = partition(nums, lo, hi);
            if(j < k) {
                lo = j + 1;
            } else if (j > k) {
                hi = j - 1;
            } else {
                break;
            }
        }
        return nums[k];
    }

    public static int partition(int[] nums, int lo, int hi) {
        int pivot = nums[hi];
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (nums[j] <= pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        swap(nums, i, hi);
        return i;
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
