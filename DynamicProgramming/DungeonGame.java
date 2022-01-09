package DynamicProgramming;

/**
 * https://leetcode.com/problems/dungeon-game/
 */
public class DungeonGame {

    public static void main(String[] args) {
        int[][] dungeon = {
                {-2, -3, 3},
                {-5, -10, 1},
                {10, 30, -5}
        };
        System.out.println(new DungeonGame().calculateMinimumHPItr(dungeon));
    }

    int rows;
    int cols;
    int invalid;
    /**
     * This is recursive approach
     */
    public int calculateMinimumHPRec(int[][] dungeon) {
        if(dungeon == null || dungeon.length == 0 || dungeon[0].length == 0) return 1;
        this.rows = dungeon.length;
        this.cols = dungeon[0].length;
        this.invalid = Integer.MIN_VALUE;
        Integer[][] healthRequired = new Integer[rows][cols];

        int lostHealth = dfs(dungeon, 0, 0, healthRequired);
        int initialHealth = -lostHealth + 1;
        return Math.max(initialHealth, 1);
    }

    public int dfs(int[][] dungeon, int i, int j, Integer[][] healthRequired) {
        if(i<0 || i>=rows || j<0 || j>=cols) return invalid;
        if(i == rows-1 && j == cols-1) return Math.min(dungeon[i][j], 0);
        if(healthRequired[i][j] != null) return healthRequired[i][j];

        int down = dfs(dungeon, i+1, j, healthRequired);
        int right = dfs(dungeon, i, j+1, healthRequired);
        if(down == invalid && right == invalid) return invalid;
        int max = dungeon[i][j] + Math.max(down, right);
        // knight would need at least 1 health or else he would be dead even before,
        // therefore taking Math.min(max, 0)
        healthRequired[i][j] = Math.min(max, 0);
        return healthRequired[i][j];
    }


    /**
     * This is iterative approach
     */
    public int calculateMinimumHPItr(int[][] dungeon) {
        if(dungeon == null || dungeon.length == 0 || dungeon[0].length == 0){
            return 0;
        }
        int r = dungeon.length;
        int c = dungeon[0].length;
        int[][] dp = new int[r][c];

        for(int i = r - 1; i >= 0; i--){
            for(int j = c - 1; j >= 0; j--){
                if(i == r - 1 && j == c - 1){
                    dp[i][j] = Math.max(1, 1 - dungeon[i][j]);
                }else if(i == r - 1){
                    dp[i][j] = Math.max(dp[i][j + 1] - dungeon[i][j], 1);
                }else if(j == c - 1){
                    dp[i][j] = Math.max(dp[i + 1][j] - dungeon[i][j], 1);
                }else{
                    dp[i][j] = Math.min(
                            Math.max(dp[i + 1][j] - dungeon[i][j], 1),
                            Math.max(dp[i][j + 1] - dungeon[i][j], 1)
                    );
                }
            }
        }
        return dp[0][0];
    }
}
