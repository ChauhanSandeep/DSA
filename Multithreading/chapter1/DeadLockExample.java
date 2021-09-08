package Multithreading.chapter1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockExample {

    public static void main(String[] args) {
        new DeadLockExample().process();
    }

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    public void process() {
        new Thread(this::worker1).start();
        new Thread(this::worker2).start();

    }

    /**
     * Locks lock1 first then lock2. Its able to acquire lock1 but not lock2
     */
    private void worker1() {
        lock1.lock();
        System.out.println("Worker1 acquired lock1");

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock2.lock();
        System.out.println("Worker1 acquired lock2");
        lock1.unlock();
        lock2.unlock();

    }

    /**
     * Locks lock2 first then lock1. Its able to acquire lock2 but not lock1
     */
    private void worker2() {
        lock2.lock();
        System.out.println("Worker2 acquired lock2");

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock1.lock();
        System.out.println("Worker2 acquired lock1");
        lock2.unlock();
        lock1.unlock();
    }
}
