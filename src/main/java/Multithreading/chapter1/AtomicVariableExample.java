package Multithreading.chapter1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates lock-free shared-state updates with AtomicInteger.
 *
 * The class starts two threads that increment the same counter, then shows
 * set, lazySet, compareAndSet, and weakCompareAndSet on that counter.
 * AtomicInteger supplies atomic read-modify-write operations and visibility
 * guarantees without synchronized blocks or explicit locks.
 */
public class AtomicVariableExample {

    // Shared atomic counter
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        // Creating two threads that increment the counter
        Thread t1 = new Thread(AtomicVariableExample::incrementCounter);
        Thread t2 = new Thread(AtomicVariableExample::incrementCounter);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupted status
            System.err.println("Thread interrupted: " + e.getMessage());
        }

        // Print final counter value
        System.out.println("Final counter value: " + counter.get());

        // Demonstrating additional AtomicInteger operations
        performAtomicOperations();
    }

    /** Increments the shared AtomicInteger with atomic get-and-increment calls. */
    private static void incrementCounter() {
        for (int i = 0; i < 1000; i++) {
            counter.getAndIncrement(); // Atomically increments and returns the previous value
        }
    }

    /** Demonstrates direct writes and compare-and-set operations on the counter. */
    private static void performAtomicOperations() {
        // Set a new value
        counter.set(10000);
        System.out.println("Counter after set(10000): " + counter);

        // Lazy set (may delay visibility to other threads)
        counter.lazySet(15000);
        System.out.println("Counter after lazySet(15000): " + counter);

        // Compare-and-swap: Updates value only if current value matches expected
        boolean isUpdated = counter.compareAndSet(15000, 20000);
        System.out.println("compareAndSet(15000 -> 20000) success? " + isUpdated);
        System.out.println("Counter value: " + counter);

        // Weak compare-and-set: May fail spuriously (useful in loops)
        boolean isWeakUpdated = counter.weakCompareAndSet(20000, 30000);
        System.out.println("weakCompareAndSet(20000 -> 30000) success? " + isWeakUpdated);
        System.out.println("Counter value: " + counter);
    }
}
