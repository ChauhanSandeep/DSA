package maths;

/**
 * Given two positive integers n and k, this program finds the k-th factor of n.
 * A factor of n is any integer i such that n % i == 0.
 *
 * ### Algorithm:
 * - Iterate from `1` to `√n` to count factors.
 * - If `n / i` is a distinct factor (not a perfect square case), count it as well.
 * - Return the k-th factor if found, otherwise return -1.
 *
 * ### Time Complexity: O(√n)
 * ### Space Complexity: O(1)
 *
 * LeetCode Problem: https://leetcode.com/problems/the-kth-factor-of-n/
 */
public class KthFactor {

    public static void main(String[] args) {
        KthFactor kthFactor = new KthFactor();
        System.out.println("Kth factor: " + kthFactor.kthFactor(12, 3));  // Expected output: 3
        System.out.println("Kth factor: " + kthFactor.kthFactor(4, 4));   // Expected output: -1
    }

    /**
     * Finds the k-th factor of a given number n.
     *
     * @param n the number whose factors are considered.
     * @param k the k-th factor to retrieve.
     * @return the k-th factor or -1 if there are fewer than k factors.
     */
    public int kthFactor(int n, int k) {
        int divisor;

        // First pass: Find and count factors up to sqrt(n)
        for (divisor = 1; divisor * divisor <= n; ++divisor) {
            if (n % divisor == 0) {
                if (--k == 0) return divisor; // Found the k-th factor
            }
        }

        // Second pass: Find corresponding larger factors (n / divisor)
        for (divisor = divisor - 1; divisor >= 1; --divisor) {
            if (divisor * divisor == n) continue; // Skip perfect square case to avoid duplicates
            if (n % divisor == 0) {
                if (--k == 0) return n / divisor;
            }
        }

        return -1; // If fewer than k factors exist
    }
}
