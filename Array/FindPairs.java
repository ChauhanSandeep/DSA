package Array;

import java.util.Arrays;

public class FindPairs {
    public static void main(String[] args) {
        int[] X = {2, 1, 6};
        int[] Y = {1, 5};

        long pairs = countPairs(X, Y, 3, 2);
        System.out.println(pairs);
    }

    /**
     * Given two arrays X and Y, find the pairs for which X^Y > Y^X
     */
    static long countPairs(int x[], int y[], int M, int N)
    { long result = 0;

        Arrays.sort(x);
        Arrays.sort(y);

        int xInd = M-1;
        int yInd = 0;

        while(xInd >= 0) {
            while(yInd < N && Math.pow(x[xInd], y[yInd]) > Math.pow(y[yInd], x[xInd])) {
                yInd++;
                result++;
            }
            xInd--;
            yInd = 0;
        }
        return result;
    }
}
