package designpattern.structural.adapter;

/**
 * Adapter that lets HotAirBalloon be used as an IAircraft.
 *
 * Intent: translate the target method fly() into the adaptee method fly(String)
 * while preserving the existing HotAirBalloon code. Use it when client code wants
 * one interface but an object exposes another.
 *
 * Participants: HotAirBalloonAdapter is the adapter, IAircraft is the target,
 * HotAirBalloon is the adaptee, and Driver is the client.
 */
public class HotAirBalloonAdapter implements IAircraft {

    HotAirBalloon hotAirBalloon; // The Adaptee - object being adapted

    /** Wraps the HotAirBalloon instance that will be adapted. */
    public HotAirBalloonAdapter(HotAirBalloon hotAirBalloon) {
        this.hotAirBalloon = hotAirBalloon;
    }

    /** Adapts IAircraft.fly() to HotAirBalloon.fly(String). */
    @Override
    public void fly() {
        String fuelUsed = hotAirBalloon.inflateWithGas(); // Get required parameter
        hotAirBalloon.fly(fuelUsed); // Delegate to adaptee with proper parameter
    }
}
