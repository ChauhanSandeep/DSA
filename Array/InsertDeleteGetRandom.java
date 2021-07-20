package Array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertDeleteGetRandom {
    public static void main(String[] args) {
        RandomizedSet randomizedSet = new RandomizedSet();
        System.out.println(randomizedSet.insert(1)); // Inserts 1 to the set. Returns true as 1 was inserted successfully.
        System.out.println(randomizedSet.remove(2)); // Returns false as 2 does not exist in the set.
        System.out.println(randomizedSet.insert(2)); // Inserts 2 to the set, returns true. Set now contains [1,2].
        System.out.println(randomizedSet.getRandom()); // getRandom() should return either 1 or 2 randomly.
        System.out.println(randomizedSet.remove(1)); // Removes 1 from the set, returns true. Set now contains [2].
        System.out.println(randomizedSet.insert(2)); // 2 was already in the set, so return false.
        System.out.println(randomizedSet.getRandom()); // Since 2 is the only number in the set, getRandom()
    }
}

/**
 * Create a set which provide insert, delete and getRandom functionality in O(1) time complexity
 */
class RandomizedSet {
    List<Integer> list;
    Map<Integer, Integer> valIndexMap;

    public RandomizedSet() {
        list = new ArrayList<>();
        valIndexMap = new HashMap<>();
    }

    /** Inserts a value to the set. Returns true if the set did not already contain the specified element. */
    public boolean insert(int val) {
        if (valIndexMap.containsKey(val)){
            return false;
        }

        list.add(val);
        valIndexMap.put(val, list.size() - 1);
        return true;
    }

    /** Removes a value from the set. Returns true if the set contained the specified element. */
    public boolean remove(int val) {
        if (valIndexMap.containsKey(val)) {
            int index = valIndexMap.get(val);
            if (index < list.size() - 1) {
                swap(list, index, list.size() - 1);
                valIndexMap.put(list.get(index), index);
            }
            list.remove(list.size() - 1);
            valIndexMap.remove(val);
            return true;
        }
        return false;
    }

    /** Get a random element from the set. */
    public int getRandom() {
        int index = (int) Math.floor(Math.random() * list.size());
        return list.get(index);
    }

    public void swap(List<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
