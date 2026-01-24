package heaps;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;

/**
 * Problem: Top K Frequent Elements
 *
 * Given an integer array nums and an integer k, return the k most frequent elements.
 * You may return the answer in any order.
 *
 * Example:
 * Input: nums = [1,1,1,2,2,3], k = 2
 * Output: [1,2]
 * Explanation: 1 appears 3 times, 2 appears 2 times, 3 appears 1 time. The 2 most frequent are [1,2].
 *
 * LeetCode: https://leetcode.com/problems/top-k-frequent-elements
 *
 * Follow-up Questions:
 * 1. What if we need to return elements in order of frequency?
 *    Answer: Use max heap instead of min heap, or sort the result after extraction.
 *
 * 2. How would you handle ties in frequency?
 *    Answer: Problem guarantees unique answer, but could use lexicographic order or value order.
 *
 * 3. Can we achieve better than O(n log k) time complexity?
 *    Answer: Yes, use bucket sort approach for O(n) time complexity.
 *    Related: https://leetcode.com/problems/top-k-frequent-words/
 *
 * @author Sandeep
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class TopKFrequentElements {

    /**
     * Finds k most frequent elements using min heap approach.
     *
     * Algorithm:
     * 1. Count frequency of each element using HashMap
     * 2. Use min heap to maintain k most frequent elements
     * 3. For each element, add to heap if size < k or if frequency > min frequency in heap
     * 4. Extract all elements from heap as result
     *
     * Time Complexity: O(n log k) where n is array length because we are using min heap of size k and
     * each insertion and deletion takes O(log k) time
     * Space Complexity: O(n + k) for hashmap and heap
     *
     * @param nums Input array of integers
     * @param k Number of most frequent elements to return
     * @return Array of k most frequent elements
     */
    public int[] topKFrequent(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }

        // Count frequency of each element
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        // Min heap based on frequency - keeps k most frequent elements
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.getValue(), b.getValue())
        );

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            minHeap.offer(entry);

            // Keep only k most frequent elements
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        // Extract elements from heap
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll().getKey();
        }

        return result;
    }

    /**
     * Bucket sort approach for O(n) time complexity.
     * Uses the fact that maximum frequency is at most n.
     *
     * Steps:
     * 1. Count frequency of each element
     * 2. Create buckets for each possible frequency (0 to n)
     * 3. Add elements to buckets based on their frequency
     * 4. Collect k most frequent elements from highest frequency buckets
     *
     * Time Complexity: O(n) because we are using bucket sort
     * Space Complexity: O(n)
     */
    public int[] topKFrequentBucketSort(int[] nums, int k) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        // Create buckets for each possible frequency (0 to n)
        List<Integer>[] freqBucket = new ArrayList[nums.length + 1];

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            int key = entry.getKey();
            int frequency = entry.getValue();
            if (freqBucket[frequency] == null) {
                freqBucket[frequency] = new ArrayList<>();
            }
            freqBucket[frequency].add(key);
        }

        // Collect k most frequent elements from highest frequency buckets
        int[] result = new int[k];
        int index = 0;

        for (int freq = freqBucket.length - 1; freq >= 0 && index < k; freq--) {
            if (freqBucket[freq] != null) {
                for (int num : freqBucket[freq]) {
                    if (index < k) {
                        result[index++] = num;
                    }
                }
            }
        }

        return result;
    }
}