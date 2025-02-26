package Array;

import java.util.PriorityQueue;

/**
 * Find median in a stream of integer incoming data.
 *
 * https://leetcode.com/problems/find-median-from-data-stream/
 */
public class MedianFinder {

    private PriorityQueue<Integer> minHeap; // Holds larger half (min-heap)
    private PriorityQueue<Integer> maxHeap; // Holds smaller half (max-heap)

    public MedianFinder() {
        minHeap = new PriorityQueue<>(); // Min-heap (natural order)
        maxHeap = new PriorityQueue<>(Comparator.reverseOrder()); // Max-heap
    }

    public void addNum(int num) {
        maxHeap.offer(num); // Add to maxHeap (smaller half)
        minHeap.offer(maxHeap.poll()); // Move maxHeap top to minHeap
        
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll()); // Ensure maxHeap is never smaller than minHeap
        }
    }

    public double findMedian() {
        return (minHeap.size() == maxHeap.size()) 
            ? (minHeap.peek() + maxHeap.peek()) / 2.0
            : maxHeap.peek();
    }

    public static void main(String[] args) {
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.println(medianFinder.findMedian()); // Output: 1.5
        medianFinder.addNum(3);
        System.out.println(medianFinder.findMedian()); // Output: 2.0
    }
}
