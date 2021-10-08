package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * https://leetcode.com/problems/bus-routes/
 */
public class MinBuses {

    public static void main(String[] args) {
        int[][] routes = {
                {7, 12},
                {4, 5, 15},
                {6},
                {15, 19},
                {19, 12, 13}};
        int source = 4;
        int destination = 13;
        int numOfBuses = new MinBuses().numBusesToDestination(routes, source, destination);
        System.out.println("Min buses required to go from source to destination are " + numOfBuses);
    }

    public int numBusesToDestination(int[][] routes, int source, int destination) {
        if (source == destination) return 0;

        Map<Integer, List<Integer>> map = new HashMap<>(); // <stop, List<route index>>
        for (int i = 0; i < routes.length; i++) {
            for (int j = 0; j < routes[i].length; j++) {
                List<Integer> currRoute = map.getOrDefault(routes[i][j], new ArrayList<>());
                currRoute.add(i);
                map.put(routes[i][j], currRoute);
            }
        }

        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        int result = 0;

        queue.offer(source);
        while (!queue.isEmpty()) {
            int len = queue.size();
            result++;

            for (int i = 0; i < len; i++) {
                int currStop = queue.poll();
                List<Integer> currRoutes = map.get(currStop);
                for (Integer currRoute : currRoutes) {
                    if (visited.contains(currRoute)) continue;
                    visited.add(currRoute);
                    for (int j = 0; j < routes[currRoute].length; j++) {
                        if (routes[currRoute][j] == destination) return result;
                        queue.offer(routes[currRoute][j]);
                    }
                }
            }
        }
        return -1;
    }
}
