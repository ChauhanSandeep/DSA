package Bitwise;

/**
 * This class contains a method to find the number of non-negative integers less than or equal to a given number
 * that do not have consecutive 1s in their binary representation.
 * 
 * Algorithm:
 * - Use dynamic programming to count the valid numbers by analyzing their binary representations.
 * - Time Complexity: O(log n)
 * - Space Complexity: O(log n)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/
 */
public class ConsecutiveInteger {

    public static void main(String[] args) {
        System.out.println(findIntegers(10));
    }

    public static int findIntegers(int num) {
        StringBuilder binaryRepresentation = new StringBuilder(Integer.toBinaryString(num)).reverse();
        int length = binaryRepresentation.length();
        int[] zeroEnd = new int[length];
        int[] oneEnd = new int[length];
        
        // Base cases for DP
        zeroEnd[0] = 1;
        oneEnd[0] = 1;

        // Fill the DP arrays
        for (int i = 1; i < length; i++) {
            zeroEnd[i] = zeroEnd[i - 1] + oneEnd[i - 1];
            oneEnd[i] = zeroEnd[i - 1];
        }
        
        int result = zeroEnd[length - 1] + oneEnd[length - 1];
        
        // Subtract invalid counts
        for (int i = length - 2; i >= 0; i--) {
            if (binaryRepresentation.charAt(i) == '0' && binaryRepresentation.charAt(i + 1) == '0') {
                result -= oneEnd[i];
            }
            if (binaryRepresentation.charAt(i) == '1' && binaryRepresentation.charAt(i + 1) == '1') {
                break;
            }
        }

        return result;
    }
}
