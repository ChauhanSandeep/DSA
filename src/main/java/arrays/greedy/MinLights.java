package arrays.greedy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;

/**
 * Problem: Minimum Lights to Activate
 *
 * A corridor has working and broken bulbs. A working bulb at index i lights the
 * interval [i - range + 1, i + range - 1]. Return the fewest working bulbs that
 * cover every corridor position, or -1 if coverage is impossible.
 *
 * Source:   InterviewBit - Minimum Lights to Activate
 * Pattern:  Greedy | Interval covering | Rightmost useful choice
 *
 * Example:
 *   Input:  bulbs = [0,0,1,0,0], range = 3
 *   Output: 1
 *   Why:    the bulb at index 2 covers the full corridor after bounds are clipped.
 *
 * Follow-ups:
 *   1. What if bulbs have different ranges?
 *      Convert each working bulb to its own interval and run interval cover greedy.
 *   2. What if each bulb has an activation cost?
 *      The problem becomes weighted interval cover and usually needs DP.
 *   3. What if bulbs can be toggled on or off dynamically?
 *      Maintain available intervals with a segment tree or balanced interval index.
 */
public class MinLights {

    public static void main(String[] args) {
        MinLights solver = new MinLights();
        List<List<Integer>> inputs = Arrays.asList(
            Arrays.asList(0, 0, 1, 0, 0),
            Arrays.asList(0, 0, 0),
            Arrays.asList(1, 1, 1, 1),
            Arrays.asList(1, 0, 0, 1)
        );
        int[] ranges = {3, 2, 1, 2};
        int[] expected = {1, -1, 4, 2};

        for (int i = 0; i < inputs.size(); i++) {
            int got = solver.findMinLights(inputs.get(i), ranges[i]);
            System.out.printf("bulbs=%s range=%d -> %d  expected=%d%n",
                inputs.get(i), ranges[i], got, expected[i]);
        }
    }



    /**
     * Finds the minimum number of functional bulbs required to light the entire corridor.
     *
     * **Approach:**
     * - We need to find the minimum number of bulbs that can light up the entire corridor.
     * - Each working bulb at index `i` can light up a range `[i - (range - 1), i + (range - 1)]`.
     * - Our goal is to iteratively find the best bulb that maximizes coverage until the entire corridor is lit.
     * - If at any point no bulb is found to extend coverage, return `-1`.
     *
     * **Steps:**
     * 1. Initialize `rightMostCovered = -1` (tracks the farthest lit position).
     * 2. Initialize `leftMostCovered = 0` (tracks the next uncovered position).
     * 3. While the corridor is not fully covered:
     *    - Iterate through the list to find a bulb that covers the current gap and extends coverage maximally.
     *    - If no such bulb is found, return `-1` (impossible case).
     *    - Otherwise, update `rightMostCovered` and increment the bulb count.
     * 4. Return the total count of bulbs used.
     *
     * **Time Complexity:** `O(n^2)` (worst case, scanning the list for each bulb placement).
     * **Space Complexity:** `O(1)` (constant auxiliary space).
     *
     * @param bulbs List representing bulbs (0 -> faulty, 1 -> working)
     * @param range Range covered by each bulb
     * @return Minimum bulbs required to light the corridor, or -1 if it's not possible
     */
    public int findMinLights(List<Integer> bulbs, int range) {
        int corridorEnd = bulbs.size() - 1;
        int rightMostCovered = -1; // The farthest lit position
        int leftMostCovered = 0;  // The starting point of the next uncovered area
        int bulbsUsed = 0;

        while (leftMostCovered <= corridorEnd) {
            int bestBulb = -1;
            int nextRightMostCovered = rightMostCovered;

            // Find the best bulb that can extend coverage
            for (int i = 0; i < bulbs.size(); i++) {
                if (bulbs.get(i) == 1) {
                    int lowerRange = i - range + 1;
                    int upperRange = i + range - 1;

                    if (lowerRange <= leftMostCovered && upperRange > nextRightMostCovered) {
                        bestBulb = i;
                        nextRightMostCovered = upperRange;
                    }
                }
            }

            // No bulb found to extend coverage
            if (bestBulb == -1) return -1;

            bulbsUsed++;
            rightMostCovered = nextRightMostCovered;
            leftMostCovered = rightMostCovered + 1; // Move to the next dark position
        }

        return bulbsUsed;
    }

    /**
     * Intuition: focus on the leftmost position that is still dark. Any valid
     * solution must turn on a working bulb whose range covers that position; among
     * those choices, the rightmost bulb covers the farthest suffix and cannot make
     * a future position harder to cover.
     *
     * Algorithm:
     *   1. Keep position as the next unlit corridor index.
     *   2. Search the bulb range that can cover position from right to left.
     *   3. If a bulb is found, count it and jump position past its coverage; otherwise return -1.
     *
     * Time:  O(n) - each search advances the covered boundary and scans bounded ranges.
     * Space: O(1) - only indexes and counters are stored.
     *
     * @param bulbs list where 1 means working bulb and 0 means broken bulb
     * @param range number of positions a bulb reaches to each side including itself
     * @return minimum bulbs needed, or -1 if the corridor cannot be fully lit
     */
    public int findMinLightsGreedy(List<Integer> bulbs, int range) {
        int corridorEnd = bulbs.size();
        int position = 0;  // The next uncovered section
        int bulbsUsed = 0;

        while (position < corridorEnd) {
            int bestBulb = -1;

            // Search for the rightmost bulb in the range [position - (range-1), position + (range-1)]
            int start = Math.max(0, position - (range - 1));
            int end = Math.min(corridorEnd - 1, position + (range - 1));

            for (int i = end; i >= start; i--) {  // Scan from right to left
                if (bulbs.get(i) == 1) {
                    bestBulb = i;
                    break;
                }
            }

            // If no valid bulb is found, it's impossible to light the corridor
            if (bestBulb == -1) return -1;

            // Use this bulb and move to the next uncovered section
            bulbsUsed++;
            position = bestBulb + range;  // Move beyond the range of this bulb
        }

        return bulbsUsed;
    }
}
