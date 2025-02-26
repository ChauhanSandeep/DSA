package Array;

public class TrappingRainWater {
    public static void main(String[] args) {
        int[] heights = {0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println("Max water that can be trapped is " + trap(heights));
    }

    public static int trap(int[] height) {
        if (height == null || height.length == 0) return 0;

        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int result = 0;

        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];  // Update left max
                } else {
                    result += leftMax - height[left];  // Water trapped
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];  // Update right max
                } else {
                    result += rightMax - height[right];  // Water trapped
                }
                right--;
            }
        }

        return result;
    }
}
