package Array;

import java.util.PriorityQueue;

/**
 * Find median in stream of integer incoming data stream
 *
 * https://leetcode.com/problems/find-median-from-data-stream/
 */
public class MedianFinder {

    public static void main(String[] args) {
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.println(medianFinder.findMedian());
        medianFinder.addNum(3);
        System.out.println(medianFinder.findMedian());
    }

    PriorityQueue<Integer> minHeap;
    PriorityQueue<Integer> maxHeap;

    public MedianFinder() {
        // minHeap stores higher side of the element
        minHeap = new PriorityQueue<>();

        // maxHeap contains lower side of the element.
        // maxHeap is allowed to have one extra element in case size(minHeap) != size(maxHeap)
        maxHeap = new PriorityQueue<>((a, b) -> b - a);
    }

    public void addNum(int num) {
        maxHeap.offer(num);             // re-balance maxHeap
        minHeap.offer(maxHeap.poll());  // re-balance minHeap
        if (minHeap.size() > maxHeap.size()) {
            // if size mismatch then add extra to maxHeap
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        if ((minHeap.size() + maxHeap.size()) % 2 == 0) {
            return (double) (minHeap.peek() + maxHeap.peek()) / 2;
        } else {
            return (double) maxHeap.peek();
        }
    }
}
