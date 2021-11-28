package Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a tree with N nodes labelled from 1 to N.
 * Each node is either good or bad denoted by binary array `goodNodes` of size N where
 * if goodNodes[i] is 1 then ith node is good else if A[i] is 0 then ith node is bad.
 * Also the given tree is rooted at node 1 and you need to tell the number of
 * root to leaf paths in the tree that contain not more than k good nodes.
 *
 * https://www.interviewbit.com/problems/path-with-good-nodes/
 */
public class GoodNodesPath {
    public static void main(String[] args) {
        int[] goodNodes = {0, 1, 0, 1, 1, 1};
        int[][] relations = {
                {1, 2},
                {1, 5},
                {1, 6},
                {2, 3},
                {2, 4}
        };
        int k = 1;
        int result = new GoodNodesPath().solve(goodNodes, relations, k);
        System.out.println(result);
    }

    List<List<Integer>> adj;
    int goodNodes[];

    public int solve(int[] goodNodes, int[][] relations, int limit) {
        int size = goodNodes.length;
        if (size <= 1) return size;

        adj = new ArrayList<>();
        this.goodNodes = goodNodes;
        for (int i = 0; i <= size; i++) {
            adj.add(new ArrayList());
        }
        for (int relation[] : relations) {
            adj.get(relation[0]).add(relation[1]);
            adj.get(relation[1]).add(relation[0]);
        }
        return dfs(1, 0, limit + 1);
    }

    public int dfs(int curr, int parent, int limit) {
        if (limit == 0) return 0;
        if (adj.get(curr).size() == 1 && adj.get(curr).get(0) == parent) {
            return (limit - goodNodes[curr - 1] > 0) ? 1 : 0;
        }
        int sum = 0;
        for (int next : adj.get(curr)) {
            if (next == parent) continue;
            sum += dfs(next, curr, limit - goodNodes[curr - 1]);
        }
        return sum;
    }
}

