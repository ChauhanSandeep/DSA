package Bitwise;

/**
 * Find two numbers in array whose AND is maximum
 */
public class MaximumAnd {
    public static void main(String[] args) {
        int[] arr = {5, 8, 7, 2};
        int maxAnd = new MaximumAnd().maxAND(arr, 4);
        System.out.println(maxAnd);
    }

    /**
     * Start from most significant bit and keep searching the matching bits and add to result
     * @param arr
     * @param size
     * @return
     */
    int maxAND(int[] arr, int size) {
        int res = 0;
        int msb = findMaxSiginficantBit(arr, size);
        for (int bit = msb; bit >= 0; bit--) {
            int count = checkBit(res | (1 << bit), arr, size);
            if (count >= 2) {
                res = res | (1 << bit);
            }
        }
        return res;
    }

    int checkBit(int pattern, int[] arr, int size) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if ((pattern & arr[i]) == pattern) {
                count++;
            }
        }
        return count;
    }

    private int findMaxSiginficantBit(int[] arr, int size) {
        int max = Integer.MIN_VALUE;
        for (Integer num : arr) {
            max = Math.max(num, max);
        }
        return (int) (Math.log(max) / Math.log(2));
    }


}
