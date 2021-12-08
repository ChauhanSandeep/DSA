package DynamicProgramming;

import java.util.Arrays;

/**
 * GIven three prime numbers `first` `second` and `third` and an integer `k`.
 * <p>
 * You need to find the first(smallest) k integers which only have `first`, `second`, `third`
 * or a combination of them as their prime factors.
 */
public class SmallestPrimeSequence {
    public static void main(String[] args) {
        int[] result = new SmallestPrimeSequence().solve(2, 5, 11, 5);
        System.out.println(Arrays.toString(result));
    }

    public int[] solve(int first, int second, int third, int k) {
        if (k <= 0) return new int[]{};
        int[] result = new int[k + 1];
        Arrays.fill(result, 1);
        int firstIndex = 0;
        int secondIndex = 0;
        int thirdIndex = 0;

        for (int i = 1; i <= k; i++) {
            int min = Math.min(first * result[firstIndex], Math.min(second * result[secondIndex], third * result[thirdIndex]));
            if (min == first * result[firstIndex]) firstIndex++;
            if (min == second * result[secondIndex]) secondIndex++;
            if (min == third * result[thirdIndex]) thirdIndex++;

            result[i] = min;
        }

        return Arrays.copyOfRange(result, 1, result.length);

    }
}
