package dailybytes.string;

/**
 * ✅ Problem: Robot Return to Origin
 *
 * Given a string `route` representing moves of a robot (or vacuum cleaner),
 * determine if it returns to its original starting point `(0, 0)`.
 *
 * Valid moves:
 *  - 'L': move left
 *  - 'R': move right
 *  - 'U': move up
 *  - 'D': move down
 *
 * 🔗 Leetcode: https://leetcode.com/problems/robot-return-to-origin/
 *
 * 🧠 Example:
 * Input:  "LR"
 * Output: true  → L, then R (back to origin)
 *
 * Input:  "URURD"
 * Output: false
 *
 * 🔍 Follow-up:
 * 1. What if invalid characters exist? ➤ You can skip or throw an error.
 * 2. What if moves are diagonal or 3D? ➤ Need vector-based position tracking.
 * 3. Can you extend to return path length? ➤ Track manhattan distance.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class VacuumCleaner {

    public static void main(String[] args) {
        System.out.println("Vacuum cleaner returns to origin? " + didReturnToOrigin("LR"));
        System.out.println("Vacuum cleaner returns to origin? " + didReturnToOrigin("URURD"));
        System.out.println("Vacuum cleaner returns to origin? " + didReturnToOrigin("RUULLDRD"));
    }

    /**
     * ✅ Determines if robot returns to origin after a series of movements.
     *
     * Time Complexity: O(n) — one pass through the input
     * Space Complexity: O(1) — only counters used
     *
     * @param route Movement sequence (composed of 'L', 'R', 'U', 'D')
     * @return true if final position is origin (0, 0), false otherwise
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