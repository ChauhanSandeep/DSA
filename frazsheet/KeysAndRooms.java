package frazsheet;

import java.util.*;

/**
 * 841. Keys and Rooms
 * 
 * Problem: There are n rooms numbered from 0 to n-1. Room 0 is unlocked,
 * and each room contains a set of unique keys to other rooms. Return true
 * if you can visit all rooms.
 * 
 * Example:
 * Input: rooms = [[1],[2],[3],[]]
 * Output: true
 * Explanation: Start at room 0, get key to room 1, then 2, then 3.
 * 
 * LeetCode: https://leetcode.com/problems/keys-and-rooms
 * 
 * Follow-up questions:
 * Q: What if some rooms have multiple keys to the same room?
 * A: Use Set to avoid revisiting rooms, algorithm remains the same.
 * 
 * Q: How to find the minimum steps to visit all rooms?
 * A: Use BFS to find shortest path, tracking levels.
 * 
 * Q: Can we determine which rooms are unreachable?
 * A: Track visited rooms and return the complement set.
 */
public class KeysAndRooms {
    
    /**
     * DFS approach to traverse all reachable rooms.
     * 
     * Algorithm: Depth-First Search
     * - Start from room 0 (always unlocked)
     * - Mark room as visited, collect all keys
     * - For each key, recursively visit the corresponding room if not visited
     * - Return true if all rooms have been visited
     * 
     * Time Complexity: O(n + k) where n is rooms, k is total keys
     * Space Complexity: O(n) for visited array and recursion stack
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