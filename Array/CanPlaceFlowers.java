package Array;

/**
 * Can Place Flowers
 * 
 * Problem: Given a flowerbed (0=empty, 1=flower) and integer n, determine if n new flowers
 * can be planted without violating the no-adjacent-flowers rule.
 * 
 * Example: flowerbed = [1,0,0,0,1], n = 1 -> Output: true
 * Can plant one flower at index 2: [1,0,1,0,1]
 * 
 * LeetCode: https://leetcode.com/problems/can-place-flowers
 * 
 * Follow-up Questions:
 * - How to find maximum flowers that can be planted? (Count all valid positions)
 * - What if flowers can be adjacent with distance >= 2? (Modify checking condition)
 * - How to handle circular flowerbed? (Check wraparound conditions)
 */
public class CanPlaceFlowers {

    /**
     * Determines if n flowers can be planted without adjacent placement.
     * 
     * Algorithm:
     * 1. Iterate through each position in flowerbed
     * 2. For each empty spot, check if neighbors are empty or out of bounds
     * 3. If valid position found, plant flower and decrement n
     * 4. Early return true if n becomes 0
     * 5. Return whether all flowers were successfully placed
     * 
     * Time Complexity: O(m) where m is length of flowerbed
     * Space Complexity: O(1) - modify input array in place
     * 
     * @param flowerbed array representing flower positions
     * @param n number of flowers to plant
     * @return true if n flowers can be planted
     */
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        int length = flowerbed.length;

        for (int i = 0; i < length && n > 0; i++) {
            // Check if current position is empty
            if (flowerbed[i] == 0) {
                // Check left neighbor (empty or out of bounds)
                boolean leftEmpty = (i == 0) || (flowerbed[i - 1] == 0);
                // Check right neighbor (empty or out of bounds)
                boolean rightEmpty = (i == length - 1) || (flowerbed[i + 1] == 0);

                // If both neighbors are empty, plant flower
                if (leftEmpty && rightEmpty) {
                    flowerbed[i] = 1;
                    n--;
                }
            }
        }

        return n == 0;
    }

    /**
     * Alternative approach without modifying input array
     * Time Complexity: O(m), Space Complexity: O(1)
     */
    public boolean canPlaceFlowersNoModify(int[] flowerbed, int n) {
        int length = flowerbed.length;
        int planted = 0;

        for (int i = 0; i < length && planted < n; i++) {
            if (flowerbed[i] == 0) {
                boolean leftEmpty = (i == 0) || (flowerbed[i - 1] == 0);
                boolean rightEmpty = (i == length - 1) || (flowerbed[i + 1] == 0);

                if (leftEmpty && rightEmpty) {
                    planted++;
                    // Skip next position since we can't plant adjacent
                    i++;
                }
            }
        }

        return planted >= n;
    }
}
