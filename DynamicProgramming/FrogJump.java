package DynamicProgramming;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Given stones array containing position of stones in a river(ascending order)
 * Find if from can jump from starting position to end.
 * If the frog's last jump was k units, its next jump must be either k - 1, k, or k + 1 units.
 * The frog can only jump in the forward direction.
 * First jump is always 1.
 */
public class FrogJump {
    public static void main(String[] args) {
        int[] stones = {0,1,3,5,6,8,12,17};
        System.out.println("Can frog cross the river? " + new FrogJump().canCross(stones));
    }

    public boolean canCross(int[] stones) {

        for (int i = 3; i < stones.length; i++) {
            if (stones[i] > stones[i - 1] * 2) {
                return false;
            }
        }

        Stack<Integer> positions = new Stack<>();
        Stack<Integer> jumps = new Stack<>();
        positions.add(0);
        jumps.add(0);

        Set<Integer> stonePositions = new HashSet<>();
        for (int stone : stones) {
            stonePositions.add(stone);
        }
        Set<String> visited = new HashSet<>();
        visited.add("0-0"); // position,jump

        int lastStone = stones[stones.length - 1];

        while (!positions.isEmpty()) {
            int currentPosition = positions.pop();
            int currentJump = jumps.pop();

            for (int nextJump = currentJump - 1; nextJump <= currentJump + 1; nextJump++) {
                if (nextJump <= 0) continue;

                int nextPosition = currentPosition + nextJump;

                if (nextPosition == lastStone) {
                    return true;
                }
                String key = nextPosition + "-" + nextJump;
                if (stonePositions.contains(nextPosition) && !visited.contains(key)) {
                    visited.add(key);
                    positions.push(nextPosition);
                    jumps.push(nextJump);
                }
            }
        }
        return false;
    }


}
