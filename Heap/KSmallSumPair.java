package Heap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * LeetCode: https://leetcode.com/problems/find-k-pairs-with-smallest-sums/
 *
 * Given two sorted arrays, find the k pairs with the smallest sums.
 *
 * Approach:
 * - Use a Min Heap (PriorityQueue) to efficiently extract the smallest sum pairs.
 * - Push initial pairs from nums1 with the first element of nums2.
 * - Extract the smallest pair and push the next potential pair from nums2.
 *
 * Time Complexity: O(k log k) (Heap operations dominate)
 * Space Complexity: O(k) (To store results and heap elements)
 */
public class KSmallSumPair {

    public static void main(String[] args) {
        int[] nums1 = {1, 7, 11};
        int[] nums2 = {2, 4, 6};
        int k = 5;
        List<List<Integer>> result = new KSmallSumPair().kSmallestPairs(nums1, nums2, k);
        System.out.println(result);
    }

    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        PriorityQueue<HeapNode> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.sum, b.sum));
        List<List<Integer>> result = new ArrayList<>();

        if (nums1.length == 0 || nums2.length == 0 || k == 0) return result;

        // Initialize heap with first k pairs from nums1 and the first element of nums2
        for (int i = 0; i < Math.min(k, nums1.length); i++) {
            minHeap.offer(new HeapNode(nums1[i], nums2[0], 0));
        }

        while (k-- > 0 && !minHeap.isEmpty()) {
            HeapNode node = minHeap.poll();
            result.add(node.pair);

            int nextIndex = node.index + 1;
            if (nextIndex < nums2.length) {
                minHeap.offer(new HeapNode(node.pair.get(0), nums2[nextIndex], nextIndex));
            }
        }
        return result;
    }
}

/**
 * Helper class to store pairs and their sum for priority queue processing.
 */
class HeapNode {
    List<Integer> pair;
    int index;
    int sum;

    public HeapNode(int num1, int num2, int index) {
        this.pair = Arrays.asList(num1, num2);
        this.index = index;
        this.sum = num1 + num2;
    }
}
