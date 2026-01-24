package maths;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: The Kth Factor of N
 *
 * Given two positive integers n and k, return the kth positive divisor of n.
 * A divisor of n is a positive integer that divides n without a remainder.
 * Note that the divisors are returned in ascending order.
 * If n has less than k divisors, return -1.
 *
 * Example 1:
 * Input: n = 12, k = 3
 * Output: 3
 * Explanation: Factors of 12 are [1, 2, 3, 4, 6, 12], the 3rd factor is 3.
 *
 * LeetCode: https://leetcode.com/problems/the-kth-factor-of-n/
 *
 * Follow-up Questions (FAANG-style):
 * 1. What if we need to find all factors of n?
 *    - Modify to collect all factors and return the list.
 * 2. How would you handle multiple queries for different k values on the same n?
 *    - Precompute and store all factors once, then answer queries in O(1).
 * 3. What if n can be very large (up to 10^18)?
 *    - Use prime factorization and mathematical formula for factor count.
 *    - Related: https://leetcode.com/problems/count-primes/
 * 4. How to find the kth largest factor instead of kth smallest?
 *    - Reverse the iteration order or use total_factors - k + 1 approach.
 * 5. What if we need factors in a specific range [L, R]?
 *    - Modify the algorithm to only consider factors within the given range.
 * 6. How to optimize for multiple test cases with similar n values?
 *    - Use memoization or precompute factors for commonly queried numbers.
 * LeetCode Contest Rating: 1232
 **/
public class KthFactorOfNumber {
  public static void main(String[] args) {
    int n = 12, k = 3;
    System.out.println("The " + k + "th factor of " + n + " is: " + findKthFactor(n, k));
  }
  /**
   * This approach collects factors in a list for better clarity.
   *
   * Algorithm: Factor Collection with Square Root Optimization
   * Steps:
   * 1. Collect smaller factors (≤ √n) in ascending order
   * 2. Collect larger factors (> √n) in descending order by division
   * 3. Handle perfect square case to avoid duplicates
   * 4. Return k-th factor if it exists
   *
   * Time Complexity: O(√n)
   * Space Complexity: O(√n) for storing factors
   *
   * @param n the number to find factors of
   * @param k the position of factor to find
   * @return k-th smallest factor or -1 if doesn't exist
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

  /**
   * Checks if a number is a divisor of another number.
   *
   * @param number the number to check divisibility for
   * @param divisor the potential divisor
   * @return true if divisor divides number evenly
   */
  private static boolean isDivisor(int number, int divisor) {
    return number % divisor == 0;
  }

}
