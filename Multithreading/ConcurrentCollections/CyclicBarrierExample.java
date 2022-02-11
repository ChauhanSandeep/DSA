package Multithreading.ConcurrentCollections;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierExample {

    public static void main(String[] args) {
        new CyclicBarrierExample().drive();
    }

    private void drive() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            System.out.println("All threads are completed successfully");
        });

        int i=0;
        for(; i<5; i++) {
            executorService.execute(new Worker(i+1, barrier));
        }

        // we can reset the barrier and reuse it
        barrier.reset();

        for(; i<10; i++) {
            executorService.execute(new Worker(i+1, barrier));
        }

        executorService.shutdown();
    }

    class Worker implements Runnable {
        int id;
        CyclicBarrier barrier;

        public Worker(int id, CyclicBarrier barrier) {
            this.id = id;
            this.barrier = barrier;
        }

        public void run() {
            System.out.println("Running thread with id " + this.id);
            try {
                TimeUnit.SECONDS.sleep(1);
                barrier.await();
                System.out.println("This runs in thread " + this.id + "after all threads are completed");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

        }
    }
}
