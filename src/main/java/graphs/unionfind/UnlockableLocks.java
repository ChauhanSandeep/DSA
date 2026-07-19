package graphs.unionfind;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Problem: Unlockable Locks
 *
 * Each person owns one lock and one reusable key. Friends in the same connected
 * component can share all keys, so a person's lock is unlockable when its number
 * appears in the key set pooled by that component.
 *
 * Pattern:  Graph | Union-Find | Connected component aggregation
 *
 * Example:
 *   Input:  locks = [1,2,3,4], keys = [2,1,4,5], friends = [[0,1],[1,2]]
 *   Output: 2
 *   Why:    component {0,1,2} has keys {1,2,4}, which unlock locks 1 and 2 only.
 *
 * Follow-ups:
 *   1. Friendships are directed?
 *      Compress strongly connected components, then reason about reachability between SCCs.
 *   2. Keys are single-use?
 *      Count matching lock/key frequencies per component and sum min(lockCount, keyCount).
 *   3. Friendships change over time?
 *      DSU handles union-only updates; deletions need offline dynamic connectivity or link-cut trees.
 *
 * Related: Number of Provinces (547), Accounts Merge (721), Connected Components.
 */
public class UnlockableLocks {

  /**
   * Computes the maximum number of locks that can be unlocked.
   *
   * Step-by-step:
   *  1. Build a DSU over n people and union every friendship pair so each connected
   *     component is represented by a single root.
   *  2. For every person i, add keys[i] to the key-set associated with their root.
   *     This gives, per component, the union of all key numbers available to share.
   *  3. For every person i, look up the key-set of their root. If it contains
   *     locks[i], that lock can be unlocked — increment the counter.
   *
   * Key Insight:
   * The friendship relation defines an equivalence on people; within each class all
   * keys are pooled. Since keys are reusable, only the *set* of key numbers matters
   * (not multiplicity), so a HashSet per component suffices.
   *
   * Algorithm: Union-Find with path compression and union by rank, plus per-root
   * HashSet aggregation.
   *
   * Time Complexity:  O((n + m) * α(n)) where m = friends.length and α is the
   *                   inverse Ackermann function (effectively constant).
   * Space Complexity: O(n) for the DSU arrays plus O(n) for the per-root key sets.
   */
  public int maxUnlockedLocks(int[] locks, int[] keys, int[][] friends) {
    int len = locks.length;
    DisjointSetUnion dsu = new DisjointSetUnion(len);

    for (int[] friend : friends) {
      dsu.union(friend[0], friend[1]);
    }

    // Aggregate the set of available key numbers for each connected component.
    Map<Integer, Set<Integer>> rootToKeys = new HashMap<>();
    
    for (int i = 0; i < len; i++) {
      int root = dsu.find(i);
      rootToKeys.computeIfAbsent(root, k -> new HashSet<>()).add(keys[i]);
    }

    // A person's lock is unlockable iff some key in their component matches it.
    int unlocked = 0;
    for (int i = 0; i < len; i++) {
      int root = dsu.find(i);
      if (rootToKeys.get(root).contains(locks[i])) {
        unlocked++;
      }
    }
    return unlocked;
  }

  /**
   * Alternative: BFS labelling instead of DSU.
   *
   * Step-by-step:
   *  1. Build an undirected adjacency list from the friendship pairs.
   *  2. BFS from every unvisited person, tagging each reachable person with the
   *     current componentId. This produces the same equivalence classes as DSU
   *     but in one explicit pass.
   *  3. Pool keys[i] into a HashSet per componentId.
   *  4. For each person i, increment the counter if their component's key-set
   *     contains locks[i].
   *
   * Key Insight:
   * Same three-phase "discover components -> bucket -> match" template as the DSU
   * solution. Use this variant when the graph is given up-front and you may want
   * per-component traversal data; DSU is preferred when edges arrive incrementally.
   *
   * Time Complexity:  O(n + m) -- single BFS over the friendship graph.
   * Space Complexity: O(n + m) for the adjacency list and componentOf array.
   */
  public int maxUnlockedLocksBFS(int[] locks, int[] keys, int[][] friends) {
    int len = locks.length;

    // --- Phase 1: build adjacency list ------------------------------------
    List<List<Integer>> adjacency = new ArrayList<>(len);
    for (int i = 0; i < len; i++) {
      adjacency.add(new ArrayList<>());
    }
    for (int[] edge : friends) {
      adjacency.get(edge[0]).add(edge[1]);
      adjacency.get(edge[1]).add(edge[0]);
    }

    // --- Phase 2: BFS-label every person with its componentId -------------
    int[] componentOf = new int[len];
    Arrays.fill(componentOf, -1);
    int componentCount = 0;

    for (int start = 0; start < len; start++) {
      if (componentOf[start] != -1) continue;
      Deque<Integer> bfsQueue = new ArrayDeque<>();
      bfsQueue.offer(start);
      componentOf[start] = componentCount;
      while (!bfsQueue.isEmpty()) {
        int person = bfsQueue.poll();
        for (int friend : adjacency.get(person)) {
          if (componentOf[friend] == -1) {
            componentOf[friend] = componentCount;
            bfsQueue.offer(friend);
          }
        }
      }
      componentCount++;
    }

    // --- Phase 3: pool keys per component ---------------------------------
    List<Set<Integer>> keysOfComponent = new ArrayList<>(componentCount);
    for (int c = 0; c < componentCount; c++) {
      keysOfComponent.add(new HashSet<>());
    }
    for (int i = 0; i < len; i++) {
      keysOfComponent.get(componentOf[i]).add(keys[i]);
    }

    // --- Phase 4: count unlockable locks ----------------------------------
    int unlocked = 0;
    for (int i = 0; i < len; i++) {
      if (keysOfComponent.get(componentOf[i]).contains(locks[i])) {
        unlocked++;
      }
    }
    return unlocked;
  }

  /**
   * Disjoint Set Union with path compression and union by rank.
   * Both operations run in amortized O(α(n)) time.
   */
  private static class DisjointSetUnion {
    private final int[] parent;
    private final int[] rank;

    DisjointSetUnion(int n) {
      parent = new int[n];
      rank = new int[n];
      for (int i = 0; i < n; i++) {
        parent[i] = i;
      }
    }

    int find(int x) {
      if (parent[x] != x) {
        parent[x] = find(parent[x]);
      }
      return parent[x];
    }

    void union(int x, int y) {
      int rx = find(x);
      int ry = find(y);
      if (rx == ry) {
        return;
      }
      if (rank[rx] < rank[ry]) {
        parent[rx] = ry;
      } else if (rank[rx] > rank[ry]) {
        parent[ry] = rx;
      } else {
        parent[ry] = rx;
        rank[rx]++;
      }
    }
  }

  public static void main(String[] args) {
    UnlockableLocks solver = new UnlockableLocks();
    int[] locks1 = {1, 2, 3, 4};
    int[] keys1 = {2, 1, 4, 5};
    int[][] friends1 = {{0, 1}, {1, 2}};
    int[] locks2 = {1, 2};
    int[] keys2 = {1, 3};
    int[][] friends2 = {};

    System.out.printf("locks=%s keys=%s friends=%s -> dsu=%d bfs=%d  expected=2%n",
        Arrays.toString(locks1), Arrays.toString(keys1), Arrays.deepToString(friends1),
        solver.maxUnlockedLocks(locks1, keys1, friends1), solver.maxUnlockedLocksBFS(locks1, keys1, friends1));
    System.out.printf("locks=%s keys=%s friends=%s -> dsu=%d bfs=%d  expected=1%n",
        Arrays.toString(locks2), Arrays.toString(keys2), Arrays.deepToString(friends2),
        solver.maxUnlockedLocks(locks2, keys2, friends2), solver.maxUnlockedLocksBFS(locks2, keys2, friends2));
  }


}
