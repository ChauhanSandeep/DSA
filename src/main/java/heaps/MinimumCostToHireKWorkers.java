package heaps;

import java.util.*;

/**
 * Problem: Minimum Cost to Hire K Workers
 *
 * Choose exactly k workers while paying everyone in the group at the same wage
 * per quality ratio, and at least each worker's minimum wage. For any chosen
 * group, the highest required ratio determines every worker's pay.
 *
 * Leetcode: https://leetcode.com/problems/minimum-cost-to-hire-k-workers/ (Hard)
 * Rating:   2260 (zerotrac Elo)
 * Pattern:  Heap | Greedy by wage-to-quality ratio | Keep smallest quality sum
 *
 * Example:
 *   Input:  quality = [10,20,5], wage = [70,50,30], k = 2
 *   Output: 105.00000
 *   Why:    workers 0 and 2 use ratio 7, costing 10*7 + 5*7 = 105.
 *
 * Follow-ups:
 *   1. Why sort by wage / quality ratio?
 *      Once a worker is the highest ratio in the group, that ratio prices all chosen quality.
 *   2. Why use a max heap of qualities?
 *      For a fixed ratio, the cheapest group keeps the k smallest total qualities.
 *   3. What if k equals n?
 *      The cost is total quality times the maximum wage-to-quality ratio.
 *   4. What if workers can be hired fractionally?
 *      The discrete heap choice changes into a continuous optimization problem.
 *
 * Related: Maximum Performance of a Team (1383), IPO (502).
 */

public class MinimumCostToHireKWorkers {

    public static void main(String[] args) {
        MinimumCostToHireKWorkers solver = new MinimumCostToHireKWorkers();
        int[][] qualities = { {10, 20, 5}, {5} };
        int[][] wages = { {70, 50, 30}, {30} };
        int[] kValues = {2, 1};
        double[] expected = {105.0, 30.0};

        for (int i = 0; i < qualities.length; i++) {
            double got = solver.mincostToHireWorkers(qualities[i], wages[i], kValues[i]);
            System.out.printf("quality=%s wage=%s k=%d -> %.5f  expected=%.5f%n",
                Arrays.toString(qualities[i]), Arrays.toString(wages[i]), kValues[i], got, expected[i]);
        }
    }
        /**
     * Intuition: if a worker with ratio r is the most demanding person in a group,
     * every selected worker must be paid r times their quality. So after sorting
     * workers by ratio, each step asks: among workers with ratio at most r, what is
     * the smallest total quality of k workers?
     *
     * Algorithm:
     *   1. Build worker pairs [wage / quality ratio, quality].
     *   2. Sort workers by ratio ascending.
     *   3. Add qualities to a max heap and remove the largest when more than k exist.
     *   4. When exactly k workers are kept, minimize sumQuality * current ratio.
     *
     * Time:  O(n log n) - sorting plus heap updates for every worker.
     * Space: O(n) - worker pairs and the quality heap are stored.
     *
     * @param quality quality for each worker
     * @param wage minimum wage expectation for each worker
     * @param k exact number of workers to hire
     * @return minimum total cost to hire k workers
     */

    public double mincostToHireWorkers(int[] quality, int[] wage, int k) {
        int n = quality.length;
        // Create an array of worker data: [wage/quality ratio, quality]
        double[][] workers = new double[n][2];
        for (int i = 0; i < n; i++) {
            workers[i] = new double[]{(double) wage[i] / quality[i], (double) quality[i]};
        }

        // Sort workers by their wage/quality ratio (ascending order)
        Arrays.sort(workers, (a, b) -> Double.compare(a[0], b[0]));

        // Max-heap to keep track of the k-1 highest quality workers
        PriorityQueue<Double> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        double sumQuality = 0;
        double minCost = Double.MAX_VALUE;

        for (double[] worker : workers) {
            double ratio = worker[0];
            double q = worker[1];

            sumQuality += q;
            maxHeap.offer(q);

            if (maxHeap.size() > k) {
                // Remove the worker with the highest quality (most expensive to pay)
                sumQuality -= maxHeap.poll();
            }

            if (maxHeap.size() == k) {
                // Calculate the total cost for current group
                minCost = Math.min(minCost, sumQuality * ratio);
            }
        }

        return minCost;
    }
}
