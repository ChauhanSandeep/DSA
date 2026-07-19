package Multithreading.concurrentcollections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Demonstrates Collections.synchronizedList for concurrent writes.
 *
 * Two writer threads add values to the same synchronized list. The synchronized
 * wrapper protects individual list operations, and the final size check is read
 * inside a synchronized block to demonstrate the external lock required for
 * compound actions such as iteration.
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

    /** Creates a writer thread that appends numbers to the shared list. */
    private static Thread createWriterThread(List<Integer> list) {
        return new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                list.add(i);
            }
        });
    }
}
