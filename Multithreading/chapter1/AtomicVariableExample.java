package Multithreading.chapter1;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicVariableExample {

    static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> increment());
        Thread t2 = new Thread(() -> increment());
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("The counter value is " + counter.get());

        // OTHER OPERATIONS ON ATOMIC INTEGER
        counter.set(10000);
        System.out.println(counter);

        counter.lazySet(15000);
        System.out.println(counter);

        boolean compareAndSetResult = counter.compareAndSet(15000, 20000);
        System.out.println("Compared and set data? " + compareAndSetResult);
        System.out.println(counter);

        boolean weakCompareAndSetResult = counter.weakCompareAndSet(20000, 30000);
        System.out.println("Weak compared and set data? " + weakCompareAndSetResult);
        System.out.println(counter);


    }

    public static void increment() {
        for (int i = 0; i < 1000; i++) {
            counter.getAndIncrement();
        }
    }
}
