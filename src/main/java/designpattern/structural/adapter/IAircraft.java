package designpattern.structural.adapter;

/**
 * Target interface for the Adapter pattern example.
 *
 * Intent: define the aircraft operation expected by client code. Use an adapter
 * when an existing class has useful behavior but does not match this interface.
 *
 * Participants: IAircraft is the target, F16 already implements the target,
 * HotAirBalloon is the adaptee, HotAirBalloonAdapter is the adapter, and Driver
 * is the client.
 */
public interface IAircraft {
    /** Performs the aircraft's flying behavior. */
    void fly();
}
