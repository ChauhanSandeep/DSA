package Multithreading.concurrentcollections;

import java.util.concurrent.*;

/**
 * Demonstrates reusable rendezvous synchronization with CyclicBarrier.
 *
 * Worker tasks pause at a shared barrier until THREAD_COUNT workers have arrived.
 * When the last worker arrives, the barrier action prints a message and the
 * waiting workers are released together. The same barrier is then reused for a
 * second batch of workers.
 */
public class CyclicBarrierExample {

    private static final int THREAD_COUNT = 5;

    public static void main(String[] args) {
        new CyclicBarrierExample().startProcessing();
    }

    private void startProcessing() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        // Barrier action runs once all threads reach the barrier
        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT, () ->
            System.out.println("All threads have reached the barrier and are proceeding.")
        );

        // Submitting first batch of worker threads
        for (int i = 1; i <= THREAD_COUNT; i++) {
            executorService.execute(new Worker(i, barrier));
        }

        // Resetting the barrier and reusing it for the second batch
        barrier.reset();

        for (int i = THREAD_COUNT + 1; i <= THREAD_COUNT * 2; i++) {
            executorService.execute(new Worker(i, barrier));
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /** Worker that waits at the CyclicBarrier after simulating work. */
    static class Worker implements Runnable {
        private final int workerId;
        private final CyclicBarrier cyclicBarrier;

        public Worker(int workerId, CyclicBarrier cyclicBarrier) {
            this.workerId = workerId;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("Thread " + workerId + " is running.");
            try {
                TimeUnit.SECONDS.sleep(1); // Simulate some work
                cyclicBarrier.await(); // Wait for all threads to reach the barrier
                System.out.println("Thread " + workerId + " proceeds after all threads reach the barrier.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread " + workerId + " was interrupted.");
            } catch (BrokenBarrierException e) {
                System.err.println("Barrier was broken before thread " + workerId + " could proceed.");
            }
        }
    }
}
