package greedy;

import java.util.Arrays;

public class CandyDistribution {
    public static void main(String[] args) {
        int[] ratings = {-255, 369, 319, 77, 128, -202, -147, 282, -26, -489, -443};
        System.out.println(new CandyDistribution().candy(ratings));
    }

    public int candy(int[] ratings) {
        if (ratings == null || ratings.length == 0) return 0;

        int size = ratings.length;
        int[] candies = new int[size];
        Arrays.fill(candies, 1); // Each child gets at least one candy

        // Left to right: Ensuring right child gets more candy if higher rated
        for (int i = 1; i < size; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }

        // Right to left: Ensuring left child gets more candy if higher rated
        int result = candies[size - 1]; // Start with last element
        for (int i = size - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
            result += candies[i]; // Accumulate result during iteration
        }

        return result;
    }
}
