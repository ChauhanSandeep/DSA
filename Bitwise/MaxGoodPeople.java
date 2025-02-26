package Bitwise;

/**
 * This class finds the maximum number of good people based on the given statements.
 * 
 * Algorithm:
 * - Use binary representation to create equations representing good and bad persons.
 * - Validate each equation against the provided statements.
 * - Keep track of the valid equation with the maximum number of good persons.
 * - Time Complexity: O(2^n * n^2)
 * - Space Complexity: O(n)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/maximum-good-people-based-on-statements/
 */
public class MaxGoodPeople {

    public static void main(String[] args) {
        int[][] statements = {
            {2, 1, 2},
            {1, 2, 2},
            {2, 0, 2}
        };
        System.out.println(new MaxGoodPeople().maximumGood(statements));
    }

    /**
     * Create an equation representing good and bad persons in binary string format.
     * For each equation, check if it is valid based on the provided statements.
     * Keep track of the valid equation with the maximum number of good persons.
     */
    public int maximumGood(int[][] statements) {
        int length = statements.length;
        int maxGoodCount = 0;

        // Iterate through all possible binary equations representing good and bad persons
        int totalCombinations = (1 << length) - 1;
        while (totalCombinations > 0) {
            StringBuilder binaryRepresentation = new StringBuilder(Integer.toString(totalCombinations, 2));
            while (binaryRepresentation.length() < length) {
                binaryRepresentation.insert(0, "0");
            }
            if (isValid(statements, binaryRepresentation.toString())) {
                int goodCount = 0;
                for (char ch : binaryRepresentation.toString().toCharArray()) {
                    if (ch == '1') goodCount++;
                }
                maxGoodCount = Math.max(goodCount, maxGoodCount);
            }
            totalCombinations--;
        }
        return maxGoodCount;
    }

    /**
     * Validate the binary equation against the provided statements.
     */
    private boolean isValid(int[][] statements, String binaryEquation) {
        for (int i = 0; i < binaryEquation.length(); i++) {
            if (binaryEquation.charAt(i) == '1') {
                int[] statement = statements[i];
                for (int j = 0; j < statement.length; j++) {
                    if (statement[j] == 2) continue;
                    if (binaryEquation.charAt(j) - '0' != statement[j]) return false;
                }
            }
        }
        return true;
    }
}
