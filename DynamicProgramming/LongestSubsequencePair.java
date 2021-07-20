package DynamicProgramming;

import java.util.Arrays;

public class LongestSubsequencePair {
    public static void main(String[] args) {
        Pair[] list = {new Pair(5,24), new Pair(15, 25), new Pair (27, 40), new Pair(50, 60)};
        int lis = findLis(list);
        System.out.println(lis);

    }

    public static int findLis(Pair[] pairList) {
        int[] lisArr = new int[pairList.length];
        Arrays.fill(lisArr, 1);

        for(int i=1; i<pairList.length; i++) {
            for(int j=1; j<i; j++) {
                if(pairList[j].y < pairList[i].x && lisArr[j] + 1 > lisArr[i]) {
                    lisArr[i] = lisArr[j] + 1;
                }
            }
        }
        return lisArr[lisArr.length - 1];
    }
}

class Pair {
    int x;
    int y;

    public Pair(){}
    public Pair(int x, int y){
        this.x = x;
        this.y = y;
    }
}
