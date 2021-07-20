package Array;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class MergeKArrays {
    public static void main(String[] args) {
        int[] arr1 = { 1, 3, 5, 7 };
        int[] arr2 = { 2, 4, 6, 8 };
        int[] arr3 = { 0, 9, 10, 11 };
        int[] result = mergeKArrays(Arrays.asList(arr1, arr2, arr3));
        System.out.println(Arrays.toString(result));
    }

    public static int[] mergeKArrays(List<int[]> arrays) {
        PriorityQueue<ArrayContainer> minHeap = new PriorityQueue<>();
        int len = 0;
        for(int[] arr: arrays) {
            minHeap.add(new ArrayContainer(arr));
            len += arr.length;
        }
        int[] result = new int[len];
        int i =0;

        while(!minHeap.isEmpty()) {
            ArrayContainer container = minHeap.poll();
            result[i++] = container.arr[container.index];

            if(container.index < container.arr.length - 1) {
                minHeap.add(new ArrayContainer(container.arr, container.index + 1));
            }
        }
        return result;
    }
}

class ArrayContainer implements Comparable<ArrayContainer> {
    int[] arr;
    int index;

    public ArrayContainer(int[] arr) {
        this.arr = arr;
        this.index = 0;
    }
    public ArrayContainer(int[] arr, int index) {
        this.arr = arr;
        this.index = index;
    }

    public int compareTo(ArrayContainer o) {
        return this.arr[this.index] - o.arr[o.index];
    }
}
