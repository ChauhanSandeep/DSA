package BinarySearch;

import java.util.Arrays;

/**
 * Problem Description:
 * Given an integer array, generate a boolean expression string that evaluates to true
 * for all integers present in the array, and false otherwise.
 *
 * The result is a disjunctive expression (e.g., "x == 1 || x == 3 || x == 5").
 *
 * Intuition:
 * We recursively split the sorted array and build a balanced OR-expression tree.
 * This improves readability and avoids generating long linear chains of OR conditions.
 *
 * Algorithm:
 * - Sort the array to handle unordered input.
 * - Recursively divide the array and construct OR sub-expressions.
 *
 * Time Complexity: O(n log n)
 * - O(n log n) due to sorting.
 * - O(n) to build the expression tree recursively.
 *
 * 🧠 Space Complexity: O(log n) for recursion stack.
 *
 */
public class CreateExpression {

    public static void main(String[] args) {
        int[] inputArray = {1, 3, 4, 5, 6, 10, 11, 12, 16, 19, 20};
        // (((x == 1 || x == 3) || (x == 4 || x == 5)) || (((x == 6 || x == 10) || (x == 11 || x == 12)) || (x == 16 || (x == 19 || x == 20))))
        String booleanExpression = buildBooleanExpression(inputArray);
        System.out.println(booleanExpression);
    }

    /**
     * Public method to create a boolean expression for a given array of integers.
     *
     * @param numbers Array of integers for which expression is to be built
     * @return A string containing the boolean expression
     */
    public static String buildBooleanExpression(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return ""; // Edge case: return empty expression for empty/null input
        }

        Arrays.sort(numbers); // Ensure the array is sorted for consistent behavior
        return constructExpression(numbers, 0, numbers.length - 1);
    }

    /**
     * Recursively constructs a balanced OR-expression.
     *
     * @param numbers Sorted array of integers
     * @param start   Start index of current subarray
     * @param end     End index of current subarray
     * @return Boolean expression for the given subarray
     */
    private static String constructExpression(int[] numbers, int start, int end) {
        // Base case: only one element
        if (start == end) {
            return "(x == " + numbers[start] + ")";
        }

        // Base case: two elements, join directly with OR
        if (end - start == 1) {
            return "(x == " + numbers[start] + " || x == " + numbers[end] + ")";
        }

        // Recursive case: split array and build OR expression
        int mid = start + (end - start) / 2;

        String leftExpr = constructExpression(numbers, start, mid);
        String rightExpr = constructExpression(numbers, mid + 1, end);

        return "(" + leftExpr + " || " + rightExpr + ")";
    }
}