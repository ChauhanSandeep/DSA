package Multithreading.ConcurrentCollections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueExample {

    public static void main(String[] args) {
        new BlockingQueueExample().drive();
    }

    private void drive() {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(10);
        FirstWorker producer = new FirstWorker(blockingQueue);
        SecondWorker consumer = new SecondWorker(blockingQueue);

        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(consumer);
        t1.start();
        t2.start();
    }

    class FirstWorker implements Runnable {
        int counter = 0;
        BlockingQueue<Integer> blockingQueue;
        public FirstWorker(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        public void run() {

            while(true) {
                try{
                    blockingQueue.put(counter);
                    System.out.println("Adding " + counter + " to the queue");
                    counter++;
                    Thread.sleep(100);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SecondWorker implements Runnable {
        BlockingQueue<Integer> blockingQueue;

        public SecondWorker(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        public void run() {
            while(true) {
                try{
                    Integer counter = blockingQueue.take();
                    System.out.println("Taking " + counter + " from the queue");
                    TimeUnit.SECONDS.sleep(1);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
