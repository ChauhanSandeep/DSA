package DesignPatterns.structural.adapter;

public class F16 implements IAircraft{
    @Override
    public void fly() {
        System.out.println("F16 is flying");
    }
}
