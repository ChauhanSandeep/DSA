package Array;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class MergeKArrays {
    public static void main(String[] args) {
        int[] arr1 = {1, 3, 5, 7};
        int[] arr2 = {2, 4, 6, 8};
        int[] arr3 = {0, 9, 10, 11};
        int[] result = mergeKArrays(Arrays.asList(arr1, arr2, arr3));
        System.out.println("Merged Array: " + Arrays.toString(result));
    }

    /**
     * Merges K sorted arrays using a Min Heap (Priority Queue)
     * Time Complexity: O(N log K) where N is total elements and K is the number of arrays.
     * Space Complexity: O(K) for the Min Heap.
     */
    public static int[] mergeKArrays(List<int[]> arrays) {
        PriorityQueue<HeapNode> minHeap = new PriorityQueue<>();
        int totalLength = 0;

        // Insert first element of each array into the minHeap
        for (int[] arr : arrays) {
            if (arr.length > 0) {
                minHeap.add(new HeapNode(arr, 0));
                totalLength += arr.length;
            }
        }

        int[] result = new int[totalLength];
        int index = 0;

        // Process the minHeap
        while (!minHeap.isEmpty()) {
            HeapNode node = minHeap.poll();
            result[index++] = node.arr[node.index];

            // If there are more elements in the array, add the next element to the heap
            if (node.index + 1 < node.arr.length) {
                minHeap.add(new HeapNode(node.arr, node.index + 1));
            }
        }
        return result;
    }
}

/**
 * Represents a node in the Min Heap containing an array and its current index.
 */
class HeapNode implements Comparable<HeapNode> {
    int[] arr;
    int index;

    public HeapNode(int[] arr, int index) {
        this.arr = arr;
        this.index = index;
    }

    @Override
    public int compareTo(HeapNode other) {
        return Integer.compare(this.arr[this.index], other.arr[other.index]);
    }
}
