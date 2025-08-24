package frazsheet;

/**
 * Given an integer n, count the total number of digit 1 appearing in all non-negative integers less than or equal to n.
 * 
 * Example 1:
 * Input: n = 13
 * Output: 6
 * Explanation: Digit 1 occurred in the following numbers: 1, 10, 11, 12, 13.
 * 
 * Example 2:
 * Input: n = 0
 * Output: 0
 * 
 * LeetCode: https://leetcode.com/problems/number-of-digit-one/
 * 
 * Follow-up Questions:
 * 1. How would you optimize the solution for very large n (e.g., 10^9)?
 *    - The mathematical approach already handles large n efficiently with O(log n) time complexity.
 * 2. What if we need to count a different digit (e.g., count digit 2)?
 *    - The solution can be generalized by replacing '1' with the target digit in the calculations.
 * 3. How would you modify the solution to count digit 1 in numbers within a range [a, b]?
 *    - We can compute count(b) - count(a-1) using the same approach.
 * 
 * Related Problems:
 * - Factorial Trailing Zeroes (https://leetcode.com/problems/factorial-trailing-zeroes/)
 * - Count Digit One (https://leetcode.com/problems/count-digit-one/)
 */
public class NumberOfDigitOne {
    /**
     * Counts the total number of digit 1 appearing in all numbers from 1 to n.
     * 
     * @param n Upper bound (inclusive)
     * @return Total count of digit 1
     */
    public int countDigitOne(int n) {
        int count = 0;
        
        // Iterate through each digit position (1s, 10s, 100s, etc.)
        for (long i = 1; i <= n; i *= 10) {
            // Split the number into higher, current, and lower parts
            long higher = n / (i * 10);  // Numbers before current digit
            long current = (n / i) % 10;  // Current digit
            long lower = n % i;           // Numbers after current digit
            
            if (current == 0) {
                // Case 1: Current digit is 0
                // Count is determined by higher digits * position
                count += higher * i;
            } else if (current == 1) {
                // Case 2: Current digit is 1
                // Count is higher * position + lower + 1
                count += higher * i + lower + 1;
            } else {
                // Case 3: Current digit > 1
                // Count is (higher + 1) * position
                count += (higher + 1) * i;
            }
        }
        
        return count;
    }
    
    /**
     * Alternative solution using string manipulation (less efficient but more intuitive).
     * This is provided for understanding but not recommended for large n.
     */
    public int countDigitOneBruteForce(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            count += countOnesInNumber(i);
        }
        return count;
    }
    
    /**
     * Helper method to count the number of '1's in a single number.
     */
    private int countOnesInNumber(int num) {
        int count = 0;
        while (num > 0) {
            if (num % 10 == 1) {
                count++;
            }
            num /= 10;
        }
        return count;
    }
}
