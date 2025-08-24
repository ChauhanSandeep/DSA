package DynamicProgramming;

import java.util.*;

/**
 * Problem: Ugly Number II
 * 
 * An ugly number is a positive integer whose prime factors are limited to 2, 3, and 5.
 * Given an integer n, return the nth ugly number.
 * 
 * Example:
 * Input: n = 10
 * Output: 12
 * Explanation: [1, 2, 3, 4, 5, 6, 8, 9, 10, 12] is the sequence of the first 10 ugly numbers.
 * 
 * LeetCode: https://leetcode.com/problems/ugly-number-ii
 * 
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 */
public class UglyNumberII {
    public int nthUglyNumber(int n) {
        if (n <= 0) return 0;
        
        int[] ugly = new int[n];
        ugly[0] = 1;
        
        // Pointers for 2, 3, 5
        int p2 = 0, p3 = 0, p5 = 0;
        
        for (int i = 1; i < n; i++) {
            // Find the next ugly number by multiplying with 2, 3, or 5
            int next2 = ugly[p2] * 2;
            int next3 = ugly[p3] * 3;
            int next5 = ugly[p5] * 5;
            
            // Get the minimum of the three candidates
            int nextUgly = Math.min(next2, Math.min(next3, next5));
            ugly[i] = nextUgly;
            
            // Move the pointers forward if their product equals the current ugly number
            if (nextUgly == next2) p2++;
            if (nextUgly == next3) p3++;
            if (nextUgly == next5) p5++;
        }
        
        return ugly[n - 1];
    }
}
