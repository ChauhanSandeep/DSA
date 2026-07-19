package trees.bfs;

import java.util.*;

    /**
     * Intuition: each employee's receive time is its manager's receive time plus
     * the manager's informTime. A BFS from the head carries that elapsed time to
     * every subordinate and the answer is the maximum elapsed time seen.
     *
     * Algorithm:
     *   1. Build manager -> direct reports adjacency lists from manager.
     *   2. Start BFS with headID at time 0.
     *   3. For each employee, enqueue every report with time + informTime[empId].
     *   4. Return the largest time removed from the queue.
     *
     * Time:  O(n) - every employee and management edge is processed once.
     * Space: O(n) - adjacency lists and queue store employees.
     *
     * @param n number of employees
     * @param headID id of the root manager
     * @param manager direct manager for each employee
     * @param informTime minutes each employee needs to inform direct reports
     * @return minutes until all employees are informed
     */
public class TimeNeededToInformAllEmployees {

    public static void main(String[] args) {
        TimeNeededToInformAllEmployees solver = new TimeNeededToInformAllEmployees();
        int[] manager = {2, 2, -1, 2, 2, 2};
        int[] informTime = {0, 0, 1, 0, 0, 0};
        int[] singleManager = {-1};
        int[] singleInformTime = {0};

        System.out.printf("n=%d headID=%d manager=%s informTime=%s -> %d  expected=%d%n",
            6, 2, Arrays.toString(manager), Arrays.toString(informTime),
            solver.numOfMinutes(6, 2, manager, informTime), 1);
        System.out.printf("n=%d headID=%d manager=%s informTime=%s -> %d  expected=%d%n",
            1, 0, Arrays.toString(singleManager), Arrays.toString(singleInformTime),
            solver.numOfMinutes(1, 0, singleManager, singleInformTime), 0);
    }

    public int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {
        // Build the tree using adjacency list
        Map<Integer, List<Integer>> tree = new HashMap<>();

        for (int i = 0; i < n; i++) {
            if (manager[i] != -1) {
                tree.computeIfAbsent(manager[i], k -> new ArrayList<>()).add(i);
            }
        }

        // Use BFS to traverse the tree and calculate the total time
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{headID, 0}); // {employeeId, timeTaken}
        int maxTime = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int empId = current[0];
            int time = current[1];

            maxTime = Math.max(maxTime, time);

            // Add all subordinates to the queue with updated time
            if (tree.containsKey(empId)) {
                for (int subId : tree.get(empId)) {
                    queue.offer(new int[]{subId, time + informTime[empId]});
                }
            }
        }

        return maxTime;
    }
}
