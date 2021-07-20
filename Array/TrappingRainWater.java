package Array;

public class TrappingRainWater {

    public static void main(String[] args) {
        int[] heights = {0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println("Max water that can be trapped is "+ trap(heights));
    }

    /**
     * Given n non-negative integers representing an elevation map where the width of each bar is 1,
     * compute how much water it can trap after raining.
     * @param height
     * @return
     */
    public static int trap(int[] height) {
        int[] leftArr = new int[height.length];
        int[] rightArr = new int[height.length];
        int max = Integer.MIN_VALUE;

        for(int i=0; i<leftArr.length; i++) {
            if(height[i] > max) {
                max = height[i];
            }
            leftArr[i] = max;
        }

        max = Integer.MIN_VALUE;
        for(int i=rightArr.length-1; i>=0; i--) {
            if(height[i] > max) {
                max = height[i];
            }
            rightArr[i] = max;
        }
        // System.out.println(Arrays.toString(leftArr));
        // System.out.println(Arrays.toString(rightArr));

        int result = 0;
        for(int i=0; i<height.length; i++) {
            result += Math.min(leftArr[i], rightArr[i]) - height[i];
        }
        return result;
    }
}
