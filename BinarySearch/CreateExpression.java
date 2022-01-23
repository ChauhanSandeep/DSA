package BinarySearch;

import java.util.Arrays;

/**
 * Create a boolean expression which returns to True for all elements in array
 * and returns False for all other elements.
 */
public class CreateExpression {
    public static void main(String[] args) {
        int[] arr = {1, 3, 4, 5, 6, 10, 11, 12, 16, 19, 20};
        String expression = createExpression(arr);
        System.out.println(expression);

//        for(int i=0; i<50; i++) {
//            validate(i);
//        }
    }

    public static String createExpression(int[] arr) {

        Arrays.sort(arr);
        return create(arr, 0, arr.length - 1);
    }

    private static String create(int[] arr, int start, int end) {
        if (start == end) return "(x == " + arr[start] + ")";

        String baseExp = "( x >= " + arr[start] + " && x <= " + arr[end] + " )";

        int mid = (start + end) / 2;
        String leftExp = create(arr, start, mid);
        String rightExp = create(arr, mid + 1, end);

        return String.join("", baseExp, " && ", " ( ", leftExp, " || ", rightExp, " )");
    }

    private static boolean validate(int x) {
        boolean res =  ( x >= 1 && x <= 20 ) &&  ( ( x >= 1 && x <= 10 ) &&  ( ( x >= 1 && x <= 4 ) &&  ( ( x >= 1 && x <= 3 ) &&  ( (x == 1) || (x == 3) ) || (x == 4) ) || ( x >= 5 && x <= 10 ) &&  ( ( x >= 5 && x <= 6 ) &&  ( (x == 5) || (x == 6) ) || (x == 10) ) ) || ( x >= 11 && x <= 20 ) &&  ( ( x >= 11 && x <= 16 ) &&  ( ( x >= 11 && x <= 12 ) &&  ( (x == 11) || (x == 12) ) || (x == 16) ) || ( x >= 19 && x <= 20 ) &&  ( (x == 19) || (x == 20) ) ) );
        System.out.println(x + " " + res);
        return res;
    }
}
