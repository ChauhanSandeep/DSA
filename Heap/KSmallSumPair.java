package Heap;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * LeetCode: https://leetcode.com/problems/find-k-pairs-with-smallest-sums/
 *
 * Problem:
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
public class KSmallestPairsFinder {

    public static void main(String[] args) {
        int[] nums1 = {1, 7, 11};
        int[] nums2 = {2, 4, 6};
        int k = 5;

        KSmallestPairsFinder finder = new KSmallestPairsFinder();
        List<List<Integer>> result = finder.findKSmallestPairs(nums1, nums2, k);
        
        System.out.println("K Smallest Pairs: " + result);
    }

    /**
     * Finds k smallest sum pairs from two sorted arrays.
     *
     * @param nums1 First sorted array
     * @param nums2 Second sorted array
     * @param k     Number of pairs to find
     * @return List of k smallest sum pairs
     */
    public List<List<Integer>> findKSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums1.length == 0 || nums2.length == 0 || k == 0) return result;

        // Min Heap to store the pairs based on their sum
        PriorityQueue<PairNode> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.sum, b.sum));

        // Initialize heap with first k pairs (nums1[i], nums2[0])
        for (int i = 0; i < Math.min(k, nums1.length); i++) {
            minHeap.offer(new PairNode(nums1[i], nums2[0], 0));
        }

        // Extract k smallest sum pairs
        while (k-- > 0 && !minHeap.isEmpty()) {
            PairNode node = minHeap.poll();
            result.add(node.pair);

            int nextIndex = node.indexInNums2 + 1;
            if (nextIndex < nums2.length) {
                minHeap.offer(new PairNode(node.pair.get(0), nums2[nextIndex], nextIndex));
            }
        }
        return result;
    }
}

/**
 * Helper class to store pairs and their sum for priority queue processing.
 */
class PairNode {
    List<Integer> pair;
    int indexInNums2;
    int sum;

    public PairNode(int num1, int num2, int indexInNums2) {
        this.pair = List.of(num1, num2);
        this.indexInNums2 = indexInNums2;
        this.sum = num1 + num2;
    }
}
