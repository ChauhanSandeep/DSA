package arrays;

import java.util.Arrays;


/**
 * Problem: Maximize Distance to Closest Person
 *
 * In a row of seats, 1 means occupied and 0 means empty. Find an empty seat that
 * maximizes the distance to the nearest occupied seat. Return that maximum nearest
 * occupied-seat distance.
 *
 * Leetcode: https://leetcode.com/problems/maximize-distance-to-closest-person/ (Medium)
 * Rating:   1383 (zerotrac Elo)
 * Pattern:  Array | Distance scan | Best empty seat
 *
 * Example:
 *   Input:  seats = [1,0,0,0,1,0,1]
 *   Output: 2
 *   Why:    sitting at index 2 is two seats away from both occupied neighbours, which is the
 *           largest nearest-person distance in this row.
 *
 * Follow-ups:
 *   1. Return the best seat index instead of the maximum distance?
 *      Track bestSeat whenever maxDistance improves.
 *   2. Seat k new people instead of one?
 *      Use a priority queue of empty segments and repeatedly split the largest effective segment.
 *   3. Handle a circular row of seats?
 *      Merge the leading and trailing zero runs because the ends are neighbours.
 *
 * Related: Exam Room (855), Can Place Flowers (605).
 */
public class MaximizeDistanceToClosestPerson {

    public static void main(String[] args) {
        MaximizeDistanceToClosestPerson solver = new MaximizeDistanceToClosestPerson();

        int[][] inputs = { {1, 0, 0, 0, 1, 0, 1}, {1, 0, 0, 0}, {0, 1} };
        int[] expected = { 2, 3, 1 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.maxDistToClosest(inputs[i]);
            System.out.printf("seats=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Two-pass approach for clarity (Optimized)
     *
     * Steps:
     * 1. First pass: Calculate distance to closest occupied seat on left
     * 2. Second pass: Calculate distance to closest occupied seat on right
     * 3. For each empty seat, distance to closest person = min(leftDistance, rightDistance)
     * 4. Track the maximum such distance
     *
     * Time Complexity: O(n),
     * Space Complexity: O(n)
     */
    public int maxDistToClosestTwoPass(int[] seats) {
        int size = seats.length;
        int[] leftDistance = new int[size]; // Distance to closest person on left
        int[] rightDistance = new int[size]; // Distance to closest person on right

        Arrays.fill(leftDistance, size);
        Arrays.fill(rightDistance, size);

        // Calculate distance to closest person on left
        for (int i = 0; i < size; i++) {
            if (seats[i] == 1) {
                leftDistance[i] = 0;
            } else if (i > 0) {
                leftDistance[i] = leftDistance[i-1] + 1;
            }
        }

        // Calculate distance to closest person on right
        for (int i = size - 1; i >= 0; i--) {
            if (seats[i] == 1) {
                rightDistance[i] = 0;
            } else if (i < size - 1) {
                rightDistance[i] = rightDistance[i+1] + 1;
            }
        }

        // Find maximum distance to closest person
        int maxDistance = 0;

        for (int i = 0; i < size; i++) {
            if (seats[i] == 0) {
                int distance = Math.min(leftDistance[i], rightDistance[i]);
                if (distance > maxDistance) {
                    maxDistance = distance;
                }
            }
        }

        return maxDistance;
    }

    /**
     * Intuition: for each empty seat, measure the nearest occupied seat on the left
     * and right. The smaller of those distances is how far this seat is from the
     * closest person, and the answer is the largest such distance.
     *
     * Algorithm:
     *   1. Scan every seat index.
     *   2. For each empty seat, call getClosestDistance to measure the nearest occupied seat.
     *   3. If that distance beats maxDistance, update maxDistance.
     *   4. Return maxDistance.
     *
     * Time:  O(n^2) - each empty seat may scan left and right across the row.
     * Space: O(1) - only the best distance is stored.
     *
     * @param seats row where 1 is occupied and 0 is empty
     * @return maximum distance from an empty seat to the closest occupied seat
     */
    public int maxDistToClosest(int[] seats) {
        int n = seats.length;
        int maxDistance = 0;

        for (int i = 0; i < n; i++) {
            if (seats[i] == 0) { // Empty seat
                int closestDistance = getClosestDistance(seats, i);
                if (closestDistance > maxDistance) {
                    maxDistance = closestDistance;
                }
            }
        }

        return maxDistance;
    }

    // Helper method to find distance to closest occupied seat
    private int getClosestDistance(int[] seats, int index) {
        int leftDistance = Integer.MAX_VALUE;
        int rightDistance = Integer.MAX_VALUE;

        // Find closest occupied seat to the left
        for (int i = index - 1; i >= 0; i--) {
            if (seats[i] == 1) {
                leftDistance = index - i;
                break;
            }
        }

        // Find closest occupied seat to the right
        for (int i = index + 1; i < seats.length; i++) {
            if (seats[i] == 1) {
                rightDistance = i - index;
                break;
            }
        }

        return Math.min(leftDistance, rightDistance);
    }
}
