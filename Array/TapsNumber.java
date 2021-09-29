package Array;

/**
 * There is a one-dimensional garden on the x-axis.
 * The garden starts at the point 0 and ends at the point n. (i.e The length of the garden is n).
 * Array ranges contains the range of tap at ith index.
 * Return the minimum number of taps that should be open to water the whole garden, If the garden cannot be watered return -1.
 */
public class TapsNumber {

    public static void main(String[] args) {
        int[] ranges = {3, 4, 1, 1, 0, 0};
        int result = new TapsNumber().minTaps(5, ranges);
        System.out.println("the min number of taps required are " + result);
    }

    public int minTaps(int n, int[] ranges) {
        int min = 0;
        int max = 0;
        int taps = 0;

        while (max < n) {
            for (int i = min; i < ranges.length; i++) {
                if (i - ranges[i] <= min && i + ranges[i] > max) {
                    max = i + ranges[i];
                }
            }
            if (min == max) return -1;
            taps++;
            min = max;
        }
        return taps;
    }


}
