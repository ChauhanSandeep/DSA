# Graph Algorithms Handbook for Technical Interviews

## Table of Contents
- [Introduction to Graphs](#introduction-to-graphs)
- [Breadth-First Search (BFS)](#breadth-first-search-bfs)
- [Depth-First Search (DFS)](#depth-first-search-dfs)
- [Topological Sort](#topological-sort)
- [Shortest Path Algorithms](#shortest-path-algorithms)
    - [Dijkstra's Algorithm](#dijkstras-algorithm)
    - [Bellman-Ford Algorithm](#bellman-ford-algorithm)
    - [Floyd-Warshall Algorithm](#floyd-warshall-algorithm)
- [Minimum Spanning Tree](#minimum-spanning-tree)
    - [Prim's Algorithm](#prims-algorithm)
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

BFS is a traversal algorithm that explores all the vertices of a graph at the present depth before moving on to vertices at the next depth level.

### Intuition
- Start at a source node and explore all its neighbors before moving to the next level
- Uses a queue data structure (First-In-First-Out)
- Visits nodes in increasing order of their distance from the source

### Use Cases
- Finding shortest paths in unweighted graphs
- Finding all connected components
- Testing if a graph is bipartite (bipartite graph is a graph whose vertices can be divided into two disjoint 
sets such that no two graph vertices within the same set are adjacent)
- Finding all nodes within one connected component

### Steps
1. Create a queue and a visited array
2. Enqueue the source vertex and mark it as visited
3. While the queue is not empty:
    - Dequeue a vertex
    - Process the vertex
    - Enqueue all unvisited adjacent vertices and mark them as visited

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

### Example LeetCode Problems
1. [LeetCode #200: Number of Islands](https://leetcode.com/problems/number-of-islands/)
2. [LeetCode #994: Rotting Oranges](https://leetcode.com/problems/rotting-oranges/)
3. [LeetCode #127: Word Ladder](https://leetcode.com/problems/word-ladder/)
4. [LeetCode #1091: Shortest Path in Binary Matrix](https://leetcode.com/problems/shortest-path-in-binary-matrix/)

## Depth-First Search (DFS)

DFS is a traversal algorithm that explores as far as possible along each branch before backtracking.

### Intuition
- Start at a source node and explore each branch completely before moving to the next branch
- Uses a stack (explicitly or implicitly through recursion)
- Goes as deep as possible before backtracking

### Use Cases
- Finding paths between two vertices
- Detecting cycles in a graph
- Topological sorting
- Finding connected components
- Solving puzzles like mazes

### Steps
1. Create a visited array
2. Start at the source vertex, mark it as visited
3. Recursively visit all unvisited adjacent vertices
4. Backtrack when there are no more unvisited neighbors

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

Topological sorting is a linear ordering of vertices in a directed acyclic graph (DAG) such that for every directed edge (u, v), vertex u comes before v in the ordering.

### Intuition
- Only applicable to Directed Acyclic Graphs (DAGs)
- Represents dependencies between tasks
- Multiple valid topological orderings may exist

### Use Cases
- Task scheduling with dependencies
- Course prerequisite planning
- Build systems
- Data serialization

### Approaches
1. **DFS-based (Recursive)**: Uses a temporary stack to store vertices in topological order
2. **Kahn's Algorithm (Iterative)**: Uses in-degree of vertices and a queue

### DFS-based Topological Sort Implementation

```java
import java.util.*;

/**
 * TopologicalSortDFS performs a topological sort of a directed acyclic graph (DAG)
 * using Depth-First Search (DFS).
 *
 * <p><strong>Intuition:</strong>
 * Topological sort is used to order the vertices of a directed acyclic graph (DAG)
 * such that for every directed edge u → v, vertex u comes before v in the ordering.
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

```java
import java.util.*;

/**
 * TopologicalSortKahn performs topological sorting of a Directed Acyclic Graph (DAG)
 * using Kahn’s Algorithm (BFS-based approach).
 *
 * <p><strong>Intuition:</strong>
 * In a DAG, if we repeatedly remove nodes with zero in-degree (no dependencies),
 * the order in which we remove them is a valid topological sort. If at any point
 * we are left with nodes having non-zero in-degrees and no zero in-degree node is available,
 * it indicates a cycle.
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

### Dijkstra's Algorithm

Dijkstra's algorithm finds the shortest paths from a source vertex to all other vertices in a weighted graph with non-negative weights.

### Intuition
- Greedy algorithm that selects the vertex with the minimum distance value
- Uses a priority queue to efficiently select the next vertex to process
- Cannot handle negative edge weights

### Use Cases
- Finding shortest routes in road networks
- Network routing protocols
- Flight scheduling

### Steps
1. Create a distance array and initialize all distances to infinity except the source vertex (0)
2. Create a priority queue and add the source vertex with distance 0
3. While the priority queue is not empty:
    - Extract the vertex with the minimum distance
    - For each adjacent vertex, if the distance through current vertex is smaller than its current distance, update it
    - Add the vertex to the priority queue with its updated distance

### Java Implementation

```java
import java.util.*;

/**
 * Dijkstra's Algorithm - Single Source Shortest Path for Weighted Directed Graph
 *
 * ✅ Intuition:
 * - Dijkstra’s algorithm finds the shortest distance from a source node to all other nodes in a weighted graph (with non-negative weights).
 * - It uses a greedy strategy by always picking the vertex with the minimum distance that has not been processed yet.
 * - A priority queue (min-heap) ensures we always pick the closest unvisited node efficiently.
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
public class Dijkstra {

  /**
   * Helper class to represent a node and its current known distance from the source.
   * Used in the priority queue for efficient minimum distance retrieval.
   */
  static class Node implements Comparable<Node> {
    int vertex;
    int distance;

    public Node(int vertex, int distance) {
      this.vertex = vertex;
      this.distance = distance;
    }

    @Override
    public int compareTo(Node other) {
      return Integer.compare(this.distance, other.distance);
    }
  }

  /**
   * Graph representation using adjacency list for weighted directed edges.
   */
  static class Graph {
    int numVertices;
    List<List<Node>> adjacencyList;

    public Graph(int numVertices) {
      this.numVertices = numVertices;
      adjacencyList = new ArrayList<>(numVertices);
      for (int i = 0; i < numVertices; i++) {
        adjacencyList.add(new ArrayList<>());
      }
    }

    /**
     * Adds a directed edge from source to destination with the given weight.
     */
    public void addEdge(int source, int destination, int weight) {
      adjacencyList.get(source).add(new Node(destination, weight));
    }
  }

  /**
   * Runs Dijkstra's algorithm from a given source and returns shortest distances
   * to all vertices.
   *
   * @param graph The input weighted graph.
   * @param source The source vertex.
   * @return An array where dist[i] is the shortest distance from source to vertex i.
   */
  public static int[] dijkstra(Graph graph, int source) {
    int V = graph.numVertices;
    int[] shortestDistances = new int[V];
    Arrays.fill(shortestDistances, Integer.MAX_VALUE);
    shortestDistances[source] = 0;

    // Min-heap to pick the node with the smallest distance
    PriorityQueue<Node> minHeap = new PriorityQueue<>();
    minHeap.offer(new Node(source, 0));

    while (!minHeap.isEmpty()) {
      Node current = minHeap.poll();
      int currentVertex = current.vertex;
      int currentDistance = current.distance;

      // Skip if a better path to currentVertex has already been processed
      if (currentDistance > shortestDistances[currentVertex]) {
        continue;
      }

      // Process all adjacent vertices
      for (Node neighbor : graph.adjacencyList.get(currentVertex)) {
        int adjacent = neighbor.vertex;
        int weight = neighbor.distance;

        // Check for a shorter path to adjacent through currentVertex
        if (shortestDistances[currentVertex] + weight < shortestDistances[adjacent]) {
          shortestDistances[adjacent] = shortestDistances[currentVertex] + weight;
          minHeap.offer(new Node(adjacent, shortestDistances[adjacent]));
        }
      }
    }

    return shortestDistances;
  }
}
```

### Time and Space Complexity
- **Time Complexity**: O((V + E) log V) using a binary heap priority queue
- **Space Complexity**: O(V) for the distance array and the priority queue

### Bellman-Ford Algorithm

Bellman-Ford algorithm finds the shortest paths from a source vertex to all other vertices in a weighted graph. Unlike Dijkstra's, it can handle negative weight edges.

### Intuition
- Dynamic programming approach
- Can detect negative weight cycles
- Less efficient than Dijkstra's but more general

### Use Cases
- Networks with negative edge weights
- Detecting negative cycles
- Currency exchange arbitrage detection

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

### Time and Space Complexity
- **Time Complexity**: O(V × E)
- **Space Complexity**: O(V) for the distance array

### Floyd-Warshall Algorithm

Floyd-Warshall algorithm finds shortest paths between all pairs of vertices in a weighted graph.
<b><u>This is not important for interviews</u></b>

### Intuition
- Dynamic programming approach
- Works with positive and negative edge weights
- Cannot handle negative cycles

### Use Cases
- All-pairs shortest paths computation
- Transitive closure of a graph
- Finding the shortest path in a weighted graph with positive or negative edge weights

### Steps
1. Initialize the distance matrix with direct edge weights (use infinity for non-adjacent vertices)
2. For each vertex k as an intermediate:
    - For each pair of vertices (i, j):
        - If dist[i][j] > dist[i][k] + dist[k][j], update dist[i][j] = dist[i][k] + dist[k][j]

### Java Implementation

```java
import java.util.*;

/**
 * FloydWarshallAlgorithm computes the shortest distances between all pairs of vertices
 * in a weighted directed graph using dynamic programming.
 *
 * ✅ Intuition:
 * For any pair (u, v), check if there's an intermediate node k that gives a shorter path.
 * If yes, update the distance: dist[u][v] = min(dist[u][v], dist[u][k] + dist[k][v])
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

### Time and Space Complexity
- **Time Complexity**: O(V³)
- **Space Complexity**: O(V²) for the distance matrix

### Example LeetCode Problems for Shortest Path Algorithms
1. [LeetCode #743: Network Delay Time](https://leetcode.com/problems/network-delay-time/)
2. [LeetCode #787: Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/)
3. [LeetCode #1334: Find the City With the Smallest Number of Neighbors at a Threshold Distance](https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/)
4. [LeetCode #399: Evaluate Division](https://leetcode.com/problems/evaluate-division/)

## Minimum Spanning Tree

A minimum spanning tree (MST) is a subset of the edges of a connected, edge-weighted undirected graph that connects all the vertices together without any cycles and with the minimum possible total edge weight.

### Prim's Algorithm

Prim's algorithm finds the MST by starting from one vertex and greedily growing the tree by adding the lowest-weight edge that connects a vertex in the tree to a vertex outside the tree.

### Intuition
- Greedy algorithm that builds the MST one vertex at a time
- Starts from an arbitrary vertex and grows the tree
- Similar to Dijkstra's algorithm but focuses on minimum spanning tree rather than shortest paths

### Use Cases
- Network design
- Cluster analysis
- Circuit design

### Steps
1. Start with any vertex as the MST
2. While MST doesn't include all vertices:
    - Find the minimum weight edge that connects a vertex in MST to a vertex outside MST
    - Add this edge and the new vertex to MST

### Java Implementation

```java
import java.util.*;

/**
 * Prim's Algorithm for Minimum Spanning Tree (MST) in a Weighted Undirected Graph.
 *
 * ✅ Intuition:
 *   - Start from any vertex (usually vertex 0).
 *   - Always pick the minimum weight edge that connects a vertex in the MST set to a vertex outside it.
 *   - Greedily grow the MST until all vertices are included.
 *
 * ✅ Efficient Implementation:
 *   - Uses a PriorityQueue (Min-Heap) to select the minimum weight edge.
 *   - Maintains key[] array to track the minimum cost to connect each vertex to the MST.
 *   - parent[] array tracks the structure of the MST.
 *
 * ✅ Time Complexity: O(E log V)
 * ✅ Space Complexity: O(V + E)
 */
public class PrimMST {

  /**
   * Represents an edge from the current node to a destination with an associated weight.
   */
  static class Edge implements Comparable<Edge> {
    int to;      // Destination vertex
    int weight;  // Weight of the edge

    public Edge(int to, int weight) {
      this.to = to;
      this.weight = weight;
    }

    // PriorityQueue uses this to sort edges by weight (min first)
    @Override
    public int compareTo(Edge other) {
      return Integer.compare(this.weight, other.weight);
    }
  }

  /**
   * Returns a list of edges that form the MST using Prim’s Algorithm.
   *
   * @param graph The input graph represented as an adjacency list.
   *              graph[u] = List of edges from vertex 'u' to its neighbors.
   * @return List of edges in the MST (each edge is represented as: to, weight)
   */
  public static List<Edge> primMST(List<List<Edge>> graph) {
    int numVertices = graph.size();           // Total number of vertices

    boolean[] inMST = new boolean[numVertices]; // Tracks if a vertex is already in MST
    int[] key = new int[numVertices];           // Minimum edge weight to connect each vertex to MST
    int[] parent = new int[numVertices];        // Tracks the parent of each vertex in the MST

    Arrays.fill(key, Integer.MAX_VALUE);        // Initially, all keys are infinity
    Arrays.fill(parent, -1);                    // No parent assigned yet

    // Min-heap based on edge weight
    PriorityQueue<Edge> minHeap = new PriorityQueue<>();

    // Start from vertex 0
    key[0] = 0;
    minHeap.offer(new Edge(0, 0));  // (vertex, weight)

    List<Edge> mstEdges = new ArrayList<>();

    // Until all vertices are included in MST
    while (!minHeap.isEmpty()) {
      // Extract vertex with the smallest key (edge weight)
      Edge current = minHeap.poll();
      int u = current.to;

      // If vertex is already in MST, skip it
      if (inMST[u]) continue;

      // Mark the current vertex as included in MST
      inMST[u] = true;

      // If u is not the starting vertex, add its connecting edge to the MST
      if (parent[u] != -1) {
        mstEdges.add(new Edge(u, key[u]));  // (child, weight of edge from parent)
      }

      // Traverse all neighbors of vertex 'u'
      for (Edge neighbor : graph.get(u)) {
        int v = neighbor.to;
        int weight = neighbor.weight;

        // If 'v' is not in MST and edge (u-v) offers a cheaper connection
        if (!inMST[v] && weight < key[v]) {
          key[v] = weight;          // Update key to new minimum weight
          parent[v] = u;            // Set parent of 'v' as 'u'
          minHeap.offer(new Edge(v, key[v]));  // Push updated edge to heap
        }
      }
    }

    return mstEdges;  // Contains MST edges (to, weight from its parent)
  }
}
```

### Time and Space Complexity
- **Time Complexity**: O(E log V) using a binary heap
- **Space Complexity**: O(V) for the key and parent arrays and the priority queue

### Kruskal's Algorithm

Kruskal's algorithm finds the MST by considering all edges in increasing order of weight and adding them to the MST if they don't form a cycle.

### Intuition
- Greedy algorithm that builds the MST by adding edges in order of increasing weight
- Uses a disjoint-set data structure to efficiently check for cycles
- Focuses on edges rather than vertices

### Use Cases
- Network design
- Clustering algorithms
- Approximation algorithms for other problems

### Steps
1. Sort all edges in non-decreasing order of their weight
2. Create a disjoint-set for all vertices
3. For each edge in the sorted order:
    - If including this edge doesn't form a cycle, include it in the MST
    - Otherwise, discard it

### Java Implementation

```java
import java.util.*;

public class KruskalMST {
    static class Edge implements Comparable<Edge> {
        int src, dest, weight;
        
         public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
    
    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}

static class DisjointSet {
    int[] parent, rank;
    
    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        
        // Initialize each element as a separate set
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    // Find the set of an element (with path compression)
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }
    
    // Union of two sets (using rank)
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) return;
        
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
}

public static List<Edge> kruskalMST(List<Edge> edges, int V) {
    // Create a list to store the MST
    List<Edge> mst = new ArrayList<>();
    
    // Sort all edges in non-decreasing order of their weight
    Collections.sort(edges);
    
    // Create disjoint sets
    DisjointSet ds = new DisjointSet(V);
    
    // Number of edges to be taken is V-1
    int e = 0;
    int i = 0;
    
    // Keep adding edges until we reach V-1 edges
    while (e < V - 1 && i < edges.size()) {
        // Get the next edge
        Edge nextEdge = edges.get(i++);
        
        int x = ds.find(nextEdge.src);
        int y = ds.find(nextEdge.dest);
        
        // If including this edge doesn't form a cycle, include it
        if (x != y) {
            mst.add(nextEdge);
            ds.union(x, y);
            e++;
        }
    }
    
    return mst;
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

### Intuition
- Identify critical components in a network
- Uses a modified DFS to track when a vertex is a potential articulation point
- Uses discovery time and lowest reachable ancestor time

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

### Intuition
- A strongly connected component is a maximal subgraph where every vertex is reachable from every other vertex
- Uses two DFS traversals and a stack to find all SCCs
- First DFS to get the finishing times, second DFS to find SCCs

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

### Intuition
- Efficiently determines if two elements are in the same set
- Uses path compression and union by rank to achieve near-constant time operations
- Essential for Kruskal's algorithm and cycle detection

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
