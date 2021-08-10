package DynamicProgramming;

/**
 * https://leetcode.com/problems/stone-game/
 */
public class StoneGame {
    public static void main(String[] args) {
        int[] piles = {5,3,4,5};
        boolean alexWins = new StoneGame().stoneGame(piles);
        System.out.println("Alex wins? " + alexWins);
    }

    /**
     * Iterative approach. Correct.
     */
    public boolean stoneGame(int[] piles) {
        int alex = 0;
        int lee = 0;
        int left = 0;
        int right = piles.length - 1;

        while (left < right) {
            if (piles[left] > piles[right]) {
                alex += piles[left++];
                lee += piles[right--];
            } else {
                alex += piles[right--];
                lee += piles[left++];
            }
        }

        return alex > lee;
    }

    /**
     * Recursive approach. TLE
     */
    public boolean stoneGame2(int[] piles) {
        int sum = 0;
        for(int i=0; i<piles.length; i++) sum += piles[i];

        return stoneGame(piles, 0, piles.length-1, true, 0, 0, sum);
    }

    public boolean stoneGame(int[] piles, int start, int end, boolean firstPlayer, int sum1, int sum2, int total) {
        if(sum1 > total/2) return true;

        if(start == end || sum2 >= total) return false;

        if(firstPlayer) {
            return stoneGame(piles, start+1, end, !firstPlayer, sum1+piles[start], sum2, total) ||
                    stoneGame(piles, start, end-1, !firstPlayer, sum1+piles[end], sum2, total);
        }else{
            return stoneGame(piles, start+1, end, !firstPlayer, sum1, sum2+piles[start], total) ||
                    stoneGame(piles, start, end-1, !firstPlayer, sum1, sum2+piles[end], total);
        }


    }
}
