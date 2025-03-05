package Array;

public class MostWaterContainer {
    public static void main(String[] args) {
        int[] heights = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("Max water that can be stored is " + maxArea(heights));
    }

    /**
     * Given an array of heights, find the two lines that together with the x-axis 
     * form a container that can store the most water.
     * @param height Array of vertical line heights
     * @return Maximum water that can be stored
     */
    public static int maxArea(int[] height) {
        int maxWater = 0;
        int left = 0, right = height.length - 1;

        while (left < right) {
            int minHeight = Math.min(height[left], height[right]);
            maxWater = Math.max(maxWater, minHeight * (right - left));

            // Move the pointer with the smaller height
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxWater;
    }
}
