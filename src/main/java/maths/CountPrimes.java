package maths;

/**
 * Problem: Count Primes
 *
 * Given an integer n, count how many prime numbers are strictly less than n.
 * A prime has exactly two positive divisors, 1 and itself, so 0, 1, and n
 * itself are not counted.
 *
 * Leetcode: https://leetcode.com/problems/count-primes/ (Medium)
 * Rating:   acceptance 36.3% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Math | Number theory | Sieve of Eratosthenes
 *
 * Example:
 *   Input:  n = 10
 *   Output: 4
 *   Why:    the primes strictly less than 10 are 2, 3, 5, and 7.
 *
 * Follow-ups:
 *   1. How would you answer many count-prime queries?
 *      Precompute a sieve once and build a prefix count array.
 *   2. How would you reduce memory for very large n?
 *      Use a bitset or segmented sieve over smaller ranges.
 *   3. How would you list primes instead of counting them?
 *      Collect indices that remain true after the sieve pass.
 *
 * Related: Ugly Number (263), Kth Factor of N (1492).
 */

public class CountPrimes {

    public static void main(String[] args) {
        CountPrimes solver = new CountPrimes();
        int[] inputs = { 0, 2, 10, 100 };
        int[] expected = { 0, 0, 4, 25 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.countPrimes(inputs[i]);
            System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: instead of testing each number by trial division, mark the
     * composites that are forced by every prime already discovered. Once all
     * multiples of prime values have been marked, the remaining true entries are
     * exactly the primes below upperLimit.
     *
     * Algorithm:
     *   1. Return 0 when upperLimit is at most 2.
     *   2. Mark every number from 2 to upperLimit - 1 as initially prime.
     *   3. For each prime i with i * i < upperLimit, mark its multiples as composite.
     *   4. Count the entries that remain prime.
     *
     * Time:  O(n log log n) - sieve marking touches harmonic prime multiples.
     * Space: O(n) - one boolean slot per value below upperLimit.
     *
     * @param upperLimit exclusive upper bound for prime counting
     * @return number of primes strictly less than upperLimit
     */

    public int countPrimes(int upperLimit) {
        if (upperLimit <= 2) return 0;  // No primes less than 2
        
        // Initialize sieve: assume all numbers are prime
        boolean[] isPrime = new boolean[upperLimit];
        for (int i = 2; i < upperLimit; i++) {
            isPrime[i] = true;
        }
        
        // Sieve of Eratosthenes
        // Only need to check up to √n
        for (int i = 2; i * i < upperLimit; i++) {
            if (isPrime[i]) {
                // Mark all multiples of i as composite
                // Start from i² (optimization: smaller multiples already marked)
                // Eg for i = 3, mark 9, 12, 15, ...
                // For i = 5, mark 25, 30, 35, ...
                for (int j = i * 2; j < upperLimit; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        
        // Count primes
        int count = 0;
        for (int i = 2; i < upperLimit; i++) {
            if (isPrime[i]) {
                count++;
            }
        }
        
        return count;
    }
}
