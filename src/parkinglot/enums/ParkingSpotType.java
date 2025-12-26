package parkinglot.enums;

/**
 * Represents different types of parking spots.
 * @author Baidyanath
 */
public enum ParkingSpotType {
    SMALL("BIKE"),
    MEDIUM("CAR"),
    LARGE("TRUCK");

    private final String displayName;

    ParkingSpotType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
