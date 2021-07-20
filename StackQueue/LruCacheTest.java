package StackQueue;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LruCacheTest {
    public static void main(String[] args) {
        LruCache cache = new LruCache(2);
        cache.set(1, 10);
        cache.set(2, 20);
        System.out.println("Value for the key: 1 is " + cache.get(1));

        cache.set(3, 30);
        System.out.println("Value for the key: 2 is " + cache.get(2));

        cache.set(4, 40);
        System.out.println("Value for the key: 1 is " + cache.get(1));
        System.out.println("Value for the key: 3 is " + cache.get(3));
        System.out.println("Value for the key: 4 is " + cache.get(4));
    }
}

// Queue used in this will cause O(n) time complexity to fetch a item (To remove a item etc)
// Check LruCacheTestImproved
class LruCache {
    LinkedList<Node> queue = new LinkedList<>();
    Map<Integer, Node> map = new HashMap<>();
    int capacity;

    public LruCache(int capacity) {
        this.capacity = capacity;
    }

    /**
     * To get value from cache
     * @param key
     * @return
     */
    public int get(int key) {
        if(!map.containsKey(key)) return -1;

        Node node = map.get(key);
        queue.remove(node);
        queue.addFirst(node);
        return node.value;
    }

    /**
     * To add the key, value to cache
     * @param key
     * @param value
     */
    public void set(int key, int value) {
        if(!map.containsKey(key)) {
            Node node = new Node(key, value);
            queue.addFirst(node);
            map.put(key, node);
        }else {
            Node node = map.get(key);
            queue.remove(node);
            node.value = value;
            queue.addFirst(node);
            map.put(key, node);
        }

        int currCapacity = map.size();
        while(currCapacity > capacity) {
            Node node = queue.pollLast();
            map.remove(node.key);
            currCapacity--;
        }
    }


}


class Node {
    int key;
    int value;

    Node(){}

    Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}
