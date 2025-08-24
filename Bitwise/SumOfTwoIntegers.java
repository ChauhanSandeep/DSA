package Bitwise;

/**
 * Problem: Sum of Two Integers
 * 
 * Given two integers a and b, return the sum of the two integers without using the operators + and -.
 * 
 * Example:
 * Input: a = 1, b = 2
 * Output: 3
 * 
 * LeetCode: https://leetcode.com/problems/sum-of-two-integers
 * 
 * Time Complexity: O(1) as we're working with fixed-size integers (32 bits)
 * Space Complexity: O(1)
 */
public class SumOfTwoIntegers {
    public int getSum(int a, int b) {
        // Iterate until there is no carry
        while (b != 0) {
            // Calculate the carry - this will have bits set to 1 if there's a carry
            int carry = a & b;
            
            // Sum of bits of a and b where at least one of the bits is not set
            a = a ^ b;
            
            // Carry is shifted by one so that adding it to a gives the required sum
            b = carry << 1;
            
            // In Java, we need to handle negative numbers carefully due to two's complement
            // and the fact that integers are 32-bit
            if (b != 0 && (b & 0x80000000) != 0) {
                break;
            }
        }
        
        return a;
    }
}
