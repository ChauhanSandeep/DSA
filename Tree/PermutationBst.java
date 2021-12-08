package Tree;

/**
 * Find number of BST that can be created with N nodes of height h
 */
public class PermutationBst {
    int[] arr;

    public static void main(String[] args) {
        System.out.println(new PermutationBst().countTrees(3, 2));
    }

    int countTrees(int N, int h) {
        this.arr = new int[N];
        return countTrees(N, h, 0);
    }

    int countTrees(int nodes, int height, int currHeight) {
        if (nodes <= 1) {
            return (0);
        } else {
            int sum = 0;
            int left, right, root;

            for (root = 1; root <= nodes; root++) {
                if (currHeight + 1 <= height) {
                    left = countTrees(root - 1, height, currHeight + 1);
                    right = countTrees(nodes - root, height, currHeight + 1);
                    if (currHeight == 0) {
                        arr[root - 1] += left * right;
                    }
                    sum += left * right;
                }
            }
            return sum;
        }
    }

//    public int cntPermBST(int nodes, int height) {
//        int MOD = (int) (1e9 + 7);
//
//        int[][] C = new int[nodes + 1][nodes + 1];
//        long[][] dp = new long[nodes + 1][height + 1];
//
//        for (int i = 0; i <= nodes; i++) {
//            C[i][0] = 1;
//            C[i][i] = 1;
//        }
//        for (int i = 1; i <= nodes; i++) {
//            for (int j = 1; j < i; j++) {
//                C[i][j] = (C[i - 1][j - 1] + C[i - 1][j]) % MOD;
//            }
//        }
//
//        dp[0][0] = 1;
//        dp[1][0] = 1;
//        for (int len = 2; len <= nodes; len++) {
//            for (int h = 1; h <= height; h++) {
//                for (int i = 0; i < len; i++) {
//                    int left = i;
//                    int right = len - i - 1;
//                    long cur = 0;
//
//                    for (int j = 0; j <= h - 2; j++) {
//                        cur = cur + (dp[left][j] * dp[right][h - 1]) % MOD;
//                        cur = cur + (dp[left][h - 1] * dp[right][j]) % MOD;
//                    }
//
//                    cur = cur + (dp[left][h - 1] * dp[right][h - 1]) % MOD;
//                    cur = (cur * C[left + right][left]) % MOD;
//                    dp[len][h] = (dp[len][h] + cur) % MOD;
//                }
//            }
//        }
//        return (int) (dp[nodes][height]) % MOD;
//    }
}
