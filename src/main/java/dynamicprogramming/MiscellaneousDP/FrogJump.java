package dynamicprogramming.MiscellaneousDP;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Frog Jump
 *
 * A frog starts on stone 0 and must reach the last stone. If its last jump was k,
 * the next jump may be k - 1, k, or k + 1 units, and the first jump must be 1.
 * Return whether the frog can land on the final stone.
 *
 * Leetcode: https://leetcode.com/problems/frog-jump/
 * Rating:   acceptance 47.5% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic programming | Reachable states | DFS with memoized states
 *
 * Example:
 *   Input:  stones = [0,1,3,5,6,8,12,17]
 *   Output: true
 *   Why:    jumps 1,2,2,3,4,5 land exactly on the final stone at position 17.
 *
 * Follow-ups:
 *   1. Return one valid jump sequence?
 *      Store a parent state whenever a new (position, jump) state is discovered.
 *   2. Count how many ways can reach the last stone?
 *      Change visited into a DP count map keyed by (position, lastJump).
 *   3. What if backward jumps are allowed?
 *      Add direction or previous-position state and bound the search to avoid infinite loops.
 *
 * Related: Jump Game III (1306), Minimum Jumps to Reach Home (1654).
 */
public class FrogJump {

    public static void main(String[] args) {
        FrogJump solver = new FrogJump();
        int[][] inputs = {
            {0},
            {0, 1, 3, 5, 6, 8, 12, 17},
            {0, 2},
            {0, 1, 2, 3, 4, 8, 9, 11}
        };
        boolean[] expected = {true, true, false, false};

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.canCross(inputs[i]);
            System.out.printf("stones=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: a state is not just the current stone; it is the current stone and
     * the jump length used to get there. The same stone can be useful with one last
     * jump and useless with another, because the next three jump lengths depend on
     * it. We explore reachable states and remember every (position, lastJump) pair
     * so cycles and repeated paths do not redo work. If any state lands on the last
     * stone, the frog can cross.
     *
     * Algorithm:
     *   1. Accept a single starting stone and reject obviously unreachable gaps.
     *   2. Put all stones in a set and start a DFS stack from position 0 with jump 0.
     *   3. For each state, try jumps lastJump - 1, lastJump, and lastJump + 1.
     *   4. Return true when a jump reaches the last stone; otherwise avoid repeated states with visited.
     *
     * Time:  O(n^2) - there can be O(n^2) reachable (stone, lastJump) states in the worst case.
     * Space: O(n^2) - the visited set and stack may store those states.
     *
     * @param stones sorted stone positions
     * @return true if the frog can reach the last stone
     */
    public boolean canCross(int[] stones) {
        int length = stones.length;
        if (length <= 1) return true;

        // Early termination: If a stone is unreachable
        for (int i = 3; i < length; i++) {
            if (stones[i] > stones[i - 1] * 2) {
                return false;
            }
        }

        Set<Integer> stoneSet = new HashSet<>();
        for (int stone : stones) {
            stoneSet.add(stone);
        }

        Deque<int[]> dfsStack = new ArrayDeque<>();
        dfsStack.push(new int[]{0, 0}); // {currentPosition, lastJumpSize}

        Set<String> visited = new HashSet<>();

        int lastStone = stones[length - 1];

        while (!dfsStack.isEmpty()) {
            // SELECT : pop the current state from the stack
            int[] state = dfsStack.pop();
            int position = state[0];
            int lastJump = state[1];

            // MARK(*) : Check if we have already visited this state. If not, then mark it as visited and proceed.
            String stateKey = position + "-" + lastJump;
            if (visited.contains(stateKey)) {
                continue; // Already visited this state
            }
            visited.add(stateKey);

            // WORK : Try all possible jump sizes (k-1, k, k+1)
            for (int jump = lastJump - 1; jump <= lastJump + 1; jump++) {
                if (jump <= 0) continue; // Cannot make non-positive jumps

                int nextPosition = position + jump;
                if (nextPosition == lastStone) return true; // Reached last stone

                // ADD(*) : Add to queue, if the next position is a valid stone and not visited with this jump size
                if (stoneSet.contains(nextPosition) && !visited.contains(nextPosition + "-" + jump)) {
                    dfsStack.push(new int[]{nextPosition, jump});
                }
            }
        }
        return false;
    }
}
