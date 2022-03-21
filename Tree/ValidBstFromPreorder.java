package Tree;

import java.util.Stack;

/**
 * Check if a valid BST can be created from the preorder sequence.
 */
public class ValidBstFromPreorder {
    public static void main(String[] args) {
        /*
                   40
                 /   \
               30    80
              /  \     \
             25  35    100
         */
        int[] arr = {40, 30, 25, 35, 80, 100};
        int isValid = new ValidBstFromPreorder().canCreateBst(arr);
        System.out.println("isValid? " + isValid);
    }

    public int canCreateBst(int[] arr) {
        int size = arr.length;

        Stack<Integer> stack = new Stack<>();
        /**
         - we push elements to the right of the root to the stack.
         Initially we can consider root to be -Inf. All the elements in tree are
         on right of this.
         **/
        int root = Integer.MIN_VALUE;

        for(int i=0; i<size; i++) {

/*           we only iterate through elements to right of the root.
             so element should never be smaller than root.*/
            if(arr[i] < root) return 0;

/*           All elements present in stack, smaller than current elements are
             either on left side of subtree, or immediate parent of current element.*/
            while(!stack.isEmpty() && stack.peek() < arr[i]) {
                // remove all left element and make immediate parent as root.
                root = stack.pop();
            }

            stack.push(arr[i]);
        }
        return 1;
    }
}
