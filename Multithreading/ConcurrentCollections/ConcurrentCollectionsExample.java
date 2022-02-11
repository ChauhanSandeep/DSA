package Multithreading.ConcurrentCollections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ConcurrentCollectionsExample {

    public static void main(String[] args) {
//        List<Integer> nums = new ArrayList<>();
        List<Integer> nums = Collections.synchronizedList(new ArrayList<>());
        Collections.synchronizedSet(new HashSet<>());
        Collections.synchronizedMap(new HashMap<>());
        Thread t1 = new Thread(() -> {
            for(int i= 0; i<1000; i++) {
                nums.add(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for(int i= 0; i<1000; i++) {
                nums.add(i);
            }
        });

        t1.start();
        t2.start();

        try{
            t1.join();
            t2.join();
        }catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(nums.size());
    }
}
