package dailybytes.string;

import java.util.Arrays;

/**
 * Problem: Robot Return to Origin
 *
 * Given a route made of L, R, U, and D moves, decide whether the robot finishes
 * at its starting coordinate. Horizontal moves must cancel out, and vertical
 * moves must cancel out independently.
 *
 * Leetcode: https://leetcode.com/problems/robot-return-to-origin/ (Easy)
 * Rating:   acceptance 78.2% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  String | Simulation | Coordinate balance
 *
 * Example:
 *   Input:  route = "LR"
 *   Output: true
 *   Why:    the left move is canceled by the right move, leaving the robot at
 *           the origin.
 *
 * Follow-ups:
 *   1. Allow diagonal or 3D moves?
 *      Replace the two counters with a vector of one counter per dimension.
 *   2. Return the final displacement instead of a boolean?
 *      Return the horizontal and vertical counters after the scan.
 *   3. Validate or recover from invalid commands?
 *      Either throw immediately, as this code does, or skip commands by policy.
 *   4. Find whether any prefix returns to origin?
 *      Check the counters after every move and record the earliest zero state.
 *
 * Related: Walking Robot Simulation (874), Robot Bounded In Circle (1041).
 */
public class VacuumCleaner {

    public static void main(String[] args) {
        String[] inputs = { "LR", "URURD", "RUULLDRD", "" };
        boolean[] expected = { true, false, true, true };

        for (int i = 0; i < inputs.length; i++) {
            boolean output = didReturnToOrigin(inputs[i]);
            System.out.printf("route=%s -> %b  expected=%b%n", inputs[i], output, expected[i]);
        }
    }

    /**
     * Intuition: returning to the origin means the net movement on each axis is
     * zero. Count left and right moves against horizontal, count up and down
     * moves against vertical, then both counters must finish at zero.
     *
     * Algorithm:
     *   1. Return true for a null or empty route.
     *   2. Scan each direction and update horizontal or vertical.
     *   3. Throw an exception for any invalid direction character.
     *   4. Return true only when both counters are zero.
     *
     * Time:  O(n) - each route character is processed once.
     * Space: O(1) - only two movement counters are stored.
     *
     * @param route movement sequence using L, R, U, and D
     * @return true when the route ends at the origin
     */
    public static boolean didReturnToOrigin(String route) {
        if (route == null || route.isEmpty()) {
            return true;
        }

        int horizontal = 0; // Horizontal movement counter
        int vertical = 0; // Vertical movement counter

        for (char direction : route.toCharArray()) {
            switch (direction) {
                case 'L':
                    horizontal--;
                    break;
                case 'R':
                    horizontal++;
                    break;
                case 'U':
                    vertical++;
                    break;
                case 'D':
                    vertical--;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid direction character: " + direction);
            }
        }

        return horizontal == 0 && vertical == 0;
    }
}