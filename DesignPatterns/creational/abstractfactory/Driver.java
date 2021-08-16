package DesignPatterns.creational.abstractfactory;

public class Driver {
    public static void main(String[] args) {
        testAbstractFactory();
    }

    // PCFactory returns PC
    // ServerFactory returns Server
    // ComputerAbstractFactory returns Computer
    private static void testAbstractFactory() {
        Computer pc = ComputerFactory.getComputer(new PCFactory("2 GB","500 GB","2.4 GHz"));
        Computer server = ComputerFactory.getComputer(new ServerFactory("16 GB","1 TB","2.9 GHz"));
        System.out.println("AbstractFactory PC Config::"+pc);
        System.out.println("AbstractFactory Server Config::"+server);
    }
}
