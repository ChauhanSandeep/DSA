package arrays.greedy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Problem: Minimum Lights to Activate
 *
 * Given a list of integers representing light bulbs in a corridor where 1 indicates
 * a functional bulb and 0 indicates a non-functional bulb, find the minimum number
 * of functional bulbs needed to light the entire corridor. Each bulb at index i
 * can light up positions in the range [i - (range-1), i + (range-1)].
 *
 * Example:
 * Input: bulbs = [0,0,0,1,0], range = 3
 * Output: 1
 * Explanation: The bulb at index 3 can light positions [1, 5], covering the entire corridor.
 *
 * Constraints:
 * - 1 <= bulbs.length <= 10^5
 * - 0 <= bulbs[i] <= 1
 * - 1 <= range <= bulbs.length
 *
 * InterviewBit Problem: https://www.interviewbit.com/problems/minimum-lights-to-activate/
 *
 * Follow-up Questions:
 *
 * 1. What if some bulbs have different ranges based on their type?
 *    Answer: Store range with each bulb. When calculating coverage, use the specific
 *    bulb's range instead of the universal range parameter.
 *
 * 2. How would you handle if bulbs can be turned ON or OFF (not fixed)?
 *    Answer: Use dynamic programming where state is (position, bulbsLeft). For each
 *    position, try turning on different bulbs and track minimum needed.
 *
 * 3. What if you need to minimize power consumption (different bulbs use different power)?
 *    Answer: Modify greedy approach to select bulb with minimum power among those
 *    covering current position, instead of rightmost. This requires sorting by power.
 *
 * 4. Can you extend this to 2D grid where each bulb covers a square area?
 *    Answer: Similar greedy approach but process row by row. For each row, find minimum
 *    bulbs to cover it, considering coverage from bulbs above/below within range.
 *
 * 5. What if there's a cost to activate each bulb?
 *    Answer: Use greedy with modified selection criteria. Choose bulb that covers
 *    current position with minimum cost-per-coverage ratio among valid bulbs.
 */
public class MinLights {

    public static void main(String[] args) {
        List<Integer> lights = Stream.of(0, 0, 0, 1, 0).collect(Collectors.toList());
        int result = new MinLights().findMinLights(lights, 3);
        System.out.println("Minimum lights needed: " + result);
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
     * 1. Initialize `rightMostCovered = 0` (tracks the farthest lit position).
     * 2. Initialize `leftMostCovered = 0` (tracks the start of the next uncovered section).
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
        int rightMostCovered = 0; // The farthest lit position
        int leftMostCovered = 0;  // The starting point of the next uncovered area
        int bulbsUsed = 0;

        while (rightMostCovered < corridorEnd) {
            int bestBulb = -1;

            // Find the best bulb that can extend coverage
            for (int i = 0; i < bulbs.size(); i++) {
                if (bulbs.get(i) == 1) {
                    int lowerRange = i - range + 1;
                    int upperRange = i + range - 1;

                    if (lowerRange <= leftMostCovered && upperRange > rightMostCovered) {
                        bestBulb = i;
                        rightMostCovered = upperRange;
                    }
                }
            }

            // No bulb found to extend coverage
            if (bestBulb == -1) return -1;

            bulbsUsed++;
            leftMostCovered = rightMostCovered; // Move the coverage boundary forward
        }

        return bulbsUsed;
    }

    /**
     * Optimized approach using a greedy strategy.
     *
     * **Approach:**
     * - We start from the leftmost uncovered position (`position = 0`).
     * - At each step, we find the rightmost bulb that can cover this position.
     * - If no such bulb exists, return -1 (lighting the corridor is impossible).
     * - Otherwise, use that bulb, update the covered position, and continue.
     *
     * Steps:
     * 1. Initialize position = 0 (tracks the leftmost uncovered section).
     * 2. While position < bulbs.size():
     *    - Find the rightmost bulb in the range [position - (range-1), position + (range-1)].
     *    - If found, update position to the next uncovered section.
     *    - If not found, return -1.
     * 3. Repeat until the entire corridor is lit.
     *
     * **Time Complexity:** `O(n)`
     * - We iterate over the list at most once.
     * - Each bulb is considered at most once.
     *
     * **Space Complexity:** `O(1)`
     * - No extra space is used apart from a few integer variables.
     *
     * @param bulbs List representing bulbs (0 -> faulty, 1 -> working)
     * @param range Range covered by each bulb
     * @return Minimum bulbs required to light the corridor, or -1 if it's not possible
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
