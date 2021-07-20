package Array;

/**
 * Given an array containing integers from 1 to N with one number missing
 * Find the missing number
 */
public class MissingNumber {
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8,10};
        int missingNumber = findMissingNumber(arr);
        System.out.println(missingNumber);
    }

    private static int findMissingNumber(int[] arr) {
        int size = arr.length + 1;
        int totalSum = (size * (size + 1))/2;
        int sum = 0;
        for (Integer i : arr) {
            sum += i;
        }
        return totalSum - sum;
    }
}
