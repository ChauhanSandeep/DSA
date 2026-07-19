package designpattern.structural.adapter;

/**
 * Concrete aircraft that already matches the Adapter pattern target interface.
 *
 * Intent: provide a baseline IAircraft implementation that needs no adapter. Use
 * it beside the adapted HotAirBalloon to show that clients can treat native and
 * adapted objects uniformly.
 *
 * Participants: F16 is a concrete target implementation, IAircraft is the target
 * interface, and Driver is the client.
 */
public class F16 implements IAircraft{
    /** Prints the F16 flying behavior. */
    @Override
    public void fly() {
        System.out.println("F16 is flying");
    }
}
