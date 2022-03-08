package Array;

/**
 * Given an array (signify garden) containing taps. Each element in array signify range of tap at that index (-range, +range)
 * You are also given last index of array till which we need to water the garden.
 * find the min taps that needs to be open to water the complete garden
 */
public class MinTaps {

    public static void main(String[] args) {
        int target = 5;
        int[] ranges = {4,3,1,2,0,0};
        int taps = new MinTaps().minTaps(target, ranges);
        System.out.println("Min taps required are " + taps);
    }

    /**
     *
     * @param target last index of array
     * @param ranges array containing range of tap at each index
     * @return min taps required to water complete garden
     */
    public int minTaps(int target, int[] ranges) {
        int min = 0;
        int max = 0;
        int taps = 0;

        while (max < target) {
            for (int i = 0; i < ranges.length; i++) {
                int lowerRange = i - ranges[i];
                int upperRange = i + ranges[i];
                if (lowerRange <= min && upperRange > max) {
                    max = upperRange;
                }
            }
            if (min == max) return -1;
            taps++;
            min = max;
        }
        return taps;
    }
}
