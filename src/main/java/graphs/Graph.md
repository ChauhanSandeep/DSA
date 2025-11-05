# Graph Algorithms Handbook for Technical Interviews

## Table of Contents
- [Introduction to Graphs](#introduction-to-graphs)
- [Breadth-First Search (BFS)](#breadth-first-search-bfs) `(SELECT → MARK(*) → WORK → ADD(*))` [In Queue]
- [Depth-First Search (DFS)](#depth-first-search-dfs) `(SELECT → MARK(*) → WORK → ADD(*))` [In Stack]
- [Topological Sort](#topological-sort)
    - [Topological Sort using DFS](#dfs-based-topological-sort-implementation)
    - [Kahn's Algorithm (PREFERRED)](#kahns-algorithm-based-approach) `(SELECT → MARK(*) → WORK → ADD(*))` [In Queue with 0 In-Degree]
- [Shortest Path Algorithms](#shortest-path-algorithms)
    - [Dijkstra's Algorithm](#dijkstras-algorithm) `(SELECT → MARK(*) → WORK → ADD(*))` [In Min-Heap with total path distance]
    - [Bellman-Ford Algorithm](#bellman-ford-algorithm)
    - [Floyd-Warshall Algorithm](#floyd-warshall-algorithm)
- [Disjoint Sets (Union-Find)](#disjoint-sets-union-find)
- [Minimum Spanning Tree](#minimum-spanning-tree)
    - [Prim's Algorithm](#prims-algorithm) `(SELECT → MARK(*) → WORK → ADD(*))` [In Min-Heap with individual Nodes]
    - [Kruskal's Algorithm](#kruskals-algorithm)
- [Articulation Points and Bridges](#articulation-points-and-bridges)
- [Kosaraju's Algorithm](#kosarajus-algorithm)

## Introduction to Graphs
A graph is a non-linear data structure consisting of vertices (or nodes) and edges that connect these vertices. Graphs are used to represent networks of many kinds, including social networks, computer networks, roads, and much more.

### Graph Representation

In Java, graphs can be represented in several ways:

1. **Adjacency Matrix**: A 2D array where `matrix[i][j]` is 1 (or a weight value) if there is an edge from vertex i to vertex j, and 0 otherwise.

```java
public class AdjacencyMatrixGraph {
    private int V; // Number of vertices
    private int[][] adjMatrix;
    
    public AdjacencyMatrixGraph(int v) {
        V = v;
        adjMatrix = new int[v][v];
    }
    
    // Add an edge from src to dest
    public void addEdge(int src, int dest) {
        // For undirected graph
        adjMatrix[src][dest] = 1;
        adjMatrix[dest][src] = 1;
    }
    
    // Add a weighted edge from src to dest
    public void addEdge(int src, int dest, int weight) {
        adjMatrix[src][dest] = weight;
        adjMatrix[dest][src] = weight; // For undirected graph
    }
}
```

2. **Adjacency List**: An array of lists, where each list describes the set of neighbors of a vertex.

```java
import java.util.*;

public class AdjacencyListGraph {
    private int V; // Number of vertices
    private List<List<Integer>> adjList;
    
    public AdjacencyListGraph(int v) {
        V = v;
        adjList = new ArrayList<>(v);
        for (int i = 0; i < v; i++) {
            adjList.add(new ArrayList<>());
        }
    }
    
    // Add an edge from src to dest
    public void addEdge(int src, int dest) {
        // For undirected graph
        adjList.get(src).add(dest);
        adjList.get(dest).add(src);
    }
    
    // For weighted graphs, we can use a pair class
    public static class Edge {
        int dest;
        int weight;
        
        public Edge(int dest, int weight) {
            this.dest = dest;
            this.weight = weight;
        }
    }
}
```
---
## Breadth-First Search (BFS)

BFS is an algorithm for traversing or searching a graph. It starts at a source node and explores all its neighbors level by level before moving to the next level. It’s ideal for finding the shortest path in an unweighted graph and is implemented using a queue.

### Use Cases
- Finding shortest paths in unweighted graphs
- Finding all connected components
- Testing if a graph is bipartite (bipartite graph is a graph whose vertices can be divided into two disjoint 
sets such that no two graph vertices within the same set are adjacent)
- Finding all nodes within one connected component

### Steps 
Follows patterns `(SELECT → MARK(*) → WORK → ADD(*))`
- Create a `queue` and a `visited[]` array.  
- Add the starting node to the queue and mark it as visited.
- **SELECT**: Remove the front node from the queue.
- **MARK(*)**: Ensure the node is marked as visited to avoid reprocessing.
- **WORK**: Process the node (e.g., print, track level, update parent map).
- **ADD(*)**:For all unvisited neighbors of the current node,  
  mark them as visited and add them to the queue.
- **Repeat**: Continue until the queue is empty.

### Java Implementation

```java
import java.util.*;

public class BFS {
    // Perform BFS on the graph starting from vertex s
    public static void bfs(List<List<Integer>> graph, int s) {
        int V = graph.size();
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        
        // Mark the source vertex as visited and enqueue it
        visited[s] = true;
        queue.offer(s);
        
        while (!queue.isEmpty()) {
            // Dequeue a vertex from queue and print it
            int u = queue.poll();
            System.out.print(u + " ");
            
            // Get all adjacent vertices of the dequeued vertex
            // If an adjacent vertex has not been visited, mark it
            // visited and enqueue it
            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    queue.offer(v);
                }
            }
        }
    }
    
    // BFS to find shortest path from source to all other vertices
    public static int[] shortestPath(List<List<Integer>> graph, int s) {
        int V = graph.size();
        boolean[] visited = new boolean[V];
        int[] distance = new int[V];
        Arrays.fill(distance, Integer.MAX_VALUE); // Initialize distances as infinity
        distance[s] = 0; // Distance of source from itself is 0
        
        Queue<Integer> queue = new LinkedList<>();
        visited[s] = true;
        queue.offer(s);
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            
            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    distance[v] = distance[u] + 1; // Update distance
                    queue.offer(v);
                }
            }
        }
        
        return distance;
    }
}
```

### Time and Space Complexity
- **Time Complexity**: O(V + E) where V is the number of vertices and E is the number of edges
- **Space Complexity**: O(V) for the queue and visited array

### When to Use:
- **Finding the shortest path in unweighted graphs**: BFS guarantees the shortest path from the source node to any other node when edge weights are equal.
- **Exploring a graph level by level**: When you need to visit all nodes at a particular distance from the source before moving on to the next level.
- **Flood-fill problems**: E.g., in image processing, to spread or fill regions from a starting point.
- **Connectivity checks**: To determine if a graph is connected or if a node is reachable from another node.

### Example LeetCode Problems
1. [LeetCode #200: Number of Islands](https://leetcode.com/problems/number-of-islands/)
2. [LeetCode #994: Rotting Oranges](https://leetcode.com/problems/rotting-oranges/)
3. [LeetCode #127: Word Ladder](https://leetcode.com/problems/word-ladder/)
4. [LeetCode #1091: Shortest Path in Binary Matrix](https://leetcode.com/problems/shortest-path-in-binary-matrix/)

---
## Depth-First Search (DFS)

DFS is another graph traversal algorithm where you explore as deep as possible along a branch before backtracking. It is implemented using either recursion or a stack. DFS is useful for topological sorting, pathfinding, and checking graph connectivity.

### Use Cases
- Finding paths between two vertices
- Detecting cycles in a graph
- Topological sorting
- Finding connected components
- Solving puzzles like mazes

### Steps
Follows patterns `(SELECT → MARK(*) → WORK → ADD(*))`
- Create a `stack` and a `visited[]` array.  
- Add the starting node to the stack and mark it as visited.
- **SELECT**: Pop the top node from the stack.
- **MARK(*)**: Ensure the node is marked as visited to avoid reprocessing.
- **WORK**: Process the node (e.g., print, store in result list, update metadata).
- **ADD(*)**:  For all unvisited neighbors of the current node,  
  mark them as visited and push them onto the stack.
- **Repeat**: Continue until the stack is empty (or all nodes are visited in recursion).

### Java Implementation

```java
import java.util.*;

public class DFS {
    // Recursive DFS
    public static void dfsRecursive(List<List<Integer>> graph, int vertex, boolean[] visited) {
        // Mark the current node as visited and print it
        visited[vertex] = true;
        System.out.print(vertex + " ");
        
        // Recursively process all the adjacent vertices
        for (int neighbor : graph.get(vertex)) {
            if (!visited[neighbor]) {
                dfsRecursive(graph, neighbor, visited);
            }
        }
    }
    
    // Wrapper method to call recursive DFS
    public static void dfs(List<List<Integer>> graph, int startVertex) {
        int V = graph.size();
        boolean[] visited = new boolean[V];
        
        // Call the recursive helper function
        dfsRecursive(graph, startVertex, visited);
    }
    
    // Iterative DFS using stack
    public static void dfsIterative(List<List<Integer>> graph, int startVertex) {
        int V = graph.size();
        boolean[] visited = new boolean[V];
        
        // Create a stack for DFS
        Stack<Integer> stack = new Stack<>();
        
        // Push the source vertex
        stack.push(startVertex);
        
        while (!stack.isEmpty()) {
            // 1. SELECT: Pop a vertex from the stack
            int vertex = stack.pop();
            
            // Skip if already visited
            if (visited[vertex]) {
                continue;
            }
            
            // 2. MARK(*): Mark the vertex as visited
            visited[vertex] = true;
            
            // 3. WORK: Process the vertex
            System.out.print(vertex + " ");
            
            // 4. ADD(*): Push all unvisited neighbors to stack
            for (int neighbor : graph.get(vertex)) {
                if (!visited[neighbor]) {
                    stack.push(neighbor);
                }
            }
        }
    }
    
    // DFS to detect cycles in an undirected graph
    public static boolean hasCycle(List<List<Integer>> graph) {
        int V = graph.size();
        boolean[] visited = new boolean[V];
        
        // Check for cycles starting from each unvisited vertex
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                if (hasCycleUtil(graph, i, visited, -1)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean hasCycleUtil(List<List<Integer>> graph, int vertex, 
                                       boolean[] visited, int parent) {
        // Mark the current node as visited
        visited[vertex] = true;
        
        // Check all neighbors
        for (int neighbor : graph.get(vertex)) {
            // If neighbor is not visited, recursively check if its subgraph has cycle
            if (!visited[neighbor]) {
                if (hasCycleUtil(graph, neighbor, visited, vertex)) {
                    return true;
                }
            }
            // If an adjacent vertex is visited and not the parent, then there's a cycle
            else if (neighbor != parent) {
                return true;
            }
        }
        return false;
    }
}
```

### Time and Space Complexity
- **Time Complexity**: O(V + E) where V is the number of vertices and E is the number of edges
- **Space Complexity**: O(V) for the visited array and the recursion stack

### Example LeetCode Problems
1. [LeetCode #733: Flood Fill](https://leetcode.com/problems/flood-fill/)
2. [LeetCode #547: Number of Provinces](https://leetcode.com/problems/number-of-provinces/)
3. [LeetCode #207: Course Schedule](https://leetcode.com/problems/course-schedule/)
4. [LeetCode #841: Keys and Rooms](https://leetcode.com/problems/keys-and-rooms/)

---

## Topological Sort

Topological sort is the process of ordering the vertices of a directed graph such that for every directed edge u → v, vertex u comes before v in the ordering. This is primarily used in directed acyclic graphs (DAGs) and is helpful in scheduling tasks.

**Algorithms Covered:**
1. **DFS-based Topological Sort**: Uses depth-first search with a stack to store vertices in reverse finishing order. After DFS completes, vertices are popped from the stack to get topological order. Time: O(V + E).

2. **Kahn's Algorithm (Preferred)**: Uses BFS with in-degree tracking. Repeatedly removes vertices with zero in-degree and reduces in-degree of neighbors. Easier to understand and can detect cycles. Time: O(V + E).

---

### Use Cases
- **Ordering tasks or events** with dependencies: Topological sort is used in project scheduling, task dependency resolution, and course prerequisite ordering where tasks must occur in a specific order.
- **DAGs (Directed Acyclic Graphs)**: This algorithm is specifically designed for directed acyclic graphs where cycles do not exist.
- **Dependency resolution**: In package managers, topological sort can be used to determine the order in which packages should be installed based on their dependencies.

### Approaches
1. **DFS-based (Recursive)**: Uses a temporary stack to store vertices in topological order
2. **Kahn's Algorithm (Iterative)**: Uses in-degree of vertices and a queue

### DFS-based Topological Sort Implementation
In this approach, topological sorting is performed using DFS. As DFS visits each node, it places the node on a stack once all its descendants are processed. The stack’s order gives a valid topological sort of the DAG.

#### Steps:
1. Initialize a visited array to track visited nodes
2. Perform DFS on unvisited nodes
3. Push nodes to a stack after visiting all their neighbors
4. Pop elements from the stack to get the topological order

#### Java Implementation
```java
import java.util.*;

/**
 * TopologicalSortDFS performs a topological sort of directed acyclic graph (DAG)
 * using Depth-First Search (DFS).
 *
 * Approach:
 * We perform a DFS traversal of the graph. When visiting a node, we recursively visit
 * all its unvisited neighbors. After all neighbors of a node are visited, we add the node
 * to a stack. This ensures that all dependencies of a node are placed before it in the final ordering.
 *
 * Steps:
 * 1. Initialize a visited array to track visited nodes.
 * 2. Perform DFS on unvisited nodes and push them to a stack after their neighbors are fully explored.
 * 3. Pop elements from the stack to build the topological order.
 *
 * Time Complexity: O(V + E), where V is number of vertices and E is number of edges.
 * Space Complexity: O(V) for visited array and stack.
 */
public class TopologicalSortDFS {

  public static List<Integer> topologicalSort(List<List<Integer>> adjacencyList) {
    int totalVertices = adjacencyList.size();
    boolean[] visited = new boolean[totalVertices];
    Deque<Integer> dfsStack = new ArrayDeque<>();

    // Perform DFS from each unvisited vertex
    for (int vertex = 0; vertex < totalVertices; vertex++) {
      if (!visited[vertex]) {
        dfs(vertex, adjacencyList, visited, dfsStack);
      }
    }

    // Pop all elements from the stack to get the topological order
    List<Integer> topologicalOrder = new ArrayList<>();
    while (!dfsStack.isEmpty()) {
      topologicalOrder.add(dfsStack.pop());
    }

    return topologicalOrder;
  }

  /**
   * Recursive helper function to perform DFS and push nodes to stack post DFS completion.
   *
   * @param currentVertex Current vertex being explored.
   * @param graph         The graph represented as adjacency list.
   * @param visited       Boolean array to mark visited vertices.
   * @param dfsStack      Stack to store vertices in post-order (for topological sort).
   */
  private static void dfs(int currentVertex, List<List<Integer>> graph, boolean[] visited, Deque<Integer> dfsStack) {
    visited[currentVertex] = true;

    // Visit all adjacent vertices
    for (int neighbor : graph.get(currentVertex)) {
      if (!visited[neighbor]) {
        dfs(neighbor, graph, visited, dfsStack);
      }
    }

    // After visiting all neighbors, push the current vertex to the stack
    dfsStack.push(currentVertex);
  }
}
```

### Kahn's Algorithm based approach
Kahn’s algorithm is a method of topological sorting for DAGs using in-degree tracking. Nodes with zero in-degree (no dependencies) are processed first, and their removal decreases the in-degree of their neighbors. This process continues until all nodes are sorted or a cycle is detected.

#### Steps:
Follows patterns `(SELECT → MARK(*) → WORK → ADD(*))`
- **Initialize**: Compute `in-degree` of all nodes. Create a `queue` and enqueue all nodes with in-degree = 0.
- **SELECT**: Dequeue a node from the front of the queue.
- **MARK(*)**: Conceptually mark this node as processed by adding it to the result list.
- **WORK**: Process the node (e.g., store it in the topological order list).
- **ADD(*)**: For each neighbor of the current node, reduce its in-degree by 1. If in-degree becomes 0, enqueue the neighbor.
- **Repeat**: Continue until the queue is empty. If the result list size is less than the total nodes, then there is a cycle in the graph.

#### Java Implementation
```java
import java.util.*;

/**
 * TopologicalSortKahn performs topological sorting of a Directed Acyclic Graph (DAG)
 * using Kahn’s Algorithm (BFS-based approach).
 *
 * Steps (Kahn’s Algorithm):
 * 1. Calculate the in-degree of all vertices.
 * 2. Add all vertices with in-degree 0 to a queue.
 * 3. Repeatedly dequeue a vertex, add it to the result, and decrease the in-degree
 *    of its neighbors. If any neighbor’s in-degree becomes 0, enqueue it.
 * 4. If we process all vertices, the result is a valid topological order.
 *    Otherwise, the graph has a cycle.
 *
 * Time Complexity: O(V + E)  
 * Space Complexity: O(V)
 */
public class TopologicalSortKahn {

  /**
   * Performs topological sort using Kahn’s algorithm (BFS).
   *
   * @param adjacencyList Adjacency list representing the directed graph.
   * @return List of vertices in topological order or empty list if a cycle is detected.
   */
  public static List<Integer> topologicalSort(List<List<Integer>> adjacencyList) {
    int totalVertices = adjacencyList.size();
    int[] inDegree = new int[totalVertices];

    // Step 1: Compute in-degrees of all vertices
    for (int source = 0; source < totalVertices; source++) {
      for (int target : adjacencyList.get(source)) {
        inDegree[target]++;
      }
    }

    // Step 2: Enqueue all vertices with in-degree 0
    Queue<Integer> zeroInDegreeQueue = new LinkedList<>();
    
    for (int vertex = 0; vertex < totalVertices; vertex++) {
      if (inDegree[vertex] == 0) {
        zeroInDegreeQueue.offer(vertex);
      }
    }

    // Step 3: Process vertices in BFS manner
    List<Integer> topologicalOrder = new ArrayList<>();
    int processedCount = 0;

    while (!zeroInDegreeQueue.isEmpty()) {
      // SELECT: Dequeue a vertex with in-degree 0
      int current = zeroInDegreeQueue.poll();
      // WORK: Add it to the topological order
      topologicalOrder.add(current);
      processedCount++;

      // ADD(*) : Decrease in-degree of adjacent vertices and enqueue if in-degree becomes 0
      for (int neighbor : adjacencyList.get(current)) {
        inDegree[neighbor]--;
        if (inDegree[neighbor] == 0) {
          zeroInDegreeQueue.offer(neighbor);
        }
      }
    }

    // Step 4: Check for cycle
    if (processedCount != totalVertices) {
      System.out.println("Cycle detected: No valid topological ordering possible.");
      return new ArrayList<>();
    }

    return topologicalOrder;
  }

  /**
   * Detects if a cycle exists in a directed graph using DFS.
   *
   * @param adjacencyList Adjacency list of the graph.
   * @return true if a cycle is detected, false otherwise.
   */
  public static boolean hasCycle(List<List<Integer>> adjacencyList) {
    int totalVertices = adjacencyList.size();
    boolean[] visited = new boolean[totalVertices];
    boolean[] inRecursionStack = new boolean[totalVertices];

    // Check for cycle from each vertex
    for (int vertex = 0; vertex < totalVertices; vertex++) {
      if (!visited[vertex]) {
        if (hasCycleDFS(vertex, adjacencyList, visited, inRecursionStack)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Helper DFS to detect cycle in a directed graph.
   *
   * @param current Current vertex being visited.
   * @param graph   Graph represented as an adjacency list.
   * @param visited Tracks visited vertices.
   * @param inStack Tracks the recursion stack to detect back edges.
   * @return true if cycle is found, false otherwise.
   */
  private static boolean hasCycleDFS(int current, List<List<Integer>> graph,
      boolean[] visited, boolean[] inStack) {
    visited[current] = true;
    inStack[current] = true;

    for (int neighbor : graph.get(current)) {
      if (!visited[neighbor]) {
        if (hasCycleDFS(neighbor, graph, visited, inStack)) {
          return true;
        }
      } else if (inStack[neighbor]) {
        return true; // Back edge found, cycle detected
      }
    }

    inStack[current] = false; // Backtrack
    return false;
  }
}
```

### Time and Space Complexity
- **Time Complexity**: O(V + E) for both DFS and Kahn's algorithms
- **Space Complexity**: O(V) for the visited array and the stack/queue

### Example LeetCode Problems
1. [LeetCode #207: Course Schedule](https://leetcode.com/problems/course-schedule/)
2. [LeetCode #210: Course Schedule II](https://leetcode.com/problems/course-schedule-ii/)
3. [LeetCode #329: Longest Increasing Path in a Matrix](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/)
4. [LeetCode #802: Find Eventual Safe States](https://leetcode.com/problems/find-eventual-safe-states/)

---
## Shortest Path Algorithms

These algorithms are used to find the shortest path between nodes in a graph. The choice of algorithm depends on the graph structure and constraints:

**Algorithms Covered:**
1. **Dijkstra's Algorithm**: Finds shortest paths from a single source to all other vertices in graphs with **non-negative edge weights**. Uses a greedy approach with a priority queue. Time: O((V + E) log V).

2. **Bellman-Ford Algorithm**: Handles graphs with **negative edge weights** and can detect negative cycles. Works by relaxing all edges V-1 times. Time: O(V × E).

3. **Floyd-Warshall Algorithm**: Computes shortest paths between **all pairs of vertices**. Uses dynamic programming to consider all intermediate vertices. Time: O(V³).

---

### Dijkstra's Algorithm
Dijkstra’s algorithm is a greedy method for finding the shortest path in a graph with non-negative weights. It works by iteratively selecting the node with the smallest tentative distance, updating its neighbors, and continuing until the destination is reached or all nodes are processed.

### Example Leetcode Problems
- [LeetCode #743: Network Delay Time](https://leetcode.com/problems/network-delay-time/)
- [LeetCode #1631: Path With Minimum Effort](https://leetcode.com/problems/path-with-minimum-effort/)
- [LeetCode #787: Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/)

### Use Cases
- **Shortest path in weighted graphs** (with non-negative weights): Ideal for finding the shortest path from a source node to all other nodes.
- **Routing algorithms**: Used in network protocols to find the best path between nodes, such as in GPS systems or internet routing.
- **Network delay problems**: Where the goal is to minimize the time for a signal to travel across a network.

### Steps
Follows patterns `(SELECT → MARK(*) → WORK → ADD(*))`
- **Initialize**: Create a `min-heap` (priority queue) to store `(node, distance)` and a `distance[]` array initialized to ∞ for all nodes except the source (0). Add the source node to the heap.
- **SELECT**: Extract the node with the smallest distance from the min-heap.
- **MARK(*)**: If the node is already finalized (visited), skip it; otherwise, mark it as processed.
- **WORK**: Update the shortest path info for the current node (e.g., record distance to the node).
- **ADD(*)**: For all unvisited neighbors of the current node, if a shorter path is found via the current node, update their distance and add them to the min-heap.
- **Repeat**: Continue until the min-heap is empty.

### Java Implementation

```java
import java.util.*;

/**
 * Dijkstra's Algorithm - Single Source Shortest Path for Weighted Graphs
 *
 * Steps:
 * 1. Initialize all distances as infinity (except source = 0).
 * 2. Use a priority queue to fetch the node with the minimum distance.
 * 3. For each neighbor, check if the new path is shorter than the known path and update.
 * 4. Repeat until all nodes are processed.
 *
 * Time Complexity: O((V + E) * log V) using PriorityQueue
 * Space Complexity: O(V) for distance array and priority queue
 */
public class DijkstraShortestPath {
    
    // Class representing an edge in the graph
    static class Edge {
        int destination;
        int weight;
        
        public Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }
    
    // Helper class for priority queue entries
    static class Node {
        int id;
        int distance;
        
        public Node(int id, int distance) {
            this.id = id;
            this.distance = distance;
        }
    }
    
    /**
     * Finds shortest paths from source to all vertices using Dijkstra's algorithm.
     * 
     * @param graph Adjacency list where graph[i] contains list of edges from vertex i
     * @param source The starting vertex
     * @return Array of shortest distances from source to each vertex
     */
    public static int[] dijkstra(List<List<Edge>> graph, int source) {
        int numVertices = graph.size();
        int[] distance = new int[numVertices];
        boolean[] visited = new boolean[numVertices];
        
        // Initialize all distances as infinity
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[source] = 0;
        
        // Min-heap: prioritize nodes with smallest distance
        PriorityQueue<Node> minHeap = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));
        minHeap.offer(new Node(source, 0));
        
        while (!minHeap.isEmpty()) {
            // SELECT: Extract node with minimum distance
            Node current = minHeap.poll();
            int currentNode = current.id;
            
            // MARK(*): Skip if already processed
            if (visited[currentNode]) continue;
            visited[currentNode] = true;
            
            // WORK: Current node's shortest path is finalized
            // (Optional: can track parent for path reconstruction)
            
            // ADD(*): Explore all neighbors and update distances
            for (Edge edge : graph.get(currentNode)) {
                int neighbor = edge.destination;
                int newDistance = distance[currentNode] + edge.weight;
                
                // Relaxation: update if we found a shorter path
                if (!visited[neighbor] && newDistance < distance[neighbor]) {
                    distance[neighbor] = newDistance;
                    minHeap.offer(new Node(neighbor, newDistance));
                }
            }
        }
        
        return distance;
    }
}
```

### Time and Space Complexity
- **Time Complexity**: O((V + E) log V) using a binary heap priority queue
- **Space Complexity**: O(V) for the distance array and the priority queue

### Bellman-Ford Algorithm
Bellman-Ford is a single-source shortest path algorithm that works even with negative edge weights. It relaxes all edges up to n-1 times (where n is the number of nodes) and can detect negative weight cycles, unlike Dijkstra’s algorithm.

### Use Cases
- **Graphs with negative edge weights**: Bellman-Ford can handle graphs with negative weight edges, unlike Dijkstra’s.
- **Detecting negative cycles**: If there’s a cycle in the graph where the total weight is negative, Bellman-Ford can detect this.
- **Shortest path in graphs where edge weights may change dynamically**: Unlike Dijkstra, Bellman-Ford is simpler for dealing with edge weights that change over time.


### Steps
1. Start with setting distance to source as 0, and all others as ∞ (unknown).
   - This is like saying: I know where I am, but don’t know how far the other cities are.
2. Relax All Edges (n - 1) Times
   - Why n - 1?
     - The longest possible simple path in a graph with n nodes has n - 1 edges.
     - So, to propagate information across all cities, you need n - 1 rounds of updates.
   - “Relaxing an edge” means: Can I reach `to` from `from` more cheaply than I thought before?
3. After n - 1 rounds, if any edge can still be relaxed, it means you’ve found a cycle that can keep reducing cost forever — a trap!

### Java Implementation
```java
import java.util.*;

public class BellmanFord {
    
    /**
     * Represents a directed edge in the graph with weight.
     */
    static class Edge {
        int from;
        int to;
        int weight;
        
        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
    
    /**
     * Finds shortest paths from source to all vertices using Bellman-Ford algorithm.
     * Can handle negative edge weights and detect negative cycles.
     *
     * @param numVertices Number of vertices in the graph
     * @param edges       List of all edges in the graph
     * @param source      Starting vertex
     * @return Array of shortest distances, or null if negative weight cycle is detected
     */
    public static int[] findShortestPaths(int numVertices, List<Edge> edges, int source) {
        int[] distance = new int[numVertices];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[source] = 0; // Distance from source to itself is 0
        
        // Step 1: Relax all edges (V - 1) times
        // Why V-1? Longest simple path has at most V-1 edges
        for (int iteration = 0; iteration < numVertices - 1; iteration++) {
            boolean updated = false;
            
            for (Edge edge : edges) {
                // Relaxation: Check if path through 'from' is shorter
                if (distance[edge.from] != Integer.MAX_VALUE &&
                    distance[edge.from] + edge.weight < distance[edge.to]) {
                    distance[edge.to] = distance[edge.from] + edge.weight;
                    updated = true;
                }
            }
            
            // Early termination: if no update in this iteration, we're done
            if (!updated) break;
        }
        
        // Step 2: Check for negative-weight cycles
        // If we can still relax any edge, a negative cycle exists
        for (Edge edge : edges) {
            if (distance[edge.from] != Integer.MAX_VALUE &&
                distance[edge.from] + edge.weight < distance[edge.to]) {
                System.out.println("⚠️ Negative weight cycle detected!");
                return null;
            }
        }
        
        return distance;
    }
}
```

### Time and Space Complexity
- **Time Complexity**: O(V × E)
- **Space Complexity**: O(V) for the distance array
---
### Floyd-Warshall Algorithm
Imagine you want to know the shortest distance between every pair of cities in the world. But instead of checking each route directly, you ask:
“What if I go through city X as a pit stop — is that cheaper?”  You do this for every possible city X as a middle stop, for every pair of cities.

That’s the Floyd-Warshall algorithm: try improving every path by passing through every possible intermediate node.

<b><u>This is not important for interviews</u></b>

### Use Cases
- **All-pairs shortest path**: If you need the shortest path between every pair of nodes in a graph, especially when the graph is relatively small.
- **Graph with dense edges**: Floyd-Warshall is particularly effective when the graph is dense, as it computes all distances in one go.
- **Handling negative distances**: Like Bellman-Ford, it can handle graphs with negative distances, but it handles all pairs simultaneously.


### Steps
1. Initialize a distance matrix where distance[i][j] = distance of direct edge i → j, or ∞ if no direct edge.
2.	Loop over every node k as an intermediate node.
      - For every pair (i, j), check:
      - “Can I go from i to j via k and get a shorter path?”
      - If yes, update distance[i][j] = distance[i][k] + distance[k][j]
3.	After all updates, if distance to itself (any diagonal distance[i][i] < 0 ), a negative weight cycle exists.

### Java Implementation

```java
import java.util.*;

public class FloydWarshall {
    
    private static final int INF = Integer.MAX_VALUE;
    
    /**
     * Computes shortest distances between all pairs of vertices using Floyd-Warshall.
     * 
     * @param graph Adjacency matrix where graph[i][j] = weight from i to j, 
     *              or INF if no direct edge exists
     * @return 2D array of shortest distances, or null if negative cycle detected
     */
    public static int[][] allPairsShortestPath(int[][] graph) {
        int numVertices = graph.length;
        int[][] distance = new int[numVertices][numVertices];
        
        // Step 1: Initialize distance matrix with original graph values
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                distance[i][j] = graph[i][j];
            }
        }
        
        // Step 2: Try each vertex as an intermediate point
        // For each pair (i, j), check if path i -> k -> j is shorter than i -> j
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    
                    // Skip if either segment doesn't exist
                    if (distance[i][k] == INF || distance[k][j] == INF) {
                        continue;
                    }
                    
                    // Relaxation: update if path through k is shorter
                    if (distance[i][k] + distance[k][j] < distance[i][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }
        
        // Step 3: Check for negative weight cycles
        // If distance from a vertex to itself becomes negative, cycle exists
        for (int i = 0; i < numVertices; i++) {
            if (distance[i][i] < 0) {
                System.out.println("⚠️ Negative weight cycle detected!");
                return null;
            }
        }
        
        return distance;
    }
}
```

### Time and Space Complexity
- **Time Complexity**: O(V³) - Triple nested loop
- **Space Complexity**: O(V²) for the distance matrix

### Example LeetCode Problems for Shortest Path Algorithms
1. [LeetCode #743: Network Delay Time](https://leetcode.com/problems/network-delay-time/)
2. [LeetCode #787: Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/)
3. [LeetCode #1334: Find the City With the Smallest Number of Neighbors at a Threshold Distance](https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/)
4. [LeetCode #399: Evaluate Division](https://leetcode.com/problems/evaluate-division/)

---

## Disjoint Sets (Union-Find)
A Disjoint Set (also called Union-Find) is a data structure that keeps track of non-overlapping (disjoint) groups of elements.

It supports:
- Efficient detection of whether two elements belong to the same group
- Fast merging of two groups into one

You can think of it as managing social circles — where each person belongs to one circle, and you need to figure out if two people are already connected, or connect them.

### Use Cases
- Kruskal's MST algorithm
- Detecting cycles in an undirected graph
- Finding connected components
- Network connectivity

### Operations
1. **Find(x)**: Returns the final parent (leader) of the set containing x.
    - This operation is optimized with path compression, which flattens the structure of the tree whenever `find` is called, making future queries faster.
2. **Union(x, y)**: Merge two sets containing x and y into a single set.
    - This is optimized with union by rank, which ensures that the smaller tree is always added under the root of the larger tree, keeping the structure balanced.
3. **Connected(x, y)**: Checks if x and y are in the same set by comparing their roots.

### Java Implementation

```java
public class DisjointSet {
  private int[] parent; // parent[i] is the parent of i
  private int[] rank; // rank[i] is the rank (depth) of the tree rooted at i

  /**
   * Constructor to create 'n' disjoint sets (0 to n - 1),
   * where each element starts in its own group.
   */
  public DisjointSet(int n) {
    parent = new int[n];
    rank = new int[n];

    // Initially, every node is its own parent (self-rooted)
    for (int i = 0; i < n; i++) {
      parent[i] = i;
    }
  }

  /**
   * Find the root (representative) of the set that 'x' belongs to.
   * Uses path compression to flatten the tree structure.
   */
  public int find(int x) {
    if (parent[x] != x) {
      parent[x] = find(parent[x]); // Path compression
    }
    return parent[x];
  }

  /**
   * Union the sets containing 'x' and 'y'.
   * Uses union-by-rank to attach smaller tree under the larger one.
   */
  public void union(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);

    if (rootX == rootY) return; // Already in same set

    // Union by rank
    if (rank[rootX] < rank[rootY]) {
      parent[rootX] = rootY;
    } else if (rank[rootX] > rank[rootY]) {
      parent[rootY] = rootX;
    } else {
      parent[rootY] = rootX;
      rank[rootX]++;
    }
  }

  /**
   * Check if two nodes are in the same set.
   */
  public boolean connected(int x, int y) {
    return find(x) == find(y);
  }
}
```

### Cycle Detection in Undirected Graph
A common use of Union-Find is detecting cycles in undirected graphs. If while adding an edge, the two nodes already belong to the same set, a cycle is formed.

```java
import java.util.*;

public class CycleDetectionUsingDisjointSet {

  static class Edge {
    int from;
    int to;

    public Edge(int from, int to) {
      this.from = from;
      this.to = to;
    }
  }

  public boolean hasCycle(List<Edge> edges, int totalNodes) {
    DisjointSet dsu = new DisjointSet(totalNodes);

    for (Edge edge : edges) {
      int rootFrom = dsu.find(edge.from);
      int rootTo = dsu.find(edge.to);

      // If both nodes are already in the same set, cycle exists
      if (rootFrom == rootTo) {
        return true;
      }

      dsu.union(rootFrom, rootTo);
    }

    return false;
  }
}
```

### Time and Space Complexity
- **Time Complexity**:
    - find: O(α(n)) => O(1) for practical purposes
    - union: O(α(n)) => O(1) for practical purposes
- **Space Complexity**:
    - find: O(n) for the parent and rank arrays
    - union: O(n) for the parent and rank arrays

**Notes** : α(n) is practically ≤ 5 for all reasonable n, so operations are near-constant time in practice.

### Example LeetCode Problems
1. [LeetCode #547: Number of Provinces](https://leetcode.com/problems/number-of-provinces/)
2. [LeetCode #684: Redundant Connection](https://leetcode.com/problems/redundant-connection/)
3. [LeetCode #1319: Number of Operations to Make Network Connected](https://leetcode.com/problems/number-of-operations-to-make-network-connected/)
4. [LeetCode #990: Satisfiability of Equality Equations](https://leetcode.com/problems/satisfiability-of-equality-equations/)

---
## Minimum Spanning Tree

An MST of a weighted undirected graph is a tree that spans all the vertices and has the minimum possible total edge weight. It ensures all nodes are connected with the least amount of weight, useful in network design, clustering, etc.

**Algorithms Covered:**
1. **Prim's Algorithm**: Grows the MST by adding the minimum weight edge connecting a vertex in the MST to a vertex outside. Uses a priority queue to efficiently select edges. Best for dense graphs. Time: O(E log V).

2. **Kruskal's Algorithm**: Sorts all edges by weight and adds them one by one, skipping edges that create cycles (using Union-Find). Best for sparse graphs. Time: O(E log E).

---

### Prim's Algorithm
Prim’s algorithm is a greedy algorithm to find the MST of a graph. Starting from any node, it repeatedly adds the nearest vertex not in the MST, until all vertices are included. It uses a priority queue to track the edge with the smallest weight at each step.

### Use Cases
- **Dense graphs**: If the graph has a large number of edges, Prim’s is preferred over Kruskal’s since it avoids sorting edges.
- **Incrementally constructing a minimum spanning tree (MST)**: You start from a node and grow the MST one edge at a time.
- **When edge weights are available** and you need to connect all nodes with the minimum possible total weight.


### Steps
Follows patterns `(SELECT → MARK(*) → WORK → ADD(*))`
- **Initialize**: Create a `min-heap` (priority queue) to store `(toNode, distance)`, and a `visited[]` array. Start with an arbitrary node (usually 0), and add `(0, 0)` to the heap. Initialize `totalMstDistance = 0`.
- **SELECT**: Extract the edge with the minimum distance from the heap.
- **MARK(*)**: If the destination node is already visited, skip it; otherwise, mark it as visited.
- **WORK**: Add the edge’s distance to the total MST cost.
- **ADD(*)**: For all unvisited neighbors of the current node, add them to the heap along with their edge distance.
- **Repeat**: Continue until all nodes are added to the MST (heap is empty or all nodes are visited).

**Note:** In Dijkstra's (to find shortest distance between a source to destinations), the heap stores (shortestDistanceFromSource, node) while in Prim's (to find MST), it stores (currentNode, distance).

### Java Implementation

```java
import java.util.*;

public class PrimMST {
    
    // Represents an edge in the graph
    static class Edge {
        int from;
        int to;
        int weight;
        
        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
    
    /**
     * Computes the total weight of the Minimum Spanning Tree (MST)
     * using Prim's Algorithm with a min-heap.
     *
     * Time Complexity: O(E * log V), where E = edges, V = vertices
     * Space Complexity: O(V + E)
     *
     * @param numVertices Number of vertices in the graph
     * @param graph       Adjacency list where graph[i] contains edges from vertex i
     * @return Total weight of the MST
     */
    public static int computeMST(int numVertices, List<List<Edge>> graph) {
        // Min-heap: prioritize edges with minimum weight
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        
        boolean[] inMST = new boolean[numVertices]; // Track vertices included in MST
        int totalMSTWeight = 0;
        int edgesAdded = 0;
        
        // Start from vertex 0 (arbitrary choice)
        // Use -1 as 'from' for the starting vertex
        minHeap.offer(new Edge(-1, 0, 0));
        
        while (!minHeap.isEmpty() && edgesAdded < numVertices) {
            // SELECT: Extract edge with minimum weight
            Edge current = minHeap.poll();
            int vertex = current.to;
            
            // MARK(*): Skip if vertex already in MST
            if (inMST[vertex]) continue;
            inMST[vertex] = true;
            
            // WORK: Add edge weight to MST total
            totalMSTWeight += current.weight;
            edgesAdded++;
            
            // Optional: Print selected edge
            if (current.from != -1) {
                System.out.printf("Edge added: %d -> %d (weight = %d)%n",
                    current.from, vertex, current.weight);
            }
            
            // ADD(*): Add all edges from current vertex to unvisited neighbors
            for (Edge edge : graph.get(vertex)) {
                if (!inMST[edge.to]) {
                    minHeap.offer(edge);
                }
            }
        }
        
        return totalMSTWeight;
    }
}
```

### Time and Space Complexity
- **Time Complexity**: O(E log V) using a binary heap
- **Space Complexity**: O(V) for the key and parent arrays and the priority queue

### Kruskal's Algorithm
Instead of starting from a node and growing like Prim’s, Kruskal’s starts with all nodes as isolated components, and adds the shortest edge available that does not create a cycle — until all nodes are connected. It heavily relies on a Disjoint Set Union (DSU) or Union-Find data structure to efficiently check whether adding an edge would form a cycle.

Imagine the graph as a bunch of islands (nodes) and edges as bridges between them.
- We want to connect all islands using the cheapest bridges, without forming loops (cycles).
- So we:
  - Sort bridges by cost
  - Use Union-Find to only connect two islands if they’re still separate
  - Repeat until all islands are connected (i.e., MST has V - 1 edges)

### Use Cases
- **Sparse graphs**: When the graph has fewer edges, Kruskal’s algorithm is more efficient since it works by sorting edges and is faster when edges are sparse.
- **Building a minimum spanning tree (MST)**: If the task is to find the MST of a graph, Kruskal's is one of the most straightforward algorithms, particularly if the edges are already sorted.
- **Union-Find operations are efficient**: Kruskal’s works well when you can efficiently manage disjoint sets using Union-Find (or DSU) to check for cycles.

### Steps
1.	Sort all edges in ascending order of weight
2. Initialize a disjoint-set (Union-Find) structure for the nodes
3. For each edge:
   - it connects two different components, include it in MST and merge the components
   - it connects nodes in the same component, skip it (would form a cycle)
4.	Stop when MST has exactly V - 1 edges

### Java Implementation

```java
import java.util.*;

public class KruskalMST {
    
    // Represents a weighted edge between two vertices
    static class Edge {
        int from;
        int to;
        int weight;
        
        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
    
    /**
     * Computes the Minimum Spanning Tree (MST) using Kruskal's Algorithm.
     * Uses greedy approach with Union-Find data structure.
     *
     * Time Complexity: O(E log E) for edge sorting + O(E * α(V)) for Union-Find
     * Space Complexity: O(V + E)
     *
     * @param numVertices Number of vertices in the graph
     * @param edges       List of all edges in the graph
     * @return Total weight of the MST
     */
    public static int computeMST(int numVertices, List<Edge> edges) {
        // Step 1: Sort edges by weight in ascending order
        edges.sort(Comparator.comparingInt(e -> e.weight));
        
        // Step 2: Initialize Union-Find (Disjoint Set)
        int[] parent = new int[numVertices];
        int[] rank = new int[numVertices];
        
        for (int i = 0; i < numVertices; i++) {
            parent[i] = i;  // Each vertex is its own parent initially
            rank[i] = 0;    // Rank for union-by-rank optimization
        }
        
        int totalWeight = 0;
        int edgesAdded = 0;
        
        // Step 3: Process edges in sorted order
        for (Edge edge : edges) {
            int rootFrom = find(edge.from, parent);
            int rootTo = find(edge.to, parent);
            
            // Only add edge if it connects two different components (no cycle)
            if (rootFrom != rootTo) {
                union(rootFrom, rootTo, parent, rank);
                totalWeight += edge.weight;
                edgesAdded++;
                
                System.out.printf("Edge added: %d -> %d (weight = %d)%n",
                    edge.from, edge.to, edge.weight);
                
                // MST is complete when we have V-1 edges
                if (edgesAdded == numVertices - 1) {
                    break;
                }
            }
        }
        
        return totalWeight;
    }
    
    /**
     * Find operation with path compression.
     * Returns the root of the component containing the given vertex.
     */
    private static int find(int vertex, int[] parent) {
        if (parent[vertex] != vertex) {
            parent[vertex] = find(parent[vertex], parent); // Path compression
        }
        return parent[vertex];
    }
    
    /**
     * Union operation with union-by-rank.
     * Merges two components efficiently.
     */
    private static void union(int root1, int root2, int[] parent, int[] rank) {
        // Attach smaller rank tree under root of higher rank tree
        if (rank[root1] < rank[root2]) {
            parent[root1] = root2;
        } else if (rank[root1] > rank[root2]) {
            parent[root2] = root1;
        } else {
            parent[root2] = root1;
            rank[root1]++;
        }
    }
}
```

### Time and Space Complexity
- **Time Complexity**: O(E log E) or O(E log V) due to sorting of edges
- **Space Complexity**: O(V + E) for the disjoint-set and the result

### Example LeetCode Problems for MST
1. [LeetCode #1584: Min Cost to Connect All Points](https://leetcode.com/problems/min-cost-to-connect-all-points/)
2. [LeetCode #1135: Connecting Cities With Minimum Cost](https://leetcode.com/problems/connecting-cities-with-minimum-cost/)
3. [LeetCode #1489: Find Critical and Pseudo-Critical Edges in Minimum Spanning Tree](https://leetcode.com/problems/find-critical-and-pseudo-critical-edges-in-minimum-spanning-tree/)

---

## Articulation Points and Bridges

Articulation points (or cut vertices) are vertices in an undirected graph whose removal increases the number of connected components. Bridges are edges whose removal increases the number of connected components. These are critical for understanding the vulnerability of a network.

### Use Cases
- Network reliability analysis
- Critical infrastructure identification

### Steps for Finding Articulation Points
1. Use DFS to traverse the graph
2. For each vertex, track:
    - Discovery time
    - Lowest vertex reachable from its subtree
3. A vertex is an articulation point if either:
    - It is the root of the DFS tree and has at least two children
    - It is not the root and has a child whose lowest reachable vertex is not an ancestor of the vertex

### Java Implementation

```java
import java.util.*;

public class ArticulationPointsAndBridges {
    private int time = 0;
    
    // Find articulation points in an undirected graph
    public List<Integer> findArticulationPoints(List<List<Integer>> graph) {
        int V = graph.size();
        boolean[] visited = new boolean[V];
        int[] disc = new int[V];    // Discovery times of vertices
        int[] low = new int[V];     // Earliest visited vertex reachable from subtree
        int[] parent = new int[V];  // Parent vertices in DFS tree
        boolean[] ap = new boolean[V]; // To mark articulation points
        
        Arrays.fill(parent, -1);
        
        // Call the recursive helper function for all unvisited vertices
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfsArticulationPoint(graph, i, visited, disc, low, parent, ap);
            }
        }
        
        // Collect articulation points
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            if (ap[i]) {
                result.add(i);
            }
        }
        
        return result;
    }
    
    private void dfsArticulationPoint(List<List<Integer>> graph, int u, boolean[] visited, 
                                    int[] disc, int[] low, int[] parent, boolean[] ap) {
        // Count of children in DFS tree
        int children = 0;
        
        // Mark the current node as visited
        visited[u] = true;
        
        // Initialize discovery time and low value
        disc[u] = low[u] = ++time;
        
        // Go through all neighbors
        for (int v : graph.get(u)) {
            // Skip if v is parent of u
            if (parent[u] == v) {
                continue;
            }
            
            // If v is not visited yet, make it a child in DFS tree
            if (!visited[v]) {
                children++;
                parent[v] = u;
                
                // Recursive call for the neighbor
                dfsArticulationPoint(graph, v, visited, disc, low, parent, ap);
                
                // Check if subtree rooted at v has a connection to an ancestor of u
                low[u] = Math.min(low[u], low[v]);
                
                // u is an articulation point in following cases:
                // 1) u is root of DFS tree and has two or more children
                if (parent[u] == -1 && children > 1) {
                    ap[u] = true;
                }
                
                // 2) u is not root and low value of one of its children is greater than or equal to discovery value of u
                if (parent[u] != -1 && low[v] >= disc[u]) {
                    ap[u] = true;
                }
            }
            // Update low value of u for parent function calls
            else if (v != parent[u]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }
    
    // Class to represent a bridge edge
    static class Bridge {
        int src, dest;
        
        public Bridge(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }
    }
    
    // Find bridges in an undirected graph
    public List<Bridge> findBridges(List<List<Integer>> graph) {
        int V = graph.size();
        boolean[] visited = new boolean[V];
        int[] disc = new int[V];
        int[] low = new int[V];
        int[] parent = new int[V];
        List<Bridge> bridges = new ArrayList<>();
        
        Arrays.fill(parent, -1);
        time = 0;
        
        // Call the recursive helper function for all unvisited vertices
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfsBridge(graph, i, visited, disc, low, parent, bridges);
            }
        }
        
        return bridges;
    }
    
    private void dfsBridge(List<List<Integer>> graph, int u, boolean[] visited,
                         int[] disc, int[] low, int[] parent, List<Bridge> bridges) {
        // Mark the current node as visited
        visited[u] = true;
        
        // Initialize discovery time and low value
        disc[u] = low[u] = ++time;
        
        // Go through all neighbors
        for (int v : graph.get(u)) {
            // Skip if v is parent of u
            if (parent[u] == v) {
                continue;
            }
            
            // If v is not visited yet, make it a child in DFS tree
            if (!visited[v]) {
                parent[v] = u;
                
                // Recursive call for the neighbor
                dfsBridge(graph, v, visited, disc, low, parent, bridges);
                
                // Check if subtree rooted at v has a connection to an ancestor of u
                low[u] = Math.min(low[u], low[v]);
                
                // If the lowest vertex reachable from subtree under v is
                // below u in DFS tree, then u-v is a bridge
                if (low[v] > disc[u]) {
                    bridges.add(new Bridge(u, v));
                }
            }
            // Update low value of u for parent function calls
            else if (v != parent[u]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }
}
```

### Time and Space Complexity
- **Time Complexity**: O(V + E) as we do a simple DFS traversal
- **Space Complexity**: O(V) for the visited, discovery time, low, and parent arrays

### Example LeetCode Problems
1. [LeetCode #1192: Critical Connections in a Network](https://leetcode.com/problems/critical-connections-in-a-network/)
2. [LeetCode #1568: Minimum Number of Days to Disconnect Island](https://leetcode.com/problems/minimum-number-of-days-to-disconnect-island/)

---
---

## Kosaraju's Algorithm

Kosaraju's algorithm finds all strongly connected components (SCCs) in a directed graph.

### Use Cases
- Finding closely related items in social networks
- Decomposing complex systems
- Compiler optimizations

### Steps
1. Perform a DFS traversal of the graph and push vertices to a stack as they finish
2. Create the transpose of the graph (reverse all edges)
3. Pop vertices from the stack one by one and do a DFS on the transposed graph
4. Each DFS traversal gives one SCC

### Java Implementation

```java
import java.util.*;

public class KosarajuSCC {
    // Find all strongly connected components using Kosaraju's algorithm
    public List<List<Integer>> findSCCs(List<List<Integer>> graph) {
        int V = graph.size();
        boolean[] visited = new boolean[V];
        Stack<Integer> stack = new Stack<>();
        List<List<Integer>> result = new ArrayList<>();
        
        // Step 1: Fill vertices in stack according to their finishing times
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                fillOrder(graph, i, visited, stack);
            }
        }
        
        // Step 2: Create the transpose of the graph
        List<List<Integer>> transposedGraph = transposeGraph(graph);
        
        // Reset visited array
        Arrays.fill(visited, false);
        
        // Step 3: Process all vertices in the stack
        while (!stack.isEmpty()) {
            int v = stack.pop();
            
            // Print SCC of popped vertex
            if (!visited[v]) {
                List<Integer> scc = new ArrayList<>();
                dfsUtil(transposedGraph, v, visited, scc);
                result.add(scc);
            }
        }
        
        return result;
    }
    
    // Fills vertices in stack according to their finishing times
    private void fillOrder(List<List<Integer>> graph, int v, boolean[] visited, Stack<Integer> stack) {
        visited[v] = true;
        
        // Recur for all adjacent vertices
        for (int adj : graph.get(v)) {
            if (!visited[adj]) {
                fillOrder(graph, adj, visited, stack);
            }
        }
        
        // Push current vertex to stack after all its adjacent vertices are processed
        stack.push(v);
    }
    
    // Function to get the transpose of a graph
    private List<List<Integer>> transposeGraph(List<List<Integer>> graph) {
        int V = graph.size();
        List<List<Integer>> transposed = new ArrayList<>(V);

        for (int i = 0; i < V; i++) {
            transposed.add(new ArrayList<>());
        }

        for (int v = 0; v < V; v++) {
            // For every edge v->u, add edge u->v in transposed graph
            for (int u : graph.get(v)) {
                transposed.get(u).add(v);
            }
        }

        return transposed;
    }

    // DFS utility function
    private void dfsUtil(List<List<Integer>> graph, int v, boolean[] visited, List<Integer> scc) {
        visited[v] = true;
        scc.add(v);

        // Recur for all adjacent vertices
        for (int adj : graph.get(v)) {
            if (!visited[adj]) {
                dfsUtil(graph, adj, visited, scc);
            }
        }
    }
}
```

### Time and Space Complexity
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V) for the visited array and the stack

### Example LeetCode Problems
1. [LeetCode #1192: Critical Connections in a Network](https://leetcode.com/problems/critical-connections-in-a-network/)
2. [LeetCode #802: Find Eventual Safe States](https://leetcode.com/problems/find-eventual-safe-states/)

---

## Eulerian Path, Circuit & Graph

### What it is
- An Euler Path is a trail that visits every edge exactly once, but does not necessarily end where it started.
- An Euler Circuit (Cycle) is a path that visits every edge exactly once and starts and ends at the same vertex.
- A graph that has an Euler Circuit is called an Eulerian Graph.
- A graph that has only an Euler Path (not a circuit) is called a Semi-Eulerian Graph.

### Key rules (for undirected graphs):
- Eulerian Graph: All vertices have even degree, and the graph is connected.
- Semi-Eulerian Graph: Exactly 2 vertices have odd degree, and the graph is connected.
- If more than 2 vertices have odd degree → No Euler Path or Circuit.

### Use Cases
- Route planning (e.g., garbage truck, mail delivery)
- Drawing problems (e.g., draw without lifting pen)
- Bioinformatics (e.g., genome assembly using de Bruijn graphs)
- Leetcode problems:
  - Reconstruct Itinerary (LC 332)
  - Cracking the Safe (LC 753)

### Steps
To find an Eulerian Circuit (or Path), we use Hierholzer’s Algorithm:
1. Start from a vertex with:
   - Any vertex (for circuit)
   - A vertex with odd degree (for path)
2.	Use DFS to explore and greedily follow edges, removing them after use.
3.	When you reach a dead-end, backtrack and add the vertex to result.
4.	Reverse the result at the end to get the Eulerian Path/Circuit.


### Java Implementation

Assumes the graph is connected and either Eulerian or Semi-Eulerian.

```java
import java.util.*;

public class EulerianPathFinder {
    
    /**
     * Finds an Eulerian Path or Circuit in an undirected graph.
     * Uses Hierholzer's Algorithm with DFS.
     * Assumes the graph is connected and has at most two vertices of odd degree.
     *
     * @param graph Map of vertex to list of adjacent vertices
     * @return List of vertices representing the Eulerian Path or Circuit
     */
    public List<String> findEulerianPath(Map<String, List<String>> graph) {
        // Create a working copy (will be modified during traversal)
        Map<String, Deque<String>> adjacency = new HashMap<>();
        for (String vertex : graph.keySet()) {
            adjacency.put(vertex, new ArrayDeque<>(graph.get(vertex)));
        }
        
        // Find the starting vertex
        String startVertex = findStartVertex(adjacency);
        List<String> path = new ArrayList<>();
        
        // Perform DFS to build the path
        buildPath(startVertex, adjacency, path);
        
        // Reverse to get the correct order (nodes added in reverse during DFS)
        Collections.reverse(path);
        return path;
    }
    
    /**
     * Recursive DFS to construct the Eulerian path.
     * Removes edges as they are traversed.
     */
    private void buildPath(String vertex, Map<String, Deque<String>> adjacency, List<String> path) {
        Deque<String> neighbors = adjacency.get(vertex);
        
        while (neighbors != null && !neighbors.isEmpty()) {
            String nextVertex = neighbors.pollFirst(); // Remove edge
            
            // For undirected graph, also remove reverse edge
            adjacency.get(nextVertex).remove(vertex);
            
            // Recursively visit next vertex
            buildPath(nextVertex, adjacency, path);
        }
        
        // Add vertex to path after all its edges are explored
        path.add(vertex);
    }
    
    /**
     * Finds the appropriate starting vertex:
     * - For Euler Path: vertex with odd degree
     * - For Euler Circuit: any vertex
     */
    private String findStartVertex(Map<String, Deque<String>> adjacency) {
        String start = null;
        
        for (String vertex : adjacency.keySet()) {
            int degree = adjacency.get(vertex).size();
            
            // If odd degree, must start here for Euler Path
            if (degree % 2 == 1) {
                return vertex;
            }
            
            // Keep track of any vertex as fallback
            if (start == null) {
                start = vertex;
            }
        }
        
        return start;
    }
}
```
