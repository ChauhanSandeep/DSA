package Array;

public class MostWaterContainer {
    public static void main(String[] args) {
        int[] heights = {1,8,6,2,5,4,8,3,7};
        System.out.println("Max water that can be stored is "+ maxArea(heights));
    }

    /**
     * Find two lines, which, together with the x-axis forms a container, such that the container contains the most water
     * @param height
     * @return
     */
    public static int maxArea(int[] height) {
        int result = 0;

        int i = 0;
        int j = height.length - 1;
        while (i < j) {
            int minHeight = Math.min(height[i], height[j]);
            result = Math.max(result, minHeight * (j - i));
            if (height[i] < height[j]) {
                i++;
            } else {
                j--;
            }
        }
        return result;
    }
}
