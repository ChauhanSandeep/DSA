package maths;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Sieve of Eratosthenes algorithm to find all prime numbers less than a given number n.
 *
 * Algorithm:
 * - Create a boolean array where `false` represents a prime number.
 * - Iterate through numbers, marking multiples as `true` (not prime).
 * - Collect and return the list of prime numbers.
 *
 * Time Complexity: O(n log log n)
 * Space Complexity: O(n)
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
     * @param n the upper limit (exclusive) to find prime numbers.
     * @return a list of prime numbers less than n.
     */
    public List<Integer> getPrimes(int n) {
        if (n <= 2) return new ArrayList<>(); // No primes exist below 2.

        boolean[] isComposite = new boolean[n]; // false means prime, true means composite
        List<Integer> primes = new ArrayList<>();

        for (int i = 2; i * i < n; i++) {
            if (!isComposite[i]) { // If i is prime
                for (int j = i * i; j < n; j += i) {
                    isComposite[j] = true; // Mark multiples of i as composite
                }
            }
        }

        // Collect all prime numbers
        for (int i = 2; i < n; i++) {
            if (!isComposite[i]) {
                primes.add(i);
            }
        }
        return primes;
    }
}
