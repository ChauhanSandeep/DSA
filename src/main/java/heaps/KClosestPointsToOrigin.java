package heaps;

import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Random;

/**
 * Problem: K Closest Points to Origin
 *
 * Given points on a 2D plane, return any k points with the smallest Euclidean
 * distance from the origin. Squared distance is enough because square root keeps
 * the same ordering.
 *
 * Leetcode: https://leetcode.com/problems/k-closest-points-to-origin (Medium)
 * Rating:   1214 (zerotrac Elo)
 * Pattern:  Heap | Max heap of size k | QuickSelect alternative
 *
 * Example:
 *   Input:  points = [[1,3],[-2,2]], k = 1
 *   Output: [[-2,2]]
 *   Why:    (-2,2) has squared distance 8, which is smaller than 10 for (1,3).
 *
 * Follow-ups:
 *   1. Can you get average O(n) time?
 *      Use QuickSelect to partition by squared distance and keep the first k points.
 *   2. What if k is close to n?
 *      A min-heap of all points or QuickSelect can avoid paying log k for every point.
 *   3. What if the target is not the origin?
 *      Replace x*x + y*y with squared distance from the requested target point.
 *   4. What if points arrive as a stream?
 *      Keep the same size-k max heap and evict the farthest point after each offer.
 *
 * Related: Kth Largest Element in an Array (215), Top K Frequent Elements (347).
 */

public class KClosestPointsToOrigin {

    public static void main(String[] args) {
        KClosestPointsToOrigin solver = new KClosestPointsToOrigin();
        int[][][] inputs = { {{1, 3}, {-2, 2}}, {{3, 3}} };
        int[] kValues = {1, 0};
        int[][][] expected = { {{-2, 2}}, {} };

        for (int i = 0; i < inputs.length; i++) {
            int[][] got = solver.kClosest(inputs[i], kValues[i]);
            System.out.printf("points=%s k=%d -> %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), kValues[i],
                Arrays.deepToString(got), Arrays.deepToString(expected[i]));
        }
    }

        /**
     * Intuition: keep only the best k candidates seen so far. A max heap puts the
     * farthest saved point at the top, so once the heap grows past k, polling drops
     * exactly the point that cannot belong to the final closest set.
     *
     * Algorithm:
     *   1. Return an empty matrix for null, empty, or non-positive k input.
     *   2. Offer every point into a max heap ordered by squared distance.
     *   3. Whenever the heap has more than k points, poll the current farthest.
     *   4. Poll the heap into the result array.
     *
     * Time:  O(n log k) - each of n points does one heap offer and maybe one poll.
     * Space: O(k) - the heap stores at most k closest candidates.
     *
     * @param points array of [x, y] points
     * @param k number of closest points to return
     * @return k points closest to the origin, in heap poll order
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

    /** Partitions until the first k positions contain the k closest points. */
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

    /** Partitions points around a random pivot distance. */
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

    /** Swaps two point references in the points array. */
    private void swap(int[][] points, int i, int j) {
        int[] temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }

    /** Returns squared distance from the origin. */
    private int getSquaredDistance(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }


}