package graphs;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * Problem: Robot Room Cleaner
 *
 * A robot starts in an unknown room and exposes only four operations: move,
 * turnLeft, turnRight, and clean. Clean every reachable open cell without knowing
 * the room layout in advance.
 *
 * Leetcode: https://leetcode.com/problems/robot-room-cleaner/
 * Rating:   acceptance 78.0% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Backtracking DFS | Unknown grid exploration
 *
 * Example:
 *   Input:  hidden room with start inside a small open area and some blocked cells
 *   Output: every reachable open cell is cleaned
 *   Why:    DFS tries each direction from every discovered cell, records relative
 *           coordinates, and physically returns to the parent cell after exploring a branch.
 *
 * Follow-ups:
 *   1. Minimize robot movement while cleaning?
 *      This becomes an online exploration problem; exact optimality is not available without the map.
 *   2. The robot has limited battery?
 *      Track distance back to the start and stop exploring paths that cannot safely return.
 *   3. Multiple robots clean together?
 *      Share the visited set and assign frontier cells, but avoid collisions with coordination.
 *
 * Related: Number of Islands (200), Shortest Path in a Grid with Obstacles Elimination (1293).
 */
public class RobotRoomCleaner {

    public static void main(String[] args) {
        RobotRoomCleaner solver = new RobotRoomCleaner();
        class MockRobot extends Robot {
            private final int[][] room;
            private final Set<String> cleanedCells = new HashSet<>();
            private int row;
            private int col;
            private int direction;
            MockRobot(int[][] room) { solver.super(); this.room = room; }
            @Override public boolean move() {
                int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
                int nextRow = row + dirs[direction][0];
                int nextCol = col + dirs[direction][1];
                if (nextRow < 0 || nextCol < 0 || nextRow >= room.length || nextCol >= room[0].length || room[nextRow][nextCol] == 0) return false;
                row = nextRow; col = nextCol; return true;
            }
            @Override public void turnRight() { direction = (direction + 1) % 4; }
            @Override public void clean() { cleanedCells.add(row + "," + col); }
            int cleanedCount() { return cleanedCells.size(); }
        }
        int[][][] rooms = {{{1, 1}, {0, 1}}, {{1}}};
        int[] expected = {3, 1};
        for (int i = 0; i < rooms.length; i++) {
            MockRobot robot = new MockRobot(rooms[i]);
            solver.cleanRoom(robot);
            System.out.printf("room=%s -> %d  expected=%d%n", Arrays.deepToString(rooms[i]), robot.cleanedCount(), expected[i]);
        }
    }
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
