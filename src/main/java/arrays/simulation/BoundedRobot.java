package arrays.simulation;

/**
 * Determines if a robot remains bounded in a circular path based on given movement instructions.
 *
 * Approach:
 * - The robot starts at (0,0) facing North.
 * - It moves according to the instructions:
 *   - 'G': Move forward by one unit.
 *   - 'L': Turn left (90 degrees counterclockwise).
 *   - 'R': Turn right (90 degrees clockwise).
 * - If after one cycle, the robot either returns to the origin or does not face North, it is bounded.
 * - Runs in **O(N) time complexity**, where N is the length of instructions.
 * - Space complexity is **O(1)** as only a few variables are used.
 *
 * LeetCode Problem: https://leetcode.com/problems/robot-bounded-in-circle/
 * LeetCode Contest Rating: 1521
 */
public class BoundedRobot {
    public static void main(String[] args) {
        String instructions = "GGLLGG";
        System.out.println("Is robot bounded? " + isRobotBounded(instructions));
        instructions = "GG";
        System.out.println("Is robot bounded? " + isRobotBounded(instructions));
        instructions = "GL";
        System.out.println("Is robot bounded? " + isRobotBounded(instructions));
    }

    /**
     * Checks if the robot remains in a bounded circle after executing the given instructions.
     *
     * @param instructions The movement instructions for the robot.
     * @return True if the robot is bounded; otherwise, false.
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
