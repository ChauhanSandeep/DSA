package Multithreading.ConcurrentCollections;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LatchExample {

    public static void main(String[] args) {
        new LatchExample().drive();
    }

    private void drive() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(5);   // this will wait only 5 threads to execute
        for(int i=0; i<10; i++) {                       // executes 10 threads
            executorService.execute(new Worker(i+1, latch));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Desired number of threads executed successfully");

        // shutdown
        executorService.shutdown();

        try{
            if(executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class Worker implements Runnable {

        int id;
        CountDownLatch latch;

        public Worker(int id, CountDownLatch latch) {
            this.id = id;
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("Thread with id " + this.id + " has started");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }
}
