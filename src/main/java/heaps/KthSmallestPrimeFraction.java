package heaps;

import java.util.*;

/**
 * Problem: K-th Smallest Prime Fraction
 *
 * Given a sorted array that starts with 1 and then primes, return the kth
 * smallest fraction arr[i] / arr[j] with i < j. Fractions are compared by their
 * numeric value, but the answer must return the numerator and denominator.
 *
 * Leetcode: https://leetcode.com/problems/k-th-smallest-prime-fraction/ (Medium)
 * Rating:   2169 (zerotrac Elo)
 * Pattern:  Heap | K-way merge of fraction rows | Binary search alternative
 *
 * Example:
 *   Input:  arr = [1,2,3,5], k = 3
 *   Output: [2,5]
 *   Why:    sorted fractions begin 1/5, 1/3, 2/5, so the 3rd is 2/5.
 *
 * Follow-ups:
 *   1. How do you avoid floating-point comparison?
 *      Compare a/b and c/d by cross multiplication a*d versus c*b.
 *   2. What if k is close to the number of all fractions?
 *      Binary search on fraction value can count candidates without storing them all.
 *   3. How do you list the first k fractions, not just the kth?
 *      Keep polling the heap and append each removed fraction.
 *   4. What if arr contains non-prime sorted positives?
 *      The heap ordering still works because only sorted positive values are needed.
 *
 * Related: Kth Smallest Element in a Sorted Matrix (378), Find K Pairs with Smallest Sums (373).
 */

public class KthSmallestPrimeFraction {

    public static void main(String[] args) {
        KthSmallestPrimeFraction solver = new KthSmallestPrimeFraction();
        int[][] inputs = { {1, 2, 3, 5}, {1, 7} };
        int[] kValues = {3, 1};
        int[][] expected = { {2, 5}, {1, 7} };

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.kthSmallestPrimeFraction(inputs[i], kValues[i]);
            System.out.printf("arr=%s k=%d -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), kValues[i],
                Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }

        /**
     * Intuition: for a fixed numerator arr[i], denominators from right to left
     * produce increasing fractions. Seed each numerator row with its smallest
     * fraction, then repeatedly poll the global smallest and advance that row.
     *
     * Algorithm:
     *   1. Seed the min heap with arr[i] / arr[n - 1] for every numerator i.
     *   2. Poll k - 1 fractions from the heap.
     *   3. After each poll, offer the same numerator with the next smaller denominator.
     *   4. Poll once more and return that numerator and denominator.
     *
     * Time:  O(k log n) - each extracted fraction does one heap poll and maybe one offer.
     * Space: O(n) - the heap stores one active fraction per numerator row.
     *
     * @param arr sorted array containing 1 and prime numbers
     * @param k one-based rank among all valid fractions
     * @return numerator and denominator of the kth smallest fraction
     */

    public int[] kthSmallestPrimeFraction(int[] arr, int k) {
        int n = arr.length;

        // Min-heap: [fraction_value, numerator_index, denominator_index]
        PriorityQueue<double[]> pq = new PriorityQueue<>((a, b) -> Double.compare(a[0], b[0]));

        // Add all fractions with largest denominator (smallest fractions)
        for (int i = 0; i < n - 1; i++) {
            pq.offer(new double[]{(double) arr[i] / arr[n - 1], i, n - 1});
        }

        // Extract k-1 smallest fractions
        for (int i = 0; i < k - 1; i++) {
            double[] current = pq.poll();
            int numIdx = (int) current[1];
            int denIdx = (int) current[2];

            // Add next fraction with same numerator (if exists)
            if (denIdx - 1 > numIdx) {
                double nextFraction = (double) arr[numIdx] / arr[denIdx - 1];
                pq.offer(new double[]{nextFraction, numIdx, denIdx - 1});
            }
        }

        // The k-th smallest fraction
        double[] result = pq.poll();
        return new int[]{arr[(int) result[1]], arr[(int) result[2]]};
    }

    /**
     * Binary search approach on fraction values.
     * More efficient for large k values.
     */
    public int[] kthSmallestPrimeFractionBinarySearch(int[] arr, int k) {
        int n = arr.length;
        double left = 0.0, right = 1.0;

        while (left < right) {
            double mid = left + (right - left) / 2;

            FractionCount count = countFractions(arr, mid);

            if (count.count == k) {
                return new int[]{count.numerator, count.denominator};
            } else if (count.count < k) {
                left = mid;
            } else {
                right = mid;
            }
        }

        return new int[]{};
    }

    /** Counts fractions at most target and records the largest one seen. */
    private FractionCount countFractions(int[] arr, double target) {
        int count = 0;
        int bestNum = 0, bestDen = 1;
        double maxFraction = 0.0;
        int n = arr.length;

        for (int i = 0, j = 1; i < n - 1; i++) {
            // Find largest j such that arr[i]/arr[j] <= target
            while (j < n && arr[i] > target * arr[j]) {
                j++;
            }

            if (j == n) break;

            // All fractions arr[i]/arr[j..n-1] are <= target
            count += n - j;

            // Update best fraction
            double fraction = (double) arr[i] / arr[j];
            if (fraction > maxFraction) {
                maxFraction = fraction;
                bestNum = arr[i];
                bestDen = arr[j];
            }
        }

        return new FractionCount(count, bestNum, bestDen);
    }

    /** Stores count and best fraction found during binary search counting. */
    private static class FractionCount {
        int count;
        int numerator;
        int denominator;

        FractionCount(int count, int numerator, int denominator) {
            this.count = count;
            this.numerator = numerator;
            this.denominator = denominator;
        }
    }

    /**
     * Alternative priority queue approach using custom comparator.
     * Stores actual indices instead of fraction values for precision.
     */
    public int[] kthSmallestPrimeFractionPrecise(int[] arr, int k) {
        int n = arr.length;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            return Integer.compare(arr[a[0]] * arr[b[1]], arr[b[0]] * arr[a[1]]);
        });

        // Add all fractions with largest denominator
        for (int i = 0; i < n - 1; i++) {
            pq.offer(new int[]{i, n - 1});
        }

        // Extract k-1 smallest
        for (int i = 0; i < k - 1; i++) {
            int[] current = pq.poll();
            int numIdx = current[0];
            int denIdx = current[1];

            if (denIdx - 1 > numIdx) {
                pq.offer(new int[]{numIdx, denIdx - 1});
            }
        }

        int[] result = pq.poll();
        return new int[]{arr[result[0]], arr[result[1]]};
    }

    /**
     * Brute force approach generating all fractions.
     * Only suitable for small arrays due to O(n^2) space.
     */
    public int[] kthSmallestPrimeFractionBruteForce(int[] arr, int k) {
        List<Fraction> fractions = new ArrayList<>();
        int n = arr.length;

        // Generate all fractions
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                fractions.add(new Fraction(arr[i], arr[j]));
            }
        }

        // Sort and return k-th smallest
        Collections.sort(fractions);
        Fraction result = fractions.get(k - 1);
        return new int[]{result.numerator, result.denominator};
    }

    /** Comparable fraction value used by the brute force approach. */
    private static class Fraction implements Comparable<Fraction> {
        int numerator;
        int denominator;

        Fraction(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        @Override
        public int compareTo(Fraction other) {
            return Integer.compare(this.numerator * other.denominator,
                                 other.numerator * this.denominator);
        }
    }

    /**
     * Optimized heap approach with early termination.
     * Stops generating fractions once we have k elements.
     */
    public int[] kthSmallestPrimeFractionOptimized(int[] arr, int k) {
        int n = arr.length;

        PriorityQueue<FractionNode> pq = new PriorityQueue<>((a, b) ->
            Integer.compare(a.numerator * b.denominator, b.numerator * a.denominator));

        Set<String> visited = new HashSet<>();

        // Start with smallest fraction
        pq.offer(new FractionNode(arr[0], arr[n - 1], 0, n - 1));
        visited.add("0," + (n - 1));

        for (int i = 0; i < k - 1; i++) {
            FractionNode current = pq.poll();

            // Add adjacent fractions
            // Next numerator with same denominator
            if (current.numIdx + 1 < current.denIdx) {
                String key1 = (current.numIdx + 1) + "," + current.denIdx;
                if (!visited.contains(key1)) {
                    pq.offer(new FractionNode(arr[current.numIdx + 1], arr[current.denIdx],
                                             current.numIdx + 1, current.denIdx));
                    visited.add(key1);
                }
            }

            // Same numerator with previous denominator
            if (current.denIdx - 1 > current.numIdx) {
                String key2 = current.numIdx + "," + (current.denIdx - 1);
                if (!visited.contains(key2)) {
                    pq.offer(new FractionNode(arr[current.numIdx], arr[current.denIdx - 1],
                                             current.numIdx, current.denIdx - 1));
                    visited.add(key2);
                }
            }
        }

        FractionNode result = pq.poll();
        return new int[]{result.numerator, result.denominator};
    }

    /** Fraction value with numerator and denominator indices. */
    private static class FractionNode {
        int numerator;
        int denominator;
        int numIdx;
        int denIdx;

        FractionNode(int numerator, int denominator, int numIdx, int denIdx) {
            this.numerator = numerator;
            this.denominator = denominator;
            this.numIdx = numIdx;
            this.denIdx = denIdx;
        }
    }

    /**
     * Two-pointer approach for finding k-th smallest.
     * Uses sorted property more directly.
     */
    public int[] kthSmallestPrimeFractionTwoPointer(int[] arr, int k) {
        int n = arr.length;
        List<Fraction> candidates = new ArrayList<>();

        // Generate fractions using two pointers
        for (int i = 0; i < n - 1; i++) {
            for (int j = n - 1; j > i; j--) {
                candidates.add(new Fraction(arr[i], arr[j]));
                if (candidates.size() >= k * 2) break; // Early pruning
            }
        }

        Collections.sort(candidates);
        Fraction result = candidates.get(k - 1);
        return new int[]{result.numerator, result.denominator};
    }
}