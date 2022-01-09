package Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * https://leetcode.com/problems/maximum-employees-to-be-invited-to-a-meeting/
 */
public class MaxInvitations {

    public static void main(String[] args) {
        int[] invitations =  {1, 2, 0};
        System.out.println(new MaxInvitations().maximumInvitations(invitations));

    }

    public int maximumInvitations(int[] favorite) {
        int size = favorite.length;
        boolean[] visited = new boolean[size];
        // topological sort which picks out acyclic part.
        int[] inDegree = new int[size];
        for (int i = 0; i < size; ++i) {
            int neighbor = favorite[i]; // i -> favorite[i].
            ++inDegree[neighbor];
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < size; ++i) {
            if (inDegree[i] == 0) {
                visited[i] = true;
                queue.offer(i);
            }
        }

        int[] longestPath = new int[size]; // longestPath[i] is the longest path leading to i exclusively.
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            int neighbor = favorite[curr];
            longestPath[neighbor] = Math.max(longestPath[neighbor], longestPath[curr] + 1);
            if (--inDegree[neighbor] == 0) {
                visited[neighbor] = true;
                queue.offer(neighbor);
            }
        }
        // now not visited nodes are all loops. check each loop's length.
        int result1 = 0; // loops of length > 2.
        int result2 = 0; // loops of length 2 and paths leading to both nodes.
        for (int i = 0; i < size; ++i) {
            if (!visited[i]) {
                int curr = i;
                int loopSize = 0;

                while(!visited[curr]) {
                    visited[curr] = true;
                    curr = favorite[curr];
                    loopSize++;
                }

                if (loopSize == 2) {
                    // 2 nodes + 1 route for each node.
                    // There can be multiple combinations of 2 nodes so use result2 +=
                    result2 += 2 + longestPath[i] + longestPath[favorite[i]];
                } else {
                    result1 = Math.max(result1, loopSize);
                }
            }
        }
        return Math.max(result1, result2);
    }
}
