package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class CourseSchedule2 {

    public static void main(String[] args) {
        int numCourses = 4;
        int[][] prerequisites = {
                {1, 0},
                {2, 0},
                {3, 1},
                {3, 2}
        };
        int[] order = new CourseSchedule2().findOrder(numCourses, prerequisites);
        System.out.println(Arrays.toString(order));
    }

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<Integer>());
        }

        for (int[] relation : prerequisites) {
            adj.get(relation[1]).add(relation[0]);
        }
        System.out.println(adj);

        Stack<Integer> stack = new Stack<>();
        Boolean[] cycle = new Boolean[numCourses];

        for (int i = 0; i < numCourses; i++) {
            if (findTopoSort(i, cycle, adj, stack)) {
                return new int[]{};
            }
        }

        int[] result = new int[stack.size()];
        int index = 0;
        while (!stack.isEmpty()) {
            result[index++] = stack.pop();
        }
        return result;
    }

    public boolean findTopoSort(int node, Boolean[] cycle, ArrayList<ArrayList<Integer>> adj, Stack<Integer> stack) {
        if (cycle[node] != null) return cycle[node];

        cycle[node] = true;

        for (int i : adj.get(node)) {
            if (findTopoSort(i, cycle, adj, stack)) {
                cycle[node] = true;
                return true;
            }
        }
        stack.push(node);
        cycle[node] = false;
        return false;
    }

}
