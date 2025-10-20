package designpattern.structural.adapter;

/**
 * HotAirBalloon - Adaptee in Adapter Pattern
 *
 * This class represents an existing class with an incompatible interface that
 * we want to use with our IAircraft interface. In Adapter Pattern, this is
 * called the "Adaptee".
 *
 * The Problem:
 * - HotAirBalloon has a fly(String gasUsed) method that requires a parameter
 * - IAircraft expects a parameterless fly() method
 * - We can't modify HotAirBalloon (might be a third-party library or legacy code)
 * - We can't modify IAircraft (it's our standard interface)
 *
 * The Solution:
 * - Create an adapter (HotAirBalloonAdapter) that bridges the gap
 * - Adapter implements IAircraft and wraps HotAirBalloon
 * - Adapter translates calls from IAircraft format to HotAirBalloon format
 *
 * Real-World Analogy:
 * - Like a power plug adapter that converts one plug type to another
 * - The HotAirBalloon is the device with a different plug
 * - The adapter makes it compatible with the standard socket (IAircraft)
 *
 * Use Cases for Adapter Pattern:
 * - Integrating third-party libraries with incompatible interfaces
 * - Working with legacy code without modifying it
 * - Creating reusable classes that work with unrelated classes
 * - Adapting classes from different frameworks to work together
 *
 * @author Sandeep Chauhan
 */
public class HotAirBalloon {

    String gasUsed = "Helium";

    /**
     * Makes the hot air balloon fly using specified gas.
     * This method signature is incompatible with IAircraft.fly()
     *
     * @param gasUsed The type of gas used for flight
     */
    void fly(String gasUsed) {
        System.out.println("Hot air baloon is flying using " + gasUsed);
    }

    /**
     * Returns the gas used by the balloon for flight.
     * This method provides the data needed by fly() method.
     *
     * @return The type of gas used by this balloon
     */
    String inflateWithGas() {
        return gasUsed;
    }
}