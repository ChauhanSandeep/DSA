package Multithreading.concurrentcollections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates the usage of BlockingQueue with a Producer-Consumer model.
 *
 * Producer (`ProducerWorker`) adds numbers to the queue.
 * Consumer (`ConsumerWorker`) takes numbers from the queue.
 *
 * Uses an `ArrayBlockingQueue` with a fixed size of 10.
 * Ensures proper exception handling and modern Java best practices.
 */
public class BlockingQueueExample {

    public static void main(String[] args) {
        new BlockingQueueExample().startProcessing();
    }

    /**
     * Initializes and starts Producer and Consumer threads.
     */
    private void startProcessing() {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        Thread producerThread = new Thread(new ProducerWorker(queue));
        Thread consumerThread = new Thread(new ConsumerWorker(queue));

        producerThread.start();
        consumerThread.start();
    }

    /**
     * Producer thread that adds numbers to the BlockingQueue.
     */
    class ProducerWorker implements Runnable {
        private final BlockingQueue<Integer> queue;
        private int currentValue = 0;

        public ProducerWorker(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    queue.put(currentValue);
                    System.out.println("Produced: " + currentValue);
                    currentValue++;
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupt flag
                    System.out.println("Producer thread interrupted.");
                }
            }
        }
    }

    /**
     * Consumer thread that retrieves numbers from the BlockingQueue.
     */
    class ConsumerWorker implements Runnable {
        private final BlockingQueue<Integer> queue;

        public ConsumerWorker(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Integer value = queue.take();
                    System.out.println("Consumed: " + value);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupt flag
                    System.out.println("Consumer thread interrupted.");
                }
            }
        }
    }
}
