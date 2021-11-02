package Array;

/**
 * A binary string is monotone increasing if it consists of some number of 0's (possibly none), followed by some number of 1's (also possibly none).
 * You are given a binary string s. You can flip s[i] changing it from 0 to 1 or from 1 to 0.
 *
 * https://leetcode.com/problems/flip-string-to-monotone-increasing/
 */
public class MinFlips {

    public static void main(String[] args) {
        String input = "00110000111111";
        int flips = new MinFlips().minFlipsMonoIncr(input);
        System.out.println(flips);
    }

    /**
     * Going by DP approach. when a character `ch` comes at the end of string, these are the cases:
     * -> When '1' comes, no more flip should be applied, since '1' is appended to the tail of the original string.
     * -> When '0' comes, things become a little bit complicated. There are two options for us: flip the newly appended '0' to '1', after flipCount flips for the original string; or flip oneCount '1' in the original string to '0'.
     * Hence, the result of the next step of DP, in the '0' case, is std::min(flipCount + 1, oneCount);
     */
    public int minFlipsMonoIncr(String str) {
        if(str == null || str.length() == 0) return 0;

        char[] strArr = str.toCharArray();
        int flipCount = 0; // zeroCount initially. can be switched to ones later.
        int oneCount = 0;

        for(int i=0; i<strArr.length; i++) {
            char curr = strArr[i];
            if(curr == '1') {
                oneCount++;
            }else{
                flipCount++;
            }
            flipCount = Math.min(oneCount, flipCount);
        }
        return flipCount;
    }
}
