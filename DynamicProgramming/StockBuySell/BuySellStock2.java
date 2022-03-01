package DynamicProgramming.StockBuySell;

/**
 * Find max profit with infinite transactions allowed with min one
 * day of cooling period after selling a stock.
 */
public class BuySellStock2 {

    public static void main(String[] args) {
        int[] prices = {10, 20, 15, 30};
        int profit = new BuySellStock2().maxProfit(prices);
        System.out.println("Max profit with cooldown is " + profit);
    }

    private int maxProfit(int[] arr) {
        int obsp = -arr[0]; // old bought state profit, Profit with one stock bought
        int ossp = 0;       // old sold state profit, Profit with all stock sold
        int ocsp = 0;       // old cooling state profit, Profit in cooling state

        for(int i=1; i<arr.length; i++) {
            int nbsp = 0;
            int nssp = 0;
            int ncsp = 0;

            if(ocsp - arr[i] > obsp) {
                // we can buy from old cooling state
                nbsp = ocsp - arr[i];
            }else{
                nbsp = obsp;
            }

            if(obsp + arr[i] > ossp) {
                // we can sell from old bought state profit
                nssp = obsp + arr[i];
            }else{
                nssp = ossp;
            }

            if(ossp > ocsp) {
                // we can go to cooling state from last sold state profit
                ncsp = ossp;
            }else{
                ncsp = ocsp;
            }

            obsp = nbsp;
            ossp = nssp;
            ocsp = ncsp;
        }
        return ossp;
    }
}
