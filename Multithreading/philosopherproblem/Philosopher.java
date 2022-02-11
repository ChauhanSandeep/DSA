package Multithreading.philosopherproblem;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable{
    private int id;
    private Chopstick leftChopstick;
    private Chopstick rightChopstick;
    private volatile boolean isFull = false;
    private Random random;
    private int eatingCounter;

    public Philosopher(int id, Chopstick leftChopstick, Chopstick rightChopstick) {
        this.id = id;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        random = new Random();
    }

    @Override
    public void run() {
        try{
            while(!isFull) {
                think();
                if(leftChopstick.pickUp(this, State.LEFT)) {
                    if(rightChopstick.pickUp(this, State.RIGHT)){
                        eat();
                        rightChopstick.putDown(this, State.RIGHT);
                    }
                    leftChopstick.putDown(this, State.LEFT);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void think() throws InterruptedException {
        System.out.println(this.id + " is thinking");
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
    }

    private void eat() throws InterruptedException {
        System.out.println(this.id + " is eating");
        this.eatingCounter++;
        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Chopstick getLeftChopstick() {
        return leftChopstick;
    }

    public void setLeftChopstick(Chopstick leftChopstick) {
        this.leftChopstick = leftChopstick;
    }

    public Chopstick getRightChopstick() {
        return rightChopstick;
    }

    public void setRightChopstick(Chopstick rightChopstick) {
        this.rightChopstick = rightChopstick;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public int getEatingCounter() {
        return eatingCounter;
    }

    public void setEatingCounter(int eatingCounter) {
        this.eatingCounter = eatingCounter;
    }
}
