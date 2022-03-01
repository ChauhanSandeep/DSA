package Multithreading.utilities;

import static java.lang.Thread.sleep;

public class OddEvenDemo {

    public static void main(String[] args) {
        Object lock = new Object();
        Thread t1 = new Thread(new TaskEvenOdd(lock, false, 1, 2), "Odd");
        Thread t2 = new Thread(new TaskEvenOdd(lock, true, 2, 2), "Even");

        t1.start();
        t2.start();

    }
}

class TaskEvenOdd implements Runnable {
    static boolean runEven = false;

    final Object lock;
    boolean isEven;
    int count;
    int step;

    public TaskEvenOdd(Object lock, boolean isEven, int count, int step) {
        this.lock = lock;
        this.count = count;
        this.step = step;
        this.isEven = isEven;
    }

    @Override
    public void run() {
        while(true) {
            synchronized (lock) {
                while(this.isEven != runEven) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + ": " + count);
                count += step;
                runEven = !runEven;
                sleep(500);
                lock.notifyAll();
            }
        }
    }

    public void sleep(long time) {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
