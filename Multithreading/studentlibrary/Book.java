package Multithreading.studentlibrary;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Book {

    private int id;
    private Lock lock;

    public Book(int id) {
        this.id = id;
        this.lock = new ReentrantLock();
    }

    public void read(Student student) throws InterruptedException{
        lock.lock();
        System.out.println("student " + student + " started reading book " + this);
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println("student " + student + " finished reading book " + this);
        lock.unlock();
    }

    @Override
    public String toString() {
        return "Book#" + this.id;
    }
}
