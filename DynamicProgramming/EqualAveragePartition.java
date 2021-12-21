package DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Need to understand this
 * https://www.interviewbit.com/problems/equal-average-partition/
 */
public class EqualAveragePartition {

    public static void main(String[] args) {
        int[] input = {28, 10, 2, 44, 33, 31, 39, 46, 1, 24, 32, 31, 28, 9, 13, 40, 46, 1, 16, 18, 39, 13, 48, 5};
        System.out.println(Arrays.deepToString(new EqualAveragePartition().avgset(input)));
    }

    HashMap<Long, Boolean> map;
    ArrayList<Integer> left, right;

    public int[][] avgset(int[] input) {
        Arrays.sort(input);
        int size = input.length;
        Integer sum = Arrays.stream(input).sum();

        for (int i = 1; i < size; i++) {
            if ((sum * i) % size == 0) {
                int r = (sum * i) / size;
                int[][] result = getResult(input, r, i);
                if (result != null && result.length == 2) {
                    return result;
                }
            }
        }
        return new int[][]{};
    }

    int[][] getResult(int[] input, int s, int index) {
        map = new HashMap<>();
        left = new ArrayList<Integer>();
        right = new ArrayList<Integer>();

        boolean valid = go(input, 0, s, index);
        if (!valid) {
            return new int[][]{};
        }

        int[] x = convert(left);
        int[] y = convert(right);
        Arrays.sort(x);
        Arrays.sort(y);
        return new int[][]{x, y};
    }

    boolean go(int[] input, int i, int rs, int re) {
        if (i >= input.length) {
            if (rs == 0 && re == 0) {
                return true;
            }
            return false;
        }

        if (rs < 0 || re < 0) {
            return false;
        }

        long k = rs * (long) 1e9 + re * 1000 + i;
        if (map.containsKey(k)) {
            return map.get(k);
        }

        boolean y = go(input, i + 1, rs - input[i], re - 1);
        if (y) {
            left.add(input[i]);
            return true;
        }

        boolean x = go(input, i + 1, rs, re);
        if (x) {
            right.add(input[i]);
            return true;
        }

        map.put(k, false);
        return false;
    }

    int[] convert(ArrayList<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }


}
