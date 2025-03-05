package DailyBytes.StringPackage;

/**
 * This class contains a method to determine if a vacuum cleaner, based on a series of movements, returns to its original position.
 * 
 * Algorithm:
 * - Use horizontal and vertical counters to track the movements.
 * - Check if the final position is the original position (0, 0).
 * - Time Complexity: O(n)
 * - Space Complexity: O(1)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/robot-return-to-origin/
 */
public class VacuumCleaner {

    public static void main(String[] args) {
        System.out.println("Vacuum cleaner returns to original position? " + isOriginalPosition("LR"));
        System.out.println("Vacuum cleaner returns to original position? " + isOriginalPosition("URURD"));
        System.out.println("Vacuum cleaner returns to original position? " + isOriginalPosition("RUULLDRD"));
    }

    /**
     * Determines if the vacuum cleaner returns to its original position based on a series of movements.
     * @param route Series of movements.
     * @return True if the vacuum cleaner returns to the original position, false otherwise.
     */
    public static boolean isOriginalPosition(String route) {
        int horizontal = 0;
        int vertical = 0;

        for (char c : route.toCharArray()) {
            switch (c) {
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
                    break;
            }
        }
        return horizontal == 0 && vertical == 0;
    }
}
