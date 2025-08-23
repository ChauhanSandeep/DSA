package Array;

import java.util.Arrays;


/**
 * Maximize Distance To Closest Person
 *
 * Problem: Find seat that maximizes distance to closest person. Array represents seats
 * where 1 = occupied, 0 = empty. Return index of optimal empty seat.
 *
 * Example: seats = [1,0,0,0,1,0,1] -> Output: 2
 * Sitting at index 2 gives distance 2 to nearest person (at indices 0 and 4).
 *
 * LeetCode: https://leetcode.com/problems/maximize-distance-to-closest-person
 *
 * Follow-up Questions:
 * - What if multiple seats have same max distance? (Return any one of them)
 * - How to handle all seats occupied? (Problem guarantees at least one empty seat)
 * - Can we solve without finding all distances? (Yes, track segments between occupied seats)
 */
public class MaximizeDistanceToClosestPerson {

    /**
     * Finds seat that maximizes distance to closest person.
     *
     * Algorithm:
     * 1. For each empty seat, calculate distance to closest occupied seat
     * 2. Track seat with maximum such distance
     * 3. Handle edge cases: seats at beginning/end of array
     *
     * Time Complexity: O(n²) where n is number of seats
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param seats array where 1=occupied, 0=empty
     * @return index of seat with maximum distance to closest person
     */
    public int maxDistToClosest(int[] seats) {
        int n = seats.length;
        int maxDistance = 0;
        int bestSeat = 0;

        for (int i = 0; i < n; i++) {
            if (seats[i] == 0) { // Empty seat
                int closestDistance = getClosestDistance(seats, i);
                if (closestDistance > maxDistance) {
                    maxDistance = closestDistance;
                    bestSeat = i;
                }
            }
        }

        return bestSeat;
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

    /**
     * Optimized approach using segment analysis
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int maxDistToClosestOptimized(int[] seats) {
        int n = seats.length;
        int maxDistance = 0;
        int bestSeat = 0;

        int lastOccupied = -1;

        for (int i = 0; i < n; i++) {
            if (seats[i] == 1) {
                if (lastOccupied == -1) {
                    // Handle segment at beginning
                    if (i > maxDistance) {
                        maxDistance = i;
                        bestSeat = 0;
                    }
                } else {
                    // Handle segment between two occupied seats
                    int segmentLength = i - lastOccupied - 1;
                    if (segmentLength > 0) {
                        int distance = (segmentLength + 1) / 2;
                        if (distance > maxDistance) {
                            maxDistance = distance;
                            bestSeat = lastOccupied + distance;
                        }
                    }
                }
                lastOccupied = i;
            }
        }

        // Handle segment at the end
        if (lastOccupied != -1 && n - 1 - lastOccupied > maxDistance) {
            maxDistance = n - 1 - lastOccupied;
            bestSeat = n - 1;
        }

        return bestSeat;
    }

    /**
     * Two-pass approach for clarity
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int maxDistToClosestTwoPass(int[] seats) {
        int n = seats.length;
        int[] leftDistance = new int[n];
        int[] rightDistance = new int[n];

        Arrays.fill(leftDistance, Integer.MAX_VALUE);
        Arrays.fill(rightDistance, Integer.MAX_VALUE);

        // Calculate distance to closest person on left
        for (int i = 0; i < n; i++) {
            if (seats[i] == 1) {
                leftDistance[i] = 0;
            } else if (i > 0) {
                leftDistance[i] = leftDistance[i-1] + 1;
            }
        }

        // Calculate distance to closest person on right
        for (int i = n - 1; i >= 0; i--) {
            if (seats[i] == 1) {
                rightDistance[i] = 0;
            } else if (i < n - 1) {
                rightDistance[i] = rightDistance[i+1] + 1;
            }
        }

        // Find seat with maximum distance to closest person
        int maxDistance = 0;
        int bestSeat = 0;

        for (int i = 0; i < n; i++) {
            if (seats[i] == 0) {
                int distance = Math.min(leftDistance[i], rightDistance[i]);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    bestSeat = i;
                }
            }
        }

        return bestSeat;
    }
}
