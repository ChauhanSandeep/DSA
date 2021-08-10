package Array;

/**
 * A binary string is monotone increasing if it consists of some number of 0's (possibly none), followed by some number of 1's (also possibly none).
 * You are given a binary string s. You can flip s[i] changing it from 0 to 1 or from 1 to 0.
 */
public class MinFlips {

    public static void main(String[] args) {
        String input = "10011111110010111011";
        int flips = new MinFlips().minFlipsMonoIncr(input);
        System.out.println(flips);
    }

    public int minFlipsMonoIncr(String str) {
        if(str == null || str.length() == 0) return 0;

        char[] strArr = str.toCharArray();
        int flipCount = 0; // zeroCount initially. can be switched to ones later.
        int oneCount = 0;

        for(int i=0; i<strArr.length; i++) {
            char curr = strArr[i];
            if(curr == '0') {
                if(oneCount == 0) continue;
                flipCount++;
            }else{
                oneCount++;
            }
            if(flipCount > oneCount) flipCount = oneCount; // make more sense to flip ones.
        }
        return flipCount;
    }
}
