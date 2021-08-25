package DesignPatterns.structural.adapter;

/**
 * Adapter is created implementing the result interface.
 * It contains Adaptee as instance variable.
 * result interface method. It is methods responsiblity to convert adaptee behaviour to result interface behaviour
 * https://www.educative.io/courses/software-design-patterns-best-practices/gx2rnY2QgK9
 */
public class HotAirBalloonAdapter implements IAircraft {

    HotAirBalloon hotAirBalloon;

    public HotAirBalloonAdapter(HotAirBalloon hotAirBalloon) {
        this.hotAirBalloon = hotAirBalloon;
    }

    @Override
    public void fly() {
        String fuelUsed = hotAirBalloon.inflateWithGas();
        hotAirBalloon.fly(fuelUsed);
    }
}