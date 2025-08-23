package com.sandeep.frazsheet.sorting;

/**
 * Problem: Consecutive Numbers Sum
 * 
 * Given an integer n, return the number of ways you can write n as the sum of consecutive positive integers.
 * 
 * Example:
 * Input: n = 9
 * Output: 3
 * Explanation: 9 = 9 = 4 + 5 = 2 + 3 + 4
 * 
 * LeetCode: https://leetcode.com/problems/consecutive-numbers-sum
 * 
 * Follow-up Questions:
 * 1. What if we allow negative integers in the consecutive sequence?
 *    Answer: Problem becomes more complex as we need to handle different cases for negative ranges.
 * 
 * 2. How would you modify for non-consecutive arithmetic progressions?
 *    Answer: Generalize to arithmetic sequences with any common difference, not just 1.
 * 
 * 3. What if we need to find the actual sequences instead of just counting?
 *    Answer: Modify algorithm to store and return the actual consecutive number sequences.
 *    Related: https://leetcode.com/problems/sum-of-consecutive-numbers/
 * 
 * @author Sandeep
 */
public class ConsecutiveNumbersSum {
    
    /**
     * Counts ways to express n as sum of consecutive positive integers using mathematical approach.
     * 
     * Algorithm:
     * 1. For k consecutive numbers starting from a: n = k*a + k*(k-1)/2
     * 2. Rearranging: a = (n - k*(k-1)/2) / k
     * 3. For valid sequence, 'a' must be positive integer
     * 4. Iterate through possible values of k and count valid sequences
     * 5. Stop when k*(k-1)/2 >= n (no valid positive 'a' possible)
     * 
     * Time Complexity: O(√n) where n is the input number
     * Space Complexity: O(1) - only using constant extra space
     * 
     * @param n Input positive integer
     * @return Number of ways to write n as sum of consecutive positive integers
     */
    public int consecutiveNumbersSum(int n) {
        if (n <= 0) return 0;
        
        int count = 0;
        
        // Try different lengths of consecutive sequences
        for (int k = 1; k * (k - 1) / 2 < n; k++) {
            // For k consecutive numbers: n = k*a + k*(k-1)/2
            // Solving for a: a = (n - k*(k-1)/2) / k
            
            int numerator = n - k * (k - 1) / 2;
            
            // Check if 'a' is a positive integer
            if (numerator > 0 && numerator % k == 0) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Alternative mathematical approach using divisor analysis.
     * Based on the fact that n = k*(2a + k - 1)/2.
     * 
     * Time Complexity: O(√n)
     * Space Complexity: O(1)
     */
    public int consecutiveNumbersSumDivisor(int n) {
        int count = 0;
        
        // Transform: n = k*(2a + k - 1)/2
        // So: 2n = k*(2a + k - 1)
        // We need 2a + k - 1 > 0, so 2a > -(k-1), which means a > -(k-1)/2
        // Since a >= 1, this is automatically satisfied for positive k
        
        for (int k = 1; k <= Math.sqrt(2 * n); k++) {
            if ((2 * n) % k == 0) {
                int quotient = (2 * n) / k;
                
                // Check if we can form a valid sequence
                // quotient = 2a + k - 1, so a = (quotient - k + 1) / 2
                if ((quotient - k + 1) % 2 == 0 && (quotient - k + 1) / 2 > 0) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * Brute force approach for small values and verification.
     * Tests all possible starting points and sequence lengths.
     * 
     * Time Complexity: O(n^1.5)
     * Space Complexity: O(1)
     */
    public int consecutiveNumbersSumBruteForce(int n) {
        int count = 0;
        
        // Try all possible starting points
        for (int start = 1; start <= n; start++) {
            int sum = 0;
            
            // Try consecutive sequences starting from 'start'
            for (int current = start; sum < n; current++) {
                sum += current;
                
                if (sum == n) {
                    count++;
                    break;
                }
            }
        }
        
        return count;
    }
    
    /**
     * Optimized approach using number theory insights.
     * Based on factorization of (2n + k^2 - k).
     * 
     * Time Complexity: O(√n)
     * Space Complexity: O(1)
     */
    public int consecutiveNumbersSumOptimized(int n) {
        int count = 0;
        
        // For k consecutive integers starting at 'a':
        // Sum = k*a + k*(k-1)/2 = n
        // Rearranging: k*(2a + k - 1) = 2n
        // So we need k to divide 2n and (2a + k - 1) = 2n/k
        // For positive a: 2a + k - 1 > k - 1, so 2n/k > k - 1
        // This gives us: 2n > k*(k-1), or k < (1 + √(1 + 8n))/2
        
        int maxK = (int) ((1 + Math.sqrt(1 + 8.0 * n)) / 2);
        
        for (int k = 1; k <= maxK; k++) {
            if ((2 * n) % k == 0) {
                int temp = (2 * n) / k - k + 1;
                if (temp > 0 && temp % 2 == 0) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * Returns all actual consecutive sequences that sum to n.
     * Useful for debugging and verification.
     * 
     * @param n Input number
     * @return List of consecutive sequences
     */
    public java.util.List<java.util.List<Integer>> findAllConsecutiveSequences(int n) {
        java.util.List<java.util.List<Integer>> sequences = new java.util.ArrayList<>();
        
        for (int k = 1; k * (k - 1) / 2 < n; k++) {
            int numerator = n - k * (k - 1) / 2;
            
            if (numerator > 0 && numerator % k == 0) {
                int start = numerator / k;
                
                java.util.List<Integer> sequence = new java.util.ArrayList<>();
                for (int i = 0; i < k; i++) {
                    sequence.add(start + i);
                }
                sequences.add(sequence);
            }
        }
        
        return sequences;
    }
    
    /**
     * Mathematical verification using closed form solution.
     * Verifies if a sequence with given start and length sums to n.
     * 
     * @param start Starting number of sequence
     * @param length Length of consecutive sequence
     * @param n Target sum
     * @return true if sequence sums to n
     */
    public boolean verifySequence(int start, int length, int n) {
        // Sum of arithmetic sequence: length * (2*start + length - 1) / 2
        int calculatedSum = length * (2 * start + length - 1) / 2;
        return calculatedSum == n;
    }
    
    /**
     * Finds the minimum and maximum length sequences for a given n.
     * 
     * @param n Input number
     * @return Array [minLength, maxLength] of valid sequence lengths
     */
    public int[] findSequenceLengthRange(int n) {
        int minLength = Integer.MAX_VALUE;
        int maxLength = Integer.MIN_VALUE;
        boolean found = false;
        
        for (int k = 1; k * (k - 1) / 2 < n; k++) {
            int numerator = n - k * (k - 1) / 2;
            
            if (numerator > 0 && numerator % k == 0) {
                minLength = Math.min(minLength, k);
                maxLength = Math.max(maxLength, k);
                found = true;
            }
        }
        
        return found ? new int[]{minLength, maxLength} : new int[]{-1, -1};
    }
    
    /**
     * Analyzes the mathematical properties of the input number.
     * 
     * @param n Input number
     * @return Analysis result with various properties
     */
    public NumberAnalysis analyzeNumber(int n) {
        java.util.List<java.util.List<Integer>> sequences = findAllConsecutiveSequences(n);
        int[] lengthRange = findSequenceLengthRange(n);
        
        // Calculate some interesting properties
        boolean isPowerOfTwo = (n & (n - 1)) == 0;
        boolean isPrime = isPrime(n);
        int oddDivisorCount = countOddDivisors(n);
        
        return new NumberAnalysis(
            n,
            sequences.size(),
            sequences,
            lengthRange[0],
            lengthRange[1],
            isPowerOfTwo,
            isPrime,
            oddDivisorCount
        );
    }
    
    // Helper method to check if number is prime
    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
    
    // Helper method to count odd divisors
    private int countOddDivisors(int n) {
        int count = 0;
        for (int i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                if (i % 2 == 1) count++;
                if (i != n / i && (n / i) % 2 == 1) count++;
            }
        }
        return count;
    }
    
    // Analysis result class
    static class NumberAnalysis {
        int number;
        int wayCount;
        java.util.List<java.util.List<Integer>> sequences;
        int minLength;
        int maxLength;
        boolean isPowerOfTwo;
        boolean isPrime;
        int oddDivisorCount;
        
        NumberAnalysis(int number, int wayCount, java.util.List<java.util.List<Integer>> sequences,
                      int minLength, int maxLength, boolean isPowerOfTwo, boolean isPrime, int oddDivisorCount) {
            this.number = number;
            this.wayCount = wayCount;
            this.sequences = sequences;
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.isPowerOfTwo = isPowerOfTwo;
            this.isPrime = isPrime;
            this.oddDivisorCount = oddDivisorCount;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Number: %d\\n", number));
            sb.append(String.format("Ways to express as consecutive sum: %d\\n", wayCount));
            sb.append(String.format("Sequence lengths range: [%d, %d]\\n", minLength, maxLength));
            sb.append(String.format("Is power of 2: %b\\n", isPowerOfTwo));
            sb.append(String.format("Is prime: %b\\n", isPrime));
            sb.append(String.format("Odd divisor count: %d\\n", oddDivisorCount));
            sb.append("Sequences:\\n");
            
            for (java.util.List<Integer> seq : sequences) {
                sb.append("  ").append(seq).append("\\n");
            }
            
            return sb.toString();
        }
    }
}