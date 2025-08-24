package array;

import java.util.PriorityQueue;

/**
 * Find median in a stream of incoming integers.
 * What is median?
 * - Median is the middle value in an ordered integer list.
 * - If the size of the list is even, there is no middle value. So the median is the mean of the two middle values.
 * - For example, [2,3,4], the median is 3.
 * - For example, [2,3], the median is (2 + 3) / 2 = 2.5.
 *
 * Intuition:
 * - To find the median, we need to know the middle element(s) of the list.
 * - We can maintain two heaps to store the smaller and larger halves of the list.
 * - The median is either the top of the max heap (odd count) or the average of the two heap tops (even count).
 *
 * Approach:
 * - Maintain two heaps:
 *   1. **Max Heap (`maxHeap`)** - Stores the smaller half of numbers.
 *   2. **Min Heap (`minHeap`)** - Stores the larger half of numbers.
 * - The median is either the **top of maxHeap** (odd count) or the **average of the two heap tops** (even count).
 *
 * Time Complexity:
 * - **Insertion:** O(log N) (due to heap operations)
 * - **Finding Median:** O(1)
 *
 * LeetCode: https://leetcode.com/problems/find-median-from-data-stream/
 */
public class MedianFinder {

    public static void main(String[] args) {
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.println(medianFinder.findMedian()); // Output: 1.5
        medianFinder.addNum(3);
        System.out.println(medianFinder.findMedian()); // Output: 2.0
    }

    private PriorityQueue<Integer> maxHeap; // Lower half (stores largest of smaller numbers)
    private PriorityQueue<Integer> minHeap; // Upper half (stores smallest of larger numbers)

    public MedianFinder() {
        maxHeap = new PriorityQueue<>((a, b) -> b - a); // Max heap for the smaller half
        minHeap = new PriorityQueue<>(); // Min heap for the larger half
    }

    /**
     * Adds a number to the data stream.
     * Time complexity : O(log N) due to heap operations.
     * @param num Number to be added to the stream
     */
    public void addNum(int num) {
        maxHeap.offer(num);              // Step 1: Add to maxHeap
        minHeap.offer(maxHeap.poll());   // Step 2: Move largest of maxHeap to minHeap

        // Step 3: Balance - maxHeap is allowed to have at most 1 extra element
        if (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    /**
     * Returns the median of all elements so far.
     * @return Median of the data stream
     */
    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek(); // Odd number of elements
        }
        return (maxHeap.peek() + minHeap.peek()) / 2.0; // Even number of elements
    }
}