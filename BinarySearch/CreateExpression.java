package BinarySearch;

import java.util.Arrays;

/**
 * Generates a boolean expression that evaluates to True for all elements in the given array
 * and False for all other elements.
 */
public class CreateExpression {
    public static void main(String[] args) {
        int[] arr = {1, 3, 4, 5, 6, 10, 11, 12, 16, 19, 20};
        String expression = createExpression(arr);
        System.out.println(expression);
    }

    public static String createExpression(int[] arr) {
        Arrays.sort(arr);
        return generateExpression(arr, 0, arr.length - 1);
    }

    private static String generateExpression(int[] arr, int start, int end) {
        if (start == end) return "(x == " + arr[start] + ")";
        if (end - start == 1) return "(x == " + arr[start] + " || x == " + arr[end] + ")";

        int mid = (start + end) / 2;
        String leftExp = generateExpression(arr, start, mid);
        String rightExp = generateExpression(arr, mid + 1, end);

        return "(" + leftExp + " || " + rightExp + ")";
    }
}
