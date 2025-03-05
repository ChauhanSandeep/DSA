package Array;

public class MissingNumber {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 10};

        int missingNumberSum = findMissingNumberUsingSum(arr);
        System.out.println("Missing Number (Sum Method): " + missingNumberSum);

        int missingNumberXOR = findMissingNumberUsingXOR(arr);
        System.out.println("Missing Number (XOR Method): " + missingNumberXOR);
    }

    // ✅ Method 1: Sum Formula (Handles overflow with long)
    private static int findMissingNumberUsingSum(int[] arr) {
        int size = arr.length + 1; // Since one number is missing
        long totalSum = ((long) size * (size + 1)) / 2; // Prevent overflow
        long sum = 0;

        for (int num : arr) {
            sum += num;
        }
        return (int) (totalSum - sum); // Convert back to int
    }

    // ✅ Method 2: XOR Method (O(N) Time, O(1) Space)
    private static int findMissingNumberUsingXOR(int[] arr) {
        int size = arr.length + 1;
        int xorAll = 0, xorArr = 0;

        // XOR all numbers from 1 to N
        for (int i = 1; i <= size; i++) {
            xorAll ^= i;
        }

        // XOR all elements in the array
        for (int num : arr) {
            xorArr ^= num;
        }

        return xorAll ^ xorArr; // Missing number remains
    }
}
