package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * https://leetcode.com/problems/cheapest-flights-within-k-stops/
 */
public class CheapestFlighWithStops {

    public static void main(String[] args) {
        int[][] flights = {{0, 3, 3}, {3, 4, 3}, {4, 1, 3}, {0, 5, 1}, {5, 1, 100}, {0, 6, 2}, {6, 1, 100}, {0, 7, 1}, {7, 8, 1}, {8, 9, 1}, {9, 1, 1}, {1, 10, 1}, {10, 2, 1}, {1, 2, 100}};
        System.out.println(new CheapestFlighWithStops().findCheapestPriceDijkstra(11, flights, 0, 2, 4));
    }

    public int findCheapestPriceBfs(int len, int[][] flights, int src, int dst, int k) {
        // flight [source, destination, price]
        Map<Integer, List<int[]>> map = new HashMap<>(); // <source, [destination, cost]>

        for (int[] flight : flights) {
            int from = flight[0];
            int to = flight[1];
            int price = flight[2];
            List<int[]> neighbors = map.getOrDefault(from, new ArrayList<>());
            neighbors.add(new int[]{to, price});
            map.put(from, neighbors);
        }

        Queue<int[]> queue = new LinkedList<>(); // <curr, costTillNow>
        queue.offer(new int[]{src, 0});
        int result = Integer.MAX_VALUE;
        int stops = 0;

        while (stops <= k + 1) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] pair = queue.poll();
                int curr = pair[0];
                int costTillNow = pair[1];
                if (curr == dst) {
                    result = Math.min(result, costTillNow);
                    continue;
                }
                if (map.containsKey(curr)) {
                    for (int[] neighbor : map.get(curr)) {
                        if (costTillNow + neighbor[1] < result) {
                            queue.offer(new int[]{neighbor[0], costTillNow + neighbor[1]});
                        }
                    }
                }
            }
            stops++;
        }
        return result == Integer.MAX_VALUE ? -1 : result;
    }

    public int findCheapestPriceBellmanFord(int n, int[][] flights, int src, int dst, int K) {
        int[] cost = new int[n];
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[src] = 0;
        for (int i = 0; i <= K; i++) {
            int[] temp = Arrays.copyOf(cost, n);
            for (int[] f : flights) {
                int curr = f[0], next = f[1], price = f[2];
                if (cost[curr] == Integer.MAX_VALUE)
                    continue;
                temp[next] = Math.min(temp[next], cost[curr] + price);
            }
            cost = temp;
        }
        return cost[dst] == Integer.MAX_VALUE ? -1 : cost[dst];
    }

    public int findCheapestPriceDijkstra(int n, int[][] flights, int src, int dst, int k) {

        Map<Integer, List<int[]>> map = new HashMap<>(); // <from , [to, cost]>
        for (int[] flight : flights) {
            int from = flight[0];
            int to = flight[1];
            int cost = flight[2];
            map.putIfAbsent(from, new ArrayList<>());
            map.get(from).add(new int[]{to, cost});
        }
        PriorityQueue<Node> queue = new PriorityQueue<>((a, b) -> a.costSoFar - b.costSoFar);
        queue.offer(new Node(src, 0, k + 1));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int vertex = node.vertex;
            int costSoFar = node.costSoFar;
            int allowedStops = node.allowedStops;

            if (vertex == dst) return costSoFar;
            if (allowedStops > 0) {
                if (!map.containsKey(vertex)) continue;
                for (int[] neighbor : map.get(vertex)) {
                    int nVertex = neighbor[0];
                    int jumpCost = neighbor[1];
                    queue.add(new Node(costSoFar + jumpCost, nVertex, allowedStops - 1));
                }
            }
        }
        return -1;
    }
}

class Node {
    public int vertex;
    public int costSoFar;
    public int allowedStops;

    public Node(int vertex, int costSoFar, int allowedStops) {
        this.vertex = vertex;
        this.costSoFar = costSoFar;
        this.allowedStops = allowedStops;
    }
}
