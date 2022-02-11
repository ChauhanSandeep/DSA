package Multithreading.chapter1;

public class Chapter1 {
    public static void main(String[] args) {
        Runner1 runner = new Runner1();
        Thread thread1 = new Thread(runner);

        Thread thread2 = new Thread(new Runner2());
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i=0; i<10; i++) {
                        Thread.sleep(1000);
                        System.out.println("Running intermittently");
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread4 = new Thread(() -> {
            for(int i=0; i<10; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("Running anonymously");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}


class Runner1 implements Runnable {
    public void run() {
        try{
            for(int i=0; i<10; i++) {
                Thread.sleep(1000);
                System.out.println("Runner1 " + i);
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
 }

 class Runner2 extends Thread {
    public void run() {
        try{
            for(int i=0; i<10; i++) {
                Thread.sleep(1000);
                System.out.println("Runner2 " + i);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
 }
