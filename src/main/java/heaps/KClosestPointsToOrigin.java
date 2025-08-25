package heaps;

import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Random;

/**
 * Problem: K Closest Points to Origin
 *
 * Given an array of points where points[i] = [xi, yi] represents a point on the X-Y plane
 * and an integer k, return the k closest points to the origin (0, 0).
 * The distance between two points on the X-Y plane is the Euclidean distance
 * (i.e., √((x1 - x2)² + (y1 - y2)²)).
 * You may return the answer in any order. The answer is guaranteed to be unique.
 *
 * Example:
 * Input: points = [[1,3],[-2,2]], k = 1
 * Output: [[-2,2]]
 * Explanation: Distance between (1, 3) and origin is sqrt(10). Distance between (-2, 2) and origin is sqrt(8).
 * Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
 *
 * LeetCode: https://leetcode.com/problems/k-closest-points-to-origin
 *
 * Follow-up Questions:
 * 1. How would you handle the case where k is very close to n?
 *    Answer: Use min-heap instead of max-heap, or use QuickSelect algorithm for O(n) average time.
 *
 * 2. What if we need to find k closest points to an arbitrary point instead of origin?
 *    Answer: Modify distance calculation to use the given point instead of (0,0).
 *
 * 3. How would you solve this in a distributed system with millions of points?
 *    Answer: Use MapReduce with local k-closest computation followed by global merge.
 *    Related: https://leetcode.com/problems/k-closest-points-to-origin/
 *
 * @author Sandeep
 */
public class KClosestPointsToOrigin {

    /**
     * Finds k closest points using max heap approach.
     *
     * Algorithm:
     * 1. Use max heap to maintain k closest points seen so far
     * 2. For each point, calculate squared distance (avoid sqrt for efficiency)
     * 3. If heap size < k, add point to heap
     * 4. If heap size == k and current point is closer than farthest in heap, replace
     * 5. Return all points in the heap
     *
     * Time Complexity: O(n log k) where n is number of points
     * Space Complexity: O(k) for the heap
     *
     * @param points Array of points [x, y]
     * @param k Number of closest points to return
     * @return Array of k closest points to origin
     */
    public int[][] kClosest(int[][] points, int k) {
        if (points == null || points.length == 0 || k <= 0) {
            return new int[0][0];
        }

        // Max heap based on squared distance (avoid sqrt for efficiency)
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) ->
            Integer.compare(getSquaredDistance(b), getSquaredDistance(a))
        );

        for (int[] point : points) {
            maxHeap.offer(point);

            // Keep only k closest points
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        // Convert heap to result array
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }

        return result;
    }

    /**
     * QuickSelect approach for optimal average time complexity.
     * Partitions array similar to quicksort but only processes one side.
     *
     * Time Complexity: O(n) average, O(n²) worst case
     * Space Complexity: O(1) excluding result array
     *
     * @param points Array of points
     * @param k Number of closest points needed
     * @return Array of k closest points
     */
    public int[][] kClosestQuickSelect(int[][] points, int k) {
        if (points == null || points.length == 0 || k <= 0) {
            return new int[0][0];
        }

        quickSelect(points, 0, points.length - 1, k);
        return Arrays.copyOfRange(points, 0, k);
    }

    // QuickSelect implementation
    private void quickSelect(int[][] points, int left, int right, int k) {
        if (left >= right) return;

        int pivotIndex = partition(points, left, right);

        if (pivotIndex == k - 1) {
            return; // Found exactly k closest points
        } else if (pivotIndex < k - 1) {
            // Need more points, search right side
            quickSelect(points, pivotIndex + 1, right, k);
        } else {
            // Have too many points, search left side
            quickSelect(points, left, pivotIndex - 1, k);
        }
    }

    // Partition function for QuickSelect
    private int partition(int[][] points, int left, int right) {
        Random rand = new Random();
        int randomIndex = left + rand.nextInt(right - left + 1);
        swap(points, randomIndex, right); // Move random pivot to end

        int pivot = getSquaredDistance(points[right]);
        int i = left;

        for (int j = left; j < right; j++) {
            if (getSquaredDistance(points[j]) <= pivot) {
                swap(points, i, j);
                i++;
            }
        }

        swap(points, i, right); // Place pivot in correct position
        return i;
    }

    // Swap two points in array
    private void swap(int[][] points, int i, int j) {
        int[] temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }

    /**
     * Sorting approach for comparison (less efficient for small k).
     *
     * Time Complexity: O(n log n)
     * Space Complexity: O(1) excluding result
     */
    public int[][] kClosestSorting(int[][] points, int k) {
        Arrays.sort(points, (a, b) ->
            Integer.compare(getSquaredDistance(a), getSquaredDistance(b))
        );
        return Arrays.copyOfRange(points, 0, k);
    }

    /**
     * Min-heap approach (alternative when k is close to n).
     * More efficient when we need most of the points.
     *
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int[][] kClosestMinHeap(int[][] points, int k) {
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) ->
            Integer.compare(getSquaredDistance(a), getSquaredDistance(b))
        );

        for (int[] point : points) {
            minHeap.offer(point);
        }

        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll();
        }

        return result;
    }

    // Calculate squared distance from origin (avoid sqrt for efficiency)
    private int getSquaredDistance(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }

    /**
     * Generic distance calculation method for extensibility.
     *
     * @param point1 First point
     * @param point2 Second point
     * @return Squared distance between points
     */
    private int getSquaredDistance(int[] point1, int[] point2) {
        int dx = point1[0] - point2[0];
        int dy = point1[1] - point2[1];
        return dx * dx + dy * dy;
    }

    /**
     * Find k closest points to arbitrary center point.
     *
     * @param points Array of points
     * @param center Center point [x, y]
     * @param k Number of closest points
     * @return k closest points to center
     */
    public int[][] kClosestToPoint(int[][] points, int[] center, int k) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) ->
            Integer.compare(getSquaredDistance(b, center), getSquaredDistance(a, center))
        );

        for (int[] point : points) {
            maxHeap.offer(point);
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }

        return result;
    }
}