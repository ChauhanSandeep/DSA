package StackQueue;

import java.util.ArrayList;
import java.util.List;

public class ParenthesisChecker2 {

    public static void main(String[] args) {
        String str = "lee(t(c)o)de)";
        System.out.println(minRemoveToMakeValid(str));
    }

    /**
     * Remove minimum number of parenthesis to make the parenthesis valid in provided string.
     * Return the corrected string.
     */
    public static String minRemoveToMakeValid(String str) {
        List<Integer> open = new ArrayList<>();
        List<Integer> close = new ArrayList<>();

        for(int i=0; i<str.length(); i++) {
            Character c = str.charAt(i);
            if(c == '(') {
                open.add(i);
            }else if(c == ')'){
                if(open.size() != 0){
                    open.remove(open.size()-1);
                }else{
                    close.add(i);
                }
            }
        }

        open = merge(open, close);
        StringBuffer buffer = new StringBuffer(str);

        for(int i=open.size()-1; i>=0; i--) {
            buffer.deleteCharAt(open.get(i));
        }
        return buffer.toString();

    }

    public static List<Integer> merge(List<Integer> list1, List<Integer> list2) {
        List<Integer> result = new ArrayList<>();
        int first = 0;
        int second = 0;

        while(first < list1.size() && second < list2.size()) {
            if(list1.get(first) < list2.get(second)) {
                result.add(list1.get(first));
                first++;
            }else{
                result.add(list2.get(second));
                second++;
            }
        }
        while(first < list1.size()) {
            result.add(list1.get(first));
            first++;
        }
        while(second < list2.size()) {
            result.add(list2.get(second));
            second++;
        }
        return result;
    }
}
