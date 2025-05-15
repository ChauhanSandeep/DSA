package Graph.FloydWarshal;

import java.util.ArrayList;
import java.util.List;


/**
 * Leetcode Problem: 1462. Course Schedule IV
 *
 * Problem Statement:
 * There are n courses labeled from 0 to n-1. You are given an array of prerequisite pairs
 * and a list of query pairs. Each prerequisite pair [a, b] means course a is a prerequisite of course b.
 *
 * Your task is to determine for each query [u, v] whether u is a prerequisite of v (directly or indirectly).
 *
 * Link: https://leetcode.com/problems/course-schedule-iv/
 *
 * Example:
 * Input: n = 3, prerequisites = [[0,1],[1,2]], queries = [[0,2],[1,2],[2,0]]
 * Output: [true,true,false]
 */
public class CourseScheduleIV {

    /**
     * Uses Floyd-Warshall to compute transitive closure over course prerequisites.
     *
     * Intuition:
     * - Model the problem as a directed graph: edges from prerequisite → course.
     * - Compute whether a course can reach another using transitive closure (Floyd-Warshall).
     * - For each query, simply lookup reachability.
     *
     * Time Complexity: O(n^3 + q), where n is number of courses, q is number of queries
     * Space Complexity: O(n^2) for reachability matrix
     *
     * @param n Number of courses
     * @param prerequisites Array of direct prerequisites [u, v]
     * @param queries List of [u, v] pairs to check if u is a prerequisite of v
     * @return List of booleans representing the answers to each query
     */
    public List<Boolean> checkIfPrerequisite(int n, int[][] prerequisites, int[][] queries) {
        boolean[][] reachable = new boolean[n][n];

        // Set direct prerequisites
        for (int[] pair : prerequisites) {
            int from = pair[0], to = pair[1];
            reachable[from][to] = true;
        }

        // Floyd-Warshall: compute transitive prerequisites
        for (int via = 0; via < n; via++) {           // intermediate course
            for (int from = 0; from < n; from++) {       // from course
                for (int to = 0; to < n; to++) {   // to course
                    if (reachable[from][via] && reachable[via][to]) {
                        reachable[from][to] = true;
                    }
                }
            }
        }

        // Answer each query using the precomputed reachability matrix
        List<Boolean> result = new ArrayList<>();
        for (int[] query : queries) {
            int from = query[0], to = query[1];
            result.add(reachable[from][to]);
        }

        return result;
    }
}