package designpattern.structural.adapter;

/**
 * Adaptee in the Adapter pattern example.
 *
 * Intent: represent useful existing behavior whose method shape does not match
 * IAircraft. Use an adapter when this class should be reused without changing its
 * original interface.
 *
 * Participants: HotAirBalloon is the adaptee, HotAirBalloonAdapter wraps it,
 * IAircraft is the target interface, and Driver is the client.
 */
public class HotAirBalloon {

    String gasUsed = "Helium";

    /** Flies the balloon with the provided gas value. */
    void fly(String gasUsed) {
        System.out.println("Hot air baloon is flying using " + gasUsed);
    }

    /** Returns the gas value the adapter passes into fly(String). */
    String inflateWithGas() {
        return gasUsed;
    }
}
