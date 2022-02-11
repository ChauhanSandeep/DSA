package Multithreading.philosopherproblem;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Chopstick {

    private Lock lock;
    private int id;

    public Chopstick(int id) {
        this.id = id;
        this.lock = new ReentrantLock();
    }

    public boolean pickUp(Philosopher philosopher, State state) throws InterruptedException {
        if(this.lock.tryLock(10, TimeUnit.MILLISECONDS)) {
            System.out.println(philosopher.getId() + " picked up " + state + " " + this.id);
            return true;
        }
        return false;
    }

    public void putDown(Philosopher philosopher, State state) {
        lock.unlock();
        System.out.println(philosopher.getId() + " put down " + state + " " + this.id);
    }
}
