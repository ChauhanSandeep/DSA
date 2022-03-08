package String;

/**
 * countAndSay(1) = "1"
 * countAndSay(2) = say "1" = one 1 = "11"
 * countAndSay(3) = say "11" = two 1's = "21"
 * countAndSay(4) = say "21" = one 2 + one 1 = "12" + "11" = "1211"
 *
 * https://leetcode.com/problems/count-and-say/
 */
public class CountAndSay {
    public static void main(String[] args) {
        System.out.println(new CountAndSay().countAndSay(4));
    }

    public String countAndSay(int n) {
        return countSay(new StringBuilder("1"), 1, n).toString();
    }

    public StringBuilder countSay(StringBuilder builder, int curr, int target) {
        if(curr == target) return new StringBuilder(builder);

        StringBuilder nextBuilder = new StringBuilder();
        int count = 1;
        char c = builder.charAt(0);
        for(int i = 1; i<builder.length(); i++) {
            if(builder.charAt(i - 1) == builder.charAt(i)) {
                count++;
            }else{
                nextBuilder.append(count);
                nextBuilder.append(c);
                count = 1;
                c = builder.charAt(i);
            }
        }
        nextBuilder.append(count);
        nextBuilder.append(c);
        return countSay(nextBuilder, curr+1, target);
    }
}
