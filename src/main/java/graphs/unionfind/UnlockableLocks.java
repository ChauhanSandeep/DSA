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
 * You are given n people. Each person owns exactly one lock and exactly one key.
 * - locks[i] is the lock number owned by person i.
 * - keys[i]  is the key  number owned by person i.
 * A lock numbered x can only be unlocked using a key numbered x. A key can be reused
 * any number of times.
 *
 * You are also given a list of friendship pairs friends[i] = [u, v]. Friendship is
 * bidirectional and transitive (if A~B and B~C then A, B, C can all share keys).
 * Friends in the same connected component can freely exchange all their keys.
 *
 * Return the maximum number of locks that can be unlocked across all people.
 *
 * Example 1:
 *   locks   = [1, 2, 3, 4]
 *   keys    = [2, 1, 4, 5]
 *   friends = [[0,1], [1,2]]
 *   Component {0,1,2}: locks={1,2,3}, keys={1,2,4} -> locks 1 & 2 unlockable
 *   Person 3 isolated: lock 4, key 5 -> not unlockable
 *   Output: 2
 *
 * Example 2:
 *   locks   = [1, 2, 3, 4]
 *   keys    = [4, 3, 2, 1]
 *   friends = [[0,1], [1,2], [2,3]]
 *   Single component holds keys {1,2,3,4} -> all 4 locks unlockable
 *   Output: 4
 *
 * Constraints:
 *   1 <= n <= 1e5,  0 <= friends.length <= 1e5,  1 <= locks[i], keys[i] <= 1e9
 *
 * Follow-up Questions:
 *
 * 1. What if friendships were directed (A can give keys to B but not vice versa)?
 *    Answer: This becomes a reachability problem on a directed graph. Compute
 *    Strongly Connected Components (Tarjan/Kosaraju), condense into a DAG, then
 *    propagate the union of available keys from sources to sinks via topological
 *    order. A lock is unlockable iff its key number appears in any SCC reachable
 *    backwards from the lock owner's SCC.
 *
 * 2. What if keys were single-use (each key unlocks at most one lock)?
 *    Answer: Within each component, build a multiset of available keys and a
 *    multiset of locks; the count of unlockable locks equals the sum over key
 *    numbers x of min(count_of_locks_x, count_of_keys_x). The current solution
 *    treats keys as reusable, so a single key x covers all locks numbered x.
 *
 * 3. What if friendships changed over time and we needed to answer queries online?
 *    Answer: For union-only updates, maintain a DSU keyed by component-root that
 *    tracks the set of keys per component (small-to-large merging gives
 *    O(n log n) total). For deletions, switch to Link-Cut Trees or offline
 *    processing with reverse-time DSU.
 *
 * @author Sandeep Chauhan
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

    // Example 1 -> 2
    System.out.println(solver.maxUnlockedLocks(
        new int[]{1, 2, 3, 4},
        new int[]{2, 1, 4, 5},
        new int[][]{{0, 1}, {1, 2}}));

    // Example 2 -> 4
    System.out.println(solver.maxUnlockedLocks(
        new int[]{1, 2, 3, 4},
        new int[]{4, 3, 2, 1},
        new int[][]{{0, 1}, {1, 2}, {2, 3}}));

    // Edge case: no friendships, every person isolated
    // locks=[1,2], keys=[1,3] -> only person 0 can unlock their own lock -> 1
    System.out.println(solver.maxUnlockedLocks(
        new int[]{1, 2},
        new int[]{1, 3},
        new int[][]{}));

    // BFS variant should produce identical results
    System.out.println("--- BFS variant ---");
    System.out.println(solver.maxUnlockedLocksBFS(
        new int[]{1, 2, 3, 4},
        new int[]{2, 1, 4, 5},
        new int[][]{{0, 1}, {1, 2}}));
    System.out.println(solver.maxUnlockedLocksBFS(
        new int[]{1, 2, 3, 4},
        new int[]{4, 3, 2, 1},
        new int[][]{{0, 1}, {1, 2}, {2, 3}}));
    System.out.println(solver.maxUnlockedLocksBFS(
        new int[]{1, 2},
        new int[]{1, 3},
        new int[][]{}));
  }
}
