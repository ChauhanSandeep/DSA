package frazsheet;

import java.util.*;

/**
 * There are n workers. You are given two integer arrays quality and wage where quality[i] is the quality of the ith worker and wage[i] is the minimum wage expectation for the ith worker.
 * 
 * We want to hire exactly k workers to form a paid group. When hiring a group of k workers, we must pay them according to the following rules:
 * 1. Every worker in the paid group must be paid at least their minimum wage expectation.
 * 2. In the group, each worker must be paid in the ratio of their quality compared to other workers in the paid group.
 * 
 * Return the least amount of money needed to form a paid group satisfying the above conditions.
 * 
 * Example 1:
 * Input: quality = [10,20,5], wage = [70,50,30], k = 2
 * Output: 105.00000
 * Explanation: We pay 70 to 0th worker and 35 to 2nd worker.
 * 
 * Example 2:
 * Input: quality = [3,1,10,10,1], wage = [4,8,2,2,7], k = 3
 * Output: 30.66667
 * Explanation: We pay 4 to 0th worker, 13.33333 to 2nd and 3rd workers.
 * 
 * LeetCode: https://leetcode.com/problems/minimum-cost-to-hire-k-workers/
 * 
 * Follow-up Questions:
 * 1. How would you handle very large input sizes (e.g., n = 10000)?
 *    - The current solution is O(n log n) which should handle large inputs efficiently.
 * 2. What if k = n (we need to hire all workers)?
 *    - The solution will work correctly, but we could optimize by directly calculating the total cost.
 * 3. How would you handle if quality or wage contains zeros?
 *    - The problem guarantees quality[i] > 0 and wage[i] > 0.
 * 
 * Related Problems:
 * - Maximum Performance of a Team (https://leetcode.com/problems/maximum-performance-of-a-team/)
 * - IPO (https://leetcode.com/problems/ipo/)
 */
public class MinimumCostToHireKWorkers {
    /**
     * Calculates the minimum cost to hire k workers with given quality and wage constraints.
     * 
     * @param quality Array representing quality of each worker
     * @param wage Array representing minimum wage expectation of each worker
     * @param k Number of workers to hire
     * @return Minimum cost to hire k workers
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
