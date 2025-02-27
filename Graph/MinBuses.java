package Graph;

import java.util.*;

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
        System.out.println("Min buses required to go from source to destination: " + numOfBuses);
    }

    public int numBusesToDestination(int[][] routes, int source, int destination) {
        if (source == destination) return 0;

        // Map: stop -> list of route indices
        Map<Integer, List<Integer>> stopToRoutesMap = new HashMap<>();
        for (int i = 0; i < routes.length; i++) {
            for (int stop : routes[i]) {
                stopToRoutesMap.putIfAbsent(stop, new ArrayList<>());
                stopToRoutesMap.get(stop).add(i);
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visitedRoutes = new HashSet<>();
        queue.offer(source);

        int busesTaken = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            busesTaken++;

            for (int i = 0; i < levelSize; i++) {
                int currStop = queue.poll();
                List<Integer> routesAtStop = stopToRoutesMap.get(currStop);

                if (routesAtStop == null) continue; // Prevent NullPointerException

                for (int routeIndex : routesAtStop) {
                    if (visitedRoutes.contains(routeIndex)) continue;
                    visitedRoutes.add(routeIndex);

                    for (int nextStop : routes[routeIndex]) {
                        if (nextStop == destination) return busesTaken;
                        queue.offer(nextStop);
                    }
                }
            }
        }
        return -1;
    }
}
