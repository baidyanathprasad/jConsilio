package parkinglot.models;

import java.util.List;
import parkinglot.exceptions.InvalidArgumentException;

/**
 * Represents a level in the parking lot.
 * @author Baidyanath
 */
public class Level {
    private final Integer floorNumber;
    private final List<ParkingSpot> parkingSpots;

    public Level(Integer floorNumber, List<ParkingSpot> parkingSpots) throws InvalidArgumentException {
        if (floorNumber == null || floorNumber < 0) {
            throw new InvalidArgumentException("Floor number cannot be null or negative");
        }
        if (parkingSpots == null) {
            throw new InvalidArgumentException("Parking spots list cannot be null");
        }
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
