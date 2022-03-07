package Graph;

/**
 * The network rank of two different cities is defined as the total number of directly connected roads to either city.
 * If a road is directly connected to both cities, it is only counted once.
 *
 * https://leetcode.com/problems/maximal-network-rank/
 */
public class MinimalNetworkRank {

    public static void main(String[] args) {
        int[][] matrix = {
                {0, 1},
                {0, 3},
                {1, 2},
                {1, 3}
        };
        int result = new MinimalNetworkRank().maximalNetworkRank(4, matrix);
        System.out.println("max networks of two nodes are " + result);
    }


    public int maximalNetworkRank(int n, int[][] roads) {
        boolean[][] dp = new boolean[n][n];
        int[] counts = new int[n];
        for (int[] r : roads) {
            int node1 = r[0];
            int node2 = r[1];
            counts[node1]++;
            counts[node2]++;
            dp[node1][node2] = true;
            dp[node2][node1] = true;
        }
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                result = Math.max(result, counts[i] + counts[j] - (dp[i][j] ? 1 : 0));
            }
        }
        return result;
    }
}
