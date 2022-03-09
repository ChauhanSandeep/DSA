package Array;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a list of integer signifying light bulbs and range.
 * Bulbs marked 1 are functional and bulbs marked 0 are non-functional
 * Find the min number of functional bulbs which can be used to light complete corridor.
 *
 * https://www.interviewbit.com/problems/minimum-lights-to-activate/
 */
public class MinLights {
    public static void main(String[] args) {
        ArrayList<Integer> lights = (ArrayList<Integer>) Stream.of(0, 0, 0, 1, 0).collect(Collectors.toList());
        int solve = new MinLights().solve(lights, 3);
        System.out.println(solve);
    }

    /**
     *
     * @param list list of bulbs (0-> faulty, 1 -> working)
     * @param range range of each bulb
     * @return min bulbs required to light complete corridor
     */
    public int solve(ArrayList<Integer> list, int range) {
        int max = 0;
        int min = 0;
        int target = list.size() - 1;
        int count = 0;

        while (max < target) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == 1) {
                    int upperRange = i + range - 1;
                    int lowerRange = i - range + 1;
                    if (lowerRange <= min && upperRange > max) {
                        max = upperRange;
                    }
                }
            }
            if (min == max) return -1;
            count++;
            min = max;
        }
        return count;
    }


}
