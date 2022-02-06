package AmazonOa;

public class AnalogousArrays {
    public static void main(String[] args) {
        int[] diffs = {-2, -1, -2, 5};
        System.out.println(new AnalogousArrays().analogousArrays(diffs, 3, 10));
    }

    public int analogousArrays(int[] diffs, int lowerBound, int upperBound) {
        int max = 0;
        int min = 0;
        int curr = 0;

        for(Integer diff: diffs) {
            curr += diff;
            max = Math.max(max, curr);
            min = Math.min(min, curr);
        }
        int range = max - min;
        int allowedRange = upperBound - lowerBound;
        int result = allowedRange - range + 1;
        return result < 0 ? 0 : result;
    }
}
