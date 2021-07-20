package Array;

import java.util.HashSet;
import java.util.Set;

public class CountTriplet {

    public static void main(String args[]) {
        int arr[] = {5, 32, 1, 7, 10, 50, 19, 21, 2};
        int n = arr.length;
        countTriplet(arr);
    }

    /**
     * Given a array find all the triplets such that sum of two elements equals the third element.
     * @param arr
     * @return
     */
    static int countTriplet(int arr[]) {
        Set<Integer> set = new HashSet<>();
        int result = 0;
        for (int i = 0; i < arr.length; i++) {
            set.add(arr[i]);
        }

        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (set.contains(arr[i] + arr[j])) result++;
            }
        }
        return result;
    }
}
