package Bitwise;

import java.util.HashSet;
/**
 * Find two numbers in array whose XOR is maximum
 */
public class MaximumXor {

    public static void main(String[] args) {
        int[] arr = {5, 8, 2};
        int n = arr.length;

        System.out.println(new MaximumXor().maxXor(arr, n));
    }

    int maxXor(int[] arr, int size) {
        int result = 0, mask = 0;
        HashSet<Integer> set = new HashSet<>();
        int msb = findMaxSiginficantBit(arr);

        for (int i = msb; i >= 0; i--) {
            mask = mask | (1 << i);
            for (int j = 0; j < size; ++j) {
                set.add(arr[j] & mask);
            }

            int newMax = result | (1 << i);
            for (int prefix : set) {
                if (set.contains(newMax ^ prefix)) {
                    result = newMax;
                    break;
                }
            }
            set.clear();
        }
        return result;
    }

    private int findMaxSiginficantBit(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (Integer num : arr) {
            max = Math.max(num, max);
        }
        return (int) (Math.log(max) / Math.log(2));
    }
}
