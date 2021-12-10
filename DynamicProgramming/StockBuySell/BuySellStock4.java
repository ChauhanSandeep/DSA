package DynamicProgramming.StockBuySell;

/**
 * Given price of stock on each day.
 * Find max profit that can be made by having at most k transactions
 */
public class BuySellStock4 {

    public static void main(String[] args) {
        int[] arr = {19230, 13765, 6863, 3840, 8367, 15603, 16327, 15140, 5582, 12937, 9472, 14190, 9541, 4126, 2757, 400, 19685, 15908, 4929, 18136, 16050, 6622, 13439, 265, 5846, 3188, 8756, 4960, 18781, 11139, 5152, 12314 };
        System.out.println(new BuySellStock4().solve(arr, 100000089));
    }

    public int solve(int[] prices, int k) {
        if(prices.length <= 1 || k <= 0) return 0;

        int len = prices.length;
        int[][] profit = new int[k+1][len];
        for(int i=0; i<k+1; i++) {
            profit[i][0] = 0;
        }
        for(int i=0; i<len; i++) {
            profit[0][i] = 0;
        }

        for(int i=1; i<k+1; i++) {
            for(int j=1; j<len; j++) {
                profit[i][j] = Math.max(profit[i][j-1], findMax(prices, profit, i-1, j));
            }
        }

        return profit[k][len-1];
    }

    public int findMax(int[] price, int[][] profit, int row, int index) {
        int max = 0;
        for(int curr = 0; curr<index; curr++) {
            max = Math.max(max, profit[row][curr] + (price[index] - price[curr]));
        }
        return max;
    }

    /**
     * This is more optimum approach
     * @param prices
     * @param k
     * @return
     */
    public int maxProfit(int[] prices, int k) {
        int len = prices.length;
        if (k >= len / 2) return quickSolve(prices);

        int[][] profit = new int[k + 1][len];
        for (int i = 1; i <= k; i++) {
            int prevMax =  -prices[0];
            for (int j = 1; j < len; j++) {
                profit[i][j] = Math.max(profit[i][j - 1], prices[j] + prevMax);
                prevMax =  Math.max(prevMax, profit[i - 1][j - 1] - prices[j]);
            }
        }
        return profit[k][len - 1];
    }

    private int quickSolve(int[] prices) {
        int len = prices.length, profit = 0;
        for (int i = 1; i < len; i++) {
//          as long as there is a price gap, we gain a profit.
            if (prices[i] > prices[i - 1]) {
                profit += prices[i] - prices[i - 1];
            }
        }
        return profit;
    }
}
