package Array;

import java.util.HashMap;


/**
 * count the number of pairs (arr[i], arr[j]) such that arr[i] – arr[j] = i - j
 * https://www.geeksforgeeks.org/count-the-pairs-in-an-array-such-that-the-difference-between-them-and-their-indices-is-equal/
 */
public class CountPairs {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        System.out.println(new CountPairs().countPairs(arr));
    }

    /*
    arr[i] – arr[j] = i - j
    arr[i] - i = arr[j] - j
     */
    int countPairs(int[] arr) {
        int len = arr.length;
        HashMap<Integer,Integer> map = new HashMap<>();

        for(int i=0; i<len; i++) {
            map.put(arr[i] - i, map.getOrDefault(arr[i] - i, 0) + 1);
        }
        int total = 0;
        for (int val : map.values()) {
            total += val*val; // combination with same element is also allowed
        }

        return total;
    }
}
