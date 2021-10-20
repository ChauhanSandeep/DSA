package Maths;

/**
 * Given two positive integers n and k.
 * A factor of an integer n is defined as an integer i where n % i == 0.
 * Consider a list of all factors of n sorted in ascending order,
 * return the kth factor in this list or return -1 if n has less than k factors.
 *
 * https://leetcode.com/problems/the-kth-factor-of-n/
 */
public class KthFactor {

    public static void main(String[] args) {
        new KthFactor().kthFactor(4, 4);
    }

    // This run in sqrt(n) time complexity
    int kthFactor(int num, int k) {
        int factor = 1;
        for (; factor * factor <= num; ++factor) {
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
