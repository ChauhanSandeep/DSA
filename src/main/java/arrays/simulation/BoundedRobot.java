package arrays.simulation;

/**
 * Problem: Robot Bounded In Circle
 *
 * A robot starts at the origin facing north and repeatedly executes an instruction
 * string made of G, L, and R. Decide whether repeating that instruction string
 * forever keeps the robot within some circle instead of drifting away.
 *
 * Leetcode: https://leetcode.com/problems/robot-bounded-in-circle/
 * Rating:   1521 (zerotrac Elo, Q1, weekly-contest-136)
 * Pattern:  Simulation | Geometry | One-cycle direction check
 *
 * Example:
 *   Input:  "GGLLGG"
 *   Output: true
 *   Why:    after one cycle the robot is back at the origin, so repeating the
 *           same cycle cannot move it farther away.
 *
 * Follow-ups:
 *   1. What if instructions include variable step lengths?
 *      Apply the same position and direction simulation with parsed distances.
 *   2. What if the robot moves in 3D?
 *      Track orientation as one of the possible 3D basis directions or rotations.
 *   3. What if you need the smallest bounding radius?
 *      Simulate enough cycles to close the path and track the maximum distance from origin.
 */
public class BoundedRobot {
    public static void main(String[] args) {
        String[] inputs = {"GGLLGG", "GG", "GL"};
        boolean[] expected = {true, false, true};

        for (int i = 0; i < inputs.length; i++) {
            boolean got = isRobotBounded(inputs[i]);
            System.out.printf("instructions=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

    /**
     * Intuition: if one instruction cycle ends at the origin, the path is clearly
     * bounded because every cycle restarts from the same state. If it does not end
     * at the origin but the robot faces a different direction, repeating the same
     * instructions rotates the displacement; after at most four cycles those rotated
     * displacements cancel out. The only unbounded case is ending away from the
     * origin still facing north, because then every cycle adds the same displacement.
     *
     * Time:  O(n) - each instruction is interpreted once.
     * Space: O(1) - only position and direction are stored.
     *
     * @param instructions movement instructions containing G, L, and R
     * @return true if the repeated path is bounded, otherwise false
     */
    public static boolean isRobotBounded(String instructions) {
        int x = 0, y = 0; // Robot's position
        int direction = 0; // 0 = North, 1 = East, 2 = South, 3 = West

        for (char command : instructions.toCharArray()) {
            switch (command) {
                case 'L':
                    direction = (direction + 3) % 4; // Turn left (equivalent to -1 mod 4)
                    break;
                case 'R':
                    direction = (direction + 1) % 4; // Turn right
                    break;
                case 'G':
                    if (direction == 0) y++;       // Move North
                    else if (direction == 1) x++;  // Move East
                    else if (direction == 2) y--;  // Move South
                    else x--;                      // Move West
                    break;
            }
        }
        // Robot is bounded if it returns to origin or does not face North
        return (x == 0 && y == 0) || (direction != 0);
    }
}
