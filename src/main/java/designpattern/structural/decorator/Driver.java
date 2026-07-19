package designpattern.structural.decorator;

/**
 * Demo client for the Decorator pattern.
 *
 * Intent: show that features can be layered by wrapping one Car in another Car.
 * Use this pattern when objects need different feature combinations at runtime
 * without changing the base component or creating subclasses for every mix.
 *
 * Participants: Driver is the client, BasicCar is the concrete component,
 * SportsCar and LuxuryCar are decorators, and CarDecorator stores the wrapped
 * component.
 */
public class Driver {

    /** Builds several decorated car combinations and prints their assembly. */
    public static void main(String[] args) {
        System.out.println("Creating sports car");
        Car sportsCar = new SportsCar(new BasicCar());
        sportsCar.assemble();

        System.out.println("\nCreating luxury car");
        Car luxuryCar = new LuxuryCar(new BasicCar());
        luxuryCar.assemble();

        System.out.println("\ncreating luxury sports car");
        Car luxurySportsCar = new SportsCar(new LuxuryCar(new BasicCar()));
        luxurySportsCar.assemble();
    }

}
