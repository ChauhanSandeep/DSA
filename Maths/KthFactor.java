package maths;

/**
 * Given two positive integers n and k.
 * A factor of an integer n is defined as an integer i where n % i == 0.
 * Consider a list of all factors of n sorted in ascending order,
 * return the kth factor in this list or return -1 if n has less than k factors.
 *
 * Time Complexity: O(√n)
 * Space Complexity: O(1)
 *
 * LeetCode Problem: https://leetcode.com/problems/the-kth-factor-of-n/
 */
public class KthFactor {

    public static void main(String[] args) {
        KthFactor kthFactor = new KthFactor();
        System.out.println("Kth factor: " + kthFactor.kthFactor(4, 4));
    }

    /**
     * Finds the k-th factor of a given number n.
     *
     * @param num the number whose factors are considered.
     * @param k the k-th factor to retrieve.
     * @return the k-th factor or -1 if there are fewer than k factors.
     */
    public int kthFactor(int num, int k) {
        int factor;
        for (factor = 1; factor * factor <= num; ++factor) {
            if (num % factor == 0 && --k == 0) {
                return factor;
            }
        }
        for (factor = factor - 1; factor >= 1; --factor) {
            if (factor * factor == num) continue;
            if (num % factor == 0 && --k == 0) {
                return num / factor;
            }
        }
        return -1;
    }
}
