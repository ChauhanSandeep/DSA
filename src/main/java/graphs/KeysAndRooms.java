package graphs;

import java.util.*;

/**
 * Problem: Keys and Rooms
 *
 * There are n locked rooms, and room 0 is initially open. Each room contains keys
 * to other rooms. Decide whether all rooms can be visited by repeatedly using the
 * keys found in rooms already opened.
 *
 * Leetcode: https://leetcode.com/problems/keys-and-rooms/ (Medium)
 * Rating:   1412 (zerotrac Elo)
 * Pattern:  Graph | DFS/BFS reachability | Directed traversal
 *
 * Example:
 *   Input:  rooms = [[1],[2],[3],[]]
 *   Output: true
 *   Why:    room 0 gives key 1, room 1 gives key 2, and room 2 gives key 3, so
 *           every room becomes reachable.
 *
 * Follow-ups:
 *   1. Return rooms that remain locked?
 *      After traversal, collect every index that was never visited.
 *   2. Find the shortest key sequence to each room?
 *      Use BFS and store the parent room for each first visit.
 *   3. Support keys that expire after use?
 *      The state must include both current room and remaining key inventory.
 *
 * Related: Number of Provinces (547), Course Schedule (207).
 */
public class KeysAndRooms {


    public static void main(String[] args) {
        KeysAndRooms solver = new KeysAndRooms();
        List<List<List<Integer>>> inputs = Arrays.asList(
            Arrays.asList(Arrays.asList(1), Arrays.asList(2), Arrays.asList(3), Arrays.asList()),
            Arrays.asList(Arrays.asList(1, 3), Arrays.asList(3, 0, 1), Arrays.asList(2), Arrays.asList(0))
        );
        boolean[] expected = {true, false};

        for (int i = 0; i < inputs.size(); i++) {
            boolean output = solver.canVisitAllRooms(inputs.get(i));
            System.out.printf("rooms=%s  ->  %s  expected=%s%n", inputs.get(i), output, expected[i]);
        }
    }
    /**
     * Intuition: rooms and keys form a directed graph. Starting in room 0, DFS opens
     * every room reachable through collected keys. If the reachable count equals the
     * number of rooms, every room can be visited.
     *
     * Algorithm:
     *   1. Start DFS from room 0 with all rooms initially unvisited.
     *   2. Mark a room visited as soon as it is entered.
     *   3. Recursively follow every key found in that room.
     *   4. Return true only if every room was marked visited.
     *
     * Time:  O(V+E) - each room and key is processed at most once.
     * Space: O(V) - visited storage plus recursion stack.
     *
     * @param rooms rooms[i] contains keys found in room i
     * @return true if all rooms are reachable from room 0
     */
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        boolean[] visited = new boolean[rooms.size()];
        dfs(rooms, 0, visited);

        // Check if all rooms have been visited
        for (boolean visit : visited) {
            if (!visit) {
                return false;
            }
        }

        return true;
    }

    // DFS helper to visit rooms recursively
    private void dfs(List<List<Integer>> rooms, int room, boolean[] visited) {
        visited[room] = true;

        // Visit all rooms accessible from current room
        for (int key : rooms.get(room)) {
            if (!visited[key]) {
                dfs(rooms, key, visited);
            }
        }
    }

    /**
     * BFS approach using queue for iterative traversal.
     * Avoids potential stack overflow for very deep room structures.
     */
    public boolean canVisitAllRoomsBFS(List<List<Integer>> rooms) {
        boolean[] visited = new boolean[rooms.size()];
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(0);
        visited[0] = true;

        while (!queue.isEmpty()) {
            int currentRoom = queue.poll();

            // Add all accessible rooms to queue
            for (int key : rooms.get(currentRoom)) {
                if (!visited[key]) {
                    visited[key] = true;
                    queue.offer(key);
                }
            }
        }

        // Check if all rooms visited
        for (boolean visit : visited) {
            if (!visit) {
                return false;
            }
        }

        return true;
    }

    /**
     * Optimized approach counting visited rooms.
     * Early termination when all rooms are visited.
     */
    public boolean canVisitAllRoomsOptimized(List<List<Integer>> rooms) {
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();

        stack.push(0);

        while (!stack.isEmpty()) {
            int room = stack.pop();

            if (!visited.contains(room)) {
                visited.add(room);

                // Early termination
                if (visited.size() == rooms.size()) {
                    return true;
                }

                // Add all keys from current room
                for (int key : rooms.get(room)) {
                    if (!visited.contains(key)) {
                        stack.push(key);
                    }
                }
            }
        }

        return visited.size() == rooms.size();
    }

    /**
     * Union-Find approach treating rooms as connected components.
     * Alternative perspective using disjoint set data structure.
     */
    public boolean canVisitAllRoomsUnionFind(List<List<Integer>> rooms) {
        int n = rooms.size();
        UnionFind uf = new UnionFind(n);

        // Connect rooms that have keys to each other
        for (int room = 0; room < n; room++) {
            for (int key : rooms.get(room)) {
                uf.union(room, key);
            }
        }

        // Check if all rooms are connected to room 0
        int root0 = uf.find(0);
        for (int i = 1; i < n; i++) {
            if (uf.find(i) != root0) {
                return false;
            }
        }

        return true;
    }

    // Union-Find data structure
    private static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return;

            if (rank[px] < rank[py]) {
                parent[px] = py;
            } else if (rank[px] > rank[py]) {
                parent[py] = px;
            } else {
                parent[py] = px;
                rank[px]++;
            }
        }
    }

    /**
     * Returns list of unreachable rooms instead of boolean.
     * Extension that provides more detailed information.
     */
    public List<Integer> findUnreachableRooms(List<List<Integer>> rooms) {
        boolean[] visited = new boolean[rooms.size()];
        dfs(rooms, 0, visited);

        List<Integer> unreachable = new ArrayList<>();
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                unreachable.add(i);
            }
        }

        return unreachable;
    }

    /**
     * BFS approach that also tracks the minimum steps to reach each room.
     * Provides shortest path information for each reachable room.
     */
    public Map<Integer, Integer> canVisitAllRoomsWithSteps(List<List<Integer>> rooms) {
        Map<Integer, Integer> roomSteps = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(0);
        roomSteps.put(0, 0);

        while (!queue.isEmpty()) {
            int currentRoom = queue.poll();
            int currentSteps = roomSteps.get(currentRoom);

            for (int key : rooms.get(currentRoom)) {
                if (!roomSteps.containsKey(key)) {
                    roomSteps.put(key, currentSteps + 1);
                    queue.offer(key);
                }
            }
        }

        return roomSteps;
    }

    /**
     * Iterative DFS using explicit stack.
     * Avoids recursion while maintaining DFS behavior.
     */
    public boolean canVisitAllRoomsIterativeDFS(List<List<Integer>> rooms) {
        boolean[] visited = new boolean[rooms.size()];
        Stack<Integer> stack = new Stack<>();

        stack.push(0);

        while (!stack.isEmpty()) {
            int room = stack.pop();

            if (!visited[room]) {
                visited[room] = true;

                // Add all keys from current room
                for (int key : rooms.get(room)) {
                    if (!visited[key]) {
                        stack.push(key);
                    }
                }
            }
        }

        // Verify all rooms visited
        for (boolean visit : visited) {
            if (!visit) return false;
        }

        return true;
    }

    /**
     * Functional approach using streams and recursion.
     * Demonstrates functional programming style.
     */
    public boolean canVisitAllRoomsFunctional(List<List<Integer>> rooms) {
        Set<Integer> visited = new HashSet<>();
        visitRoomsFunctional(rooms, 0, visited);
        return visited.size() == rooms.size();
    }

    private void visitRoomsFunctional(List<List<Integer>> rooms, int room, Set<Integer> visited) {
        if (visited.contains(room)) return;

        visited.add(room);
        rooms.get(room).stream()
            .filter(key -> !visited.contains(key))
            .forEach(key -> visitRoomsFunctional(rooms, key, visited));
    }

    /**
     * Parallel processing approach for very large room networks.
     * Uses parallel streams for room exploration.
     */
    public boolean canVisitAllRoomsParallel(List<List<Integer>> rooms) {
        Set<Integer> visited = java.util.concurrent.ConcurrentHashMap.newKeySet();
        Queue<Integer> queue = new java.util.concurrent.ConcurrentLinkedQueue<>();

        queue.offer(0);
        visited.add(0);

        while (!queue.isEmpty()) {
            int size = queue.size();

            // Process current level in parallel
            java.util.stream.IntStream.range(0, size)
                .parallel()
                .forEach(i -> {
                    Integer room = queue.poll();
                    if (room != null) {
                        rooms.get(room).parallelStream()
                            .filter(key -> !visited.contains(key))
                            .forEach(key -> {
                                if (visited.add(key)) {
                                    queue.offer(key);
                                }
                            });
                    }
                });
        }

        return visited.size() == rooms.size();
    }
}
