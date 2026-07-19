package maths;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: The Kth Factor of N
 *
 * Given positive integers n and k, return the kth positive factor of n in
 * ascending order. If n has fewer than k factors, return -1.
 *
 * Leetcode: https://leetcode.com/problems/the-kth-factor-of-n/ (Medium)
 * Rating:   1232 (zerotrac Elo)
 * Pattern:  Math | Divisors | Square-root factor pairing
 *
 * Example:
 *   Input:  n = 12, k = 3
 *   Output: 3
 *   Why:    the factors are [1, 2, 3, 4, 6, 12], so the 3rd one is 3.
 *
 * Follow-ups:
 *   1. How would you answer many k queries for the same n?
 *      Precompute the sorted factor list once and index into it.
 *   2. How would you find the kth largest factor?
 *      Convert it to the (totalFactors - k + 1)th smallest factor.
 *   3. How would this scale for n up to 10^18?
 *      Use prime factorization to generate divisors without scanning every value.
 *
 * Related: Count Primes (204), Ugly Number (263).
 */

public class KthFactorOfNumber {
  public static void main(String[] args) {
    int[][] inputs = { {12, 3}, {7, 2}, {4, 4} };
    int[] expected = { 3, 7, -1 };

    for (int i = 0; i < inputs.length; i++) {
      int got = findKthFactor(inputs[i][0], inputs[i][1]);
      System.out.printf("n=%d k=%d -> %d  expected=%d%n",
          inputs[i][0], inputs[i][1], got, expected[i]);
    }
  }
    /**
   * Intuition: factors come in pairs around the square root. Scanning only up
   * to sqrt(n) finds the smaller half in ascending order and the larger half in
   * reverse order through n / divisor. Combining those two lists gives the full
   * sorted order without checking every number up to n.
   *
   * Algorithm:
   *   1. Reject non-positive n or k.
   *   2. Scan divisor from 1 through sqrt(n).
   *   3. Add each small divisor and its distinct paired larger factor.
   *   4. If k is outside the total factor count, return -1.
   *   5. Return from smallerFactors directly or from largerFactors in reverse.
   *
   * Time:  O(sqrt(n)) - divisibility is tested only up to the square root.
   * Space: O(sqrt(n)) - discovered factor pairs are stored in two lists.
   *
   * @param n number whose factors are being searched
   * @param k one-based factor position in ascending order
   * @return kth factor, or -1 when it does not exist
   */

  public static int findKthFactor(int n, int k) {
    if (n <= 0 || k <= 0) {
      return -1;
    }

    List<Integer> smallerFactors = new ArrayList<>();
    List<Integer> largerFactors = new ArrayList<>();

    int sqrtN = (int) Math.sqrt(n);

    // Collect all factors efficiently
    for (int divisor = 1; divisor <= sqrtN; divisor++) {
      if (isDivisor(n, divisor)) {
        smallerFactors.add(divisor);

        // Add corresponding larger factor if it's different (not perfect square)
        int correspondingFactor = n / divisor;
        if (correspondingFactor != divisor) {
          largerFactors.add(correspondingFactor);
        }
      }
    }

    // Calculate total factors count
    int totalFactors = smallerFactors.size() + largerFactors.size();

    if (k > totalFactors) {
      return -1; // k-th factor doesn't exist
    }

    // Return k-th factor
    if (k <= smallerFactors.size()) {
      return smallerFactors.get(k - 1); // k-th factor is in smaller factors
    } else {
      // k-th factor is in larger factors (in reverse order)
      int indexInLargerFactors = largerFactors.size() - (k - smallerFactors.size());
      return largerFactors.get(indexInLargerFactors);
    }
  }

    /** Returns true when divisor divides number evenly. */

  private static boolean isDivisor(int number, int divisor) {
    return number % divisor == 0;
  }

}
