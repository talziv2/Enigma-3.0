package mta.patmal.enigma.dto;

/**
 * DTO containing details about a rotor configuration.
 */
public class RotorDetails {
    private final int id;
    private final int notch;
    private final String wiringRight;
    private final String wiringLeft;

    public RotorDetails(int id, int notch, String wiringRight, String wiringLeft) {
        this.id = id;
        this.notch = notch;
        this.wiringRight = wiringRight;
        this.wiringLeft = wiringLeft;
    }

    public int getId() {
        return id;
    }

    public int getNotch() {
        return notch;
    }

    public String getWiringRight() {
        return wiringRight;
    }

    public String getWiringLeft() {
        return wiringLeft;
    }
}

