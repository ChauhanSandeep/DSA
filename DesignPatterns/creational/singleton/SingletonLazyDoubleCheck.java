package DesignPatterns.creational.singleton;

public class SingletonLazyDoubleCheck {

    private volatile static SingletonLazyDoubleCheck sc = null;

    private SingletonLazyDoubleCheck() {

    }

    public static SingletonLazyDoubleCheck getInstance() {

        if (sc == null) {
            synchronized (SingletonLazyDoubleCheck.class) {
                if (sc == null) {
                    System.out.println("Getting new instance");
                    sc = new SingletonLazyDoubleCheck();
                }
            }
        }

        return sc;
    }
}