package graphs;

import java.util.HashSet;
import java.util.Set;

/**
 * // This is the robot's control interface.
 * // You should not implement it, or speculate about its implementation
 * interface Robot {
 *     // Returns true if the cell in front is open and robot moves into the cell.
 *     // Returns false if the cell in front is blocked and robot stays in the current cell.
 *     public boolean move();
 *
 *     // Robot will stay in the same cell after calling turnLeft/turnRight.
 *     // Each turn will be 90 degrees.
 *     public void turnLeft();
 *     public void turnRight();
 *
 *     // Clean the current cell.
 *     public void clean();
 * }
 */

public class RobotRoomCleaner {
    // Directions: up, right, down, left (clockwise order)
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    /**
     * Cleans the entire room using the robot's API.
     *
     * @param robot The robot instance
     */
    public void cleanRoom(Robot robot) {
        // Use a set to keep track of cleaned cells
        Set<String> cleaned = new HashSet<>();
        // Start from position (0, 0) facing up (direction 0)
        backtrack(robot, 0, 0, 0, cleaned);
    }

    /**
     * Backtracking helper method to clean the room.
     *
     * @param robot The robot instance
     * @param row Current row position
     * @param col Current column position
     * @param direction Current direction (0=up, 1=right, 2=down, 3=left)
     * @param cleaned Set of cleaned positions
     */
    private void backtrack(Robot robot, int row, int col, int direction, Set<String> cleaned) {
        // Clean the current cell
        robot.clean();
        cleaned.add(row + "," + col);

        // Explore all 4 directions
        for (int i = 0; i < 4; i++) {
            int newDir = (direction + i) % 4;
            int newRow = row + DIRECTIONS[newDir][0];
            int newCol = col + DIRECTIONS[newDir][1];
            String position = newRow + "," + newCol;

            if (!cleaned.contains(position) && robot.move()) {
                // Move to the new cell and continue cleaning
                backtrack(robot, newRow, newCol, newDir, cleaned);
                // Backtrack: move back to the original position and direction
                goBack(robot);
            }

            // Turn right to face the next direction
            robot.turnRight();
        }
    }

    /**
     * Moves the robot back to the previous cell and resets its direction.
     */
    private void goBack(Robot robot) {
        // Turn 180 degrees to face the opposite direction
        robot.turnRight();
        robot.turnRight();
        // Move back
        robot.move();
        // Turn 180 degrees again to face the original direction
        robot.turnRight();
        robot.turnRight();
    }

    /**
     * Alternative approach with explicit direction handling.
     * This version might be more readable for some developers.
     */
    public void cleanRoomAlternative(Robot robot) {
        // Use a set to keep track of cleaned cells
        Set<String> cleaned = new HashSet<>();
        // Start from position (0, 0) facing up (direction 0)
        cleanRoomHelper(robot, 0, 0, 0, cleaned);
    }

    private void cleanRoomHelper(Robot robot, int row, int col, int dir, Set<String> cleaned) {
        // Clean current cell
        String position = row + "," + col;
        if (cleaned.contains(position)) {
            return;
        }

        robot.clean();
        cleaned.add(position);

        // Try all 4 directions
        for (int i = 0; i < 4; i++) {
            // Calculate new direction and position
            int newDir = (dir + i) % 4;
            int newRow = row + DIRECTIONS[newDir][0];
            int newCol = col + DIRECTIONS[newDir][1];

            // Face the robot in the new direction
            if (i > 0) {
                robot.turnRight(); // Turn right to face the next direction
            }

            if (!cleaned.contains(newRow + "," + newCol) && robot.move()) {
                cleanRoomHelper(robot, newRow, newCol, newDir, cleaned);
                // After returning from recursion, move back
                moveBack(robot);
            }
        }

        // Turn right to face the original direction
        robot.turnRight();
        robot.turnRight();
    }

    /**
     * Moves the robot back to the previous cell.
     */
    private void moveBack(Robot robot) {
        robot.turnRight();
        robot.turnRight(); // Turn 180 degrees
        robot.move();      // Move back
        robot.turnRight();
        robot.turnRight(); // Turn back to original direction
    }

    private class Robot {
        public boolean move() { return true; }
        public void turnLeft() {}
        public void turnRight() {}
        public void clean() {}
    }
}