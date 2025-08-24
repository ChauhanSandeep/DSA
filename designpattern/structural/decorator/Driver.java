package designpattern.structural.decorator;

public class Driver {

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
