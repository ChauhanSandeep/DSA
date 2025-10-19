package Multithreading.concurrentcollections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Demonstrates the use of synchronized collections in a multithreaded environment.
 *
 * - Uses a synchronized `ArrayList` to prevent concurrent modification issues.
 * - Properly synchronizes access during iteration.
 * - Ensures correct thread joining and exception handling.
 */
public class ConcurrentCollectionsExample {

    public static void main(String[] args) {
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());

        // Start multiple threads adding elements to the list
        Thread writerThread1 = createWriterThread(synchronizedList);
        Thread writerThread2 = createWriterThread(synchronizedList);

        writerThread1.start();
        writerThread2.start();

        try {
            writerThread1.join();
            writerThread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt flag
            System.out.println("Main thread interrupted.");
        }

        // Ensuring thread-safe iteration
        synchronized (synchronizedList) {
            System.out.println("Final list size: " + synchronizedList.size());
        }
    }

    /**
     * Creates a writer thread that adds numbers to the given list.
     */
    private static Thread createWriterThread(List<Integer> list) {
        return new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                list.add(i);
            }
        });
    }
}
