package DesignPatterns.structural.adapter;

public class HotAirBalloon {

    String gasUsed = "Helium";

    void fly(String gasUsed) {
        System.out.println("Hot air baloon is flying using " + gasUsed);
    }

    // Function returns the gas used by the balloon for flight
    String inflateWithGas() {
        return gasUsed;
    }
}