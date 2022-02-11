package Multithreading.chapter1;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1, true); // semaphore with one permit is called mutex
        Thread t1 = new Thread(new Thread1(semaphore));
        Thread t2 = new Thread(new Thread2(semaphore));

        t1.start();
        t2.start();
        try{
            t1.join();
            t2.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("-------Allowing two threads-------");

        semaphore = new Semaphore(2, true);
        Thread t3 = new Thread(new Thread1(semaphore));
        Thread t4 = new Thread(new Thread2(semaphore));
        Thread t5 = new Thread(new Thread2(semaphore));
        t3.start();
        t4.start();
        t5.start();
    }
}

class Thread1 implements Runnable{
    Semaphore semaphore;
    public Thread1(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public void run() {
        try{
            semaphore.acquire();
            for(int i=0; i<5; i++) {
                System.out.println("printing from sender " + i);
                Thread.sleep(500);
            }
            semaphore.release();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Thread2 implements Runnable{
    Semaphore semaphore;
    public Thread2(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public void run() {
        try{
            semaphore.acquire();
            for(int i=0; i<5; i++) {
                System.out.println("printing from receiver " + i);
                Thread.sleep(500);
            }
            semaphore.release();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
