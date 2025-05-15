# Graph Algorithms Handbook for Technical Interviews

## Table of Contents
- [Introduction to Graphs](#introduction-to-graphs)
- [Breadth-First Search (BFS)](#breadth-first-search-bfs) `(SELECT → MARK(*) → WORK → ADD(*))` [In Queue]
- [Depth-First Search (DFS)](#depth-first-search-dfs) `(SELECT → MARK(*) → WORK → ADD(*))` [In Stack]
- [Topological Sort](#topological-sort)
    - [Topological Sort using DFS](#dfs-based-topological-sort-implementation)
    - [Kahn's Algorithm (PREFERRED)](#kahns-algorithm-based-appraoch) `(SELECT → MARK(*) → WORK → ADD(*))` [In Queue with 0 In-Degree]
- [Shortest Path Algorithms](#shortest-path-algorithms)
    - [Dijkstra's Algorithm](#dijkstras-algorithm) `(SELECT → MARK(*) → WORK → ADD(*))` [In Min-Heap]
    - [Bellman-Ford Algorithm](#bellman-ford-algorithm)
    - [Floyd-Warshall Algorithm](#floyd-warshall-algorithm)
- [Minimum Spanning Tree](#minimum-spanning-tree)
    - [Prim's Algorithm](#prims-algorithm) `(SELECT → MARK(*) → WORK → ADD(*))`
    - [Kruskal's Algorithm](#kruskals-algorithm)
- [Articulation Points and Bridges](#articulation-points-and-bridges)
- [Kosaraju's Algorithm](#kosarajus-algorithm)
- [Disjoint Sets (Union-Find)](#disjoint-sets-union-find)

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
            // Pop a vertex from stack
            int vertex = stack.pop();
            
            // Process the vertex if not visited
            if (!visited[vertex]) {
                System.out.print(vertex + " ");
                visited[vertex] = true;
            }
            
            // Get all adjacent vertices
            // Push all unvisited neighbors to stack
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

## Topological Sort

Topological sort is the process of ordering the vertices of a directed graph such that for every directed edge u → v, vertex u comes before v in the ordering. This is primarily used in directed acyclic graphs (DAGs) and is helpful in scheduling tasks.

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
 * TopologicalSortDFS performs a topological sort of a directed acyclic graph (DAG)
 * using Depth-First Search (DFS).
 *
 * <p><strong>Approach:</strong>
 * We perform a DFS traversal of the graph. When visiting a node, we recursively visit
 * all its unvisited neighbors. After all neighbors of a node are visited, we add the node
 * to a stack. This ensures that all dependencies of a node are placed before it in the final ordering.
 *
 * <p><strong>Steps:</strong>
 * 1. Initialize a visited array to track visited nodes.
 * 2. Perform DFS on unvisited nodes and push them to a stack after their neighbors are fully explored.
 * 3. Pop elements from the stack to build the topological order.
 *
 * <p><strong>Time Complexity:</strong> O(V + E), where V is number of vertices and E is number of edges.
 * <p><strong>Space Complexity:</strong> O(V) for visited array and stack.
 */
public class TopologicalSortDFS {

  /**
   * Performs topological sort on the given directed graph using DFS.
   *
   * @param adjacencyList The adjacency list representing the graph.
   *                      adjacencyList.get(u) contains a list of all vertices v such that there's an edge u → v.
   * @return A list of vertices in topological order.
   */
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

### Kahn's Algorithm based appraoch
Kahn’s algorithm is a method of topological sorting for DAGs using in-degree tracking. Nodes with zero in-degree (no dependencies) are processed first, and their removal decreases the in-degree of their neighbors. This process continues until all nodes are sorted or a cycle is detected.

#### Steps:
Follows patterns `(SELECT → MARK(*) → WORK → ADD(*))`
- **Initialize**: Compute `in-degree` of all nodes. Create a `queue` and enqueue all nodes with in-degree = 0.
- **SELECT**: Dequeue a node from the front of the queue.
- **MARK(*)**: Conceptually mark this node as processed by adding it to the result list.
- **WORK**: Process the node (e.g., store it in the topological order list).
- **ADD(*)**: For each neighbor of the current node, reduce its in-degree by 1. If in-degree becomes 0, enqueue the neighbor.
- **Repeat**: Continue until the queue is empty. If the result list size is less than the total nodes, then there is a cycle in the graph.

### Java Implementation
```java
import java.util.*;

/**
 * TopologicalSortKahn performs topological sorting of a Directed Acyclic Graph (DAG)
 * using Kahn’s Algorithm (BFS-based approach).
 *
 * <p><strong>Steps (Kahn’s Algorithm):</strong>
 * 1. Calculate the in-degree of all vertices.
 * 2. Add all vertices with in-degree 0 to a queue.
 * 3. Repeatedly dequeue a vertex, add it to the result, and decrease the in-degree
 *    of its neighbors. If any neighbor’s in-degree becomes 0, enqueue it.
 * 4. If we process all vertices, the result is a valid topological order.
 *    Otherwise, the graph has a cycle.
 *
 * <p><strong>Time Complexity:</strong> O(V + E)  
 * <strong>Space Complexity:</strong> O(V)
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
      int current = zeroInDegreeQueue.poll();
      topologicalOrder.add(current);
      processedCount++;

      // Decrease in-degree of adjacent vertices
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

## Shortest Path Algorithms
These algorithms are used to find the shortest path between nodes in a graph. Depending on the graph structure (weighted/unweighted, directed/undirected), various algorithms can be used, such as Dijkstra’s, Bellman-Ford, or Floyd-Warshall.

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
 * Dijkstra's Algorithm - Single Source Shortest Path for Weighted Directed Graph
 *
 * ✅ Steps:
 * 1. Initialize all distances as infinity (except source = 0).
 * 2. Use a priority queue to fetch the node with the minimum distance.
 * 3. For each neighbor, check if the new path is shorter than the known path and update.
 * 4. Repeat until all nodes are processed.
 *
 * ✅ Time Complexity: O((V + E) * log V) using PriorityQueue
 * ✅ Space Complexity: O(V) for distance array and priority queue
 */
import java.util.*;

// Class representing an edge in the graph
static class Edge {
  int source;
  int destination;
  int weight;

  public Edge(int source, int destination, int weight) {
    this.source = source;
    this.destination = destination;
    this.weight = weight;
  }
}

// Helper class for priority queue
static class NodeInfo {
  int currentNode;
  String pathSoFar;
  int totalWeightSoFar;

  public NodeInfo(int currentNode, String pathSoFar, int totalWeightSoFar) {
    this.currentNode = currentNode;
    this.pathSoFar = pathSoFar;
    this.totalWeightSoFar = totalWeightSoFar;
  }
}

// Dijkstra's algorithm implementation
private void dijkstraShortestPaths(int numberOfVertices, ArrayList<Edge>[] graph, int source) {
  // Distance array: stores shortest distance from source to each node
  int[] shortestDistance = new int[numberOfVertices + 1];
  Arrays.fill(shortestDistance, Integer.MAX_VALUE);
  shortestDistance[source] = 0;

  // Min-heap to always pick the node with the smallest distance
  PriorityQueue<NodeInfo> minHeap = new PriorityQueue<>(Comparator.comparingInt(n -> n.totalWeightSoFar));
  minHeap.offer(new NodeInfo(source, source + "", 0));

  // Visited array to avoid reprocessing nodes
  boolean[] visited = new boolean[numberOfVertices + 1];

  while (!minHeap.isEmpty()) {
    NodeInfo current = minHeap.poll();

    // Skip if already visited
    if (visited[current.currentNode]) continue;
    visited[current.currentNode] = true;

    // Output the path and cost from source to current node
    System.out.printf("To node: %d | Path: %s | Total weight: %d%n",
        current.currentNode, current.pathSoFar, current.totalWeightSoFar);

    // Explore neighbors
    for (Edge edge : graph[current.currentNode]) {
      int neighbor = edge.destination;
      int newDistance = current.totalWeightSoFar + edge.weight;

      // Only consider if not visited and new path is shorter
      if (!visited[neighbor] && newDistance < shortestDistance[neighbor]) {
        shortestDistance[neighbor] = newDistance;
        minHeap.offer(new NodeInfo(neighbor, current.pathSoFar + "->" + neighbor, newDistance));
      }
    }
  }
}
```

### Time and Space Complexity
- **Time Complexity**: O((V + E) log V) using a binary heap priority queue
- **Space Complexity**: O(V) for the distance array and the priority queue

## Bellman-Ford Algorithm
Bellman-Ford is a single-source shortest path algorithm that works even with negative edge weights. It relaxes all edges up to n-1 times (where n is the number of nodes) and can detect negative weight cycles, unlike Dijkstra’s algorithm.

### Use Cases
- **Graphs with negative edge weights**: Bellman-Ford can handle graphs with negative weight edges, unlike Dijkstra’s.
- **Detecting negative cycles**: If there’s a cycle in the graph where the total weight is negative, Bellman-Ford can detect this.
- **Shortest path in graphs where edge weights may change dynamically**: Unlike Dijkstra, Bellman-Ford is simpler for dealing with edge weights that change over time.


### Steps
1. Initialize distances from source to all vertices as infinite and distance to source as 0
2. Relax all edges V-1 times. For each edge (u, v) with weight w, if dist[u] + w < dist[v], then update dist[v] = dist[u] + w
3. Check for negative-weight cycles. For each edge (u, v) with weight w, if dist[u] + w < dist[v], then graph contains a negative-weight cycle

### Java Implementation
<b><u>Need to understand this properly</u></b>
```java
import java.util.*;

/**
 * Bellman-Ford Algorithm - Single Source Shortest Path for Graphs with Negative Weights
 *
 * ✅ Intuition:
 * - Bellman-Ford computes shortest distances from a single source to all other vertices.
 * - Unlike Dijkstra, it works with graphs that contain **negative weights**.
 * - The algorithm relaxes all edges V-1 times. If we can relax any edge further, a **negative-weight cycle** exists.
 *
 * ✅ Use Case:
 * - Graphs with **negative weights** (e.g., currency arbitrage, routing protocols like RIP)
 *
 * ✅ Time Complexity: O(V * E)
 * ✅ Space Complexity: O(V)
 */
public class BellmanFord {

  /**
   * Class representing a weighted edge in a directed graph.
   */
  static class Edge {
    int source, destination, weight;

    public Edge(int source, int destination, int weight) {
      this.source = source;
      this.destination = destination;
      this.weight = weight;
    }
  }

  /**
   * Graph representation using edge list.
   */
  static class Graph {
    int numVertices, numEdges;
    List<Edge> edgeList;

    public Graph(int numVertices, int numEdges) {
      this.numVertices = numVertices;
      this.numEdges = numEdges;
      edgeList = new ArrayList<>(numEdges);
    }

    public void addEdge(int src, int dest, int weight) {
      edgeList.add(new Edge(src, dest, weight));
    }
  }

  /**
   * Executes Bellman-Ford algorithm from a source node and returns shortest distances.
   *
   * @param graph The input graph
   * @param source The starting vertex
   * @return Shortest distances from source to each vertex; null if negative-weight cycle exists.
   */
  public static int[] bellmanFord(Graph graph, int source) {
    int V = graph.numVertices;
    int E = graph.numEdges;
    int[] distance = new int[V];
    Arrays.fill(distance, Integer.MAX_VALUE);
    distance[source] = 0;

    // Step 1: Relax all edges V - 1 times
    /**
     * Logic : For each edge (u, v) with weight w, if distance[u] + w < distance[v], then update distance[v] = distance[u] + w.
     * This means we can reach vertex v from u with a shorter path than before.
     * We repeat this process for V - 1 times to ensure all paths are relaxed.
     */
    for (int i = 0; i < V - 1; i++) {
      for (Edge edge : graph.edgeList) {
        int currentSource = edge.source;
        int currentDestination = edge.destination;
        int weight = edge.weight;

        if (distance[currentSource] != Integer.MAX_VALUE && distance[currentSource] + weight < distance[currentDestination]) {
          distance[currentDestination] = distance[currentSource] + weight;
        }
      }
    }

    // Step 2: Check for negative-weight cycles
    // Logic: If we can still relax any edge, a negative cycle exists
    // This means we can reach a vertex with a shorter path than before
    // If we can relax any edge, it indicates a negative cycle
    // We can do this by iterating through all edges again
    for (Edge edge : graph.edgeList) {
      int currentSource = edge.source;
      int currentDestination = edge.destination;
      int weight = edge.weight;

      if (distance[currentSource] != Integer.MAX_VALUE && distance[currentSource] + weight < distance[currentDestination]) {
        System.out.println("Graph contains a negative-weight cycle");
        return null;
      }
    }

    return distance;
  }
}
```

#### Time and Space Complexity
- **Time Complexity**: O(V × E)
- **Space Complexity**: O(V) for the distance array

### Floyd-Warshall Algorithm
Floyd-Warshall is an all-pairs shortest path algorithm for graphs with both positive and negative edge weights (without negative weight cycles). It uses dynamic programming to compute the shortest paths between all pairs of vertices, iterating over all pairs and possible intermediates.
<b><u>This is not important for interviews</u></b>

#### Use Cases
- **All-pairs shortest path**: If you need the shortest path between every pair of nodes in a graph, especially when the graph is relatively small.
- **Graph with dense edges**: Floyd-Warshall is particularly effective when the graph is dense, as it computes all distances in one go.
- **Handling negative weights**: Like Bellman-Ford, it can handle graphs with negative weights, but it handles all pairs simultaneously.


#### Steps
1. Initialize the distance matrix with direct edge weights (use infinity for non-adjacent vertices)
2. For each vertex k as an intermediate:
    - For each pair of vertices (i, j):
        - If dist[i][j] > dist[i][k] + dist[k][j], update dist[i][j] = dist[i][k] + dist[k][j]

#### Java Implementation

```java
import java.util.*;

/**
 * FloydWarshallAlgorithm computes the shortest distances between all pairs of vertices
 * in a weighted directed graph using dynamic programming.
 *
 * ✅ Steps:
 * 1. Initialize the distance matrix using the input graph matrix.
 * 2. For each vertex k (intermediate), update all dist[u][v] if u → k → v is shorter.
 * 3. After all updates, check if any diagonal element dist[v][v] < 0 → negative weight cycle.
 *
 * ⚠️ Assumes INF (Integer.MAX_VALUE) for no direct edge.
 */
public class FloydWarshallAlgorithm {

  private static final int INF = Integer.MAX_VALUE;

  /**
   * Computes shortest distances between all pairs of vertices.
   *
   * @param graph Adjacency matrix representing the graph. graph[u][v] = weight of edge u→v or INF.
   * @return Distance matrix with shortest distances between all pairs, or null if negative cycle exists.
   */
  public static int[][] computeAllPairsShortestPaths(int[][] graph) {
    int numVertices = graph.length;
    int[][] distance = new int[numVertices][numVertices];

    // Step 1: Initialize the distance matrix with the input graph
    for (int source = 0; source < numVertices; source++) {
      for (int destination = 0; destination < numVertices; destination++) {
        distance[source][destination] = graph[source][destination];
      }
    }

    // Step 2: Dynamic programming update
    for (int intermediate = 0; intermediate < numVertices; intermediate++) {
      for (int source = 0; source < numVertices; source++) {
        for (int destination = 0; destination < numVertices; destination++) {
          // Only update if both source→intermediate and intermediate→destination paths exist
          if (distance[source][intermediate] != INF && distance[intermediate][destination] != INF) {
            int newDistance = distance[source][intermediate] + distance[intermediate][destination];
            if (newDistance < distance[source][destination]) {
              distance[source][destination] = newDistance;
            }
          }
        }
      }
    }

    // Step 3: Check for negative weight cycles
    for (int vertex = 0; vertex < numVertices; vertex++) {
      if (distance[vertex][vertex] < 0) {
        System.out.println("Graph contains a negative weight cycle.");
        return null;
      }
    }

    return distance;
  }
}
```

#### Time and Space Complexity
- **Time Complexity**: O(V³)
- **Space Complexity**: O(V²) for the distance matrix

#### Example LeetCode Problems for Shortest Path Algorithms
1. [LeetCode #743: Network Delay Time](https://leetcode.com/problems/network-delay-time/)
2. [LeetCode #787: Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/)
3. [LeetCode #1334: Find the City With the Smallest Number of Neighbors at a Threshold Distance](https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/)
4. [LeetCode #399: Evaluate Division](https://leetcode.com/problems/evaluate-division/)

### Minimum Spanning Tree
An MST of a weighted undirected graph is a tree that spans all the vertices and has the minimum possible total edge weight. It ensures all nodes are connected with the least amount of weight, useful in network design, clustering, etc.

### Prim's Algorithm
Prim’s algorithm is a greedy algorithm to find the MST of a graph. Starting from any node, it repeatedly adds the nearest vertex not in the MST, until all vertices are included. It uses a priority queue to track the edge with the smallest weight at each step.

### Use Cases
- **Dense graphs**: If the graph has a large number of edges, Prim’s is preferred over Kruskal’s since it avoids sorting edges.
- **Incrementally constructing a minimum spanning tree (MST)**: You start from a node and grow the MST one edge at a time.
- **When edge weights are available** and you need to connect all nodes with the minimum possible total weight.


### Steps
Follows patterns `(SELECT → MARK(*) → WORK → ADD(*))`
- **Initialize**: Create a `min-heap` (priority queue) to store `(toNode, weight)`, and a `visited[]` array. Start with an arbitrary node (usually 0), and add `(0, 0)` to the heap. Initialize `minCost = 0`.
- **SELECT**: Extract the edge with the minimum weight from the heap.
- **MARK(*)**: If the destination node is already visited, skip it; otherwise, mark it as visited.
- **WORK**: Add the edge’s weight to the total MST cost.
- **ADD(*)**: For all unvisited neighbors of the current node, add them to the heap along with their edge weight.
- **Repeat**: Continue until all nodes are added to the MST (heap is empty or all nodes are visited).

### Java Implementation

```java
import java.util.*;

// Represents an edge between two nodes with a weight
class Edge {
  int from;     // Source vertex of the edge
  int to;       // Destination vertex of the edge
  int weight;   // Weight of the edge

  public Edge(int from, int to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }
}

public class PrimMST {

  /**
   * Prim's Algorithm to find the Minimum Spanning Tree (MST) of a connected, undirected graph.
   *
   * Intuition:
   * - Start from any vertex (here, vertex 0).
   * - Maintain a min-heap to always pick the edge with the smallest weight that connects
   *   a visited vertex to an unvisited one.
   * - Use a visited array to track vertices already included in the MST.
   * - Add edges only if they connect to unvisited vertices.
   *
   * Time Complexity: O(E * log V) where E = number of edges, V = number of vertices
   * Space Complexity: O(V + E)
   *
   * @param numOfVertices - Total number of vertices in the graph
   * @param adjList - Adjacency list representing the graph
   * @return Total weight of the MST
   */
  public int primMST(int numOfVertices, List<List<Edge>> adjList) {
    // Min-heap to pick the edge with the minimum weight at every step
    PriorityQueue<Edge> minHeap = new PriorityQueue<>((a, b) -> a.weight - b.weight);

    // Track vertices already included in the MST
    boolean[] visited = new boolean[numOfVertices];

    // Start from vertex 0: we use -1 as 'from' since this is the starting node
    minHeap.offer(new Edge(-1, 0, 0));

    int mstWeight = 0;

    while (!minHeap.isEmpty()) {
      Edge currentEdge = minHeap.poll();

      int currentVertex = currentEdge.to;

      // If already included in MST, skip
      if (visited[currentVertex]) continue;

      // Mark the vertex as included in MST
      visited[currentVertex] = true;

      // Add the edge's weight to the total MST weight
      mstWeight += currentEdge.weight;

      // Debug print (optional): Show selected edge
      if (currentEdge.from != -1) {
        System.out.printf("Edge selected: from %d to %d with weight %d%n",
            currentEdge.from, currentEdge.to, currentEdge.weight);
      }

      // Add all adjacent unvisited edges to the heap
      for (Edge neighborEdge : adjList.get(currentVertex)) {
        if (!visited[neighborEdge.to]) {
          minHeap.offer(neighborEdge);
        }
      }
    }

    return mstWeight;
  }
}
```

### Time and Space Complexity
- **Time Complexity**: O(E log V) using a binary heap
- **Space Complexity**: O(V) for the key and parent arrays and the priority queue

### Kruskal's Algorithm
Kruskal’s algorithm is another greedy algorithm for finding the MST. It works by sorting all edges in increasing order of weight and then adding the smallest edge to the MST, ensuring no cycles are formed. It uses the union-find data structure to detect cycles efficiently.

### Use Cases
- **Sparse graphs**: When the graph has fewer edges, Kruskal’s algorithm is more efficient since it works by sorting edges and is faster when edges are sparse.
- **Building a minimum spanning tree (MST)**: If the task is to find the MST of a graph, Kruskal's is one of the most straightforward algorithms, particularly if the edges are already sorted.
- **Union-Find operations are efficient**: Kruskal’s works well when you can efficiently manage disjoint sets using Union-Find (or DSU) to check for cycles.


### Steps
1. Sort all edges in increasing order of their weight
2. Initialize a disjoint-set for tracking connected components. Disjoint set is a data structure that keeps track of a set of elements partitioned into disjoint subsets.
3. For each edge:
    - If the edge connects two different components, include it in the MST and union the components
    - If the edge connects two vertices in the same component, skip it (to avoid cycles)
4. Stop when we have V-1 edges in the MST

### Java Implementation

```java
import java.util.*;

class Edge {
  int from;
  int to;
  int weight;

  public Edge(int from, int to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }
}

public class KruskalMST {

  /**
   * Kruskal’s Algorithm to find Minimum Spanning Tree (MST) of a connected, undirected graph.
   *
   * Intuition:
   * - Sort all edges by increasing weight.
   * - Use Union-Find (Disjoint Set Union) to avoid forming cycles.
   * - Keep adding the lightest edge that connects two different components.
   *
   * Time Complexity: O(E log E) — sorting edges dominates
   * Space Complexity: O(V) — for union-find structures
   *
   * @param numOfVertices Number of vertices in the graph
   * @param edges List of all edges in the graph
   * @return Total weight of the MST
   */
  public int kruskalMST(int numOfVertices, List<Edge> edges) {
    // Sort all edges by weight
    edges.sort(Comparator.comparingInt(edge -> edge.weight));

    // Initialize Union-Find (Disjoint Set Union) structures
    int[] parent = new int[numOfVertices];
    int[] rank = new int[numOfVertices];

    for (int i = 0; i < numOfVertices; i++) {
      parent[i] = i;
      rank[i] = 0;
    }

    int mstWeight = 0;
    int edgesUsed = 0;

    for (Edge edge : edges) {
      int rootFrom = find(edge.from, parent);
      int rootTo = find(edge.to, parent);

      // If from and to are in different components, add the edge to MST
      if (rootFrom != rootTo) {
        union(rootFrom, rootTo, parent, rank);
        mstWeight += edge.weight;
        edgesUsed++;

        System.out.printf("Edge added: from %d to %d with weight %d%n",
            edge.from, edge.to, edge.weight);
      }

      // Early stopping: MST will have (V - 1) edges
      if (edgesUsed == numOfVertices - 1) break;
    }

    return mstWeight;
  }

  // Union-Find helper to find root of a node with path compression
  private int find(int node, int[] parent) {
    if (parent[node] != node) {
      parent[node] = find(parent[node], parent); // Path compression
    }
    return parent[node];
  }

  // Union by rank
  private void union(int u, int v, int[] parent, int[] rank) {
    if (rank[u] < rank[v]) {
      parent[u] = v;
    } else if (rank[u] > rank[v]) {
      parent[v] = u;
    } else {
      parent[v] = u;
      rank[u]++;
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

## Articulation Points and Bridges

Articulation points (or cut vertices) are nodes that, when removed, increase the number of connected components in a graph. Bridges are edges that, when removed, increase the number of connected components.

### Use Cases
- Network reliability analysis
- Finding vulnerabilities in networks
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

## Disjoint Sets (Union-Find)

Disjoint-set or Union-Find is a data structure that keeps track of elements which are split into disjoint (non-overlapping) sets.

### Use Cases
- Kruskal's MST algorithm
- Detecting cycles in an undirected graph
- Finding connected components
- Network connectivity

### Operations
1. **MakeSet**: Create a new set containing just one element
2. **Find**: Determine which set an element belongs to
3. **Union**: Merge two sets into a single set

### Java Implementation

```java
public class DisjointSet {
    private int[] parent;
    private int[] rank;
    
    // Constructor: creates n disjoint sets (one for each item)
    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        
        // Each element is initially its own parent
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    // Find the set of an element (with path compression)
    public int find(int x) {
        // If x is not the parent of itself, then it's not the representative of its set
        if (parent[x] != x) {
            // Path compression: make the parent of x the root of the set
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    // Union of two sets by rank
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        // Elements are already in the same set
        if (rootX == rootY) {
            return;
        }
        
        // Attach smaller rank tree under root of higher rank tree
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            // If ranks are same, make one as root and increment its rank
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }
    
    // Check if two elements are in the same set
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }
}
```

### Cycle Detection in Undirected Graph

```java
import java.util.*;

public class CycleDetectionUsingDisjointSet {
    static class Edge {
        int src, dest;
        
        public Edge(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }
    }
    
    // Detect cycle in an undirected graph using Disjoint Set
    public boolean hasCycle(List<Edge> edges, int V) {
        DisjointSet ds = new DisjointSet(V);
        
        // Iterate through all edges
        for (Edge edge : edges) {
            int x = ds.find(edge.src);
            int y = ds.find(edge.dest);
            
            // If both vertices are already in the same set, cycle exists
            if (x == y) {
                return true;
            }
            
            // Union the two sets
            ds.union(x, y);
        }
        
        return false;
    }
}
```

### Time and Space Complexity
- **Time Complexity**:
    - With path compression and union by rank: O(α(n)) per operation, where α is the inverse Ackermann function, which grows very slowly
    - Without optimizations: O(n) per operation
- **Space Complexity**: O(n) for the parent and rank arrays

### Example LeetCode Problems
1. [LeetCode #547: Number of Provinces](https://leetcode.com/problems/number-of-provinces/)
2. [LeetCode #684: Redundant Connection](https://leetcode.com/problems/redundant-connection/)
3. [LeetCode #1319: Number of Operations to Make Network Connected](https://leetcode.com/problems/number-of-operations-to-make-network-connected/)
4. [LeetCode #990: Satisfiability of Equality Equations](https://leetcode.com/problems/satisfiability-of-equality-equations/)
