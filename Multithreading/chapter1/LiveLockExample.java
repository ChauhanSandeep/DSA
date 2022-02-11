package Multithreading.chapter1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In livelock, two or more threads keep on transferring states between one another instead of waiting infinitely
 */
public class LiveLockExample {
    public static void main(String[] args) {
        new LiveLockExample().process();
    }

    public void process() {
        new Thread(this::worker1, "Worker1Thread").start();
        new Thread(this::worker2, "Worker2Thread").start();
    }

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    /**
     * worker 1 needs thread1 and thread2 to finish its task
     * if worker1 cannot acquire thread2 then it releases thread1 so that other worker can use thread1 and finished up its work
     */
    public void worker1() {
        while (true) {
            lock1.lock();
            System.out.println("worker1 acquired lock1");
            System.out.println("worker1 trying to get lock2");
            try {
                if (lock2.tryLock(500, TimeUnit.MILLISECONDS)) {
                    System.out.println("worker1 acquired lock2");
                    System.out.println("worker1 work finished");
                    lock2.unlock();
                } else {
                    System.out.println("worker1 cannot acquire lock2");
                    lock1.unlock();
                    continue;
                }
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock1.unlock();
        }
    }

    /**
     * worker 2 needs thread1 and thread2 to finish its task
     * if worker2 cannot acquire thread1 then it releases thread2 so that other worker can use thread2 and finished up its work
     */
    public void worker2() {
        while (true) {
            lock2.lock();
            System.out.println("worker2 acquired lock2");
            System.out.println("worker2 trying to get lock1");

            try {
                if (lock1.tryLock(50, TimeUnit.MILLISECONDS)) {
                    System.out.println("worker2 acquired lock1");
                    System.out.println("worker2 work finished");
                    lock1.unlock();
                }else{
                    System.out.println("worker2 cannot acquire lock1");
                    lock2.unlock();
                    continue;
                }
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.unlock();
        }
    }
}
