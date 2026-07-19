package Multithreading.concurrentcollections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates a producer-consumer handoff with a BlockingQueue.
 *
 * ProducerWorker uses put, so it blocks when the fixed-size ArrayBlockingQueue
 * is full. ConsumerWorker uses take, so it blocks when the queue is empty. The
 * queue itself provides the thread-safe coordination; no explicit synchronized
 * block is needed around producer or consumer access.
 */
public class BlockingQueueExample {

    public static void main(String[] args) {
        BlockingQueueExample demo = new BlockingQueueExample();
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        Thread producerThread = new Thread(demo.new ProducerWorker(queue), "Producer");
        Thread consumerThread = new Thread(demo.new ConsumerWorker(queue), "Consumer");

        producerThread.start();
        consumerThread.start();

        try {
            TimeUnit.SECONDS.sleep(3);
            producerThread.interrupt();
            consumerThread.interrupt();
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Demo interrupted.");
        }
    }

    /** Initializes and starts the unbounded producer-consumer demo. */
    private void startProcessing() {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        Thread producerThread = new Thread(new ProducerWorker(queue));
        Thread consumerThread = new Thread(new ConsumerWorker(queue));

        producerThread.start();
        consumerThread.start();
    }

    /** Producer that blocks on queue.put when the queue is full. */
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

    /** Consumer that blocks on queue.take when the queue is empty. */
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
