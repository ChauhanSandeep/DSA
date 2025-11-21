package maths;

/**
 * Problem Statement:
 * Given an integer n, return the number of prime numbers that are strictly less than n.
 * A prime number is a natural number greater than 1 that has no positive divisors other than 1 and itself.
 * 
 * Example:
 * Input: n = 10
 * Output: 4
 * Explanation:
 * There are 4 prime numbers less than 10: 2, 3, 5, 7
 * 
 * LeetCode Problem: https://leetcode.com/problems/count-primes/
 */
public class CountPrimes {

    public static void main(String[] args) {
        CountPrimes sieve = new CountPrimes();
        int limit = 10;
        int primeCount = sieve.countPrimes(limit);
        System.out.println("Number of prime numbers less than " + limit + ": " + primeCount);
    }

    /**
     * Main method: Sieve of Eratosthenes algorithm (Optimal).
     * Step-by-step:
     *  1. Create boolean array isPrime[n], initially all true
     *  2. Mark 0 and 1 as not prime (base cases)
     *  3. Start from 2 (first prime):
     *     a. If isPrime[i] is true, i is prime
     *     b. Mark all multiples of i as not prime: i², i²+i, i²+2i, ...
     *     c. Optimization: start from i² instead of 2i (smaller multiples already marked)
     *  4. Only iterate up to √n because:
     *     - If i > √n, then i² > n (no multiples to mark within range)
     *  5. Count remaining true values in isPrime array
     *
     * Key Insight:
     * If a number is composite, it must have a factor ≤ √n. By marking multiples
     * of each prime, we eliminate all composites. Starting from i² is safe because
     * all smaller multiples (like 2i, 3i, ..., (i-1)i) were already marked by smaller primes.
     *
     * Algorithm: Sieve of Eratosthenes.
     * Time Complexity: O(n log log n), outer loop runs √n times, inner loop complexity sums to O(n log log n).
     * Space Complexity: O(n) for boolean array.
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
                for (int j = i * i; j < upperLimit; j += i) {
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
