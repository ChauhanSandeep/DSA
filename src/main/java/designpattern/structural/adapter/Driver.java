package designpattern.structural.adapter;

/**
 * Driver - Client Class for Adapter Pattern Demo
 *
 * This class demonstrates how the Adapter Pattern allows incompatible classes
 * to work together through a common interface.
 *
 * Demonstration:
 * 1. F16 natively implements IAircraft - works directly without adaptation
 * 2. HotAirBalloon has incompatible interface - needs adapter to work with IAircraft
 * 3. HotAirBalloonAdapter bridges the gap, allowing HotAirBalloon to be used as IAircraft
 *
 * Key Benefit Shown:
 * - Both F16 and adapted HotAirBalloon can be used polymorphically through IAircraft
 * - Client code doesn't need to know about the incompatibility or adapter implementation
 * - Can store both in an IAircraft array and call fly() uniformly
 *
 * Real-World Analogy:
 * - F16 = Modern USB-C device (works with USB-C port directly)
 * - HotAirBalloon = Old USB-A device (incompatible with USB-C)
 * - HotAirBalloonAdapter = USB-A to USB-C adapter (makes it compatible)
 *
 * @author Sandeep Chauhan
 */
public class Driver {
    /**
     * Main method demonstrating Adapter Pattern.
     *
     * Shows:
     * 1. Direct usage of a compatible class (F16)
     * 2. Adapted usage of an incompatible class (HotAirBalloon via adapter)
     * 3. Both can be used through the same interface (IAircraft)
     */
    public static void main(String[] args) {
        // Direct usage - F16 implements IAircraft natively
        F16 f16 = new F16();
        f16.fly();

        // Adapted usage - HotAirBalloon needs adapter to work with IAircraft
        HotAirBalloon hotAirBalloon = new HotAirBalloon();
        HotAirBalloonAdapter hotAirBalloonAdapter = new HotAirBalloonAdapter(hotAirBalloon);

        // Using adapter, HotAirBalloon can now use the fly() method like any IAircraft
        hotAirBalloonAdapter.fly();

        // Polymorphic usage - both can be stored in IAircraft array
        IAircraft[] aircraft = {f16, hotAirBalloonAdapter};
        System.out.println("\nPolymorphic usage:");
        for (IAircraft craft : aircraft) {
            craft.fly(); // Uniform interface, different implementations
        }
    }
}
