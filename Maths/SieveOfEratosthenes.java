package Maths;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implementation of the Sieve of Eratosthenes algorithm to find all prime numbers less than a given number n.
 *
 * ### Intuition:
 * - A boolean array is used to track composite (non-prime) numbers.
 * - Starting from 2, all multiples of a prime are marked as composite.
 * - The remaining unmarked numbers are prime.
 *
 * ### Algorithm:
 * 1. Create a boolean array where `true` represents a prime number.
 * 2. Iterate through numbers, marking multiples as `false` (not prime).
 * 3. Collect and return the list of prime numbers.
 *
 * ### Complexity Analysis:
 * - **Time Complexity:** O(n log log n) — Efficient for large n.
 * - **Space Complexity:** O(n) — Requires an array of size n.
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
     * Returns a list of all prime numbers less than the given integer n.
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
            if (isPrime[i]) { // If i is still prime
                for (int j = i * i; j < n; j += i) {
                    isPrime[j] = false; // Mark multiples of i as composite
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
