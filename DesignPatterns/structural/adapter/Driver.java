package DesignPatterns.structural.adapter;

public class Driver {
    public static void main(String[] args) {
        F16 f16 = new F16();
        f16.fly();

        HotAirBalloon hotAirBalloon = new HotAirBalloon();
        HotAirBalloonAdapter hotAirBalloonAdapter = new HotAirBalloonAdapter(hotAirBalloon);

        // using adapter, now hotAirBalloon also uses fly() method
        hotAirBalloonAdapter.fly();
    }
}
