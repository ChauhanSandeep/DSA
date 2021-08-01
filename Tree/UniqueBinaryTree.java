package Tree;
import java.util.*;

/**
 * Find the number of binary subtree that can be created with n nodes.
 */
public class UniqueBinaryTree {
    public static void main(String[] args) {
        int result = new UniqueBinaryTree().numTrees(3);
        System.out.println(result);
    }

    public int numTrees(int n) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0,1);
        map.put(1,1);
        return numTrees(n, map);
    }

    private int numTrees(int n, Map<Integer, Integer> map){
        if(n == 0 || n == 1) return 1;
        if(map.containsKey(n)) return map.get(n);
        // recursion
        int sum = 0;
        for(int i = 1;i <= n;i++)
//                 left subtree            right subtree
            sum += numTrees(i-1, map) * numTrees(n-i, map);
        map.put(n, sum);
        return sum;
    }
}
