package StackQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class NextGreaterElement {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(11, 3, 21, 30);
        printNGE(list);
    }

    public static void printNGE(List<Integer> list) {
        Stack<Integer> stack = new Stack<>();
        List<Integer> resultList = new ArrayList<>();
        for(int i=0; i<list.size(); i++) {
            resultList.add(-1);
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            if (!stack.empty()) {
                while (!stack.empty() && stack.peek() <= list.get(i)) {
                    stack.pop();
                }
            }
            resultList.set(i, stack.empty() ? -1 : stack.peek());
            stack.push(list.get(i));
        }

        for (int i = 0; i < list.size(); i++)
            System.out.println(list.get(i) + " --> " + resultList.get(i));
    }


}
