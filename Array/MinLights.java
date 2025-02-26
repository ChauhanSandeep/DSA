package Array;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a list of integer signifying light bulbs and range.
 * Bulbs marked 1 are functional and bulbs marked 0 are non-functional.
 * Find the min number of functional bulbs which can be used to light the complete corridor.
 *
 * https://www.interviewbit.com/problems/minimum-lights-to-activate/
 */
public class MinLights {

    public static void main(String[] args) {
        ArrayList<Integer> lights = new ArrayList<>(Stream.of(0, 0, 0, 1, 0).collect(Collectors.toList()));
        int result = new MinLights().solve(lights, 3);
        System.out.println("Minimum lights needed: " + result);
    }

    /**
     * @param bulbs  List of bulbs (0 -> faulty, 1 -> working)
     * @param range  The range of each bulb
     * @return       Minimum bulbs required to light the entire corridor, or -1 if impossible
     */
    public int solve(ArrayList<Integer> bulbs, int range) {
        int count = 0;
        int i = 0;
        int n = bulbs.size();

        while (i < n) {
            int rightMost = -1;
            
            // Look for the rightmost working bulb in range [i - (range - 1), i + (range - 1)]
            for (int j = Math.min(n - 1, i + range - 1); j >= Math.max(0, i - (range - 1)); j--) {
                if (bulbs.get(j) == 1) {
                    rightMost = j;
                    break;
                }
            }

            // If no working bulb found, return -1 (impossible to light the corridor)
            if (rightMost == -1) return -1;

            count++;  // Increase light count
            i = rightMost + range;  // Jump to the next unlit position
        }

        return count;
    }
}
