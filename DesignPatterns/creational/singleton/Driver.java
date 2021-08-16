package DesignPatterns.creational.singleton;

public class Driver {

    public static void main(String[] args) {
        // getting new instance is printed only once
        SingletonLazyDoubleCheck singleton1 = SingletonLazyDoubleCheck.getInstance();
        SingletonLazyDoubleCheck singleton2 = SingletonLazyDoubleCheck.getInstance();
    }
}
