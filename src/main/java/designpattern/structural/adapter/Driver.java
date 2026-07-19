package designpattern.structural.adapter;

/**
 * Demo client for the Adapter pattern.
 *
 * Intent: show that a native aircraft and an adapted hot air balloon can both be
 * used through the IAircraft target interface. Use this pattern when incompatible
 * APIs need to collaborate without changing either side.
 *
 * Participants: Driver is the client, IAircraft is the target, F16 is a native
 * implementation, HotAirBalloon is the adaptee, and HotAirBalloonAdapter is the
 * adapter.
 */
public class Driver {
    /** Runs native and adapted aircraft through the same interface. */
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
