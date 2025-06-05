package Maths;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implementation of the Sieve of Eratosthenes algorithm to find all prime numbers less than a given number n.
 *
 * LeetCode Problem: https://leetcode.com/problems/count-primes/
 */
public class SieveOfEratosthenes {

    public static void main(String[] args) {
        SieveOfEratosthenes sieve = new SieveOfEratosthenes();
        int limit = 10;
        List<Integer> primes = sieve.getPrimes(limit);
        System.out.println("Prime numbers less than " + limit + ": " + primes);
    }

    /**
     * ### Algorithm:
     * 1. Create a boolean array where `true` represents a prime number.
     * 2. Iterate through numbers, starting from 2, and mark all multiples of each prime as `false`.
     *    This is done because the multiple of a prime numbers will not be prime.
     * 3. Continue this process until you reach the square root of n.
     *
     * ### Complexity Analysis:
     * - **Time Complexity:** O(n log log n) — Efficient for large n.
     * - **Space Complexity:** O(n) — Requires an array of size n.
     *
     * @param n The upper limit (exclusive) to find prime numbers.
     * @return A list of prime numbers less than n.
     */
    public List<Integer> getPrimes(int n) {
        if (n <= 2) return new ArrayList<>(); // No primes exist below 2.

        boolean[] isPrime = new boolean[n]; // true means prime, false means composite
        for (int i = 2; i < n; i++) {
            isPrime[i] = true; // Initially mark all as prime
        }

        for (int i = 2; i * i <= n; i++) { // Iterate up to sqrt(n)
            if (isPrime[i]) { // If i is still prime, mark its multiples as composite
                // start from i*i instead of i+1 to avoid redundant checks
                for (int j = i * i; j < n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        // Collect all prime numbers
        return IntStream.range(2, n)
                .filter(i -> isPrime[i])
                .boxed()
                .collect(Collectors.toList());
    }
}
