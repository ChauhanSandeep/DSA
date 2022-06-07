package Heap;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given two sorted arrays, find k smallest pairs of sum
 */
public class KSmallSumPair {

    public static void main(String[] args) {
        int[] nums1 = {1, 7, 11};
        int[] nums2 = {2, 4, 6};
        int k = 5;
        List<List<Integer>> lists = new KSmallSumPair().kSmallestPairs(nums1, nums2, k);
        System.out.println(lists);
    }

    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        PriorityQueue<HeapNode> heap = new PriorityQueue<>((a, b) -> a.sum - b.sum);
        int len1 = nums1.length;
        int len2 = nums2.length;

        for(int i=0; i<len1; i++) {
            HeapNode heapNode = new HeapNode(nums1[i], nums2[0], 0);
            heap.offer(heapNode);
        }

        List<List<Integer>> result = new ArrayList<>();
        for(int i=0; i<k && !heap.isEmpty(); i++) {
            HeapNode polled = heap.poll();
            result.add(polled.pair);

            int nIndex = polled.index + 1;
            if(nIndex < len2) {
                heap.offer(new HeapNode(polled.pair.get(0), nums2[nIndex], nIndex));
            }
        }
        return result;
    }
}

class HeapNode {
    List<Integer> pair;
    int index;
    int sum;

    public HeapNode(int v1, int v2, int index) {
        this.pair = Stream.of(v1, v2).collect(Collectors.toList());
        this.index = index;
        this.sum = v1 + v2;
    }
}
