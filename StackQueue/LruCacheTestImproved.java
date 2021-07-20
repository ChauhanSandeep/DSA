package StackQueue;

import java.util.HashMap;
import java.util.Map;

public class LruCacheTestImproved {
    public static void main(String[] args) {
        LRUCache1 cache = new LRUCache1(2);
        cache.put(1, 1);
        cache.put(2, 2);

        System.out.println(cache.get(1));
        cache.put(3, 3);
        System.out.println(cache.get(2));
        cache.put(4, 4);
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
    }
}

class LRUCache1 {

    int capacity;
    int size;
    Map<Integer, CacheNode> map;
    CacheQueue queue;

    public LRUCache1(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        map = new HashMap<>();
        queue = new CacheQueue();
    }

    public int get(int key) {
        if(!map.containsKey(key)) {
            return -1;
        }

        CacheNode node = map.get(key);
        queue.moveToFirst(node);
        return node.val;
    }

    public void put(int key, int value) {
        if(!map.containsKey(key)) {
            CacheNode node = new CacheNode(key, value);
            map.put(key, node);
            queue.addFirst(node);
            this.size++;
        }else{
            CacheNode node = map.get(key);
            queue.removeNode(node);
            node.val = value;
            queue.addFirst(node);
        }

        while(this.size > this.capacity) {
            int lastKey = queue.removeLast();
            map.remove(lastKey);
            this.size--;
        }
    }
}

class CacheNode {
    int key;
    int val;
    CacheNode prev;
    CacheNode next;

    public CacheNode(int key, int val) {
        this.key = key;
        this.val = val;
    }
}

class CacheQueue {
    CacheNode head;
    CacheNode tail;

    public CacheQueue(){}

    public void addFirst(CacheNode node) {
        if(head == null || tail == null) {
            firstElement(node);
            return;
        }
        node.next = head;
        node.prev = null;
        head.prev = node;
        head = node;
    }

    private void firstElement(CacheNode node) {
        head = node;
        tail = node;
    }

    public int removeLast() {
        if(tail == null) return -1;
        int key = tail.key;
        tail = tail.prev;
        if (tail != null)
            tail.next = null;
        return key;
    }

    public void removeNode(CacheNode node) {
        CacheNode prev = node.prev;
        CacheNode next = node.next;
        if(prev == null && next == null) {
            this.head = null;
            this.tail = null;
            return;
        }
        if(prev == null) {
            this.head = next;
        }
        if(next == null){
            this.tail = prev;
        }
        if(prev != null)
            prev.next = next;
        if(next != null)
            next.prev = prev;

        node.next = null;
        node.prev = null;
    }

    public void moveToFirst(CacheNode node) {
        removeNode(node);
        addFirst(node);
    }
}





