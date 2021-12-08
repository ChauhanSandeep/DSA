package StackQueue;
import java.util.*;

/**
 * Remove duplicates appearing together
 */
public class RemoveDuplicate {
    public static void main(String[] args) {
        String str = "pbbcggttciiippooaais";
        String res = new RemoveDuplicate().removeDuplicates(str, 2);
        System.out.println(res);
    }

    public String removeDuplicates(String str, int k) {
        Stack<Character> mainStack = new Stack<Character>();
        Stack<Character> auxStack = new Stack<Character>();

        for (char c : str.toCharArray()) {

            if (!auxStack.isEmpty() && auxStack.peek() == c) {
                auxStack.push(c);
            } else if (!auxStack.isEmpty()) {
                while (!auxStack.isEmpty() && auxStack.size() % k == 0) {
                    for (int i = 0; i < k; i++) {
                        auxStack.pop();
                    }
                }
                while (!auxStack.isEmpty()) {
                    char temp = auxStack.pop();
                    mainStack.push(temp);
                }
                while(!mainStack.isEmpty() && mainStack.peek() == c) {
                    mainStack.pop();
                    auxStack.push(c);
                }
                auxStack.push(c);
            } else if (!mainStack.isEmpty() && mainStack.peek() == c) {
                auxStack.push(c);
                while (!mainStack.isEmpty() && mainStack.peek() == c) {
                    mainStack.pop();
                    auxStack.push(c);
                }
            }else{
                mainStack.push(c);
            }
        }

        while (!auxStack.isEmpty() && auxStack.size() % k == 0) {
            for (int i = 0; i < k; i++) {
                auxStack.pop();
            }
        }
        while (!auxStack.isEmpty()) {
            char temp = auxStack.pop();
            mainStack.push(temp);
        }

        StringBuilder builder = new StringBuilder();
        while (!mainStack.isEmpty()) {
            builder.append(mainStack.pop());
        }
        return builder.reverse().toString();

    }
}
