package parkinglot.models;

import java.util.List;

/**
 * Represents a level in the parking lot.
 * @author Baidyanath
 */
public class Level {
    private final Integer floorNumber;
    private final List<ParkingSpot> parkingSpots;

    public Level(Integer floorNumber, List<ParkingSpot> parkingSpots) {
        this.floorNumber = floorNumber;
        this.parkingSpots = parkingSpots;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }
}
