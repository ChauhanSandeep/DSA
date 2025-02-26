package Array;

public class MinTaps {

    public static void main(String[] args) {
        int target = 5;
        int[] ranges = {4, 3, 1, 2, 0, 0};
        int taps = new MinTaps().minTaps(target, ranges);
        System.out.println("Min taps required are " + taps);
    }

    /**
     * @param target last index of the garden
     * @param ranges array where ranges[i] is the reach of tap at index i
     * @return minimum taps required to water the entire garden, or -1 if not possible
     */
    public int minTaps(int target, int[] ranges) {
        int[] maxReach = new int[target + 1];

        // Step 1: Build maxReach array
        for (int i = 0; i <= target; i++) {
            int left = Math.max(0, i - ranges[i]);
            int right = Math.min(target, i + ranges[i]);
            maxReach[left] = Math.max(maxReach[left], right);
        }

        // Step 2: Use
